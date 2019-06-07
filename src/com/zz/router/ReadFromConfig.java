package com.zz.router;
/**
*@author Zhang Zhen
*@time 2019年6月5日 下午12:48:45
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ReadFromConfig {
    private static BufferedReader bf = null;

    public static ArrayList<Integer> readUDPports(String UDPConfigPath) {
        ArrayList<Integer> UDPPortsLists = new ArrayList<>();
        try {
            bf = new BufferedReader(new FileReader(UDPConfigPath));
            String line = null;
            while ((line = bf.readLine()) != null) {
                UDPPortsLists.add(Integer.parseInt(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UDPPortsLists;
    }

    public static ArrayList<ArrayList<Integer>> readTopology(String topologyConfigPath) {
        ArrayList<ArrayList<Integer>> topologyLists = new ArrayList<>();
        try {
            bf = new BufferedReader(new FileReader(topologyConfigPath));
            String line = null;
            while ((line = bf.readLine()) != null) {
                ArrayList<Integer> tempList = new ArrayList<>();
                String[] strings = line.trim().split(" ");
                for (String string : strings) {
                    tempList.add(Integer.parseInt(string));
                }
                topologyLists.add(tempList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topologyLists;
    }

}
