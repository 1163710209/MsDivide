package cn.hit.joker.kmeans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/4 14:54
 * @description k_means clustering result
 */
@Data
@AllArgsConstructor
public class KmeansResult {
    private List<Cluster> clusters = new ArrayList<Cluster>();
    private Double size;
}
