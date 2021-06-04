package cn.hit.joker.newmsdivide.model.criteria;
import cn.hit.joker.newmsdivide.utils.GenerateId;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *  微服务划分原则实例，包括：
 *  1. id
 *  2. criterion
 *  3. classMap
 */
@Data
public class CriterionInstance {
    private String id;
    private Criterion criterion;
    private List<ClassPair> classPairList;

    public CriterionInstance(Criterion criterion, List<ClassPair> classPairList) {
        this.id = GenerateId.getId();
        this.criterion = criterion;
        this.classPairList = classPairList;
    }

    public static CriterionInstance getCriterionInstance(CriterionName criterionName) {

        Criterion criterion = Criterion.getCriterion(criterionName);
        List<ClassPair> classPair = new ArrayList<>();

        return new CriterionInstance(criterion, classPair);
    }

}
