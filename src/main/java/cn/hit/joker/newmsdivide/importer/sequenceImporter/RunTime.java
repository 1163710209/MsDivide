package cn.hit.joker.newmsdivide.importer.sequenceImporter;

import lombok.Data;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/26 21:00
 * @description runtime exchange arguments
 */
@Data
public class RunTime {
    private int frequency;
    private int size;

    public RunTime() {}

    public double getScore() {
        if (frequency > 4 || frequency < 1 || size < 1 || size > 4) {
            throw new IllegalArgumentException("通信频率和传递消息大小等级只能在1-4之间，参数错误");
        }

        int frequency = this.frequency;
        int size = this.size;

        if ((frequency == 1 && size<=2) || (frequency == 2 && size == 1)) {
            return 1.0;
        } else if ((frequency == 2 && size== 2) ||
                (frequency == 1 && size == 3) ||
                (frequency == 3 && size == 1)) {
            return 0.8;
        } else if ((frequency == 2 && size == 3) || (frequency == 3 && size == 2)) {
            return 0.6;
        } else if ((frequency == 3 && size == 3) || (frequency == 2 && size == 4) ||
                (frequency == 4 && size == 2)) {
            return 0.4;
        } else if (frequency == 4 && size == 4) {
            return 0.1;
        } else {
            return 0.2;
        }
     }

}
