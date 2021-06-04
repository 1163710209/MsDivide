package cse.newman;

import cse.graph.Community;
import cse.graph.Edge;

import java.util.List;
import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/4/7 21:36
 * @description 计算一个社团划分的 Qw 值
 */
public class GetQw {
    public static double calculateQ(Community community, Map<Integer, List<String>> cluster) {
        double result = 0;

        for (Map.Entry<Integer, List<String>> entry : cluster.entrySet()) {
            Integer k = entry.getKey();
            List<String> v = entry.getValue();
            double eii = geteii(community, v);
            double ai = getai(community, v);
            result += eii - ai * ai;
        }

        return result;
    }

    /**
     * 计算某个社团内部的所有边的加权分数与整个网络的所有边的加权分数的比值
     *
     * @param community
     * @param clusterNodes
     * @return score
     */
    private static double geteii(Community community, List<String> clusterNodes) {
        double score = 0;

        for (Edge edge : community.getEdgeList()) {
            if (clusterNodes.contains(edge.getHead()) && clusterNodes.contains(edge.getTail())) {
                score += edge.getWeight();
            }
        }

        return score / getCommunityScore(community) ;
    }

    /**
     * 计算整个网络的所有边的加权分数
     *
     * @param community community
     * @return score
     */
    private static double getCommunityScore(Community community) {
        double score = 0;

        for (Edge edge : community.getEdgeList()) {
            score += edge.getWeight();
        }

        return score;
    }

    private static double getai(Community community, List<String> clusterNodes) {
        double score = 0;

        for (Edge edge : community.getEdgeList()) {
            if (clusterNodes.contains(edge.getHead()) && !clusterNodes.contains(edge.getTail())) {
                score += edge.getWeight();
            } else if (!clusterNodes.contains(edge.getHead()) && clusterNodes.contains(edge.getTail())) {
                score += edge.getWeight();
            } else if (clusterNodes.contains(edge.getHead()) && clusterNodes.contains(edge.getTail())) {
                score += edge.getWeight() * 2;
            }
        }

        return score / (2 * getCommunityScore(community));
    }
}
