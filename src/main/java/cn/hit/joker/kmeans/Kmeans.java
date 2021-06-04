package cn.hit.joker.kmeans;

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.scorer.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/4 14:55
 * @description kmeans algorithm
 */
public class Kmeans {
    /**
     * cluster algorithm
     *
     * @param input    input node list
     * @param k        cluster size
     * @param scoreMap scoreMap of two nodes
     * @return cluster result
     */
    public KmeansResult clustering(List<UmlClass> input, Integer k, Map<ClassPair, Map<String, Score>> scoreMap) {
        List<Cluster> clusters = initCenters(input, k);

        while (!isFinish(clusters)) {
            clearCluster(clusters);
            assignNodes(input, clusters, scoreMap);
        }

        return null;
    }

    // init cluster center
    private List<Cluster> initCenters(List<UmlClass> nodeList, Integer k) {
        List<Cluster> centers = new ArrayList<>();

        // init cluster center
        int size = nodeList.size();
        for (int i = 0; i < k; i++) {
            Cluster cluster = new Cluster();
            cluster.setCenterNode(nodeList.get(i));
            centers.add(cluster);
        }

        return centers;
    }

    // judge each cluster finish or not
    private boolean isFinish(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            if (!cluster.isTerminal()) {
                return false;
            }
        }
        return true;
    }

    // clear each cluster node list
    private void clearCluster(List<Cluster> clusters) {
        clusters.forEach(Cluster::clear);
    }

    // assign class nodes
    private void assignNodes(List<UmlClass> nodeList, List<Cluster> clusters, Map<ClassPair, Map<String, Score>> scoreMap) {
        for (UmlClass umlClass : nodeList) {
            Cluster center = clusters.get(0);
            double minDistance = Double.MAX_VALUE;
            for (Cluster cluster : clusters) {
                ClassPair classPair = new ClassPair(umlClass, cluster.getCenterNode());
                double distance = scoreMap.get(classPair).values().stream().mapToDouble(Score::getPrioritizedScore).sum();

                if (minDistance > distance) {
                    minDistance = distance;
                    center = cluster;
                }
            }
            center.getNodes().add(umlClass);
        }
    }

    // reCandidate center
    private void reCandidateCenters(List<Cluster> clusters) {
        for (Cluster c : clusters) {
            if (c.getNodes().isEmpty()) {
                c.setTerminal(true);
                continue;
            }
        }
    }
}