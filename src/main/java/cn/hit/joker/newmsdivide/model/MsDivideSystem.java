package cn.hit.joker.newmsdivide.model;

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.model.criteria.CriterionInstance;
import cn.hit.joker.newmsdivide.model.criteria.QualityInstance;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/26 21:54
 * @description
 */
@Data
public class MsDivideSystem {
    private String id;
    private String name;
    private List<UmlClass> classList;
    private List<CriterionInstance> criterionInstanceList;
    private List<QualityInstance> qualityInstanceList;
    private Map<ClassPair, Double> communicateScore;
    private List<Microservice> microserviceList;

    @Override
    public String toString() {
        String builder = "id: " + id + "\n" +
                "name: " + name + "\n" +
                "classList: " + classList + "\n" +
                "criterionInstanceList: " + criterionInstanceList + "\n" +
                "qualityInstanceList: " + qualityInstanceList + "\n" +
                "communicateScore: " + communicateScore + "\n";
        return builder;
    }
}
