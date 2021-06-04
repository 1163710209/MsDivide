package cn.hit.joker.newmsdivide.importer.sequenceImporter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/26 21:26
 * @description
 */
@Data
@NoArgsConstructor
public class Activity {
    private List<MethodCall> methodCalls;
}
