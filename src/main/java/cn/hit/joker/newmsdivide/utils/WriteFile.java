package cn.hit.joker.newmsdivide.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/14 16:40
 * @description write divide result to txt file
 */
public class WriteFile {
    /**
     * write
     *
     * @param pathString 要写文件的路径
     * @param data write data
     */
    public static void writeToFile(String pathString, String data, String name) {
        pathString += "\\" + name;
        Path path = Paths.get(pathString);
        // 使用newBufferedWriter创建文件并写文件
        // 这里使用了try-with-resources方法来关闭流，不用手动关闭
        try (BufferedWriter writer =
                     Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //追加写模式
//        try (BufferedWriter writer =
//                     Files.newBufferedWriter(path,
//                             StandardCharsets.UTF_8,
//                             StandardOpenOption.APPEND)){
//            writer.write("Hello World -字母哥!!");
//        }
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
//                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
//                delFolder(path + "/" + tempList[i]);//再删除空文件夹
//                flag = true;
                throw new IllegalArgumentException("目录内不应包含文件夹！");

            }
        }
        return true;
    }

}
