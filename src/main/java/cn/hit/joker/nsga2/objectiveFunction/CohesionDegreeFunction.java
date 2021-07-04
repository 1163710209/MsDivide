package cn.hit.joker.nsga2.objectiveFunction;

import cn.hit.joker.newmsdivide.MainSystem;
import cn.hit.joker.newmsdivide.analyzer.MicroserviceAnalyzer;
import cn.hit.joker.newmsdivide.importer.InputData;
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
    private InputData inputData;
    public CohesionDegreeFunction() {
        super();
    }
    public CohesionDegreeFunction(InputData inputData) {
        super();
        this.inputData = inputData;
    }

    @Override
    public String getObjectiveTitle() {
        return "cohesion degree of solution";
    }

    @Override
    public double getValue(Chromosome chromosome) {
        List<Microservice> msList = MicroserviceAnalyzer.getMsListFromChromosome(chromosome, inputData);
        MicroserviceAnalyzer.addAllToMs(msList, inputData);
        double cohesionDegree = MicroserviceAnalyzer.getCohesionDegree(msList, inputData.getClassDiagram());
        return cohesionDegree;
    }
}
