package cn.hit.joker.newmsdivide.importerTest;

import cn.hit.joker.newmsdivide.importer.ImporterUtils;
import cn.hit.joker.newmsdivide.importer.InputData;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.SequenceDiagram;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
        String path1 = "cases/healthPension/nurseServiceSequence.json";
        String path2 = "cases/healthPension/bodyInfoCollectSequence.json";
        String path3 = "cases/healthPension/homeDoctorServiceSequence.json";
        String path4 = "cases/healthPension/slowSickTreatmentSequence.json";

        try {
            SequenceDiagram sequenceDiagram1 = ImporterUtils.importSequenceDiagram(path1);
            System.out.println(sequenceDiagram1);
            System.out.println(sequenceDiagram1.getIndicatorsName());
            System.out.println("-----------------------------------------------");

            SequenceDiagram sequenceDiagram2 = ImporterUtils.importSequenceDiagram(path2);
            System.out.println(sequenceDiagram2);
            System.out.println(sequenceDiagram2.getIndicatorsName());
            System.out.println("-----------------------------------------------");

            SequenceDiagram sequenceDiagram3 = ImporterUtils.importSequenceDiagram(path3);
            System.out.println(sequenceDiagram3);
            System.out.println(sequenceDiagram3.getIndicatorsName());
            System.out.println("-----------------------------------------------");

            SequenceDiagram sequenceDiagram4 = ImporterUtils.importSequenceDiagram(path4);
            System.out.println(sequenceDiagram4);
            System.out.println(sequenceDiagram4.getIndicatorsName());
            System.out.println("-----------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMsDivideSystemTest() {
        String classPath = "cases/dddCargo/class.json";
        String sequencePath = "cases/dddCargo/sequence.json";
        InputData inputData = null;
        try {
            ClassDiagram classDiagram = ImporterUtils.importClassDiagram(classPath);
            SequenceDiagram sequenceDiagram = ImporterUtils.importSequenceDiagram(sequencePath);
            List<SequenceDiagram> sequenceDiagrams = new ArrayList<>();
            sequenceDiagrams.add(sequenceDiagram);
            inputData = new InputData(classDiagram, sequenceDiagrams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MsDivideSystem msDivideSystem = inputData.getMsDivideSystem();
        System.out.println(msDivideSystem);
    }

}
