package cn.hit.joker.newmsdivide.scorer;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/2 15:55
 * @description score of one criterion instance
 */
@Data
@AllArgsConstructor
public class Score {
    private double score;
    private double priority;

    public double getPrioritizedScore() {
        return score * priority;
    }

}