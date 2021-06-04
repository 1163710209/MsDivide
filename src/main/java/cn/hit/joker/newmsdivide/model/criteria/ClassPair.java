package cn.hit.joker.newmsdivide.model.criteria;

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import lombok.Data;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/26 22:15
 * @description class pair
 */
@Data
public class ClassPair {
    private final UmlClass classA;
    private final UmlClass classB;

    public ClassPair(UmlClass classA, UmlClass classB) {
        this.classA = classA;
        this.classB = classB;
    }

    @Override
    public boolean equals(final Object obj) {
        if(obj instanceof ClassPair) {
            ClassPair classPair = (ClassPair) obj;
            return compareTo(classPair);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        String nameA = classA.getName();
        String nameB = classB.getName();

        return nameA.hashCode() + nameB.hashCode();
    }

    private boolean compareTo(ClassPair classPair) {
        if (this.classA.getName().equals(classPair.classA.getName())) {
            return this.classB.getName().equals(classPair.classB.getName());
        } else if (this.classA.getName().equals(classPair.classB.getName())) {
            return this.classB.getName().equals(classPair.classA.getName());
        } else {
            return false;
        }
    }
}
