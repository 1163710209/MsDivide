package cn.hit.joker.newmsdivide.scorer.criterionScorer;

import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.model.criteria.CriterionInstance;

import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/2 16:05
 * @description
 */
public interface CriterionScorer {
    /**
     * get score from criterion instance
     *
     * @param instance criterion instance
     * @return score map
     */
    public Map<ClassPair, Double> getScore(final CriterionInstance instance);
}
