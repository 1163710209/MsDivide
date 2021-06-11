package cn.hit.joker.newmsdivide.model.criteria;

public enum CriterionName {

    // cohesiveness: 内聚性原则
    LifeCycleCommonality,
    // class: 类图中的组合继承原则
    ClassAssociation,
    ClassComposition,
    ClassAggregate,
    ClassDependency,
    ClassInheritance,
    // DDD: 领域驱动设计原则
    DDDValueObject,
    DDDAggregate,
    DDDService,

    // value/quality: 业务价值、质量原则
    BusinessQualityCriticality,
    BusinessQualityCouping,

    // deployment: 部署结构原则
    LocationIsolation,
    ResourceConflict,
    Communication
}
