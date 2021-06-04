package cn.hit.joker.newmsdivide.model.result;

import cn.hit.joker.newmsdivide.importer.classImporter.ClassRelation;
import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<MsInterface> interfaceList;

    public Microservice(String name, List<UmlClass> classList) {
        this.name = name;
        this.classList = classList;
    }

    @Override
    public String toString() {
        return "[name=" + name + ", classList=" + classList +  "]";
    }

}
