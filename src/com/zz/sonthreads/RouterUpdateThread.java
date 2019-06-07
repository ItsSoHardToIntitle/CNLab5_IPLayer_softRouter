package com.zz.sonthreads;

import java.util.ArrayList;

import com.zz.router.Router;
import com.zz.router.RouterTableItem;
import com.zz.sharingresource.ResourceQueue;
import com.zz.sharingresource.RouterUpdateInfo;

/**
 * @author Zhang Zhen
 * @time 2019��6��5�� ����12:57:38
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
        // ���ϴӸ�����Ϣ��������ȡ���ھ�·�ɱ�������Ϣ�����������Լ�
        while (true) { // �е��񾭵�ĵȴ�-֪ͨģʽ
            try {
                // �]�и�����Ϣ���Լ�����������cpu����
                // ������������ �ٶ� ���������
                RouterUpdateInfo routerUpdateInfo = ResourceQueue.getUpdateRecvQueue().take();
                router.update(routerUpdateInfo);
                // ͬʱ���Լ��ĸ��º���µ�·����Ϣ���������������
                sendToUpdateQueue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
