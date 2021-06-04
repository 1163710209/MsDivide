package cn.hit.joker.newmsdivide.solver;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/3 16:37
 * @description
 */
@Data
@Slf4j
public class SolverConfig {
    private Map<String, Double> algorithmParams = new HashMap<>();
    private Map<String, Double> defaultScores = new HashMap<>();
    // default algorithm
    private String algorithm = "";
    private int clusterNum;

    // default score
    private final static double defaultScore = 1.0d;

    public SolverConfig() {}

    public SolverConfig(String algorithm) {

    }

    public void setAlgorithmParams(@NonNull final Map<String, Double> mclParams) {
        this.algorithmParams = mclParams;
    }

    public void setDefaultScores(@NonNull final Map<String, Double> defaultScores) {
        this.defaultScores = defaultScores;
    }

    /**
     * get param of algorithm by key
     * not find return 1
     *
     * @param key key
     * @return param value
     */
    public Double getValueForAlgorithmParam(final String key) {
        if (!algorithmParams.containsKey(key)) {
            log.error("找不到 {} 对应的算法参数，使用默认值1", key);
            return 1d;
        }
        return algorithmParams.get(key);
    }

    /**
     * get solver config for default algorithm
     *
     * @return solver config
     */
    private static SolverConfig getDefaultConfig() {
        SolverConfig solverConfig = new SolverConfig();

        // set params for config
        Map<String, Double> mclParams = new HashMap<>();
        // param for chineseWhisper algorithm
        mclParams.put("cwNodeWeighting", 0.0d);

        // param for leung algorithm
        mclParams.put("leungDelta", 0.55d);
        mclParams.put("leungM", 0.1d);

        // param for Markov algorithm
        mclParams.put("mclExpansionOperations", 2.0d);
        mclParams.put("mclPowerCoefficient", 2.0d);

        // param for Gephi algorithm
        mclParams.put("numberOfClusters", 3d);

        // other param
        mclParams.put("inflation", 2.0d);
        mclParams.put("extraClusters", 0d);
        mclParams.put("power", 1d);
        mclParams.put("prune", 0d);

        solverConfig.setAlgorithmParams(mclParams);

        // set default score for criterion
        Map<String, Double> defaultScores = new HashMap<>();

        solverConfig.setDefaultScores(defaultScores);
        return solverConfig;
    }

    /**
     * get config for chinese whisper algorithm
     *
     * @return solverConfig
     */
    public static SolverConfig getChineseWhisperConfig() {
        SolverConfig solverConfig = getDefaultConfig();
        solverConfig.setAlgorithm(SolveSystem.MODE_CW);
        return solverConfig;
    }

    /**
     * get config for markov algorithm
     *
     * @return solverConfig
     */
    public static SolverConfig getMarkovConfig() {
        SolverConfig solverConfig = getDefaultConfig();
        solverConfig.setAlgorithm(SolveSystem.MODE_MARKOV);
        return solverConfig;
    }

    /**
     * get config for fast newman algorithm
     *
     * @return solverConfig
     */
    public static SolverConfig getFastNewmanConfig() {
        SolverConfig solverConfig = getDefaultConfig();
        solverConfig.setAlgorithm(SolveSystem.MODE_FAST_NEWMAN);
        return solverConfig;
    }

    public static SolverConfig getGephiConfig(int clusterNumber) {
        SolverConfig solverConfig = getDefaultConfig();
        solverConfig.setAlgorithm(SolveSystem.MODE_GEPHI);
        solverConfig.setClusterNum(clusterNumber);
        return solverConfig;
    }

}
