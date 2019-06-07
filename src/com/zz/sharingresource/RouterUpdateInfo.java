package com.zz.sharingresource;
/**
*@author Zhang Zhen
*@time 2019年6月6日 下午2:17:30
*/

import java.io.Serializable;
import java.util.ArrayList;

import com.zz.router.RouterTableItem;

public class RouterUpdateInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private ArrayList<RouterTableItem> routerTableItems;

    public RouterUpdateInfo(int id, ArrayList<RouterTableItem> routerTableItems) {
        this.id = id;
        this.routerTableItems = routerTableItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<RouterTableItem> getRouterTableItems() {
        return routerTableItems;
    }

    public void setRouterTableItems(ArrayList<RouterTableItem> routerTableItems) {
        this.routerTableItems = routerTableItems;
    }

}
