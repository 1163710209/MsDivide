package cn.hit.joker.newmsdivide.importer.classImporter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/20 21:16
 * @description class diagram
 */
@Data
@NoArgsConstructor
public class ClassDiagram {
    private String name;
    private List<UmlClass> classList;
    private List<ClassRelation> classRelationList;
    private List<ClassGroup> classGroups;

    public UmlClass getUmlClassByName(String name) {
        for (UmlClass umlClass : classList) {
            if (umlClass.getName().equals(name)) {
                return umlClass;
            }
        }
        return null;
    }

}
