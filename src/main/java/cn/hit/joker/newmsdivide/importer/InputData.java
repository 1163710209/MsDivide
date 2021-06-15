package cn.hit.joker.newmsdivide.importer;

import cn.hit.joker.newmsdivide.importer.classImporter.ClassDiagram;
import cn.hit.joker.newmsdivide.importer.classImporter.UmlClass;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.Indicator;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.IndicatorName;
import cn.hit.joker.newmsdivide.importer.sequenceImporter.SequenceDiagram;
import cn.hit.joker.newmsdivide.model.MsDivideSystem;
import cn.hit.joker.newmsdivide.model.criteria.*;
import cn.hit.joker.newmsdivide.utils.GenerateId;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/26 21:46
 * @description input data entity
 */
@Data
@Slf4j
public class InputData {
    private String systemName;
    private ClassDiagram classDiagram;
    private List<SequenceDiagram> sequenceDiagramList;

    public InputData(ClassDiagram classDiagram, List<SequenceDiagram> sequenceDiagramList) {
        this.systemName = classDiagram.getName();
        this.classDiagram = classDiagram;
        this.sequenceDiagramList = sequenceDiagramList;
    }

    /**
     * get msDivide system
     *
     * @return msDivide system
     */
    public MsDivideSystem getMsDivideSystem() {
        if (classDiagram == null || classDiagram.getClassList().size() == 0
                || sequenceDiagramList == null || sequenceDiagramList.size() == 0) {
            throw new IllegalArgumentException("输入的类图数据与时序图数据不能为空！");
        }

        // build msDivideSystem
        MsDivideSystem msDivideSystem = new MsDivideSystem();
        // set id and name
        msDivideSystem.setId(GenerateId.getId());
        msDivideSystem.setName(classDiagram.getName());
        // set class list
        msDivideSystem.setClassList(classDiagram.getClassList());

        // set criterionInstanceList
        List<CriterionInstance> criterionInstanceList = new ArrayList<>();
        getCriterionInstance(criterionInstanceList, classDiagram.getClassList());
        msDivideSystem.setCriterionInstanceList(criterionInstanceList);
        log.info("从类图 {} 中构建聚合维度原则实例成功！", classDiagram.getName());

        // set qualityInstanceList
        List<QualityInstance> qualityInstanceList = new ArrayList<>();
        sequenceDiagramList.forEach(sequenceDiagram -> {
            getQualityInstance(qualityInstanceList, sequenceDiagram);
            msDivideSystem.setQualityInstanceList(qualityInstanceList);
            log.info("从时序图 {} 中构建价值维度原则实例成功！", sequenceDiagram.getName());

        });
        // set communicate score
        Map<ClassPair, Double> communicateScore = new HashMap<>();
        sequenceDiagramList.forEach(sequenceDiagram -> {
            getCommunicateScore(communicateScore, sequenceDiagram);
            msDivideSystem.setCommunicateScore(communicateScore);
            log.info("从时序图 {} 中构建通信维度原则实例成功！", sequenceDiagram.getName());

        });

        log.info("从输入中构建微服务划分系统成功！");
        return msDivideSystem;
    }

    private void getCriterionInstance(List<CriterionInstance> instanceList, List<UmlClass> classList) {
        getClassRelationInstance(instanceList);
        getClassGroupInstance(instanceList);
    }

    private void getClassRelationInstance(List<CriterionInstance> instanceList) {
        CriterionInstance classAggregate = CriterionInstance.getCriterionInstance(CriterionName.ClassAggregate);
        CriterionInstance classComposition = CriterionInstance.getCriterionInstance(CriterionName.ClassComposition);
        CriterionInstance classInheritance = CriterionInstance.getCriterionInstance(CriterionName.ClassInheritance);

        // add entity relation to criterionInstance
        classDiagram.getClassRelationList().forEach(relation -> {
            CriterionName criterionName = CriterionName.valueOf(relation.getType().name());
            String nameA = relation.getOrigin().getName();
            String nameB = relation.getDestination().getName();

            switch (criterionName) {
                // handle aggregate
                case ClassAggregate:
                    addClassPairToInstance(classAggregate, nameA, nameB);
                    break;
                // handle composition
                case ClassComposition:
                    addClassPairToInstance(classComposition, nameA, nameB);
                    break;
                // handle inheritance
                case ClassInheritance:
                    addClassPairToInstance(classInheritance, nameA, nameB);
                    break;
            }
        });

        // add criterionInstance to instanceList
        instanceList.add(classAggregate);
        instanceList.add(classComposition);
        instanceList.add(classInheritance);
    }

    private void addClassPairToInstance(CriterionInstance instance, String nameA, String nameB) {
        UmlClass umlClassA = classDiagram.getUmlClassByName(nameA);
        UmlClass umlClassB = classDiagram.getUmlClassByName(nameB);

        if (umlClassA == null || umlClassB == null) {
            throw new IllegalArgumentException("类关系列表中输入的类称错误，应在提供的类范围内！");
        }
        instance.getClassPairList().add(new ClassPair(umlClassA, umlClassB));
    }

    private void getClassGroupInstance(List<CriterionInstance> instanceList) {
        // handle ddd relation
        CriterionInstance dddValueObject = CriterionInstance.getCriterionInstance(CriterionName.DDDValueObject);
        CriterionInstance dddAggregate = CriterionInstance.getCriterionInstance(CriterionName.DDDAggregate);
        CriterionInstance dddService = CriterionInstance.getCriterionInstance(CriterionName.DDDService);
        CriterionInstance lifecycleCommonality = CriterionInstance.getCriterionInstance(CriterionName.LifeCycleCommonality);

        // add ddd relation to criterionInstance
        if (classDiagram.getClassGroups() == null || classDiagram.getClassGroups().size() == 0) {
            log.warn("类图中的DDD信息为空！");
            return;
        }

        classDiagram.getClassGroups().forEach(classGroup -> {
            CriterionName groupName = CriterionName.valueOf(classGroup.getName() + "");
            switch (groupName) {
                case DDDValueObject:
                    handleDDDValueObjectInstance(dddValueObject, classGroup.getClassList());
                    break;
                case DDDAggregate:
                    addClassPairToInstance(dddAggregate, classGroup.getClassList());
                    break;
                case DDDService:
                    addClassPairToInstance(dddService, classGroup.getClassList());
                    break;
                case LifeCycleCommonality:
                    addClassPairToInstance(lifecycleCommonality, classGroup.getClassList());
                    break;
            }
        });

        instanceList.add(dddValueObject);
        instanceList.add(dddAggregate);
        instanceList.add(dddService);
        instanceList.add(lifecycleCommonality);
    }

    private void addClassPairToInstance(CriterionInstance instance, List<UmlClass> classList) {
        classList.forEach(umlClass -> {
            umlClass = classDiagram.getUmlClassByName(umlClass.getName());
        });

        for (int i = 0; i < classList.size(); i++) {
            for (int j = i + 1; j < classList.size(); j++) {
                instance.getClassPairList().add(new ClassPair(classList.get(i), classList.get(j)));
            }
        }
    }

    // handle ddd valueObject
    private void handleDDDValueObjectInstance(CriterionInstance instance, List<UmlClass> groupList) {
        List<UmlClass> classList = classDiagram.getClassList();
        groupList.forEach(umlClassA -> {
            classList.forEach(umlClassB -> {
                if (!umlClassA.equals(umlClassB)) {
                    instance.getClassPairList().add(new ClassPair(umlClassA, umlClassB));
                }
            });
        });
    }

    private void getCommunicateScore(Map<ClassPair, Double> communicateScore, SequenceDiagram sequenceDiagram) {
        sequenceDiagram.getActivityList().forEach(activity -> {
            activity.getMethodCalls().forEach(methodCall -> {
                ClassPair classPair = new ClassPair(
                        new UmlClass(methodCall.getFrom()),
                        new UmlClass(methodCall.getTo())
                );
                Double score = methodCall.getRunTime().getScore();
                communicateScore.put(classPair,
                        communicateScore.getOrDefault(classPair, 0d) + score);
            });
        });
    }

    private void getQualityInstance(List<QualityInstance> instanceList,  SequenceDiagram sequenceDiagram) {
        QualityInstance couping = QualityInstance.getQualityInstance(CriterionName.BusinessQualityCouping);
        QualityInstance critical = QualityInstance.getQualityInstance(CriterionName.BusinessQualityCriticality);

        // handle couping and critical
        sequenceDiagram.getIndicators().forEach(indicator -> {
            String name = indicator.getName();
            Map<String, Double> temp = new HashMap<>();
            indicator.getClassList().forEach((classA, degreeA) -> {
                // handle couping: compute support degree of each two different class
                temp.put(classA, degreeA);
                temp.forEach((classB, degreeB) -> {
                    if (!classA.equals(classB)) {
                        couping.getSupportClassPairs().add(
                                new SupportClassPair(classA, classB,
                                        degreeA, degreeB, name, indicator.getPriority())
                        );
                    }
                });

                // handle critical: compute support degree of every class
                critical.getClassSupportDegree().put(classA,
                        critical.getClassSupportDegree().getOrDefault(classA,
                                0d) + degreeA * indicator.getPriority());
            });
        });

        instanceList.add(couping);
        instanceList.add(critical);
    }

}
