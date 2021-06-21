package cn.hit.joker.nsga2;

import cn.hit.joker.nsga2.objectiveFunction.*;
import com.debacharya.nsgaii.Configuration;
import com.debacharya.nsgaii.NSGA2;
import com.debacharya.nsgaii.crossover.CrossoverParticipantCreatorProvider;
import com.debacharya.nsgaii.datastructure.Population;
import com.debacharya.nsgaii.objectivefunction.AbstractObjectiveFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/18 14:21
 * @description a demo of nsga2
 */
public class Nsga2Demo {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        // 设置染色体长度，这里是（类图中类的数目）
        configuration.setChromosomeLength(26);
        // 设置世代数
        configuration.setGenerations(100);
        // 设置每个世代的染色体总数，即划分方案总数
        configuration.setPopulationSize(10);
        // 设置创建遗传代码的类，即随机的生成每个类属于哪个微服务
        configuration.setGeneticCodeProducer(new DivideSolutionProducer(26));
        // 设置目标函数
        List<AbstractObjectiveFunction> functionList = new ArrayList<>();
        functionList.add(new CohesionDegreeFunction());
        functionList.add(new CouplingDegreeFunction());
        functionList.add(new AvgMsFunction());
        functionList.add(new AvgQualityFunction());
        functionList.add(new CommunicatePriceFunction());
        configuration.objectives = functionList;
        // 设置初始父代产生的方法:无需设置，使用默认方法

        // 设置子代产生的方法：设置交叉、变异的方法，创造子代的数目
        // 使用默认提供的交叉方法
        configuration.setCrossover(new MyCrossover(CrossoverParticipantCreatorProvider.selectByBinaryTournamentSelection()));
        // 设置变异方法
        configuration.setMutation(new MyMutation(1, 33));
        // 设置生成子代群体的方法


        NSGA2 nsga2 = new NSGA2(configuration);
        System.out.println(configuration.isSetup());
        Population population = nsga2.run();
        System.out.println("----------------------------------");
        System.out.println(population.toString());
    }
}
