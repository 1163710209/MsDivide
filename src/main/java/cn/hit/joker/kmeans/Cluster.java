package cn.hit.joker.kmeans;

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/4 14:51
 * @description cluster entity
 */
@Data
public class Cluster {
    private List<UmlClass> nodes = new ArrayList<>();
    private UmlClass centerNode;
    private boolean terminal = false;

    public void clear() {
        this.nodes.clear();
    }
}
