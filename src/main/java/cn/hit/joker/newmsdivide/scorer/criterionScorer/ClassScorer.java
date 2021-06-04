package cn.hit.joker.newmsdivide.scorer.criterionScorer;

import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.model.criteria.CriterionInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/2 16:11
 * @description
 */
public class ClassScorer implements CriterionScorer {
    private final static double AggregateScore = 1.0d;
    private final static double CompositionScore = 1.0d;
    private final static double InheritanceScore = 1.0d;

    @Override
    public Map<ClassPair, Double> getScore(CriterionInstance instance) {
        Map<ClassPair, Double> scoreMap = new HashMap<>();

        double defaultScore = 0d;
        switch (instance.getCriterion().getName()) {
            case ClassAggregate:
                defaultScore = AggregateScore;
                break;
            case ClassComposition:
                defaultScore = CompositionScore;
                break;
            case ClassInheritance:
                defaultScore = InheritanceScore;
                break;
        }

        double finalDefaultScore = defaultScore;
        instance.getClassPairList().forEach(classPair -> {
            scoreMap.put(classPair, finalDefaultScore + scoreMap.getOrDefault(classPair, 0d));
        });

        return scoreMap;
    }
}
