package cn.hit.joker.newmsdivide.importer.classImporter;

import lombok.Data;

import java.util.*;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/20 21:36
 * @description deploy info
 */
@Data
public class Deploy {
    private Set<Location> locations;
    private Map<String, Double> resources;

    enum Location {
        Cloud,
        Edge,
        End
    }

    public Deploy() {
        this.locations = new HashSet<>();
        this.resources = new HashMap<>();
    }

}
