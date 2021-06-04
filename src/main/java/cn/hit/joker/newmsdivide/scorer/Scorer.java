package cn.hit.joker.newmsdivide.scorer;

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.criteria.*;
import cn.hit.joker.newmsdivide.scorer.criterionScorer.ClassScorer;
import cn.hit.joker.newmsdivide.scorer.criterionScorer.CriterionScorer;
import cn.hit.joker.newmsdivide.scorer.criterionScorer.GroupScorer;
import cn.hit.joker.newmsdivide.scorer.criterionScorer.QualityScorer;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/2 15:57
 * @description 汇总所有 类对 拥有的所有原则实例打分
 */
@Slf4j
public class Scorer {
    // set constant
    public static final double MAX_SCORE = 10d;
    public static final double MIN_SCORE = -10d;
    public static final double NO_SCORE = 0d;

    /**
     * get scoreMap of input msDivideSystem
     *
     * @param msDivideSystem ms divide system
     * @return score map
     */
    public Map<ClassPair, Map<String, Score>> getScores(final MsDivideSystem msDivideSystem) {
        Map<ClassPair, Map<String, Score>> scoreMap = new HashMap<>();

        // score for criterion instance list
        criterionScore(scoreMap, msDivideSystem.getCriterionInstanceList());
        // score for quality instance list
        qualityScore(scoreMap, msDivideSystem.getQualityInstanceList(), msDivideSystem.getClassList());
        // score for communicate instance
        communicateScore(scoreMap, msDivideSystem.getCommunicateScore());
        // score for resource
        resourceScore(scoreMap, msDivideSystem.getClassList());

        log.info("根据各种规则实例为 classPair 打分完成！");
        return scoreMap;
    }
    /**
     * get original scoreMap of input msDivideSystem
     *
     * @param msDivideSystem ms divide system
     * @return score map
     */
    public Map<ClassPair, Map<String, Score>> getOriginalScores(final MsDivideSystem msDivideSystem) {
        Map<ClassPair, Map<String, Score>> scoreMap = new HashMap<>();

        // score for criterion instance list
        criterionScore(scoreMap, msDivideSystem.getCriterionInstanceList());

        log.info("根据原始规则实例为 classPair 打分完成！");
        return scoreMap;
    }


    private void criterionScore(Map<ClassPair, Map<String, Score>> result, List<CriterionInstance> criterionInstanceList) {
        criterionInstanceList.forEach(instance -> {
            CriterionScorer scorer = getScorer(instance);
            if (instance.getClassPairList().size() > 0) {
                Map<ClassPair, Double> scoreMap = scorer.getScore(instance);
                addScoresToResultByCriterion(instance, result, scoreMap);
            }
        });
        log.info("为耦合度规则实例打分完成！");
    }

    private void qualityScore(Map<ClassPair, Map<String, Score>> result, List<QualityInstance> qualityInstanceList, List<UmlClass> classList) {
        QualityScorer scorer = new QualityScorer();

        qualityInstanceList.forEach(qualityInstance -> {
            if ((qualityInstance.getClassSupportDegree() == null && qualityInstance.getSupportClassPairs().size() > 0)
                    || (qualityInstance.getSupportClassPairs() == null && qualityInstance.getClassSupportDegree().size() > 0)) {
                Map<ClassPair, Double> scoreMap = scorer.getScore(qualityInstance, classList);
                addScoresToResultByQuality(qualityInstance, result, scoreMap);
            }
        });

        log.info("为价值、质量规则实例按照质量打分器 打分完成！");
    }

    private void communicateScore(Map<ClassPair, Map<String, Score>> result, Map<ClassPair, Double> communicate) {
        for (Map.Entry<ClassPair, Double> entry: communicate.entrySet()) {
            ClassPair classPair = entry.getKey();
            Double score = entry.getValue();

            // if entityPair A == B, jump out!
            if (!classPair.getClassA().equals(classPair.getClassB())) {
                // init entityPair's Score Map
                result.computeIfAbsent(classPair, k -> new HashMap<>());
                // put score
                result.get(classPair).put(String.valueOf(CriterionName.Communication), new Score(
                        score,
                        CriterionPriority.Communicate
                ));
            }

        }
        log.info("为通讯规则实例打分完成！");
    }

    // TODO: 未完成
    private void resourceScore(Map<ClassPair, Map<String, Score>> scoreMap, List<UmlClass> classList) {
        return;
    }

    private CriterionScorer getScorer(CriterionInstance instance) {
        CriterionScorer scorer = null;

        if (instance.getCriterion().getName().name().contains("Class")) {
            scorer = new ClassScorer();
        } else {
            scorer = new GroupScorer();
        }

        return scorer;
    }

    // add score map of criterion to result
    private void addScoresToResultByCriterion(CriterionInstance instance, Map<ClassPair, Map<String, Score>> result, Map<ClassPair, Double> scoreMap) {

        if (scoreMap == null || scoreMap.size() == 0) {
            return;
        }

        for (Map.Entry<ClassPair, Double> classPairScore: scoreMap.entrySet()) {
            ClassPair classPair = classPairScore.getKey();
            Double score = classPairScore.getValue();

            // if entityPair A == B, jump out!
            if (!classPair.getClassA().equals(classPair.getClassB())) {
                // init entityPair's Score Map
                result.computeIfAbsent(classPair, k -> new HashMap<>());
                // put score
                result.get(classPair).put(instance.getCriterion().getName().toString(), new Score(
                        score,
                        instance.getCriterion().getWeight()
                ));
            }
        }
    }

    // add score map of quality to result
    private void addScoresToResultByQuality(QualityInstance instance, Map<ClassPair, Map<String, Score>> result, Map<ClassPair, Double> scoreMap) {
        for(Map.Entry<ClassPair, Double> entry: scoreMap.entrySet()) {
            ClassPair classPair = entry.getKey();
            Double score = entry.getValue();

            if (classPair.getClassA().equals(classPair.getClassB())) {
                continue;
            }

            // not exist, init
            if (!result.containsKey(classPair)) {
                result.put(classPair, new HashMap<>());
            }

            result.get(classPair).put(instance.getCriterion().getName().toString(), new Score(
                    score,
                    instance.getCriterion().getWeight()
            ));

        }
    }
}
