package cn.hit.joker.newmsdivide.utils;

import java.util.UUID;

public class GenerateId {
    public static String getId() {
        String uuid = UUID.randomUUID().toString().replace("-","");
        return uuid.substring(0,6);
    }
}
