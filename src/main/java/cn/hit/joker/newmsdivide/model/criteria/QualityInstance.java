package cn.hit.joker.newmsdivide.model.criteria;

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.utils.GenerateId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class QualityInstance {
    private String id;
    private Criterion criterion;
    private List<SupportClassPair> supportClassPairs;
    private HashMap<String, Double> classSupportDegree;

    public QualityInstance(String id, Criterion criterion, List<SupportClassPair> supportClassPairs) {
        this.id = id;
        this.criterion = criterion;
        this.supportClassPairs = supportClassPairs;
    }

    public QualityInstance(String id, Criterion criterion, HashMap<String, Double> classSupportDegree) {
        this.id = id;
        this.criterion = criterion;
        this.classSupportDegree = classSupportDegree;
    }

    public static QualityInstance getQualityInstance(CriterionName name) {
        String id = GenerateId.getId();
        Criterion criterion = Criterion.getCriterion(name);

        if (name == CriterionName.BusinessQualityCouping) {
            List<SupportClassPair> supportClassPairs = new ArrayList<>();
            return new QualityInstance(id, criterion, supportClassPairs);
        } else if (name == CriterionName.BusinessQualityCriticality) {
            HashMap<String, Double> classSupportDegree = new HashMap<>();
            return new QualityInstance(id, criterion, classSupportDegree);
        } else {
            throw new IllegalArgumentException("错误的划分原则名称！");
        }
    }

}

