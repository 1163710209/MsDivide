package cse.graph;

import java.util.List;
import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/4/8 9:29
 * @description a cluster
 */
public class Cluster {
    private Map<String, List<String>> cluster;
    // 模块度
    private double Qw;

    public Cluster(Map<String, List<String>> cluster, double qw) {
        this.cluster = cluster;
        Qw = qw;
    }

    public Cluster() {
    }

    public Map<String, List<String>> getCluster() {
        return cluster;
    }

    public void setCluster(Map<String, List<String>> cluster) {
        this.cluster = cluster;
    }

    public double getQw() {
        return Qw;
    }

    public void setQw(double qw) {
        Qw = qw;
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "cluster=" + cluster +
                ", Qw=" + Qw +
                '}';
    }
}
