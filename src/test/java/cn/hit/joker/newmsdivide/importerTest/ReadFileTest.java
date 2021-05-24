package cn.hit.joker.newmsdivide.importerTest;

import cn.hit.joker.newmsdivide.importer.ReadFile;
import org.junit.jupiter.api.Test;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/20 21:57
 * @description test read file
 */
public class ReadFileTest {
    @Test
    public void readFileTest() {
        String path = "cases/case1 ddd cargo transport/class.json";
        try {
            String fileData = ReadFile.readFromJsonFile(path);
            System.out.println(fileData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
