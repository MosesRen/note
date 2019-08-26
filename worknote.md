常用工具

zabbix监控大盘

logLevel ERROR

grafana 服务指标

soa 管理平台 故障自动分析 调用链查询

查看服务器error.log 和gc.log文件（gc时服务不可用）

zabbix 监控 cpu usage 网络 内存 集群的物理状态 IO

redis CPU使用 慢查询 db manager

MQ情况 消息积压 unac/ready 消息数量  connection 和channel状态

资源隔离
    

1. rpc mq进程间通信隔离 将通信过程与业务隔离,将redis 等访问逻辑进行封装。解耦
2. redis db hbase es等相关存储介质的隔离
3. file 本地资源的隔离
4. 资源占用的释放
5. 可复用逻辑代码的封装

一些坑

- 对静态引用指向的内存进行修改
- redis 的mXXX命令的将结果判断长度key是否存在 键不存在的时候
- mq未按业务隔离 导致主流程mq大量积压
- mq cache 优化

服务代码梳理

```
重复造轮子
对外接口参数校验，核心接口风控
资源散落在各个服务的直接，没有做统一的资源的访问接口
```

核心流程梳理
    

```
单车基础 核心领域模型抽象
外部调用矢量的梳理
上下游依赖强弱的梳理
容灾场景和方案的梳理
```

链路追踪 redis db的组件
界定服务边界
代码项目重构
根据压测数据设置熔断，降级，限流相关等级