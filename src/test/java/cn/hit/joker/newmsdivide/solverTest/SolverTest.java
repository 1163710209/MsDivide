package cn.hit.joker.newmsdivide.solverTest;

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
    public void ChineseWhisperSolverTest() {
        MainSystem.start(SolveSystem.MODE_CW, 0);
    }

    @Test
    public void MarkovSolverTest() {
        MainSystem.start(SolveSystem.MODE_MARKOV, 0);
    }

    @Test
    public void FastNewmanSolverTest() {
        MainSystem.start(SolveSystem.MODE_FAST_NEWMAN, 0);
    }

    @Test
    public void GNSolverTest() {
        MainSystem.start(SolveSystem.MODE_GEPHI, 3);
    }

    @Test
    public void DivideResultTest() {
        MainSystem.getDivideResult(SolveSystem.MODE_GEPHI);
    }
}
