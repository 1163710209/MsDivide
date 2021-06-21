package cn.hit.joker.newmsdivide.model.criteria;

/**
 * the priority of all criterion
 * range: [-10, 0)&(0, 10]
 */
public final class CriterionPriority {

    private static final double cohesionPriority = 1d;
    private static final double valuePriority = 1d;
    private static final double deployPriority = 1d;

    // cohesiveness: 内聚性原则
    public final static double LifeCycleCommonality = 10.0d * cohesionPriority;

    // class: 类图中的组合继承原则
    public final static double ClassAssociation = 2d * cohesionPriority;
    public final static double ClassComposition = 5d * cohesionPriority;
    public final static double ClassAggregate = 3d * cohesionPriority;
    public final static double ClassDependency = 1d * cohesionPriority;
    public final static double ClassInheritance = 5d * cohesionPriority;

    // DDD: 领域驱动设计原则
    public final static double DDDValueObject = -6d * cohesionPriority;
    public final static double DDDAggregate = 10d * cohesionPriority;
    public final static double DDDService = 6d * cohesionPriority;

    // value/quality: 业务价值、质量原则
    public final static double BusinessQualityCriticality = -10d * valuePriority;
    public final static double BusinessQualityCouping = 5d * valuePriority;

    // deployment: 部署结构原则
    public final static double LocationIsolation = -10d * deployPriority;
    public final static double ResourceConflict = -10d * deployPriority;
    public final static double Communicate = 5d * deployPriority;
}
