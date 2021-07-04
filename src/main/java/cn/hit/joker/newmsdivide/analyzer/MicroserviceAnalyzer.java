package cn.hit.joker.newmsdivide.analyzer;

import cn.hit.joker.newmsdivide.MainSystem;
import cn.hit.joker.newmsdivide.importer.InputData;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassRelation;
import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.Activity;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.MethodCall;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.SequenceDiagram;
import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.model.result.MsInterface;
import com.debacharya.nsgaii.datastructure.Chromosome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author joker
 * @version 1.0
 * @date 2021/6/9 21:04
 * @description microservice analyzer
 */
public class MicroserviceAnalyzer {
    /**
     * add class relation list to microservice
     *
     * @param microserviceList microserviceList
     * @param classDiagram classDiagram
     */
    public static void addClassRelationToMs(List<Microservice> microserviceList, ClassDiagram classDiagram) {
        List<ClassRelation> relationList = classDiagram.getClassRelationList();

        // handle each microservice
        microserviceList.forEach(microservice -> {
            // init umlClass
            microservice.getClassList().forEach(umlClass -> {
                umlClass = classDiagram.getUmlClassByName(classDiagram.getName());
            });

            // add relation
            List<UmlClass> classList = microservice.getClassList();
            if (classList.size() > 1) {
                for (int i = 0; i < classList.size(); i++) {
                    for (int j = i + 1; j < classList.size(); j++) {
                        ClassPair classPair = new ClassPair(classList.get(i), classList.get(j));
                        relationList.forEach(relation -> {
                            ClassPair temp = new ClassPair(relation.getOrigin(), relation.getDestination());
                            if (temp.equals(classPair)) {
                                microservice.getClassRelationList().add(relation);
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * add interface list to microservice
     *
     * @param microserviceList microserviceList
     * @param sequenceDiagram sequenceDiagram
     */
    public static void addInterfaceListToMs(List<Microservice> microserviceList, SequenceDiagram sequenceDiagram) {
        Map<UmlClass, Microservice> msMap = new HashMap<>();
        // init msMap
        microserviceList.forEach(microservice -> {
            microservice.getClassList().forEach(umlClass -> {
                msMap.put(umlClass, microservice);
            });
        });

        // add interface
        sequenceDiagram.getActivityList().forEach(activity -> {
            activity.getMethodCalls().forEach(methodCall -> {
                UmlClass umlClassA = new UmlClass(methodCall.getFrom());
                UmlClass umlClassB = new UmlClass(methodCall.getTo());
                if (msMap.get(umlClassA) != msMap.get(umlClassB)) {
                    Microservice ms = msMap.get(umlClassB);
                    MsInterface msInterface = new MsInterface(
                            methodCall.getTo(), methodCall.getMethod()
                    );
                    // add interface list if not exist
                    if (ms.getInterfaceList().contains(msInterface)) {
                        ms.getInterfaceList().forEach(temp -> {
                            if (temp.equals(msInterface)) {
                                temp.getMsNameList().add(msMap.get(umlClassA).getName());
                            }
                        });
                    } else {
                        msInterface.getMsNameList().add(msMap.get(umlClassA).getName());
                        ms.getInterfaceList().add(msInterface);
                    }
                }
            });
        });
    }

    /**
     * add quality support degree to microservice
     *
     * @param microserviceList microserviceList
     * @param sequenceDiagram sequenceDiagram
     */
    public static void addQualityDegreeToMs(List<Microservice> microserviceList, SequenceDiagram sequenceDiagram) {
        Map<UmlClass, Microservice> msMap = new HashMap<>();
        // init msMap
        microserviceList.forEach(microservice -> {
            microservice.getClassList().forEach(umlClass -> {
                msMap.put(umlClass, microservice);
            });
        });

        sequenceDiagram.getIndicators().forEach(indicator -> {
            String qName = indicator.getName();
            indicator.getClassList().forEach((k, v) -> {
                UmlClass umlClass = new UmlClass(k);
                Map<String, Double> degreeMap = msMap.get(umlClass).getQualitySupportDegree();
                degreeMap.put(qName, degreeMap.getOrDefault(qName, 0d) + v);
            });
        });
    }

    /**
     * add class relation, interface list, quality degree to microservice
     *
     * @param microserviceList microserviceList
     * @param inputData inputData
     */
    public static void addAllToMs(List<Microservice> microserviceList, InputData inputData) {
        addClassRelationToMs(microserviceList, inputData.getClassDiagram());
        inputData.getSequenceDiagramList().forEach(sequenceDiagram -> {
            addInterfaceListToMs(microserviceList, sequenceDiagram);
            addQualityDegreeToMs(microserviceList, sequenceDiagram);
        });

    }

    /**
     * compute couping degree of ms divide solution
     *
     * @param microserviceList microserviceList
     * @param classDiagram classDiagram
     */
    public static double getCoupingDegree(List<Microservice> microserviceList, ClassDiagram classDiagram) {
        double degree = microserviceList.stream().mapToDouble(microservice -> {
            return microservice.getClassRelationList().size();
        }).sum();

        System.out.println(degree);
        System.out.println(classDiagram.getClassRelationList().size());

        return (classDiagram.getClassRelationList().size() - degree) / microserviceList.size();
    }

    /**
     * compute cohesion degree of ms divide solution
     *
     * @param microserviceList microserviceList
     * @param classDiagram classDiagram
     * @return cohesion degree
     */
    public static double getCohesionDegree(List<Microservice> microserviceList, ClassDiagram classDiagram) {
        double degree = microserviceList.stream().mapToDouble(microservice -> {
            return (microservice.getClassRelationList().size() + 1d) / microservice.getClassList().size();
        }).sum();

        degree /= microserviceList.size();
        return degree;
    }

    /**
     * get communicate price of ms divide solution
     *
     * @param microserviceList microservice list
     * @param sequenceDiagramList sequenceDiagramList
     * @return communicate price
     */
    public static double getCommunicatePrice(List<Microservice> microserviceList, List<SequenceDiagram> sequenceDiagramList) {
        Map<UmlClass, Microservice> map = new HashMap<>();
        // init map
        microserviceList.forEach(microservice -> {
            microservice.getClassList().forEach(umlClass -> {
                map.put(umlClass, microservice);
            });
        });

        double communicatePrice = 0d;
        // compute communicate price
        for (SequenceDiagram sequenceDiagram : sequenceDiagramList) {
            for (Activity activity : sequenceDiagram.getActivityList()) {
                for (MethodCall methodCall : activity.getMethodCalls()) {
                    UmlClass classA = new UmlClass(methodCall.getFrom());
                    UmlClass classB = new UmlClass(methodCall.getTo());
                    // 如果方法调用的两个类不属于同一个微服务，计算通讯代价
                    if (!map.get(classA).equals(map.get(classB))) {
                        communicatePrice += computePrice(map.get(classA), map.get(classB), methodCall);
                    }
                }
            }
        }
        return communicatePrice;
    }

    // TODO: compute a methodCall communicate price between two microservice
    private static double computePrice(Microservice m1, Microservice m2, MethodCall methodCall) {
        double cScore = methodCall.getRunTime().getScore();
        double cPrice = DeployLocationCommunicatePrice.getLocationPrice(m1.getDeployLocationSet(), m2.getDeployLocationSet());
        return cScore * cPrice;
    }

    /**
     * 计算每个指标的实现平均需要多少个微服务支撑
     * 计算每个微服务平均支撑多少个质量指标
     *
     * @param microserviceList
     * @return double[0]: 每个微服务平均支撑多少个质量指标; double[1]: 每个指标的实现平均需要多少个微服务支撑
     */
    public static double[] getAverageValueSupport(List<Microservice> microserviceList) {
        double[] value = new double[2];
        double averageIndicator = 0;
        // compute each microservice average support number of indicators
        for (Microservice microservice : microserviceList) {
            averageIndicator += microservice.getQualitySupportDegree().size();
        }
        averageIndicator = averageIndicator / microserviceList.size();
        value[0] = averageIndicator;

        // compute each indicator average support number of microservices
        Map<String, Integer> map = new HashMap<>();
        for (Microservice microservice : microserviceList) {
            for (String s : microservice.getQualitySupportDegree().keySet()) {
                map.put(s, map.getOrDefault(s, 0) + 1);
            }
        }
        int msSize = map.values().stream().mapToInt(x -> x).sum();
        double averageMs = (msSize + 0d) / map.size();
        value[1] = averageMs;

        return value;
    }

    /**
     * get microservice list from chromosome
     *
     * @param chromosome chromosome
     * @return ms list
     */
    public static List<Microservice> getMsListFromChromosome(Chromosome chromosome, InputData inputData) {
        List<Integer> list = new ArrayList<>();
        chromosome.getGeneticCode().forEach(allele -> {
            list.add((Integer) allele.getGene());
        });
        if (inputData == null) {
            throw new IllegalArgumentException("inputData 为空！");
        }
        List<UmlClass> classList = inputData.getClassDiagram().getClassList();

        Map<Integer, Microservice> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            Microservice ms = map.getOrDefault(list.get(i), new Microservice("service", new ArrayList<>()));
            ms.getClassList().add(classList.get(i));
            map.put(list.get(i), ms);
        }

        List<Microservice> msList = new ArrayList<>();
        AtomicInteger i= new AtomicInteger();
        map.values().forEach(microservice -> {
            i.getAndIncrement();
            microservice.setName("service-" + i);
            msList.add(microservice);
        });

        // 设置固定位置约束的类，处于类图的最后位置
        int k = msList.size();
        for (int j = chromosome.getLength(); j < classList.size(); j++) {
            List<UmlClass> tempClassList = new ArrayList<>();
            tempClassList.add(classList.get(j));
            Microservice tempMs = new Microservice(
                    "service-" + ++k, tempClassList
            );
            msList.add(tempMs);
        }

        return msList;
    }
}
