package com.zz.sharingresource;
/**
*@author Zhang Zhen
*@time 2019��6��5�� ����7:47:10
*/

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

/*
 *     ��Ҫ���͵���Ϣ�����ö���Ľṹ���ͣ�UDP�ڷ��ͺͽ���ʱ
 * ͨ�������л����͡������л�������ת�����͡�
 */

public class ResourceQueue {
    // ��������͵� �� ����
    private static BlockingQueue<Grouping> GroupingSendQueue = new LinkedTransferQueue<>();
    // ��������͵� �����º���Լ���·�ɱ���Ϣ
    private static BlockingQueue<RouterUpdateInfo> UpdateSendQueue = new LinkedTransferQueue<>();
    // ������յģ����ڸ��µ�·�ɱ���Ϣ
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
