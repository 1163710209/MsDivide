package cn.hit.joker.newmsdivide.importerTest;

import cn.hit.joker.newmsdivide.importer.ImporterUtils;
import cn.hit.joker.newmsdivide.importer.InputData;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.SequenceDiagram;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
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
        String path = "cases/healthPension/class.json";
        try {
            ClassDiagram classDiagram = ImporterUtils.importClassDiagram(path);
            System.out.println(classDiagram);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sequenceImportTest() {
        String path = "cases/healthPension/nurseServiceSequence.json";
        try {
            SequenceDiagram sequenceDiagram = ImporterUtils.importSequenceDiagram(path);
            System.out.println(sequenceDiagram);
            System.out.println(sequenceDiagram.getIndicatorsName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMsDivideSystemTest() {
        String classPath = "cases/case1 ddd cargo transport/class.json";
        String sequencePath = "cases/case1 ddd cargo transport/sequence.json";
        InputData inputData = null;
        try {
            ClassDiagram classDiagram = ImporterUtils.importClassDiagram(classPath);
            SequenceDiagram sequenceDiagram = ImporterUtils.importSequenceDiagram(sequencePath);
            inputData = new InputData(classDiagram, sequenceDiagram);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MsDivideSystem msDivideSystem = inputData.getMsDivideSystem();
        System.out.println(msDivideSystem);
    }

}
