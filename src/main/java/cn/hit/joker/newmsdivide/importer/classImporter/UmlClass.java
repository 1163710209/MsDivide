package cn.hit.joker.newmsdivide.importer.classImporter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/20 21:12
 * @description uml class entity
 */
@Data
@NoArgsConstructor
public class UmlClass {
    private String name;
    private ClassType type;
    private List<String> attributes;
    private List<String> methods;
    private Deploy deploy;

    enum ClassType {
        Entity,
        Control
    }

    public UmlClass(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UmlClass) {
            return this.name.equals(((UmlClass) obj).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
