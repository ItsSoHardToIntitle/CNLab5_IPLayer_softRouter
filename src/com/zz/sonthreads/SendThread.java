package com.zz.sonthreads;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.table.DefaultTableModel;

import com.zz.router.Router;
import com.zz.sharingresource.Grouping;
import com.zz.sharingresource.ResourceQueue;
import com.zz.sharingresource.RouterUpdateInfo;
import com.zz.softrouter.GUI;

/**
 * @author Zhang Zhen
 * @time 2019��6��5�� ����9:35:40
 */
public class SendThread extends Thread {
    private Router route;

    private static boolean isFirstTime = true;
    private static final long timeInterval = 6000; // ÿ��6�뷢��һ���Լ���·����Ϣ
    private static long timeLastSend = 0; // ������һ��·�ɸ�����Ϣ��ʱ��

    private static InetAddress inetAddress;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket datagramPacket;

    private static ByteArrayOutputStream byteArrayOutputStream;
    private static ObjectOutputStream objectOutputStream;
    private static byte[] buffer;

    public SendThread(Router router) {
        this.route = router;
    }

    static {
        try {
            inetAddress = InetAddress.getLocalHost();
            datagramSocket = new DatagramSocket();
            datagramPacket = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ����ʵ�������л��ӿڣ��򣺶��ڸ����еķǾ�̬��Ա����Router�࣬�Լ�Router����
    // �漰���ķǾ�̬��Ա����RouterTableItems��ȣ�ҲҪʵ�����л��ӿ�
    // ���л����ͷ��� �� ·�ɱ�
    public void send(Object object, int desPort) {
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            buffer = byteArrayOutputStream.toByteArray();
            datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, desPort);
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!ResourceQueue.getGroupingSendQueue().isEmpty()) { // û�д����ͷ��飬�����������������޷����͸�����Ϣ
                    Grouping grouping = ResourceQueue.getGroupingSendQueue().take();
                    // ���ͷ���
                    int ttl = grouping.getTtl();
                    DefaultTableModel defaultTableModel = null;
                    Object[] objects = new Object[3];
                    objects[0] = grouping.getSourcePort();
                    objects[1] = grouping.getDestinationPort();
                    objects[2] = grouping.getTtl();
                    if (ttl == 0) { // ����
                        // ���½���
                        defaultTableModel = GUI.getStatisticInfoModel();
                        int discardNum = (int) defaultTableModel.getValueAt(0, 2);
                        defaultTableModel.setValueAt(discardNum + 1, 0, 2);

                        defaultTableModel = GUI.getDiscardModel();
                        defaultTableModel.addRow(objects);
                    } else { // ת�� ���� �û����루�Լ���Դ��ַ��
                        int srcPort = grouping.getSourcePort();
                        boolean own = srcPort == route.getPort();
                        // --------------------------------------???
                        if (own) { // �Լ�����ȥ�ģ���Ҫ���½���?
                            // ��ʦҪ�������ʾ�����շ��� + ת������ + ��������
                            // �Լ����͵� == ת��������
                        }
                        // �ҵ�����destination�ĳ��ڣ�����·�ɱ��ж�Ӧ��Next - Hop
                        int desPort = grouping.getDestinationPort();
                        int index = desPort % 10 - 1;
                        // ͨ��desPort�ҵ��������Next-Hop
                        int nextHop = route.getRouterTableItems().get(index).getNextHop();
                        int tempPort = nextHop + 52001;

                        // ttl ��һ �� ����
                        grouping.setTtl(own ? ttl : ttl - 1);

                        // UDP���ͷ���
                        send(grouping, tempPort);

                        // ��������ڽ����߳������Ѿ����¹��ˣ��˴��Ͳ��ø��½�����
                        // ע�������ת�� + ���� �� �ڽ��߳������
                        // ����Ķ��� �� �ڷ����̸߳���
                    }

                }
                if (isFirstTime) { // ��һ�η��͸��£���ʱ6�룬Ϊ��չʾ
                    TimeUnit.SECONDS.sleep(6);
                    isFirstTime = false;
                }

                // ����·����Ϣ��ÿ��һ��ʱ�䷢�͵����е��ھӣ��˴������е㲻����Ҫ��
                if (System.currentTimeMillis() - timeLastSend >= timeInterval) {
                    ArrayList<Integer> routerTable = Router.getRouterTabel();
                    for (int i = 0; i < routerTable.size(); i++) {
                        // ���ھӺ��Լ��������ͣ�debug�˺þã����־�Ȼ���͸����Լ�
                        if (routerTable.get(i) != 16 && routerTable.get(i) != 0) { // ���ҽ����ھ�
                            int desPort = i + 52001;
                            RouterUpdateInfo routerUpdateInfo = new RouterUpdateInfo(route.getId(),
                                    route.getRouterTableItems());
                            // UDP����·�ɱ�
                            send(routerUpdateInfo, desPort);
                            //
                            TimeUnit.SECONDS.sleep(1);
                        }
                    }
                    timeLastSend = System.currentTimeMillis();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
