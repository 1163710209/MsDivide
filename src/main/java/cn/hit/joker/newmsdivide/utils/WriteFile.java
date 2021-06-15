package cn.hit.joker.newmsdivide.utils;

import java.io.BufferedWriter;
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
}
