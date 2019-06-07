package com.zz.softrouter;

import com.zz.router.Router;
import com.zz.sonthreads.ReciveThread;
import com.zz.sonthreads.RouterUpdateThread;
import com.zz.sonthreads.SendThread;

/**
 * @author Zhang Zhen
 * @time 2019��6��5�� ����11:49:20
 */
public class Application {

    public static void main(String[] args) {
        Router router = new Router();
        int port = -1;
        // �����Լ��Ķ˿ں�
        if (args.length != 0) {
            port = Integer.parseInt(args[0]);
            router.setPort(port);
            router.setId(port % 10 - 1); // ��0��ʼ
        } else {
            // for test:
            System.out.println("args error!");
            router.setId(0);
        }
        // ��ʼ���Լ���·�ɱ�
        router.init();

        // ����
        GUI.getGUIInstance(port);

        // �����������߳̿�ʼ����
        // 1. �����߳�
        SendThread sendThread = new SendThread(router);
        sendThread.start();
        // 2. �����߳�
        ReciveThread reciveThread = new ReciveThread(router);
        reciveThread.start();

        // 3. ·�ɸ����߳�
        RouterUpdateThread routerUpdateThread = new RouterUpdateThread(router);
        routerUpdateThread.start();

        // ���̵߳ȴ��������߳�ִ����ϲ��ܽ���
        // ������ ���̵߳ĳ���, �����ȷ�Ҹ�Ч��ʵ�ֶ���߳�֮���ͨ��Э�����Ѱ�
        // ��ʹJava֧�ֵ���˺ã����ֲ������ݽṹ�Ϳ�ܡ���
        try {
            sendThread.join();
            reciveThread.join();
            routerUpdateThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
/*
 * д�����д�Ĵ���Խ�࣬�о��Լ���Java������Խ�٣�ͬʱ����һ������Ҫ�����飺
 * �����κε����⣬��JDK�Ĺٷ��ĵ��������õģ�ͬʱ��ϰ��Ӣ��Լ��ܶ�����⣬��� ͨ���Ķ���ص�APIԭ��˵���������ҵ���bug��
 * �����Ǳ������������⣺ 1. Java�ж�һ����ĳ�ʼ��˳�������Ǻ���static�飬static�����ȡ��� 2.
 * ʹ��UDP�����String�࣬����һ�����ʵ��ʱ������Ӧ��ʵ�� ���л� �ӿڣ�����
 * ���������������������ʵ����Ϊ��Ա������Ӧ���������漰������Щ�඼ʵ�� ���л� �ӿڡ�
 */
