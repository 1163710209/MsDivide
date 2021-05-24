package cn.hit.joker.newmsdivide.importer.classImporter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
}
