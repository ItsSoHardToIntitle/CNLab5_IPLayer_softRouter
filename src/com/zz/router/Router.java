package com.zz.router;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.zz.sharingresource.RouterUpdateInfo;
import com.zz.softrouter.GUI;

/**
 * @author Zhang Zhen
 * @time 2019��6��5�� ����12:50:02
 */
public class Router implements Serializable {
    private static final long serialVersionUID = 1L;

    private static List<ArrayList<Integer>> topologyList = null;
    // �൱��0-1���󣬺��������ж��Ƿ����ھӹ�ϵ
    private static ArrayList<Integer> routerTabel = null;

    private int id;
    private int port; // �Զ˿ں�Ҳ��Ϊ��ʶ
    private ArrayList<RouterTableItem> routerTableItems;

    public Router() {
        routerTableItems = new ArrayList<>();
    }

    public void init() {
        topologyList = ReadFromConfig.readTopology("Config\\TopologyInfo.txt");
        routerTabel = topologyList.get(id);
        int distance = -1;
        for (int i = 0; i < routerTabel.size(); i++) {
            distance = routerTabel.get(i);
            routerTableItems.add(new RouterTableItem(distance, i));
        }
    }

    public void update(RouterUpdateInfo routerUpdateInfo) {
        // ���½�����
        DefaultTableModel tempModel = GUI.getRouterInfoModel();
        // �����ھӵľ���
        int neighborId = routerUpdateInfo.getId();
        int distance = routerTableItems.get(neighborId).getDistance();
        for (int i = 0; i < routerTableItems.size(); i++) {
            if (i != id) { // �Լ����Լ��ǲ�����
                RouterTableItem tempItem = routerTableItems.get(i);
                int nowDistance = tempItem.getDistance(); // ��ǰ�ľ���
                int neighborDistance = routerUpdateInfo.getRouterTableItems().get(i).getDistance(); // �ھӵ�
                if (nowDistance == 16) { // ��ǰ�Լ����ɴ�
                    if (neighborDistance != 16) { // �ھ�Ŀǰ���Ե����·�ɣ��򣺸��£��˴��������࣬��ֹ��10 + 13 > 16 ��δ���£�
                        int newDistance = distance + neighborDistance;
                        tempItem.setDistance(newDistance);
                        tempItem.setNextHop(neighborId);
                        // ͬʱ���½���ı�� : �����nextHop
                        tempModel.setValueAt(newDistance, i, 1);
                        tempModel.setValueAt((char) (neighborId + 65), i, 2);
                    }
                    // �ھ�ĿǰҲ�޷������·��, �Ը�·����˵���˴θ����ޱ仯
                } else { // �������չ�ʽ����
                    int tempDistance = distance + neighborDistance;
                    if (nowDistance > tempDistance) {
                        tempItem.setDistance(tempDistance);
                        tempItem.setNextHop(neighborId);
                        // ͬ�������½���ı�� : �����nextHop
                        tempModel.setValueAt(tempDistance, i, 1);
                        tempModel.setValueAt((char) (neighborId + 65), i, 2);
                    }
                }
            }
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ArrayList<RouterTableItem> getRouterTableItems() {
        return routerTableItems;
    }

    public static ArrayList<Integer> getRouterTabel() {
        return routerTabel;
    }

    public void setRouterTableItems(ArrayList<RouterTableItem> routerTableItems) {
        this.routerTableItems = routerTableItems;
    }

}
