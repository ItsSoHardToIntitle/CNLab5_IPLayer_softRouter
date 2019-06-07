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
 * @time 2019年6月5日 下午9:35:40
 */
public class SendThread extends Thread {
    private Router route;

    private static boolean isFirstTime = true;
    private static final long timeInterval = 6000; // 每隔6秒发送一次自己的路由信息
    private static long timeLastSend = 0; // 发送上一个路由更新信息的时间

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

    // 该类实现了序列化接口，则：对于该类中的非静态成员变量Router类，以及Router类中
    // 涉及到的非静态成员变量RouterTableItems类等，也要实现序列化接口
    // 序列化发送分组 或 路由表
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
                if (!ResourceQueue.getGroupingSendQueue().isEmpty()) { // 没有待发送分组，就跳过，避免阻塞无法发送更新信息
                    Grouping grouping = ResourceQueue.getGroupingSendQueue().take();
                    // 发送分组
                    int ttl = grouping.getTtl();
                    DefaultTableModel defaultTableModel = null;
                    Object[] objects = new Object[3];
                    objects[0] = grouping.getSourcePort();
                    objects[1] = grouping.getDestinationPort();
                    objects[2] = grouping.getTtl();
                    if (ttl == 0) { // 丢弃
                        // 更新界面
                        defaultTableModel = GUI.getStatisticInfoModel();
                        int discardNum = (int) defaultTableModel.getValueAt(0, 2);
                        defaultTableModel.setValueAt(discardNum + 1, 0, 2);

                        defaultTableModel = GUI.getDiscardModel();
                        defaultTableModel.addRow(objects);
                    } else { // 转发 或者 用户输入（自己是源地址）
                        int srcPort = grouping.getSourcePort();
                        boolean own = srcPort == route.getPort();
                        // --------------------------------------???
                        if (own) { // 自己发出去的，则要更新界面?
                            // 老师要求的是显示：接收分组 + 转发分组 + 丢弃分组
                            // 自己发送的 == 转发？？？
                        }
                        // 找到到达destination的出口，即：路由表中对应的Next - Hop
                        int desPort = grouping.getDestinationPort();
                        int index = desPort % 10 - 1;
                        // 通过desPort找到最近出口Next-Hop
                        int nextHop = route.getRouterTableItems().get(index).getNextHop();
                        int tempPort = nextHop + 52001;

                        // ttl 减一 或 不变
                        grouping.setTtl(own ? ttl : ttl - 1);

                        // UDP发送分组
                        send(grouping, tempPort);

                        // 界面更新在接收线程那里已经更新过了，此处就不用更新界面了
                        // 注：分组的转发 + 接收 ： 在接线程面更新
                        // 分组的丢弃 ： 在发送线程更新
                    }

                }
                if (isFirstTime) { // 第一次发送更新，延时6秒，为了展示
                    TimeUnit.SECONDS.sleep(6);
                    isFirstTime = false;
                }

                // 发送路由信息，每隔一段时间发送到所有的邻居（此处可能有点不符合要求）
                if (System.currentTimeMillis() - timeLastSend >= timeInterval) {
                    ArrayList<Integer> routerTable = Router.getRouterTabel();
                    for (int i = 0; i < routerTable.size(); i++) {
                        // 非邻居和自己都不发送，debug了好久，发现竟然发送给了自己
                        if (routerTable.get(i) != 16 && routerTable.get(i) != 0) { // 是且仅是邻居
                            int desPort = i + 52001;
                            RouterUpdateInfo routerUpdateInfo = new RouterUpdateInfo(route.getId(),
                                    route.getRouterTableItems());
                            // UDP发送路由表
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
