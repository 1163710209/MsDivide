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
 * @date 2021/6/20 16:46
 * @description compute coupling degree of divide solution, smaller is better
 */
public class CouplingDegreeFunction extends AbstractObjectiveFunction {

    public CouplingDegreeFunction() {
        super();
    }

    @Override
    public String getObjectiveTitle() {
        return "coupling degree of solution";
    }


    @Override
    public double getValue(Chromosome chromosome) {
        List<Microservice> msList = MicroserviceAnalyzer.getMsListFromChromosome(chromosome);
        MicroserviceAnalyzer.addAllToMs(msList, MainSystem.getInputData());
        double couplingDegree = MicroserviceAnalyzer.getCoupingDegree(msList, MainSystem.getInputData().getClassDiagram());
        return -couplingDegree;
    }
}
