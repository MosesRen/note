# Spring 学习

## Spring主要框架

Spring 容器启动过程

- 1、资源定位：找到配置文件（载入配置文件/Springboot采用自动扫描）
- 2、BeanDefinition载入和解析（解析成BeanDefiniton）
- 3、BeanDefinition注册（添加到map(key,Definition)）
- 4、bean的实例化和依赖注入(getBean()方法进行实例化)

## Spring 主要功能

## ICO

## AOP









## 常用注解

*@Autowired Spring* 容器查找并注入一个bean对象 按类去匹配

*@Qualifier* 指定注入一个bean对象 按名字去匹配

*@Resource*
(1)、@Resource后面没有任何内容，默认通过name属性去匹配bean，找不到再按type去匹配
(2)、指定了name或者type则根据指定的类型去匹配bean
(3)、指定了name和type则根据指定的name和type去匹配bean，任何一个不匹配都将报错

*@Component/@Repository/@Service/@Controller*
Component 通用bean
Repository 对应数据访问层Bean
Service 对应业务逻辑层bean
Controller 对应表现层的Bean

@refrence与@resource 

前者是dubbo注解，后者是spring 的。后者@resource很简单就是注入资源，与@Autowired比较接近，只不过是按照变量名（beanid）注入。@reference也是注入，但是一般用来注入分布式的远程服务对象，需要配合dubbo配置使用。他们的区别就是一个是本地spring容器，另一个是把远程服务对象当做spring容器中的对象一样注入。