package cse.newman;

import cse.graph.Cluster;
import cse.graph.Community;
import cse.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fast Newman算法的总入口
 *
 * @author 作者 E-mail:
 * @date 创建时间： 2016-3-17 下午6:48:31
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class ProgramEntrance {
    private Community community;
    private int iterStep; // 程序需要迭代的次数，应该为图中节点的个数减去一
    private String[] mergoInfo = new String[iterStep]; // 保存节点之间合并的消息

    // 保存最终的聚类结果，其中Map的key对应于第几次迭代
    private Map<Integer, Map<Integer, List<String>>> result;

    // 保存每次的聚类结果和计算对应的模块度
    private Cluster resultCluster;

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public int getIterStep() {
        return iterStep;
    }

    public void setIterStep(int iterStep) {
        this.iterStep = iterStep;
    }

    public String[] getMergoInfo() {
        return mergoInfo;
    }

    public void setMergoInfo(String[] mergoInfo) {
        this.mergoInfo = mergoInfo;
    }

    public Map<Integer, Map<Integer, List<String>>> getResult() {
        return result;
    }

    public void setResult(Map<Integer, Map<Integer, List<String>>> result) {
        this.result = result;
    }

    public Cluster getResultCluster() {
        return resultCluster;
    }

    public void setResultCluster(Cluster resultCluster) {
        this.resultCluster = resultCluster;
    }

    public ProgramEntrance(Community community){
        this.community = community;
        int nodeNum = community.getNodeNum();
        iterStep = nodeNum - 1;
        result = new HashMap<Integer, Map<Integer, List<String>>>();

        Map<Integer, List<String>> step_0 = new HashMap<Integer, List<String>>();
        // 将所有节点先加入step0的map中

        for (int i = 0; i < community.getNodeList().size(); i++) {
            Node node = community.getNodeList().get(i);
            List<String> list = new ArrayList<>();
            list.add(node.getId());

            step_0.put(i, list);
        }

//        for (int i = 1; i <= nodeNum; ++i) {
//            List<Integer> list = new ArrayList<Integer>();
//            list.add(i);
//
//            // 将原始的数据加入到Map中去
//            step_0.put(i, list);
//        }
        result.put(0, step_0);
    }

    public void start_clustering() {
        for (int i = 1; i <= iterStep; ++i) {
            int preStep = i - 1; // 记录上一步，为了获取上一步的聚类结果

            Map<Integer, List<String>> preCLustered = result.get(preStep);

            double deltaQ = -Double.MIN_VALUE;

            // 记录应该哪两个cluster合并
            int clusterAId = -1;
            int clusterBId = -1;

            for (Integer map_key_i : preCLustered.keySet()) {
                List<String> cluster_i = preCLustered.get(map_key_i);
                for (Integer map_key_j : preCLustered.keySet()) {

                    List<String> cluster_j = preCLustered.get(map_key_j);
                    if (map_key_j.equals( map_key_i)) {
                        continue;
                    }
                    // System.out.println("cluster_i:"+cluster_i+" +cluster_j"+cluster_j);
                    double tmpDelta = new NewManAlg(community).deltaQ(cluster_i, cluster_j);
//                    System.out.println(tmpDelta);

                     System.out.println("cluster_i"+cluster_i+" cluster_j"+cluster_j+"---------tmpDelta:"+tmpDelta);

                    // 更新deltaQ的值
                    if (tmpDelta > deltaQ) {
                        deltaQ = tmpDelta;
//                        System.out.println("change deltaQ");
                        clusterAId = map_key_i;
                        clusterBId = map_key_j;
                    }else if(tmpDelta == deltaQ){
                        // System.out.println("------------出现deltaQ相等的情况---------------");
                    }
                }
            }

//            System.out.println(clusterAId);
//            System.out.println(clusterBId);

            List<String> clusterAList = preCLustered.get(clusterAId);
            List<String> clusterBList = preCLustered.get(clusterBId);

            // 将两个相近的cluster合并
            List<String> mergeCluster = new ArrayList<String>();
            mergeCluster.addAll(clusterAList);
            mergeCluster.addAll(clusterBList);

            // 保存当前聚类的结果
            Map<Integer, List<String>> currentClustered = new HashMap<Integer, List<String>>();

            // 记录当前Map的总长度
            for (Integer preMapKey : preCLustered.keySet()) {
                if (preMapKey != clusterAId && preMapKey != clusterBId) {
                    currentClustered
                            .put(preMapKey, preCLustered.get(preMapKey));
                } else {
//					System.out.println("ClusterId为：" + preMapKey
//							+ " 的簇本轮中被merge!");
                }
            }
            int oldId = clusterAId < clusterBId ? clusterAId : clusterBId;
            currentClustered.put(oldId, mergeCluster);

            // 将当前的聚类结果加入Map中
            result.put(i, currentClustered);
            // 将当前的聚类结果加入到所有结果中
            if (resultCluster == null) {
                resultCluster = getCluster(currentClustered);
            } else {
                if (GetQw.calculateQ(community, currentClustered) > resultCluster.getQw()) {
                    resultCluster = getCluster(currentClustered);
                }
            }

            // 获取上一步的聚类结果
            System.out.println(currentClustered);
            // 获取Qw打分
            System.out.println(GetQw.calculateQ(community, currentClustered));
        }

        System.out.println(getResultCluster());
    }

    private Cluster getCluster(Map<Integer, List<String>> currentClustered) {
        Cluster cluster = new Cluster();
        Map<String, List<String>> myCluster = new HashMap<>();
        currentClustered.forEach((k, v) -> {
            myCluster.put("Cluster" + k, v);
        });
        cluster.setCluster(myCluster);
        cluster.setQw(GetQw.calculateQ(community, currentClustered));

        return cluster;
    }

    public static void main(String[] args) {
        Community d = new Community(6);

        d.insertEdge("1", "2", 0.1);
        d.insertEdge("1", "3", 0.1);
        d.insertEdge("2", "1", 0.1);
        d.insertEdge("2", "3", 0.1);
        d.insertEdge("2", "4", 0.4);
        d.insertEdge("3", "1", 0.1);
        d.insertEdge("3", "2", 0.1);
        d.insertEdge("4", "2", 0.4);
        d.insertEdge("4", "5", 0.1);
        d.insertEdge("4", "6", 0.1);
        d.insertEdge("5", "joke", 0.1);
        d.insertEdge("5", "6", 0.1);
        d.insertEdge("6", "joke", 0.1);
        d.insertEdge("6", "7", 0.1);

        ProgramEntrance programEntrance = new ProgramEntrance(d);

        programEntrance.start_clustering();
        System.out.println(programEntrance.getResultCluster());

    }
}
