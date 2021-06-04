package cn.hit.joker.newmsdivide.importer.sequenceImporter;

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Indicator {
    private IndicatorName name;
    private double priority;
    private Map<String, Double> classList;
}
