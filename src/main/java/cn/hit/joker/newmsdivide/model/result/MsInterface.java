package cn.hit.joker.newmsdivide.model.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/2 15:35
 * @description
 */
@Data
@NoArgsConstructor
public class MsInterface {
    private String className;
    private String methodName;
    private List<String> msNameList;
}
