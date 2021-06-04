package cn.hit.joker.newmsdivide.solver;

import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.scorer.Score;
import cz.cvut.fit.krizeji1.girvan_newman.GirvanNewmanClusterer;
import org.gephi.clustering.api.Cluster;
import org.gephi.clustering.spi.Clusterer;
import org.gephi.graph.api.*;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/4 18:59
 * @description Girvan-Newman clustering algorithm solver
 */
public class GephiSolver extends AbstractSolver<Node, Edge> {
    private Map<String, Node> nodes;
    private UndirectedGraph undirectedGraph;
    private GraphModel graphModel;

    private Logger log = LoggerFactory.getLogger(GephiSolver.class);
    private Integer numberOfClusters;
    private char serviceIdGenerator = 'A';

    public GephiSolver(final MsDivideSystem msDivideSystem, final Map<ClassPair, Map<String, Score>> scores, final Integer numberOfClusters) {
        super(msDivideSystem, scores);
        this.numberOfClusters = numberOfClusters;
        if (msDivideSystem == null || msDivideSystem.getClassList().isEmpty()) {
            throw new InvalidParameterException("invalid msDivideSystem!");
        }

        nodes = new HashMap<>();

        graphModel = bootstrapGephi();
        undirectedGraph = graphModel.getUndirectedGraph();

        log.info("gephi solver created");
        buildNodes();
        buildEdges();

        log.info("final edges: ");
        for (Edge edge : undirectedGraph.getEdges()) {
            log.info("{}-{}: {}", edge.getSource().getNodeData().getLabel(), edge.getTarget().getNodeData().getLabel(), edge.getWeight());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Microservice> solve() {
        return solveWithGirvanNewman(numberOfClusters);
    }

    @Override
    public void printGraph() {

    }

    List<Microservice> solveWithGirvanNewman(final int numberOfClusters) {
        log.debug("solve cluster with numberOfClusters = " + numberOfClusters);
        GirvanNewmanClusterer clusterer = new GirvanNewmanClusterer();
        clusterer.setPreferredNumberOfClusters(numberOfClusters);
        clusterer.execute(graphModel);
        List<Microservice> msList = getClustererResult(clusterer);
        return msList;
    }

    // Returns a HashSet as the algorithms return redundant clusters
    private List<Microservice> getClustererResult(final Clusterer clusterer) {
        List<Microservice> result = new ArrayList<>();
        if (clusterer.getClusters() != null) {
            for (Cluster cluster : clusterer.getClusters()) {
                List<UmlClass> classList = new ArrayList<>();
                for (Node node : cluster.getNodes()) {
                    classList.add(new UmlClass(node.toString()));
                }
                Microservice microservice = new Microservice("service" + serviceIdGenerator++, classList);
                result.add(microservice);
                log.debug("Microservice found: {}", microservice.getClassList().toString());
            }
        }
        return result;

    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    protected List<Edge> getEdges() {
        return Arrays.asList(undirectedGraph.getEdges().toArray());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Edge getEdge(final UmlClass first, final UmlClass second) {
        return undirectedGraph.getEdge(getNode(first), getNode(second));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeEdge(final Edge edge) {
        undirectedGraph.removeEdge(edge);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createEdgeAndSetWeight(final UmlClass first, final UmlClass second, final double weight) {
        Edge edge = graphModel.factory().newEdge(getNode(first), getNode(second), (float) weight, false);
        undirectedGraph.addEdge(edge);
        edge.getEdgeData().setLabel(edge.getWeight() + "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double getWeight(final Edge edge) {
        return edge.getWeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setWeight(final Edge edge, final double weight) {
        edge.setWeight((float) weight);
        edge.getEdgeData().setLabel(edge.getWeight() + "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Node getNode(final String name) {
        return nodes.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createNode(final String name) {
        Node node = graphModel.factory().newNode(name);
        node.getNodeData().setLabel(name);
        undirectedGraph.addNode(node);
        nodes.put(name, node);
    }

    private GraphModel bootstrapGephi() {
        // boostrap gephi
        Lookup lookup = Lookup.getDefault();
        ProjectController pc = lookup.lookup(ProjectController.class);
        pc.newProject();
        @SuppressWarnings("unused")
        Workspace workspace = pc.getCurrentWorkspace();
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        return graphModel;
    }
}
