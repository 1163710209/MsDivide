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
 * @date 2021/6/20 16:59
 * @description
 */
public class AvgMsFunction extends AbstractObjectiveFunction {

    public AvgMsFunction() {
        super();
    }

    @Override
    public String getObjectiveTitle() {
        return "每个指标的实现平均需要多少个微服务支撑";
    }

    @Override
    public double getValue(Chromosome chromosome) {
        List<Microservice> msList = MicroserviceAnalyzer.getMsListFromChromosome(chromosome);
        MicroserviceAnalyzer.addAllToMs(msList, MainSystem.getInputData());
        double[] avgQuality = MicroserviceAnalyzer.getAverageValueSupport(msList);
        return -avgQuality[1];
    }
}
