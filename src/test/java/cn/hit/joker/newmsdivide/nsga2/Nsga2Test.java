package cn.hit.joker.newmsdivide.nsga2;

import cn.hit.joker.newmsdivide.analyzer.MicroserviceAnalyzer;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.nsga2.DivideSolutionProducer;
import cn.hit.joker.nsga2.MyCrossover;
import cn.hit.joker.nsga2.objectiveFunction.*;
import com.debacharya.nsgaii.crossover.CrossoverParticipantCreatorProvider;
import com.debacharya.nsgaii.datastructure.Chromosome;
import com.debacharya.nsgaii.datastructure.IntegerAllele;
import com.debacharya.nsgaii.datastructure.Population;
import com.debacharya.nsgaii.plugin.DefaultPluginProvider;
import com.debacharya.nsgaii.plugin.PopulationProducer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public void getMsListFromChromosomeTest() {
        Chromosome chromosome = new Chromosome(new DivideSolutionProducer(26).produce(26));
        System.out.println(chromosome.getGeneticCode());
        System.out.println("-------------------------------------------------------");
        List<Microservice> msList = MicroserviceAnalyzer.getMsListFromChromosome(chromosome);
        System.out.println(msList);
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
