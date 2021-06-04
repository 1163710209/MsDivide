package cn.hit.joker.newmsdivide.solver;

import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.scorer.Score;
import cn.hit.joker.newmsdivide.scorer.Scorer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/3 16:40
 * @description
 */
@Component
@Slf4j
public class SolveSystem {
    public static final String MODE_CW = "Chinese Whispers";
    public static final String MODE_MARKOV = "Markov (MCL)";
    public static final String MODE_FAST_NEWMAN = "Fast Newman";
    public static final String MODE_GEPHI = "Gephi";
    public static final String[] MODES = { MODE_CW, MODE_MARKOV, MODE_FAST_NEWMAN };

    public static final String TYPE_NORMAL = "originalScore";
    public static final String TYPE_NEW = "Score";

    /**
     * start algorithm depend on input and return msService set
     *
     *
     * @param msDivideSystem msDivideSystem
     * @param config solverConfig
     * @return msCandidate
     */
    public List<Microservice> solveSystem(MsDivideSystem msDivideSystem, SolverConfig config, String type) {

        if (msDivideSystem==null || config == null || config.getAlgorithmParams().isEmpty()) {
            log.error("参数为空！");
            return Collections.emptyList();
        }

        AbstractSolver solver = getSolver(msDivideSystem, config, type);

        // exec algorithm
        List<Microservice> msList = solver.solve();
        log.info(" {} 聚类算法执行成功！", config.getAlgorithm());
        log.info("微服务划分系统 {} 划分完成，得到共 {} 个候选服务：{}", msDivideSystem.getName(), msList.size(), msList.toString());

        // TODO: 分析划分结果

        return msList;
    }

    private AbstractSolver getSolver(MsDivideSystem msDivideSystem, SolverConfig config, String type) {
        AbstractSolver solver;
        String algorithm = config.getAlgorithm();
        // get scores
        Scorer scorer = new Scorer();
        Map<ClassPair, Map<String, Score>> scores = null;
        if (type.equals(TYPE_NORMAL)) {
            scores = scorer.getOriginalScores(msDivideSystem);
        } else if (type.equals(TYPE_NEW)) {
            scores = scorer.getScores(msDivideSystem);
        }
        System.out.println("-------------------------");

        scores.forEach((k, v) -> {
            System.out.println("[" + k.getClassA().getName() + "," + k.getClassB().getName() + "]:" +  v);
        } );
        System.out.println("-------------------------");

        // get algorithm solver by type
        switch(algorithm) {
            case MODE_CW:
                solver = new ChineseWhispersSolver(msDivideSystem, scores, config);
                break;
            case MODE_MARKOV:
                solver = new MarkovSolver(msDivideSystem, scores, config);
                break;
            case MODE_FAST_NEWMAN:
                solver = new FastNewmanSolver(msDivideSystem, scores);
                break;
            case MODE_GEPHI:
                solver = new GephiSolver(msDivideSystem, scores, config.getClusterNum());
                break;
            default:
                log.error("算法 {} 不支持，支持的算法列表为 {} ", algorithm, MODES);
                throw new IllegalArgumentException("算法名称错误！");
        }
        log.info("创建无向权重图 {} 成功！", msDivideSystem.getName());
        solver.printGraph();
        return solver;
    }

}
