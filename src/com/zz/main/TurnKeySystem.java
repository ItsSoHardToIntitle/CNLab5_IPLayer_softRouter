package com.zz.main;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhang Zhen
 * @time 2019年6月5日 下午2:00:21
 */
public class TurnKeySystem {

    private static List<Integer> UDPPortsList = new ArrayList<>();
    // private static final String ROOT_PATH =
    // "C:\\Users\\ASUS\\Desktop\\process\\softRouter";
    private static final String ROOT_PATH = System.getProperty("user.dir") + "\\softRouter";

    public static void main(String[] args) {
        // Step1. 读取端口配置文件
        UDPPortsList = ReadFromConfig.readUDPports("Config\\UDPPortsInfo.txt");

        // Step2. 开启多个进程，参数：端口号
        int routerNum = UDPPortsList.size();
        List<String[]> stringsList = new ArrayList<>();
        for (int i = 0; i < routerNum; i++) {
            String[] strings = new String[2];
            strings[0] = ROOT_PATH + (i + 1) + ".exe";
            strings[1] = String.valueOf(UDPPortsList.get(i));
            stringsList.add(strings);
        }
        Runtime runtime = Runtime.getRuntime();
        for (String[] eachList : stringsList) {
            try {
                runtime.exec(eachList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
