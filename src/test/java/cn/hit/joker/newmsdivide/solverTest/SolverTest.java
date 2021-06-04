package cn.hit.joker.newmsdivide.solverTest;

import cn.hit.joker.newmsdivide.importer.ImporterUtils;
import cn.hit.joker.newmsdivide.importer.InputData;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.SequenceDiagram;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.solver.SolveSystem;
import cn.hit.joker.newmsdivide.solver.SolverConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/3 16:52
 * @description
 */
public class SolverTest {
    private InputData getInputData() {
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
        return inputData;
    }

    @Test
    public void solveTest() {
        InputData inputData = getInputData();
        MsDivideSystem divideSystem = inputData.getMsDivideSystem();
        SolverConfig whisperConfig = SolverConfig.getChineseWhisperConfig();
        SolverConfig markovConfig = SolverConfig.getMarkovConfig();
        SolverConfig fastNewmanConfig = SolverConfig.getFastNewmanConfig();
        SolverConfig gephiConfig = SolverConfig.getGephiConfig(5);

        System.out.println(divideSystem);
//        System.out.println(whisperConfig);

        // start solve
//        List<Microservice> msList = new SolveSystem().solveSystem(divideSystem, whisperConfig, "Score");
//        List<Microservice> msList = new SolveSystem().solveSystem(divideSystem, markovConfig, "Score");
//        List<Microservice> msList = new SolveSystem().solveSystem(divideSystem, fastNewmanConfig, "Score");
        List<Microservice> msList = new SolveSystem().solveSystem(divideSystem, gephiConfig, SolveSystem.TYPE_NORMAL);
        System.out.println(msList);
    }
}
