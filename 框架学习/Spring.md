# Spring 学习

## Spring主要框架

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