package cn.hit.joker.kmeans;

import lombok.Data;

import java.util.List;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/4 14:42
 * @description class node
 */
@Data
public class ClassNode {
    private List<String> data;
    public ClassNode(List<String> nodes) {
        this.data = nodes;
    }

    /**
     * 获取下标为 num 的节点
     *
     * @param num num
     * @return node
     */
    public String getNodeName(int num) {
        return data.get(num);
    }

    /**
     * get data size
     * @return data size
     */
    public int size() {
        return data.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        data.forEach(node -> {
            builder.append(node).append(", ");
        });
        builder.append("]");
        return builder.toString();
    }
}
