package cn.hit.joker.newmsdivide.nsga2;

import cn.hit.joker.newmsdivide.MainSystem;
import cn.hit.joker.newmsdivide.analyzer.MicroserviceAnalyzer;
import cn.hit.joker.newmsdivide.importer.InputData;
import cn.hit.joker.newmsdivide.importer.classImporter.Deploy;
import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.utils.WriteFile;
import cn.hit.joker.nsga2.DivideSolutionProducer;
import cn.hit.joker.nsga2.MyCrossover;
import cn.hit.joker.nsga2.Nsga2DDDCargoDemo;
import cn.hit.joker.nsga2.Nsga2HealthCareDemo;
import cn.hit.joker.nsga2.objectiveFunction.*;
import com.debacharya.nsgaii.crossover.CrossoverParticipantCreatorProvider;
import com.debacharya.nsgaii.datastructure.Chromosome;
import com.debacharya.nsgaii.datastructure.IntegerAllele;
import com.debacharya.nsgaii.datastructure.Population;
import com.debacharya.nsgaii.plugin.DefaultPluginProvider;
import com.debacharya.nsgaii.plugin.PopulationProducer;
import org.junit.jupiter.api.Test;
import sun.applet.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/20 16:16
 * @description test nsga2 interface implements by me
 */
public class Nsga2Test {

    private static Chromosome getRandomChromosome() {
        Chromosome chromosome = new Chromosome(new DivideSolutionProducer(26).produce(26));
        System.out.println(chromosome.getGeneticCode());
        return chromosome;
    }

    @Test
    public void getMsListFromChromosomeOfHealthCareTest() {
        List<IntegerAllele> list = new ArrayList<>();
        int[] input = {2, 2, 16, 2, 12, 14, 11, 2, 2, 11, 24, 21, 2, 4, 19, 4, 25, 5, 3, 17, 5, 5, 15, 7, 1, 18};
        for (int i: input) {
            list.add(new IntegerAllele(i));
        }
        Chromosome chromosome = new Chromosome(list);
        System.out.println(chromosome.getGeneticCode());
        System.out.println("-------------------------------------------------------");
        List<Microservice> msList = MicroserviceAnalyzer.getMsListFromChromosome(chromosome, MainSystem.getInputData());
//        System.out.println(msList);
        InputData inputData = MainSystem.getInputData();
        List<UmlClass> classList = inputData.getClassDiagram().getClassList();
        MicroserviceAnalyzer.addAllToMs(msList, inputData);

        double cohesionDegree = MicroserviceAnalyzer.getCohesionDegree(msList, inputData.getClassDiagram());
        double coupingDegree = MicroserviceAnalyzer.getCoupingDegree(msList, inputData.getClassDiagram());

        // check satisfy deployment constraint
        boolean meet = true;
        for (Microservice microservice : msList) {
            if (microservice.getClassList().size() == 1) {
                microservice.setDeployLocationSet(inputData.getClassDiagram().getUmlClassByName(microservice.getClassList().get(0).getName()).getDeploy().getLocations());
            } else if (microservice.getClassList().size() > 1) {
                Set<Deploy.Location> locations = MainSystem.checkDeployLocation(microservice, classList);
                if (locations.size() == 0) {
                    meet = false;
                    break;
                } else {
                    microservice.setDeployLocationSet(locations);
                }
            }
        }

        double communicatePrice = MicroserviceAnalyzer.getCommunicatePrice(msList, inputData.getSequenceDiagramList());
        double[] value = MicroserviceAnalyzer.getAverageValueSupport(msList);

        StringBuilder builder = new StringBuilder();
        builder.append("------------------------------\n")
                .append("微服务划分方案为：\n")
                .append("微服务的数量为：" + msList.size() + "\n")
                .append("微服务为：" + msList + "\n")
                .append("聚合度为：" + cohesionDegree + "\n")
                .append("耦合度为：" + coupingDegree + "\n")
                .append("是否符合部署位置约束：" + meet + "\n")
                .append("通讯代价为：" + communicatePrice + "\n")
                .append("平均每个微服务支持的质量指标数：" + value[0] + "\n")
                .append("平均每个质量指标关联的微服务数：" + value[1] + "\n")
                .append("-------------------------------------------\n");

        String path = "output";
        String fileName = msList.size() + ".txt";
        WriteFile.writeToFile(path, builder.toString(), fileName);
    }

    @Test
    public void getMsListFromChromosomeOfDDDCargoTest() {
        List<IntegerAllele> list = new ArrayList<>();
        int[] input = {5, 5, 5, 6, 6, 5, 5, 4, 8};
        for (int i: input) {
            list.add(new IntegerAllele(i));
        }
        Chromosome chromosome = new Chromosome(list);
        System.out.println(chromosome.getGeneticCode());
        System.out.println("-------------------------------------------------------");
        List<Microservice> msList = MicroserviceAnalyzer.getMsListFromChromosome(chromosome, Nsga2DDDCargoDemo.getInputData());
//        System.out.println(msList);
        InputData inputData = Nsga2DDDCargoDemo.getInputData();
        List<UmlClass> classList = inputData.getClassDiagram().getClassList();
        MicroserviceAnalyzer.addAllToMs(msList, inputData);

        double cohesionDegree = MicroserviceAnalyzer.getCohesionDegree(msList, inputData.getClassDiagram());
        double coupingDegree = MicroserviceAnalyzer.getCoupingDegree(msList, inputData.getClassDiagram());

        // check satisfy deployment constraint
        boolean meet = true;
        for (Microservice microservice : msList) {
            if (microservice.getClassList().size() == 1) {
                microservice.setDeployLocationSet(inputData.getClassDiagram().getUmlClassByName(microservice.getClassList().get(0).getName()).getDeploy().getLocations());
            } else if (microservice.getClassList().size() > 1) {
                Set<Deploy.Location> locations = MainSystem.checkDeployLocation(microservice, classList);
                if (locations.size() == 0) {
                    meet = false;
                    break;
                } else {
                    microservice.setDeployLocationSet(locations);
                }
            }
        }

        double communicatePrice = MicroserviceAnalyzer.getCommunicatePrice(msList, inputData.getSequenceDiagramList());
        double[] value = MicroserviceAnalyzer.getAverageValueSupport(msList);

        StringBuilder builder = new StringBuilder();
        builder.append("------------------------------\n")
                .append("微服务划分方案为：\n")
                .append("微服务的数量为：" + msList.size() + "\n")
                .append("微服务为：" + msList + "\n")
                .append("聚合度为：" + cohesionDegree + "\n")
                .append("耦合度为：" + coupingDegree + "\n")
                .append("是否符合部署位置约束：" + meet + "\n")
                .append("通讯代价为：" + communicatePrice + "\n")
                .append("平均每个微服务支持的质量指标数：" + value[0] + "\n")
                .append("平均每个质量指标关联的微服务数：" + value[1] + "\n")
                .append("-------------------------------------------\n");

        String path = "output";
        String fileName = "dddCargo-" + msList.size() + ".txt";
        WriteFile.writeToFile(path, builder.toString(), fileName);
    }

    @Test
    public void cohesionDegreeFunctionTest() {
        Chromosome chromosome = getRandomChromosome();
        CohesionDegreeFunction function = new CohesionDegreeFunction();
        System.out.println(function.getObjectiveTitle());
        System.out.println("cohesionDegree = " + function.getValue(chromosome));

//        List<IntegerAllele> list = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 1; i <= 33; i++) {
//            IntegerAllele allele = new IntegerAllele(i);
//            list.add(allele);
//        }
//        Chromosome chromosome = new Chromosome(list);
//        CohesionDegreeFunction function = new CohesionDegreeFunction();
//        System.out.println(function.getObjectiveTitle());
//        System.out.println("cohesionDegree = " + function.getValue(chromosome));
    }

    @Test
    public void couplingDegreeFunctionTest() {
        Chromosome chromosome = getRandomChromosome();
        CouplingDegreeFunction function = new CouplingDegreeFunction();
        System.out.println(function.getObjectiveTitle());
        System.out.println("couplingDegree = " + function.getValue(chromosome));

//        List<IntegerAllele> list = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 1; i <= 33; i++) {
//            IntegerAllele allele = new IntegerAllele(i);
//            list.add(allele);
//        }
//        Chromosome chromosome = new Chromosome(list);
//        CouplingDegreeFunction function = new CouplingDegreeFunction();
//        System.out.println(function.getObjectiveTitle());
//        System.out.println("couplingDegree = " + function.getValue(chromosome));
    }

    @Test
    public void AvgMsFunctionTest() {
        Chromosome chromosome = getRandomChromosome();
        AvgMsFunction function = new AvgMsFunction();
        System.out.println(function.getObjectiveTitle());
        System.out.println("avgMs = " + function.getValue(chromosome));

//        List<IntegerAllele> list = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 1; i <= 33; i++) {
//            IntegerAllele allele = new IntegerAllele(i);
//            list.add(allele);
//        }
//        Chromosome chromosome = new Chromosome(list);
//        AvgMsFunction function = new AvgMsFunction();
//        System.out.println(function.getObjectiveTitle());
//        System.out.println("avgMs = " + function.getValue(chromosome));
    }

    @Test
    public void AvgQualityFunctionTest() {
        while (true) {
            Chromosome chromosome = getRandomChromosome();
            AvgQualityFunction function = new AvgQualityFunction();
            System.out.println(function.getObjectiveTitle());
            System.out.println("avgQuality = " + function.getValue(chromosome));
            if (function.getValue(chromosome) < 1) {
                break;
            }
        }
//        List<IntegerAllele> list = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 1; i <= 33; i++) {
//            IntegerAllele allele = new IntegerAllele(i);
//            list.add(allele);
//        }
//        Chromosome chromosome = new Chromosome(list);
//        AvgQualityFunction function = new AvgQualityFunction();
//        System.out.println(function.getObjectiveTitle());
//        System.out.println("avgQuality = " + function.getValue(chromosome));
    }

    @Test
    public void CommunicatePriceFunctionTest() {
        List<IntegerAllele> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= 26; i++) {
            IntegerAllele allele = new IntegerAllele(i);
            list.add(allele);
        }
        Chromosome chromosome = new Chromosome(list);
        CommunicatePriceFunction function = new CommunicatePriceFunction();
        System.out.println(function.getObjectiveTitle());
        System.out.println("communicatePrice = " + function.getValue(chromosome));
    }

    @Test
    public void CommunicatePriceFunctionRandomTest() {
        Chromosome chromosome = getRandomChromosome();
        CommunicatePriceFunction function = new CommunicatePriceFunction();
        System.out.println(function.getObjectiveTitle());
        System.out.println("communicatePrice = " + function.getValue(chromosome));
    }

    @Test
    public void myCrossoverTest() {
        MyCrossover crossover = new MyCrossover(CrossoverParticipantCreatorProvider.selectByBinaryTournamentSelection());
        PopulationProducer populationProducer = DefaultPluginProvider.defaultPopulationProducer();
        Population population = populationProducer.produce(10, 26, new DivideSolutionProducer(26),null);
        List<Chromosome> chromosome = crossover.perform(population);
        chromosome.forEach(chromosome1 -> {
            System.out.println(chromosome1.getGeneticCode());
        });
    }
}
