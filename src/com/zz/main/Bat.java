package com.zz.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Bat {

    public static void singleFileCopy(String srcPathStr, String desPathStr, int x) {
        if (!desPathStr.endsWith(File.separator)) {
            desPathStr += File.separator + srcPathStr.substring(srcPathStr.lastIndexOf("\\") + 1);
        }
        if (new File(desPathStr).exists()) {
            int index = desPathStr.lastIndexOf(".");
            String nameBefor = desPathStr.substring(0, index);
            String suffix = desPathStr.substring(index);
            desPathStr = nameBefor + x + suffix;
        }
        try {
            FileInputStream fis = new FileInputStream(srcPathStr);
            FileOutputStream fos = new FileOutputStream(desPathStr);
            byte datas[] = new byte[1024 * 8];
            int len = 0;
            while ((len = fis.read(datas)) != -1) {
                fos.write(datas, 0, len);
            }
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        // String path = "C:\\Users\\ASUS\\Desktop\\process\\softRouter.exe";
        String path = System.getProperty("user.dir") + "\\softRouter.exe";
        // 复制.exe文件且重命名
        String desPath = path.substring(0, path.lastIndexOf("\\"));
        for (int i = 0; i < 5; i++) {
            singleFileCopy(path, desPath, i + 1);
        }
    }
}
