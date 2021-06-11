package cn.hit.joker.newmsdivide.model.criteria;

/**
 * the priority of all criterion
 * range: [-1, 0)&(0, 1]
 */
public final class CriterionPriority {

    // cohesiveness: 内聚性原则
    public final static double LifeCycleCommonality = 10.0d;

    // class: 类图中的组合继承原则
    public final static double ClassAssociation = 2d;
    public final static double ClassComposition = 5d;
    public final static double ClassAggregate = 3d;
    public final static double ClassDependency = 1d;
    public final static double ClassInheritance = 5d;

    // DDD: 领域驱动设计原则
    public final static double DDDValueObject = -6d;
    public final static double DDDAggregate = 10d;
    public final static double DDDService = 6d;

    // value/quality: 业务价值、质量原则
    public final static double BusinessQualityCriticality = -10d;
    public final static double BusinessQualityCouping = 5d;

    // deployment: 部署结构原则
    public final static double LocationIsolation = -10d;
    public final static double ResourceConflict = -10d;
    public final static double Communicate = 5d;
}
