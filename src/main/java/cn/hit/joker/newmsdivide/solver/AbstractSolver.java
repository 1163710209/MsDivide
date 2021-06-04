package cn.hit.joker.newmsdivide.solver;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/3 15:07
 * @description
 */

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.scorer.Score;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * a abstract class to divide ms on graph build with MsDivideSystem
 *
 * @param <N> the class type for node
 * @param <E> the class type for edge
 */
@Slf4j
public abstract class AbstractSolver<N, E> {
    protected MsDivideSystem msDivideSystem;
    private final Map<ClassPair, Map<String, Score>> scores;

    public AbstractSolver(final MsDivideSystem msDivideSystem, final Map<ClassPair, Map<String, Score>> scores) {
        this.msDivideSystem = msDivideSystem;
        this.scores = scores;
        log.info("创建类型为 {} 的 solver 成功！", getClass());
    }

    /**
     * create node with name
     *
     * @param name node name
     */
    protected abstract void createNode(String name);

    /**
     * get node with node name
     * @param name node name
     * @return node
     */
    protected abstract N getNode(final String name);

    /**
     * get node with minEntity
     *
     * @param umlClass uml class
     * @return Node
     */
    protected N getNode(final UmlClass umlClass) {
        return getNode(umlClass.getName());
    }

    /**
     * create edge from minEntityA to minEntityB with given weight
     *
     * @param classA start minEntity
     * @param classB end minEntity
     * @param weight weight of the edge
     */
    protected abstract void createEdgeAndSetWeight(UmlClass classA, UmlClass classB, double weight);

    /**
     * get edge with minEntityA and minEntityB
     *
     * @param classA start minEntity
     * @param classB end minEntity
     * @return edge
     */
    protected abstract E getEdge(UmlClass classA, UmlClass classB);

    /**
     * get all edges of graph
     *
     * @return edge list
     */
    protected abstract List<E> getEdges();

    /**
     * remove edge from graph
     *
     * @param edge edge
     */
    protected abstract void removeEdge(E edge);

    /**
     * get weight by edge
     *
     * @param edge edge
     * @return weight of edge
     */
    protected abstract double getWeight(E edge);

    /**
     * set weight of edge
     *
     * @param edge edge
     * @param weight weight
     */
    protected abstract void setWeight(E edge, double weight);

    /**
     * set weight for minEntityA to minEntityB, if not exist edge in A and B
     * create a edge from A and B and set weight
     *
     * @param classA start minEntity
     * @param classB end minEntity
     * @param weight weight
     */
    protected void setWeight(UmlClass classA, UmlClass classB, double weight) {
        N nodeA = getNode(classA);
        N nodeB = getNode(classB);
        E edge = getEdge(classA, classB);

        if (edge!=null) {
            log.info("为节点 {} 与 {} 间的边设置权重为 {} !", nodeA, nodeB, weight);
            setWeight(edge, weight);
        } else {
            log.info("为节点 {} 与 {} 间创建边，并设置权重为 {} !", nodeA, nodeB, weight);
            createEdgeAndSetWeight(classA, classB, weight);
        }
    }

    /**
     * create all nodes with minEntity.allName in msDivideSystem
     */
    protected void buildNodes() {
        for(UmlClass umlClass: msDivideSystem.getClassList()) {
            createNode(umlClass.getName());
        }
    }

    /**
     * build edges of msDivideSystem and set weight with sum of score
     * delete edge with negative weight
     */
    protected void buildEdges() {
        for (Map.Entry<ClassPair, Map<String, Score>> entry : scores.entrySet()) {
            // get start minEntity and end minEntity
            UmlClass umlClassA = entry.getKey().getClassA();
            UmlClass umlClassB = entry.getKey().getClassB();
            // get weight = sum of score
            double weight = entry.getValue().values().stream().mapToDouble(Score::getPrioritizedScore).sum();
            // set weight
            setWeight(umlClassA, umlClassB, weight);
        }

        // remove edges with negative weight
        removeNegativeEdges();
    }

    /**
     * remove edges with negative weight
     */
    protected void removeNegativeEdges() {
        List<E> toRemoveEdges = new ArrayList<>();
        // warn: do not remove edge in for
        for (E edge : getEdges()) {
            if (getWeight(edge) <= 0) {
                toRemoveEdges.add(edge);
            }
        }

        log.info("删除权重小于或等于0的边共 {} 条", toRemoveEdges.size());

        for(E edge : toRemoveEdges) {
            removeEdge(edge);
        }
    }

    /**
     * Find microservice candidate set using an algorithm on created graph
     *
     * @return microservice candidate set
     */
    protected abstract List<Microservice> solve();

    /**
     * print build graph
     */
    public abstract void printGraph();

//    /**
//     * get graph json data
//     *
//     * @return json string
//     */
//    public abstract String getGraphJson();
//
//    /**
//     * get ms graph json data
//     *
//     * @return ms json string
//     */
//    public abstract String getMsGraphJson();

}
