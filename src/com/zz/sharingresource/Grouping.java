package com.zz.sharingresource;

import java.io.Serializable;

/**
 * @author Zhang Zhen
 * @time 2019年6月5日 下午1:09:24
 */
public class Grouping implements Serializable {
    private static final long serialVersionUID = 1L;

    private int sourcePort;
    private int destinationPort;
    private int ttl;
    // private String data;

    public Grouping(int sourcePort, int destinationPort, int ttl) {
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.ttl = ttl;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

}
