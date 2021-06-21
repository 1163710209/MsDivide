package cn.hit.joker.nsga2;

import com.debacharya.nsgaii.datastructure.Chromosome;
import com.debacharya.nsgaii.datastructure.IntegerAllele;
import com.debacharya.nsgaii.datastructure.ValueAllele;
import com.debacharya.nsgaii.mutation.AbstractMutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/21 10:41
 * @description
 */
public class MyMutation extends AbstractMutation {

    private final int begin;
    private final int end;

    public MyMutation(int begin, int end, float mutationProbability) {
        super(mutationProbability);
        this.begin = begin;
        this.end = end;
    }

    public MyMutation(int begin, int end) {
        super();
        this.begin = begin;
        this.end = end;
    }

    @Override
    public Chromosome perform(Chromosome chromosome) {
        List<IntegerAllele> childGeneticCode = new ArrayList<>();
        List<IntegerAllele> parentGeneticCode = chromosome.getGeneticCode().stream().map(e -> (IntegerAllele) e).collect(Collectors.toList());

        for(int i = 0; i < parentGeneticCode.size(); i++)
            childGeneticCode.add(i, new IntegerAllele(
                    this.shouldPerformMutation() ?
                            this.getMutatedValue(parentGeneticCode.get(i).getGene()) :
                            parentGeneticCode.get(i).getGene()
            ));

        return new Chromosome(new ArrayList<>(childGeneticCode));
    }

    private int getMutatedValue(int x) {
        while (true) {
            int temp = new Random().nextInt(end) + 1;
            if (temp != x) {
                return temp;
            }
        }
    }
}
