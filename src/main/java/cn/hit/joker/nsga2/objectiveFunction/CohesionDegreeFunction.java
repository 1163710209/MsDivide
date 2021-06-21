package cn.hit.joker.nsga2.objectiveFunction;

import cn.hit.joker.newmsdivide.MainSystem;
import cn.hit.joker.newmsdivide.analyzer.MicroserviceAnalyzer;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import com.debacharya.nsgaii.datastructure.Chromosome;
import com.debacharya.nsgaii.objectivefunction.AbstractObjectiveFunction;

import java.util.List;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/20 15:40
 * @description compute cohesion degree of divide solution, bigger is better
 */
public class CohesionDegreeFunction extends AbstractObjectiveFunction {

    public CohesionDegreeFunction() {
        super();
    }

    @Override
    public String getObjectiveTitle() {
        return "cohesion degree of solution";
    }

    @Override
    public double getValue(Chromosome chromosome) {
        List<Microservice> msList = MicroserviceAnalyzer.getMsListFromChromosome(chromosome);
        MicroserviceAnalyzer.addAllToMs(msList, MainSystem.getInputData());
        double cohesionDegree = MicroserviceAnalyzer.getCohesionDegree(msList, MainSystem.getInputData().getClassDiagram());
        return cohesionDegree;
    }
}
