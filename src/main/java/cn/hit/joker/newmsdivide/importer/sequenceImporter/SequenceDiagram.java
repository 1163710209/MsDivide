package cn.hit.joker.newmsdivide.importer.sequenceImporter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/26 17:03
 * @description sequence diagram entity
 */
@Data
@NoArgsConstructor
public class SequenceDiagram {
    private String name;
    private List<Activity> activityList;
    private List<Indicator> indicators;

    public List<String> getIndicatorsName() {
        return indicators.stream().map(Indicator::getName).collect(Collectors.toList());
    }
}
