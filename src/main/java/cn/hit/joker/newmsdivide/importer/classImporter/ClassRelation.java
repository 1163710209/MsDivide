package cn.hit.joker.newmsdivide.importer.classImporter;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/20 21:17
 * @description
 */
@Data
@NoArgsConstructor
public class ClassRelation {
    private UmlClass origin;
    private UmlClass destination;
    private RelationType type;

    public enum RelationType {
        ClassAggregate,
        ClassComposition,
        ClassInheritance
    }
}
