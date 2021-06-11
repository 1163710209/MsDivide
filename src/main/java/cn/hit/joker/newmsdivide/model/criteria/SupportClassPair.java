package cn.hit.joker.newmsdivide.model.criteria;

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.IndicatorName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/1 21:01
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportClassPair {
    private String umlClassA;
    private String umlClassB;

    private double degreeA;
    private double degreeB;

    private String name;
    private double priority;
}
