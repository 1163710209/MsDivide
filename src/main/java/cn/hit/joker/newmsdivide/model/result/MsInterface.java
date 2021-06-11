package cn.hit.joker.newmsdivide.model.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private Set<String> msNameList;

    public MsInterface(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
        this.msNameList = new HashSet<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MsInterface) {
            MsInterface msInterface = (MsInterface) obj;
            return this.className.equals(msInterface.getClassName()) && this.methodName.equals(msInterface.getMethodName());
        }
        return false;
    }

}
