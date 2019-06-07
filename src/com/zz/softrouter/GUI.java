package com.zz.softrouter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import com.zz.router.Router;
import com.zz.sharingresource.Grouping;
import com.zz.sharingresource.ResourceQueue;

/**
 * @author Zhang Zhen
 * @time 2019年6月5日 下午6:24:14
 */
public class GUI {
    private static GUI gui = null;

    // 控件
    private static JFrame jFrame;

    private static JTextField jTextField;

    private static final String[] ROUTER_INFO_TITILE = new String[] { "", "Distance", "Next-Hop" };
    private static JTable routerInfoTable;
    private static DefaultTableModel routerInfoModel;

    private static final String[] STATISTIC_INFO_TITLE = new String[] { "接收数", "转发数", "丢弃数" };
    private static JTable statisticInfoTable;
    private static DefaultTableModel statisticInfoModel;

    private static final String[] SPECIFIC_INFO_TITLE = new String[] { "源地址", "目的地址", "TTL" };
    private static JTable recvTable;
    private static DefaultTableModel recvModel;

    private static JTable forwordTable;
    private static DefaultTableModel forwordModel;

    private static JTable discardTable;
    private static DefaultTableModel discardModel;

    public static GUI getGUIInstance(int port) {
        if (gui == null) {
            gui = new GUI(port);
        }
        return gui;
    }

    public GUI(int port) {
        jFrame = new JFrame(port + "---by 张圳");
        init();
        addControls();
        // 这句得放在最后面
        jFrame.setVisible(true);
    }

    public void initRouterInfoTable() {
        ArrayList<Integer> routerTable = Router.getRouterTabel();
        int temp = 65;
        for (int i = 0; i < routerTable.size(); i++) {
            Object[] objects = new Object[3];
            objects[0] = (char) (temp++);
            objects[1] = routerTable.get(i);
            objects[2] = ((int) objects[1] == 16 || (int) objects[1] == 0) ? "-" : objects[0];
            routerInfoModel.addRow(objects);
        }
    }

    public void initStatisticInfoModel() {
        Object[] objects = new Object[] { 0, 0, 0 };
        statisticInfoModel.addRow(objects);
    }

    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        jFrame.setSize(600, 500);
        jFrame.setResizable(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jTextField = new JTextField();

        routerInfoTable = new JTable();
        routerInfoTable.setEnabled(false);
        routerInfoModel = (DefaultTableModel) routerInfoTable.getModel();
        routerInfoModel.setColumnIdentifiers(ROUTER_INFO_TITILE);
        initRouterInfoTable();

        statisticInfoTable = new JTable();
        statisticInfoTable.setEnabled(false);
        statisticInfoModel = (DefaultTableModel) statisticInfoTable.getModel();
        statisticInfoModel.setColumnIdentifiers(STATISTIC_INFO_TITLE);
        initStatisticInfoModel();

        recvTable = new JTable();
        recvTable.setEnabled(false);
        recvModel = (DefaultTableModel) recvTable.getModel();
        recvModel.setColumnIdentifiers(SPECIFIC_INFO_TITLE);

        forwordTable = new JTable();
        forwordTable.setEnabled(false);
        forwordModel = (DefaultTableModel) forwordTable.getModel();
        forwordModel.setColumnIdentifiers(SPECIFIC_INFO_TITLE);

        discardTable = new JTable();
        discardTable.setEnabled(false);
        discardModel = (DefaultTableModel) discardTable.getModel();
        discardModel.setColumnIdentifiers(SPECIFIC_INFO_TITLE);

    }

    public void addControls() {
        // 1. hBox : 输入框
        Box hBox = Box.createHorizontalBox();

        // 输入框：
        jTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String userInput = jTextField.getText();
                    String[] strings = userInput.trim().split(" ");
                    Grouping grouping = new Grouping(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]),
                            Integer.parseInt(strings[2]));
                    try {
                        ResourceQueue.getGroupingSendQueue().put(grouping);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        hBox.add(jTextField);

        // 2. hBox2 : vBox + vBox2
        Box hBox2 = Box.createHorizontalBox();
        // 2.1 vBox : 显示自己路由表的信息(RT)
        Box vBox = Box.createVerticalBox();
        JScrollPane routerInfoPane = new JScrollPane(routerInfoTable);
        vBox.add(routerInfoPane);

        hBox2.add(vBox);

        // 2.2 vBox2 : 统计信息 = 接收、转发、丢弃分组数 + 对应的详细信息
        Box vBox2 = Box.createVerticalBox();
        // 2.2-1 hBox3
        Box hBox3 = Box.createHorizontalBox();
        JScrollPane statisticInfoPane = new JScrollPane(statisticInfoTable);
        hBox3.add(statisticInfoPane);
        vBox2.add(hBox3);

        // 2.2-2 hBox4
        Box vBox3 = Box.createHorizontalBox();
        // 发送表
        JScrollPane sendPane = new JScrollPane(recvTable);
        vBox3.add(sendPane);
        // 转发表
        JScrollPane forwordPane = new JScrollPane(forwordTable);
        vBox3.add(forwordPane);
        // 丢弃表
        JScrollPane discardPane = new JScrollPane(discardTable);
        vBox3.add(discardPane);
        vBox2.add(vBox3);

        hBox2.add(vBox2);

        // 3. 看下面就明白了
        Box vBox4 = Box.createVerticalBox();
        vBox4.add(hBox);
        vBox4.add(hBox2);

        // finally
        jFrame.add(vBox4);

    }

    public static JTextField getjTextField() {
        return jTextField;
    }

    public static DefaultTableModel getRouterInfoModel() {
        return routerInfoModel;
    }

    public static DefaultTableModel getStatisticInfoModel() {
        return statisticInfoModel;
    }

    public static DefaultTableModel getRecvModel() {
        return recvModel;
    }

    public static DefaultTableModel getForwordModel() {
        return forwordModel;
    }

    public static DefaultTableModel getDiscardModel() {
        return discardModel;
    }
}
