package com.zz.router;

import java.io.Serializable;

/**
 * @author Zhang Zhen
 * @time 2019年6月5日 下午12:52:17
 */
public class RouterTableItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int distance;
    private int nextHop;

    public RouterTableItem(int distance, int nextHop) {
        this.distance = distance;
        this.nextHop = nextHop;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getNextHop() {
        return nextHop;
    }

    public void setNextHop(int nextHop) {
        this.nextHop = nextHop;
    }

}
