package cn.hit.joker.newmsdivide.scorerTest;

import cn.hit.joker.newmsdivide.importer.ImporterUtils;
import cn.hit.joker.newmsdivide.importer.InputData;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.SequenceDiagram;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.scorer.Score;
import cn.hit.joker.newmsdivide.scorer.Scorer;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/3 14:31
 * @description test scorer
 */
public class ScorerTest {
    @Test
    public void scorerTest() {
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
        Map<ClassPair, Map<String, Score>> scoreMap = new Scorer().getScores(msDivideSystem);
        System.out.println(scoreMap);
    }
}
