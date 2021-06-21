package cn.hit.joker.newmsdivide;

import cn.hit.joker.newmsdivide.importer.ImporterUtils;
import cn.hit.joker.newmsdivide.importer.InputData;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.classImporter.Deploy;
import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.SequenceDiagram;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.solver.SolveSystem;
import cn.hit.joker.newmsdivide.solver.SolverConfig;
import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/4 21:08
 * @description
 */
@Slf4j
public class MainSystem {
    private static InputData inputData;
    public static InputData getInputData() {

        if (inputData != null) {
            return inputData;
        }
        String path0 = "cases/healthPension/class.json";
        String path1 = "cases/healthPension/nurseServiceSequence.json";
        String path2 = "cases/healthPension/bodyInfoCollectSequence.json";
        String path3 = "cases/healthPension/homeDoctorServiceSequence.json";
        String path4 = "cases/healthPension/slowSickTreatmentSequence.json";

        try {
            ClassDiagram classDiagram = ImporterUtils.importClassDiagram(path0);
            System.out.println(classDiagram);

            SequenceDiagram sequenceDiagram1 = ImporterUtils.importSequenceDiagram(path1);
            SequenceDiagram sequenceDiagram2 = ImporterUtils.importSequenceDiagram(path2);
            SequenceDiagram sequenceDiagram3 = ImporterUtils.importSequenceDiagram(path3);
            SequenceDiagram sequenceDiagram4 = ImporterUtils.importSequenceDiagram(path4);

            List<SequenceDiagram> sequenceDiagrams = new ArrayList<>();
            sequenceDiagrams.add(sequenceDiagram1);
            sequenceDiagrams.add(sequenceDiagram2);
            sequenceDiagrams.add(sequenceDiagram3);
            sequenceDiagrams.add(sequenceDiagram4);

            inputData = new InputData(classDiagram, sequenceDiagrams);
            return inputData;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get microservice solution of input algorithm
     *
     * @param algorithm
     * @param clusterNum
     * @return
     */
    public static List<Microservice> start(String algorithm, int clusterNum, MsDivideSystem msDivideSystem) {
        SolverConfig solverConfig;
        switch (algorithm) {
            case SolveSystem.MODE_CW:
                solverConfig = SolverConfig.getChineseWhisperConfig();
                break;
            case SolveSystem.MODE_MARKOV:
                solverConfig = SolverConfig.getMarkovConfig();
                break;
            case SolveSystem.MODE_FAST_NEWMAN:
                solverConfig = SolverConfig.getFastNewmanConfig();
                break;
            case SolveSystem.MODE_GEPHI:
                solverConfig = SolverConfig.getGephiConfig(clusterNum);
                break;
            default:
                throw new IllegalArgumentException("输入的算法不支持");
        }
        List<Microservice> msList = new SolveSystem().solveSystem(msDivideSystem, solverConfig, SolveSystem.TYPE_NEW);
        System.out.println(msList);
        return msList;
    }

    /**
     * get all microservice solution in deployment constraint of input algorithm
     *
     * @param algorithm
     * @return
     */
    public static List<List<Microservice>> getDivideResult(String algorithm, InputData inputData) {
        List<List<Microservice>> msSolutionList = new ArrayList<>();
        List<UmlClass> classList = inputData.getClassDiagram().getClassList();
        MsDivideSystem msDivideSystem = inputData.getMsDivideSystem();
        for (int i=1; i<=classList.size(); i++) {
            List<Microservice> msList = start(algorithm, i, msDivideSystem);
            if (msList.size() == i) {
                msSolutionList.add(msList);
            }
        }

        // add other algorithm result
//        if (algorithm.equals(SolveSystem.MODE_GEPHI)) {
//            msSolutionList.add(start(SolveSystem.MODE_CW, 0, msDivideSystem));
//            msSolutionList.add(start(SolveSystem.MODE_MARKOV, 0, msDivideSystem));
//            msSolutionList.add(start(SolveSystem.MODE_FAST_NEWMAN, 0, msDivideSystem));
//        }

        // check deployLocation constraint
        for (int i=0; i< msSolutionList.size(); i++) {
            boolean meet = true;
            for (Microservice microservice : msSolutionList.get(i)) {
                if (microservice.getClassList().size() == 1) {
                    microservice.setDeployLocationSet(inputData.getClassDiagram().getUmlClassByName(microservice.getClassList().get(0).getName()).getDeploy().getLocations());
                } else if (microservice.getClassList().size() > 1) {
                    Set<Deploy.Location> locations = checkDeployLocation(microservice, classList);
                    if (locations.size() == 0) {
                        log.warn("微服务划分方案：[" + msSolutionList.get(i) + "] 不符合部署位置约束！");
                        msSolutionList.remove(i);
                        i--;
                        meet = false;
                        break;
                    } else {
                        microservice.setDeployLocationSet(locations);
                    }
                }
            }
            if (meet) {
                log.info("微服务划分方案：[" + msSolutionList.get(i) + "] 符合部署位置约束！");
            }
        }
        return msSolutionList;
    }

    /**
     * get all microservice solution in deployment constraint of input algorithm
     *
     * @param algorithm
     * @param inputData
     * @return
     */
    public static List<List<Microservice>> getRandomDivideResult(String algorithm, InputData inputData, int times) {
        List<List<Microservice>> msSolutionList = new ArrayList<>();
        List<UmlClass> classList = inputData.getClassDiagram().getClassList();
        MsDivideSystem msDivideSystem = inputData.getMsDivideSystem();
        for (int i=1; i<=times; i++) {
            List<Microservice> msList = start(algorithm, i, msDivideSystem);
            msSolutionList.add(msList);
        }

        // check deployLocation constraint
        for (int i=0; i< msSolutionList.size(); i++) {
            boolean meet = true;
            for (Microservice microservice : msSolutionList.get(i)) {
                if (microservice.getClassList().size() == 1) {
                    microservice.setDeployLocationSet(inputData.getClassDiagram().getUmlClassByName(microservice.getClassList().get(0).getName()).getDeploy().getLocations());
                } else if (microservice.getClassList().size() > 1) {
                    Set<Deploy.Location> locations = checkDeployLocation(microservice, classList);
                    if (locations.size() == 0) {
                        log.warn("微服务划分方案：[" + msSolutionList.get(i) + "] 不符合部署位置约束！");
                        msSolutionList.remove(i);
                        i--;
                        meet = false;
                        break;
                    } else {
                        microservice.setDeployLocationSet(locations);
                    }
                }
            }
            if (meet) {
                log.info("微服务划分方案：[" + msSolutionList.get(i) + "] 符合部署位置约束！");
            }
        }
        return msSolutionList;
    }

    public static Set<Deploy.Location> checkDeployLocation(Microservice microservice, List<UmlClass> classList) {
        Set<Deploy.Location> locations = new HashSet<>();
        locations.add(Deploy.Location.Cloud);
        locations.add(Deploy.Location.Edge);
        locations.add(Deploy.Location.End);

        microservice.getClassList().forEach(umlClass -> {
            umlClass = getClass(classList, umlClass);
            if (umlClass == null) {
                throw new IllegalArgumentException("检查部署位置约束时找不到类名，出现错误！");
            }
            locations.retainAll(umlClass.getDeploy().getLocations());
        });

        return locations;
    }

    private static UmlClass getClass(List<UmlClass> classList, UmlClass emptyClass) {
        for (UmlClass umlClass : classList) {
            if (umlClass.equals(emptyClass)) {
                return umlClass;
            }
        }
        return null;
    }
}
