package cn.hit.joker.newmsdivide.scorer.criterionScorer;

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.model.criteria.CriterionName;
import cn.hit.joker.newmsdivide.model.criteria.QualityInstance;
import cn.hit.joker.newmsdivide.model.criteria.SupportClassPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/2 16:33
 * @description
 */
public class QualityScorer {
    // 描述价值、质量关键性，值越大越应该独立存在
    private final static double TVSD = 0.5d;

    public Map<ClassPair, Double> getScore(QualityInstance qualityInstance, List<UmlClass> classList) {
        Map<ClassPair, Double> scoreMap = new HashMap<>();

        if (qualityInstance.getCriterion().getName() == CriterionName.BusinessQualityCouping) {
            // handle quality couping criterion
            qualityInstance.getSupportClassPairs().forEach(supportClassPair -> {
                double score = getVCDScore(supportClassPair);
                UmlClass classA = getUmlClass(classList, supportClassPair.getUmlClassA());
                UmlClass classB = getUmlClass(classList, supportClassPair.getUmlClassB());
                if (classA == null || classB == null) {
                    throw new IllegalArgumentException("类名异常！");
                }
                ClassPair classPair = new ClassPair(classA, classB);

                scoreMap.put(classPair, score + scoreMap.getOrDefault(classPair, 0d));
            });

        }
        else if (qualityInstance.getCriterion().getName() == CriterionName.BusinessQualityCriticality) {
            // handle quality critical criterion
            // 最后处理 TVSD 原则的打分，在之前的所有实体对的打分上减去 TVSD 的打分
            qualityInstance.getClassSupportDegree().forEach((k, v) -> {
                double score = getTVSDScore(v);
                if (score > 0) {
                    classList.forEach(umlClass -> {
                        if (!umlClass.getName().equals(k)) {
                            ClassPair classPair = new ClassPair(getUmlClass(classList, k), umlClass);
                            scoreMap.put(classPair, score + scoreMap.getOrDefault(classPair, 0d));
                        }
                    });
                }
            });

        } else {
            throw new IllegalArgumentException("质量原则实例名称错误，不在支持范围内！");
        }

        return scoreMap;
    }

    // TODO: 计算 TVSD 打分(未完成)
    private double getTVSDScore(double tvsd) {
        if (tvsd > TVSD) {
            return (tvsd - TVSD);
        }
        return 0d;
    }

    // TODO: 计算 VCD 打分(未完成)
    private double getVCDScore(SupportClassPair supportClassPair) {
        double score, degreeA, degreeB;

        // TODO: 计算方式为：质量指标的优先度 * max(degreeA, degreeB)
        degreeA = supportClassPair.getDegreeA();
        degreeB = supportClassPair.getDegreeB();
        score = supportClassPair.getPriority() * (Math.max(degreeA, degreeB));

        return score;
    }

    private UmlClass getUmlClass(List<UmlClass> classList, String name) {
        for (UmlClass umlClass : classList) {
            if (umlClass.getName().equals(name)) {
                return umlClass;
            }
        }
        return null;
    }
}
