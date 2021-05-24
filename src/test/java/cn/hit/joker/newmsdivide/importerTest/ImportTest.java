package cn.hit.joker.newmsdivide.importerTest;

import cn.hit.joker.newmsdivide.importer.ImporterUtils;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import org.junit.jupiter.api.Test;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/20 21:22
 * @description
 */
public class ImportTest {

    @Test
    public void classImportTest() {
        String path = "cases/case1 ddd cargo transport/class.json";
        try {
            ClassDiagram classDiagram = ImporterUtils.importClassDiagram(path);
            System.out.println(classDiagram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
