package cn.hit.joker.nsga2.objectiveFunction;

import cn.hit.joker.newmsdivide.MainSystem;
import cn.hit.joker.newmsdivide.analyzer.MicroserviceAnalyzer;
import cn.hit.joker.newmsdivide.importer.InputData;
import cn.hit.joker.newmsdivide.importer.classImporter.Deploy;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import com.debacharya.nsgaii.datastructure.Chromosome;
import com.debacharya.nsgaii.objectivefunction.AbstractObjectiveFunction;

import java.util.List;
import java.util.Set;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/20 17:22
 * @description
 */
public class CommunicatePriceFunction extends AbstractObjectiveFunction {
    private InputData inputData;
    public CommunicatePriceFunction() {
        super();
    }
    public CommunicatePriceFunction(InputData inputData) {
        super();
        this.inputData = inputData;
    }

    @Override
    public String getObjectiveTitle() {
        return "划分方案的通讯代价";
    }

    @Override
    public double getValue(Chromosome chromosome) {
        List<Microservice> msList = MicroserviceAnalyzer.getMsListFromChromosome(chromosome, inputData);

        // add deploy location for each microservice
        boolean suit = true;
        for (Microservice microservice : msList) {
            Set<Deploy.Location> deploySet = MainSystem.checkDeployLocation(microservice,inputData.getClassDiagram().getClassList());
            if (deploySet.size() > 0) {
                microservice.setDeployLocationSet(deploySet);
            } else {
                suit = false;
            }
        }
        if (!suit) {
            System.out.println("当前划分不满足部署位置约束！");
            return 100d;
        } else {
            System.out.println("当前划分满足部署位置约束！");
        }

        MicroserviceAnalyzer.addAllToMs(msList, inputData);
        System.out.println(msList);
        double communicatePrice = MicroserviceAnalyzer.getCommunicatePrice(msList, inputData.getSequenceDiagramList());
        return -communicatePrice;
    }
}
