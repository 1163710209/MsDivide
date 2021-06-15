package cn.hit.joker.newmsdivide.analyzer;

import cn.hit.joker.newmsdivide.utils.WriteFile;
import org.junit.jupiter.api.Test;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/14 16:54
 * @description
 */
public class WriteFileTest {
    @Test
    public void writeFileTest() {
        String path = "src/main/resources/cases/healthPension/divideResult";
        String name = "1.txt";
        WriteFile.writeToFile(path, "a test \na test", name);
    }
}
