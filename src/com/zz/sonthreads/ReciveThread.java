package com.zz.sonthreads;

import java.io.ByteArrayInputStream;

import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.table.DefaultTableModel;

import com.zz.router.Router;
import com.zz.sharingresource.Grouping;
import com.zz.sharingresource.ResourceQueue;
import com.zz.sharingresource.RouterUpdateInfo;
import com.zz.softrouter.GUI;

/**
 * @author Zhang Zhen
 * @time 2019年6月5日 下午9:35:59
 */
public class ReciveThread extends Thread {

    private Router router;

    private static DatagramSocket datagramSocket;
    private static DatagramPacket datagramPacket;
    private static final int BUFFER_SIZE = 1024;
    private static byte[] buffer;

    private static ByteArrayInputStream byteArrayInputStream;
    private static ObjectInputStream objectInputStream;

    public ReciveThread(Router router) {
        this.router = router;
    }

    static {
        buffer = new byte[BUFFER_SIZE];
        datagramPacket = new DatagramPacket(buffer, buffer.length);
    }

    // 反序列化接收对象
    @Override
    public void run() {
        int port = router.getId() + 1 + 52000;
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        while (true) {
            try {
                datagramSocket.receive(datagramPacket);
                byteArrayInputStream = new ByteArrayInputStream(buffer);
                objectInputStream = new ObjectInputStream(byteArrayInputStream);

                Object object = objectInputStream.readObject();
                // 判断接收到的是分组还是路由表
                if (object instanceof RouterUpdateInfo) {
                    RouterUpdateInfo routerUpdateInfo = (RouterUpdateInfo) object;
                    ResourceQueue.getUpdateRecvQueue().put(routerUpdateInfo);
                } else {
                    Grouping grouping = (Grouping) object;
                    int destination = grouping.getDestinationPort();
                    Object[] objects = new Object[3];
                    objects[0] = grouping.getSourcePort();
                    objects[1] = grouping.getDestinationPort();
                    objects[2] = grouping.getTtl();
                    DefaultTableModel defaultTableModel = null;
                    if (destination == router.getPort()) { // 是给自己的分组
                        // 更新界面表格
                        defaultTableModel = GUI.getStatisticInfoModel();
                        int recvNum = (int) defaultTableModel.getValueAt(0, 0); // 接收分组数目
                        defaultTableModel.setValueAt(recvNum + 1, 0, 0);

                        defaultTableModel = GUI.getRecvModel();
                        defaultTableModel.addRow(objects);
                    } else { // 转发（发送线程那里会判断ttl的）
                        ResourceQueue.getGroupingSendQueue().put(grouping);

                        // 更新界面表格
                        defaultTableModel = GUI.getStatisticInfoModel();
                        int forwordNum = (int) defaultTableModel.getValueAt(0, 1); // 转发分组数目
                        defaultTableModel.setValueAt(forwordNum + 1, 0, 1);

                        defaultTableModel = GUI.getForwordModel();
                        defaultTableModel.addRow(objects);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
