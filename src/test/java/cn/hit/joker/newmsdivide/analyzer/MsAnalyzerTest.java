package cn.hit.joker.newmsdivide.analyzer;

import cn.hit.joker.newmsdivide.MainSystem;
import cn.hit.joker.newmsdivide.importer.ImporterUtils;
import cn.hit.joker.newmsdivide.importer.InputData;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.SequenceDiagram;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.solver.SolveSystem;
import cn.hit.joker.newmsdivide.solver.SolverConfig;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/9 21:44
 * @description analyzer test
 */
public class MsAnalyzerTest {

    private InputData getInputData() {
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
        return inputData;
    }

//    @Test
//    public void addClassRelationTest() {
//        InputData inputData = getInputData();
//        MsDivideSystem divideSystem = inputData.getMsDivideSystem();
//        SolverConfig solverConfig = SolverConfig.getFastNewmanConfig();
//        List<Microservice> msList = new SolveSystem().solveSystem(divideSystem, solverConfig, SolveSystem.TYPE_NEW);
//        MicroserviceAnalyzer.addClassRelationToMs(msList, getInputData().getClassDiagram());
//        System.out.println(msList);
//    }
//
//    @Test
//    public void addInterfaceListTest() {
//        InputData inputData = getInputData();
//        MsDivideSystem divideSystem = inputData.getMsDivideSystem();
//        SolverConfig solverConfig = SolverConfig.getFastNewmanConfig();
//        List<Microservice> msList = new SolveSystem().solveSystem(divideSystem, solverConfig, SolveSystem.TYPE_NEW);
//        MicroserviceAnalyzer.addClassRelationToMs(msList, inputData.getClassDiagram());
//        MicroserviceAnalyzer.addInterfaceListToMs(msList, inputData.getSequenceDiagram());
//        System.out.println(msList);
//    }
//
//    @Test
//    public void addQualityDegreeTest() {
//        InputData inputData = getInputData();
//        MsDivideSystem divideSystem = inputData.getMsDivideSystem();
//        SolverConfig solverConfig = SolverConfig.getFastNewmanConfig();
//        List<Microservice> msList = new SolveSystem().solveSystem(divideSystem, solverConfig, SolveSystem.TYPE_NEW);
//        MicroserviceAnalyzer.addQualityDegreeToMs(msList, inputData.getSequenceDiagram());
//        System.out.println(msList);
//    }

    @Test
    public void addAllToMsTest() {
        InputData inputData = getInputData();
        MsDivideSystem divideSystem = inputData.getMsDivideSystem();
        SolverConfig solverConfig = SolverConfig.getFastNewmanConfig();
        List<Microservice> msList = new SolveSystem().solveSystem(divideSystem, solverConfig, SolveSystem.TYPE_NEW);
        MicroserviceAnalyzer.addAllToMs(msList, inputData);
        System.out.println(msList);
    }

    @Test
    public void computeTest() {
        InputData inputData = getInputData();
        MsDivideSystem divideSystem = inputData.getMsDivideSystem();
        SolverConfig solverConfig = SolverConfig.getFastNewmanConfig();
        List<Microservice> msList = new SolveSystem().solveSystem(divideSystem, solverConfig, SolveSystem.TYPE_NEW);
        MicroserviceAnalyzer.addAllToMs(msList, inputData);
        System.out.println(msList);

        double cohesionDegree = MicroserviceAnalyzer.getCohesionDegree(msList, inputData.getClassDiagram());
        double coupingDegree = MicroserviceAnalyzer.getCoupingDegree(msList, inputData.getClassDiagram());

        System.out.println("聚合度为：" + cohesionDegree);
        System.out.println("耦合度为：" + coupingDegree);
    }

    @Test
    public void MsSolutionListTest() {
        InputData inputData = getInputData();
        List<List<Microservice>> solutionList = MainSystem.getDivideResult(SolveSystem.MODE_GEPHI, inputData, 4);

        solutionList.forEach(microserviceList -> {
            MicroserviceAnalyzer.addAllToMs(microserviceList, inputData);

            double cohesionDegree = MicroserviceAnalyzer.getCohesionDegree(microserviceList, inputData.getClassDiagram());
            double coupingDegree = MicroserviceAnalyzer.getCoupingDegree(microserviceList, inputData.getClassDiagram());
            System.out.println("------------------------------");
            System.out.println("微服务划分方案为：");
            System.out.println("微服务的数量为：" + microserviceList.size());
            System.out.println("聚合度为：" + cohesionDegree);
            System.out.println("耦合度为：" + coupingDegree);
            System.out.println("-----------------------------------------------");
        });
    }

    @Test
    public void FastNewManTest() {
        InputData inputData = getInputData();
        MsDivideSystem msDivideSystem = inputData.getMsDivideSystem();
        List<Microservice> msList = MainSystem.start(SolveSystem.MODE_FAST_NEWMAN, 0, msDivideSystem, 4);
        msList.forEach(microservice -> {
            microservice.setDeployLocationSet(MainSystem.checkDeployLocation(microservice, inputData.getClassDiagram().getClassList()));
        });
        System.out.println(msList);
        double cohesionDegree = MicroserviceAnalyzer.getCohesionDegree(msList, inputData.getClassDiagram());
        double coupingDegree = MicroserviceAnalyzer.getCoupingDegree(msList, inputData.getClassDiagram());
        double communicatePrice = MicroserviceAnalyzer.getCommunicatePrice(msList, inputData.getSequenceDiagramList());
        System.out.println("\n\n-----------------------------------");
        System.out.println("微服务的数量为：" + msList.size());
        System.out.println("聚合度为：" + cohesionDegree);
        System.out.println("耦合度为：" + coupingDegree);
        System.out.println("通信代价为：" + communicatePrice);
    }

}
