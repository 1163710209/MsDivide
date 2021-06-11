package cn.hit.joker.newmsdivide.analyzer;

import cn.hit.joker.newmsdivide.importer.InputData;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.classImporter.ClassRelation;
import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.SequenceDiagram;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.criteria.ClassPair;
import cn.hit.joker.newmsdivide.model.result.Microservice;
import cn.hit.joker.newmsdivide.model.result.MsInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param microserviceList
     * @param classDiagram
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
     * @param microserviceList
     * @param sequenceDiagram
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
     * @param microserviceList
     * @param sequenceDiagram
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
     * @param microserviceList
     * @param inputData
     */
    public static void addAllToMs(List<Microservice> microserviceList, InputData inputData) {
        addClassRelationToMs(microserviceList, inputData.getClassDiagram());
        addInterfaceListToMs(microserviceList, inputData.getSequenceDiagram());
        addQualityDegreeToMs(microserviceList, inputData.getSequenceDiagram());
    }

    /**
     * compute cohesion degree of ms divide solution
     *
     * @param microserviceList
     * @param classDiagram
     */
    public static double getCoupingDegree(List<Microservice> microserviceList, ClassDiagram classDiagram) {
        double degree = microserviceList.stream().mapToDouble(microservice -> {
            return microservice.getClassRelationList().size();
        }).sum();

        return (classDiagram.getClassRelationList().size() - degree) / microserviceList.size();
    }

    /**
     * compute coping degree of ms divide solution
     *
     * @param microserviceList
     * @param classDiagram
     * @return
     */
    public static double getCohesionDegree(List<Microservice> microserviceList, ClassDiagram classDiagram) {
        double degree = microserviceList.stream().mapToDouble(microservice -> {
            return (microservice.getClassRelationList().size() + 1d) / microservice.getClassList().size();
        }).sum();

        degree /= microserviceList.size();
        return degree;
    }

}
