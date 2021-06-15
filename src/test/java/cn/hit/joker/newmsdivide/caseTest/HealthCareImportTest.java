package cn.hit.joker.newmsdivide.caseTest;

import cn.hit.joker.newmsdivide.MainSystem;
import cn.hit.joker.newmsdivide.analyzer.MicroserviceAnalyzer;
import cn.hit.joker.newmsdivide.importer.ImporterUtils;
import cn.hit.joker.newmsdivide.importer.InputData;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.SequenceDiagram;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.solver.SolveSystem;
import cn.hit.joker.newmsdivide.utils.WriteFile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class HealthCareImportTest
{
    public static InputData getInputData() {
        String path0 = "cases/healthPension/class.json";
        String path1 = "cases/healthPension/nurseServiceSequence.json";
        String path2 = "cases/healthPension/bodyInfoCollectSequence.json";
        String path3 = "cases/healthPension/homeDoctorServiceSequence.json";
        String path4 = "cases/healthPension/slowSickTreatmentSequence.json";

        try {
            ClassDiagram classDiagram = ImporterUtils.importClassDiagram(path0);
            System.out.println(classDiagram);

            SequenceDiagram sequenceDiagram1 = ImporterUtils.importSequenceDiagram(path1);
            SequenceDiagram sequenceDiagram2 = ImporterUtils.importSequenceDiagram(path2);
            SequenceDiagram sequenceDiagram3 = ImporterUtils.importSequenceDiagram(path3);
            SequenceDiagram sequenceDiagram4 = ImporterUtils.importSequenceDiagram(path4);

            List<SequenceDiagram> sequenceDiagrams = new ArrayList<>();
            sequenceDiagrams.add(sequenceDiagram1);
            sequenceDiagrams.add(sequenceDiagram2);
            sequenceDiagrams.add(sequenceDiagram3);
            sequenceDiagrams.add(sequenceDiagram4);

            InputData inputData = new InputData(classDiagram, sequenceDiagrams);
            return inputData;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Test
    public void getMsDivideSystemTest() {
        InputData inputData = getInputData();
//        MsDivideSystem msDivideSystem = inputData.getMsDivideSystem();
//        System.out.println(msDivideSystem);
        List<List<Microservice>> solutionList = MainSystem.getDivideResult(SolveSystem.MODE_GEPHI, inputData);

        solutionList.forEach(microserviceList -> {
            MicroserviceAnalyzer.addAllToMs(microserviceList, inputData);

            double cohesionDegree = MicroserviceAnalyzer.getCohesionDegree(microserviceList, inputData.getClassDiagram());
            double coupingDegree = MicroserviceAnalyzer.getCoupingDegree(microserviceList, inputData.getClassDiagram());

            StringBuilder builder = new StringBuilder();
            builder.append("------------------------------\n")
                    .append("微服务划分方案为：\n")
                    .append("微服务的数量为：" + microserviceList.size() + "\n")
                    .append("微服务为：" + microserviceList + "\n")
                    .append("聚合度为：" + cohesionDegree + "\n")
                    .append("耦合度为：" + coupingDegree + "\n")
                    .append("-------------------------------------------\n");

            String path = "src/main/resources/cases/healthPension/divideResult";
            String fileName = microserviceList.size() + ".txt";
            WriteFile.writeToFile(path, builder.toString(), fileName);

//            System.out.println("微服务划分方案为：");
//            System.out.println("微服务的数量为：" + microserviceList.size());
//            System.out.println("微服务为：" + microserviceList);
//            System.out.println("聚合度为：" + cohesionDegree);
//            System.out.println("耦合度为：" + coupingDegree);
//            System.out.println("-----------------------------------------------");
        });
    }

    @Test
    public void FastNewManTest() {
        InputData inputData = getInputData();
        MsDivideSystem msDivideSystem = inputData.getMsDivideSystem();
        List<Microservice> msList = MainSystem.start(SolveSystem.MODE_FAST_NEWMAN, 0, msDivideSystem);
        double cohesionDegree = MicroserviceAnalyzer.getCohesionDegree(msList, inputData.getClassDiagram());
        double coupingDegree = MicroserviceAnalyzer.getCoupingDegree(msList, inputData.getClassDiagram());
//        System.out.println("\n\n-----------------------------------");
//        System.out.println("微服务的数量为：" + msList.size());
//        System.out.println("聚合度为：" + cohesionDegree);
//        System.out.println("耦合度为：" + coupingDegree);

        StringBuilder builder = new StringBuilder();
        builder.append("------------------------------\n")
                .append("微服务划分方案为：\n")
                .append("微服务的数量为：" + msList.size() + "\n")
                .append("微服务为：" + msList + "\n")
                .append("聚合度为：" + cohesionDegree + "\n")
                .append("耦合度为：" + coupingDegree + "\n")
                .append("-------------------------------------------\n");

        String path = "src/main/resources/cases/healthPension/divideResult";
        String fileName = msList.size() + ".txt";
        WriteFile.writeToFile(path, builder.toString(), fileName);
    }
}
