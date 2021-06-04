package cn.hit.joker.newmsdivide.importer.sequenceImporter;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/26 21:18
 * @description method call entity in sequence
 */
@Data
@NoArgsConstructor
public class MethodCall {
    private int num;
    private String from;
    private String to;
    private String method;
    private RunTime runTime;
}
