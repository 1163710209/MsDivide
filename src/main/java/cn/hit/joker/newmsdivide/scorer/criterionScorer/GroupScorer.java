package cn.hit.joker.newmsdivide.scorer.criterionScorer;

import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.model.criteria.CriterionInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/2 16:17
 * @description
 */
public class GroupScorer implements CriterionScorer {
    private final static double DDDAggregateScore = 1.0d;
    private final static double DDDServiceScore = 1.0d;
    private final static double DDDValueObjectScore = 1.0d;
    private final static double LifeCycleCommonalityScore = 1.0d;

    @Override
    public Map<ClassPair, Double> getScore(CriterionInstance instance) {
        Map<ClassPair, Double> scoreMap = new HashMap<>();

        double defaultScore = 0d;
        switch (instance.getCriterion().getName()) {
            case DDDAggregate:
                defaultScore = DDDAggregateScore;
                break;
            case DDDService:
                defaultScore = DDDServiceScore;
                break;
            case DDDValueObject:
                defaultScore = DDDValueObjectScore;
                break;
            case LifeCycleCommonality:
                defaultScore = LifeCycleCommonalityScore;
                break;
        }

        double finalDefaultScore = defaultScore;
        instance.getClassPairList().forEach(classPair -> {
            scoreMap.put(classPair, finalDefaultScore + scoreMap.getOrDefault(classPair, 0d));
        });

        return scoreMap;
    }
}
