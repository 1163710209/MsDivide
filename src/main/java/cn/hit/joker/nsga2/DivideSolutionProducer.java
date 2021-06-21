package cn.hit.joker.nsga2;

import com.debacharya.nsgaii.datastructure.AbstractAllele;
import com.debacharya.nsgaii.datastructure.IntegerAllele;
import com.debacharya.nsgaii.plugin.GeneticCodeProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/20 15:08
 * @description
 */
public class DivideSolutionProducer implements GeneticCodeProducer {
    // 设置变量x
    public final int size;

    public DivideSolutionProducer(int size) {
        super();
        this.size = size;
    }

    @Override
    public List<? extends AbstractAllele> produce(int n) {
        List<IntegerAllele> list = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= n; i++) {
            IntegerAllele allele = new IntegerAllele(random.nextInt(size) + 1);
            list.add(allele);
        }

        return list;
    }
}
