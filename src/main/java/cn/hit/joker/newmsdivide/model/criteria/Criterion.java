package cn.hit.joker.newmsdivide.model.criteria;

import lombok.Data;

/**
 *  微服务划分原则
 */
@Data
public class Criterion {
    private CriterionName name;
    private CriterionType type;
    private String spec;            // 描述
    private double weight;           // 权重

    private Criterion(CriterionName name, CriterionType type, String spec, double weight) {
        this.name = name;
        this.type = type;
        this.spec = spec;
        this.weight = weight;
    }

    // 按照名称返回规则实例
    public static Criterion getCriterion(CriterionName name) {
        Criterion criterion = null;

        switch (name) {
            case LifeCycleCommonality:
                criterion = CriterionFactory.getLifeCycleCommonalityCriterion();
                break;
            case ClassAssociation:
                criterion = CriterionFactory.getClassAssociationCriterion();
                break;
            case ClassAggregate:
                criterion = CriterionFactory.getClassAggregateCriterion();
                break;
            case ClassComposition:
                criterion = CriterionFactory.getClassCompositionCriterion();
                break;
            case ClassInheritance:
                criterion = CriterionFactory.getClassInheritanceCriterion();
                break;
            case ClassDependency:
                criterion = CriterionFactory.getClassDependencyCriterion();
                break;
            case DDDValueObject:
                criterion = CriterionFactory.getDDDValueObjectCriterion();
                break;
            case DDDAggregate:
                criterion = CriterionFactory.getDDDAggregateCriterion();
                break;
            case DDDService:
                criterion = CriterionFactory.getDDDServiceCriterion();
                break;
            case BusinessQualityCouping:
                criterion = CriterionFactory.getBusinessQualityCoupingCriterion();
                break;
            case BusinessQualityCriticality:
                criterion = CriterionFactory.getBusinessQualityCriticalityCriterion();
                break;
            case LocationIsolation:
                criterion = CriterionFactory.getLocationIsolationCriterion();
                break;
            case ResourceConflict:
                criterion = CriterionFactory.getResourceConflictCriterion();
                break;
            case Communication:
                criterion = CriterionFactory.getCommunicationCriterion();
        }

        return criterion;
    }

    // 生成各种规则实例
    public static class CriterionFactory {

        public static Criterion getLifeCycleCommonalityCriterion() {
            CriterionName name = CriterionName.LifeCycleCommonality;
            CriterionType type = CriterionType.Cohesiveness;
            String spec = "a set of classes with the same lifecycle";
            double priority = CriterionPriority.LifeCycleCommonality;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getClassAssociationCriterion() {
            CriterionName name = CriterionName.ClassAssociation;
            CriterionType type = CriterionType.Cohesiveness;
            String spec = "";
            double priority = CriterionPriority.ClassAssociation;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getClassCompositionCriterion() {
            CriterionName name = CriterionName.ClassComposition;
            CriterionType type = CriterionType.Cohesiveness;
            String spec = "";
            double priority = CriterionPriority.ClassComposition;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getClassAggregateCriterion() {
            CriterionName name = CriterionName.ClassAggregate;
            CriterionType type = CriterionType.Cohesiveness;
            String spec = "";
            double priority = CriterionPriority.ClassAggregate;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getClassDependencyCriterion() {
            CriterionName name = CriterionName.ClassDependency;
            CriterionType type = CriterionType.Cohesiveness;
            String spec = "";
            double priority = CriterionPriority.ClassDependency;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getClassInheritanceCriterion() {
            CriterionName name = CriterionName.ClassInheritance;
            CriterionType type = CriterionType.Cohesiveness;
            String spec = "";
            double priority = CriterionPriority.ClassInheritance;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getDDDValueObjectCriterion() {
            CriterionName name = CriterionName.DDDValueObject;
            CriterionType type = CriterionType.Cohesiveness;
            String spec = "";
            double priority = CriterionPriority.DDDValueObject;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getDDDAggregateCriterion() {
            CriterionName name = CriterionName.DDDAggregate;
            CriterionType type = CriterionType.Cohesiveness;
            String spec = "";
            double priority = CriterionPriority.DDDAggregate;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getDDDServiceCriterion() {
            CriterionName name = CriterionName.DDDService;
            CriterionType type = CriterionType.Cohesiveness;
            String spec = "";
            double priority = CriterionPriority.DDDService;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getBusinessQualityCriticalityCriterion() {
            CriterionName name = CriterionName.BusinessQualityCriticality;
            CriterionType type = CriterionType.BusinessQuality;
            String spec = "";
            double priority = CriterionPriority.BusinessQualityCriticality;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getBusinessQualityCoupingCriterion() {
            CriterionName name = CriterionName.BusinessQualityCouping;
            CriterionType type = CriterionType.BusinessQuality;
            String spec = "";
            double priority = CriterionPriority.BusinessQualityCouping;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getLocationIsolationCriterion() {
            CriterionName name = CriterionName.LocationIsolation;
            CriterionType type = CriterionType.DeploymentStructure;
            String spec = "";
            double priority = CriterionPriority.LocationIsolation;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getResourceConflictCriterion() {
            CriterionName name = CriterionName.ResourceConflict;
            CriterionType type = CriterionType.DeploymentStructure;
            String spec = "";
            double priority = CriterionPriority.ResourceConflict;
            return new Criterion(name, type, spec, priority);
        }

        public static Criterion getCommunicationCriterion() {
            CriterionName name = CriterionName.Communication;
            CriterionType type = CriterionType.DeploymentStructure;
            String spec = "";
            double priority = CriterionPriority.Communicate;
            return new Criterion(name, type, spec, priority);
        }

    }
}
