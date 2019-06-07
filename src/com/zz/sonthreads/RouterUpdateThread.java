package com.zz.sonthreads;

import java.util.ArrayList;

import com.zz.router.Router;
import com.zz.router.RouterTableItem;
import com.zz.sharingresource.ResourceQueue;
import com.zz.sharingresource.RouterUpdateInfo;

/**
 * @author Zhang Zhen
 * @time 2019年6月5日 下午12:57:38
 */
public class RouterUpdateThread extends Thread {
    private Router router;

    public RouterUpdateThread(Router router) {
        this.router = router;
    }

    public void sendToUpdateQueue() {
        ArrayList<RouterTableItem> itemsList = router.getRouterTableItems();
        RouterUpdateInfo routerUpdateInfo = new RouterUpdateInfo(router.getId(), itemsList);
        try {
            ResourceQueue.getUpdateSendQueue().put(routerUpdateInfo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // 不断从更新信息阻塞队列取出邻居路由表发来的信息，进而更新自己
        while (true) { // 有点像经典的等待-通知模式
            try {
                // 沒有更新信息，自己阻塞，降低cpu消耗
                // 该种阻塞队列 速度 各方面更优
                RouterUpdateInfo routerUpdateInfo = ResourceQueue.getUpdateRecvQueue().take();
                router.update(routerUpdateInfo);
                // 同时将自己的更新后的新的路由信息放入更新阻塞队列
                sendToUpdateQueue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
