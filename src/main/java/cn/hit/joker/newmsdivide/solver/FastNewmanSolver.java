package cn.hit.joker.newmsdivide.solver;

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.scorer.Score;
import cse.graph.Community;
import cse.graph.Edge;
import cse.newman.ProgramEntrance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/3 20:27
 * @description fast GN clustering algorithm solver
 */
public class FastNewmanSolver extends AbstractSolver<String, Edge> {
    protected final Community community;

    public FastNewmanSolver(MsDivideSystem msDivideSystem, Map<ClassPair, Map<String, Score>> scores) {
        super(msDivideSystem, scores);

        community = new Community(msDivideSystem.getClassList().size());

        buildNodes();
        buildEdges();
    }

    @Override
    protected void createNode(String name) {
        community.addNode(name);
    }

    @Override
    protected String getNode(String name) {
        return name;
    }

    @Override
    protected void createEdgeAndSetWeight(UmlClass classA, UmlClass classB, double weight) {
        community.insertEdge(classA.getName(), classB.getName(), weight / 100);
    }

    @Override
    protected Edge getEdge(UmlClass classA, UmlClass classB) {
        return community.getEdge(classA.getName(), classB.getName());
    }

    @Override
    protected List<Edge> getEdges() {
        return community.getEdgeList();
    }

    @Override
    protected void removeEdge(Edge edge) {
        community.removeEdge(edge);
    }

    @Override
    protected double getWeight(Edge edge) {
        return community.getWeight(edge.getHead(), edge.getTail());
    }

    @Override
    protected void setWeight(Edge edge, double weight) {
        community.setWeight(edge, weight / 100);
    }

    @Override
    protected List<Microservice> solve() {
        ProgramEntrance programEntrance = new ProgramEntrance(community);

        programEntrance.start_clustering();


        // put result cluster in msServices
        List<Microservice> msList = new ArrayList<>();

        for (Map.Entry<String, List<String>> cluster : programEntrance.getResultCluster().getCluster().entrySet()) {
            List<UmlClass> classList = new ArrayList<>();
            cluster.getValue().forEach(s -> classList.add(new UmlClass(s)));
            msList.add(new Microservice(cluster.getKey(), classList));
        }
        return msList;
    }

    @Override
    public void printGraph() {
        System.out.println(community.getNodeList());
        community.getEdgeList().forEach(System.out::println);
    }

}
