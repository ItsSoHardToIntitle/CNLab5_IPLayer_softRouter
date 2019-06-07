package com.zz.softrouter;

import com.zz.router.Router;
import com.zz.sonthreads.ReciveThread;
import com.zz.sonthreads.RouterUpdateThread;
import com.zz.sonthreads.SendThread;

/**
 * @author Zhang Zhen
 * @time 2019年6月5日 上午11:49:20
 */
public class Application {

    public static void main(String[] args) {
        Router router = new Router();
        int port = -1;
        // 配置自己的端口号
        if (args.length != 0) {
            port = Integer.parseInt(args[0]);
            router.setPort(port);
            router.setId(port % 10 - 1); // 从0开始
        } else {
            // for test:
            System.out.println("args error!");
            router.setId(0);
        }
        // 初始化自己的路由表
        router.init();

        // 界面
        GUI.getGUIInstance(port);

        // 开启各个子线程开始工作
        // 1. 发送线程
        SendThread sendThread = new SendThread(router);
        sendThread.start();
        // 2. 接收线程
        ReciveThread reciveThread = new ReciveThread(router);
        reciveThread.start();

        // 3. 路由更新线程
        RouterUpdateThread routerUpdateThread = new RouterUpdateThread(router);
        routerUpdateThread.start();

        // 主线程等待所有子线程执行完毕才能结束
        // ？？？ 多线程的程序, 如何正确且高效地实现多个线程之间的通信协作，难啊
        // 纵使Java支持的如此好，各种并发数据结构和框架……
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
 * 写在最后，写的代码越多，感觉自己对Java的掌握越少，同时明白一个很重要的事情：
 * 遇到任何的问题，看JDK的官方文档是最有用的，同时练习了英语，自己很多的问题，最后 通过阅读相关的API原文说明，最终找到了bug。
 * 下面是本次遇到的问题： 1. Java中对一个类的初始化顺序，尤其是含有static块，static变量等…… 2.
 * 使用UDP传输非String类，传输一个类的实例时，该类应该实现 序列化 接口，此外
 * 发送类中若含有其它类的实例作为成员变量，应该让所有涉及到的这些类都实现 序列化 接口。
 */
