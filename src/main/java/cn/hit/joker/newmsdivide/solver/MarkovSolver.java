package cn.hit.joker.newmsdivide.solver;

import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.scorer.Score;
import org.nlpub.watset.graph.Clustering;
import org.nlpub.watset.graph.MarkovClustering;

import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/3 20:25
 * @description markov clustering algorithm solver
 */
public class MarkovSolver extends WatsetSolver {
    private int expansionOperations;
    private double powerCoefficient;

    public MarkovSolver(MsDivideSystem msDivideSystem, Map<ClassPair, Map<String, Score>> scores, SolverConfig config) {
        super(msDivideSystem, scores);
        this.expansionOperations = config.getValueForAlgorithmParam("mclExpansionOperations").intValue();
        this.powerCoefficient = config.getValueForAlgorithmParam("mclPowerCoefficient");
    }

    @Override
    protected Clustering<String> getAlgorithm() {
        return new MarkovClustering<>(graph, expansionOperations, powerCoefficient);
    }
}
