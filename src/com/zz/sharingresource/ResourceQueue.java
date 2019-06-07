package com.zz.sharingresource;
/**
*@author Zhang Zhen
*@time 2019年6月5日 下午7:47:10
*/

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

/*
 *     将要发送的信息，采用对象的结构发送，UDP在发送和接收时
 * 通过“序列化”和“反序列化”进行转换发送。
 */

public class ResourceQueue {
    // 保存待发送的 ： 分组
    private static BlockingQueue<Grouping> GroupingSendQueue = new LinkedTransferQueue<>();
    // 保存待发送的 ：更新后的自己的路由表信息
    private static BlockingQueue<RouterUpdateInfo> UpdateSendQueue = new LinkedTransferQueue<>();
    // 保存接收的，用于更新的路由表信息
    private static BlockingQueue<RouterUpdateInfo> UpdateRecvQueue = new LinkedTransferQueue<>();

    public static BlockingQueue<Grouping> getGroupingSendQueue() {
        return GroupingSendQueue;
    }

    public static BlockingQueue<RouterUpdateInfo> getUpdateSendQueue() {
        return UpdateSendQueue;
    }

    public static BlockingQueue<RouterUpdateInfo> getUpdateRecvQueue() {
        return UpdateRecvQueue;
    }
}
