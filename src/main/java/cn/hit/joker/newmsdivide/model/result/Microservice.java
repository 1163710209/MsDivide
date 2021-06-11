package cn.hit.joker.newmsdivide.model.result;

import cn.hit.joker.newmsdivide.importer.classImporter.ClassRelation;
import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/2 15:32
 * @description
 */
@Data
@NoArgsConstructor
public class Microservice {
    private String name;
    private List<UmlClass>classList;
    private List<ClassRelation> classRelationList;
    private Set<MsInterface> interfaceList;
    private Map<String, Double> qualitySupportDegree;

    public Microservice(String name, List<UmlClass> classList) {
        this.name = name;
        this.classList = classList;
        this.classRelationList = new ArrayList<>();
        this.interfaceList = new HashSet<>();
        this.qualitySupportDegree = new HashMap<>();
    }

    @Override
    public String toString() {
        return "\n微服务" + name + "\n\t classList = " + classList +
                "\n\t classRelationList = " + classRelationList +
                "\n\t interfaceList = " + interfaceList +
                "\n\t qualitySupportDegree = " + qualitySupportDegree +
                "\n ---------------------------------------------------------\n";
    }

}
