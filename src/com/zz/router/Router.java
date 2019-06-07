package com.zz.router;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.zz.sharingresource.RouterUpdateInfo;
import com.zz.softrouter.GUI;

/**
 * @author Zhang Zhen
 * @time 2019年6月5日 下午12:50:02
 */
public class Router implements Serializable {
    private static final long serialVersionUID = 1L;

    private static List<ArrayList<Integer>> topologyList = null;
    // 相当于0-1矩阵，后续用来判断是否是邻居关系
    private static ArrayList<Integer> routerTabel = null;

    private int id;
    private int port; // 以端口号也作为标识
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
        // 更新界面表格
        DefaultTableModel tempModel = GUI.getRouterInfoModel();
        // 到该邻居的距离
        int neighborId = routerUpdateInfo.getId();
        int distance = routerTableItems.get(neighborId).getDistance();
        for (int i = 0; i < routerTableItems.size(); i++) {
            if (i != id) { // 自己到自己是不会变的
                RouterTableItem tempItem = routerTableItems.get(i);
                int nowDistance = tempItem.getDistance(); // 当前的距离
                int neighborDistance = routerUpdateInfo.getRouterTableItems().get(i).getDistance(); // 邻居的
                if (nowDistance == 16) { // 当前自己不可达
                    if (neighborDistance != 16) { // 邻居目前可以到达该路由，则：更新（此处单独分类，防止：10 + 13 > 16 而未更新）
                        int newDistance = distance + neighborDistance;
                        tempItem.setDistance(newDistance);
                        tempItem.setNextHop(neighborId);
                        // 同时更新界面的表格 : 距离和nextHop
                        tempModel.setValueAt(newDistance, i, 1);
                        tempModel.setValueAt((char) (neighborId + 65), i, 2);
                    }
                    // 邻居目前也无法到达该路由, 对该路由来说，此次更新无变化
                } else { // 正常按照公式更新
                    int tempDistance = distance + neighborDistance;
                    if (nowDistance > tempDistance) {
                        tempItem.setDistance(tempDistance);
                        tempItem.setNextHop(neighborId);
                        // 同样，更新界面的表格 : 距离和nextHop
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
