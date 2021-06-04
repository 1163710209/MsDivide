package cn.hit.joker.newmsdivide.solver;

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.scorer.Score;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.nlpub.watset.graph.Clustering;

import java.util.*;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/3 16:13
 * @description
 */
@Slf4j
public abstract class WatsetSolver extends AbstractSolver<String, DefaultWeightedEdge> {
    protected final Graph<String, DefaultWeightedEdge> graph;

    public WatsetSolver(MsDivideSystem msDivideSystem, Map<ClassPair, Map<String, Score>> scores) {
        super(msDivideSystem, scores);

        graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        buildNodes();
        buildEdges();
    }


    @Override
    protected void createNode(String name) {
        graph.addVertex(name);
    }

    @Override
    protected String getNode(String name) {
        return name;
    }

    @Override
    protected void createEdgeAndSetWeight(UmlClass classA, UmlClass classB, double weight) {
        DefaultWeightedEdge edge = graph.addEdge(classA.getName(), classB.getName());
        setWeight(edge, weight);
    }

    @Override
    protected DefaultWeightedEdge getEdge(UmlClass classA, UmlClass classB) {
        return graph.getEdge(classA.getName(), classB.getName());
    }

    @Override
    protected List<DefaultWeightedEdge> getEdges() {
        List<DefaultWeightedEdge> edges = new ArrayList<>(graph.edgeSet());
        return edges;
    }

    @Override
    protected void removeEdge(DefaultWeightedEdge edge) {
        graph.removeEdge(edge);
    }

    @Override
    protected double getWeight(DefaultWeightedEdge edge) {
        return graph.getEdgeWeight(edge);
    }

    @Override
    protected void setWeight(DefaultWeightedEdge edge, double weight) {
        Formatter formatter = new Formatter();
        weight = Double.parseDouble(formatter.format("%.2f", weight).toString());
        graph.setEdgeWeight(edge, weight);
    }

    /**
     * get algorithm to solve on graph
     *
     * @return clusters
     */
    protected abstract Clustering<String> getAlgorithm();

    @Override
    protected List<Microservice> solve() {
        // start algorithm to get clusters
        Clustering<String> algorithm = getAlgorithm();
        algorithm.fit();
        Collection<Collection<String>> clusterSet = algorithm.getClusters();

        // put result cluster in msServices
        List<Microservice> msServices = new ArrayList<>();
        int id = 1;
        for (Collection<String> cluster: clusterSet) {
            List<UmlClass> classList = new ArrayList<>();
            cluster.forEach(s -> classList.add(new UmlClass(s)));
            msServices.add(new Microservice("service" + id, classList));
            id++;
        }
        log.info("划分结束共得到{}个候选服务", clusterSet.size());
        msServices.forEach(System.out::println);
        return msServices;
    }

    @Override
    public void printGraph() {
        System.out.println(graph.vertexSet());
        graph.edgeSet().forEach(defaultWeightedEdge -> {
            System.out.println(defaultWeightedEdge.toString() + ":" + graph.getEdgeWeight(defaultWeightedEdge));
        });
    }
}
