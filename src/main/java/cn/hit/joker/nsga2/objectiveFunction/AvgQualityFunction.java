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
 * @date 2021/6/20 16:54
 * @description
 */
public class AvgQualityFunction extends AbstractObjectiveFunction {

    private InputData inputData;

    public AvgQualityFunction() {
        super();
    }

    public AvgQualityFunction(InputData inputData) {
        super();
        this.inputData = inputData;
    }

    @Override
    public String getObjectiveTitle() {
        return "每个微服务平均支撑多少个质量指标";
    }

    @Override
    public double getValue(Chromosome chromosome) {
        List<Microservice> msList = MicroserviceAnalyzer.getMsListFromChromosome(chromosome, inputData);
        MicroserviceAnalyzer.addAllToMs(msList,inputData);
        double[] avgQuality = MicroserviceAnalyzer.getAverageValueSupport(msList);
        return -avgQuality[0];
    }
}
