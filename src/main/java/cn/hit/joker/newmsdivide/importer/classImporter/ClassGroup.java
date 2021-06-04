package cn.hit.joker.newmsdivide.importer.classImporter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/20 21:23
 * @description class group for ddd aggregate
 */
@Data
@NoArgsConstructor
public class ClassGroup {
    private Type name;
    private List<UmlClass> classList;

    enum Type {
        DDDValueObject,
        DDDAggregate,
        DDDService,
        LifeCycleCommonality
    }
}
