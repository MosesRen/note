# zookeeper学习笔记

ZooKeeper是一个开源的分布式服务框架，为分布式应用提供协调服务，用来解决分布式应用中的数据管理问题，如：配置管理、域名服务、分布式同步、集群管理等

## zookeeper应用场景

### 配置管理

在开发过程中需要获取一些配置，如果这些配置发生了改变的话，那么需要每一台的服务都去修改配置，针对这个问题，我们将一些公共的配置放在zookeeper的节点中，然后应用程序连接到zookeeper上，并监听配置的修改，这样就可以实现配置的管理。

### 分布式锁

**思路**

1. 首先zookeeper中我们可以创建一个/distributed_lock持久化节点
2. 然后再在/distributed_lock节点下创建自己的临时顺序节点，比如：/distributed_lock/task_00000000008
3. 获取所有的/distributed_lock下的所有子节点，并排序
4. 判读自己创建的节点是否最小值（第一位）
5. 如果是，则获取得到锁，执行自己的业务逻辑，最后删除这个临时节点。
6. 如果不是最小值，则需要监听自己创建节点前一位节点的数据变化，并阻塞。
7. 当前一位节点被删除时，我们需要通过递归来判断自己创建的节点是否在是最小的，如果是则执行5）；
8. 如果不是则执行6）（就是递归循环的判断）

### 集群管理

集群管理的主要内容包括：节点（机器）增删及Master选取。

**节点增删**：所有机器约定在父目录GroupMembers下创建临时目录节点，然后监听父目录节点的子节点变化消息。一旦有机器挂掉，该机器与 zookeeper的连接断开，其所创建的临时目录节点被删除，所有其他机器都收到通知：某个兄弟目录被删除，于是，所有人都知道：它上船了。新机器加入 也是类似，所有机器收到通知：新兄弟目录加入，highcount又有了。

**Master选取**：所有机器创建临时顺序编号目录节点，每次选取编号最小的机器作为master就好。

## zookeeper组成

1. 文件系统
2. 通知机制

### 文件系统

 ZooKeeper维护一个类似Linux文件系统的数据结构，用于存储数据

- 数据模型结构是一种树形结构，由许多节点构成
- 每个节点叫做ZNode（ZooKeeper Node）
- 每个节点对应一个唯一路径，通过该路径来标识节点，如 /app1/p_2
- 每个节点只能存储大约1M的数据

#### 节点类型

+ 持久化目录节点 persistent

  客户端与服务器断开连接，该节点仍然存在

+ 持久化顺序编号目录节点 persistent_sequential

  客户端与服务器断开连接，该节点仍然存在，此时节点会被顺序编号，如：000001、000002…

+ 临时目录节点 ephemeral

  客户端与服务器断开连接，该节点会被删除

+ 临时顺序编号目录节点 ephemeral_sequential

  客户端与服务器断开连接，该节点会被删除，此时节点会被顺序编号，如：000001、000002…

### 通知机制

 ZooKeeper是一个基于观察者模式设计的分布式服务管理框架

ZooKeeper负责管理和维护项目的公共数据，并授受观察者的注册（订阅）一旦这些数据发生化，ZooKeeper就会通知已注册的观察者此时观察者就可以做出相应的反应简单来说，客户端注册监听它关心的目录节点，当目录节点发生变化时，ZooKeeper会通知客户端
ZooKeeper是一个订阅中心（注册中心）

### zookeeper集群

**tips:**zookeeper集群，只要有半数以上的机器处于可用状态，那么集群就是可用的。因此一个集群通常是奇数的，因为5台和6台能提供服务的最少机器数都是一样的。

#### zk角色：

leader：负责投票的发起和决议，系统状态的更新，master？

leaner：

​	follower：处理来自client的请求，并参与投票

​	observer:仅处理客户端的连接和请求，同时不参与投票，为了扩展系统，提高读取的速度和并发的能力。

client: 客户端，请求发起方。

**集群的数据一致性协议**：ZAB协议（Zookeeper Atomic Broadcast（Zookeeper 原子广播协议））

#### **选举过程**

##### 服务器初始化启动。

**(1) 每个Server发出一个投票**。由于是初始情况，Server1和Server2都会将自己作为Leader服务器来进行投票，每次投票会包含所推举的服务器的myid和ZXID，使用(myid, ZXID)来表示，此时Server1的投票为(1, 0)，Server2的投票为(2, 0)，然后各自将这个投票发给集群中其他机器。

**(2) 接受来自各个服务器的投票**。集群的每个服务器收到投票后，首先判断该投票的有效性，如检查是否是本轮投票、是否来自LOOKING状态的服务器。

**(3) 处理投票**。针对每一个投票，服务器都需要将别人的投票和自己的投票进行PK，PK规则如下

**· 优先检查ZXID**。ZXID比较大的服务器优先作为Leader。

**· 如果ZXID相同，那么就比较myid**。myid较大的服务器作为Leader服务器。

对于Server1而言，它的投票是(1, 0)，接收Server2的投票为(2, 0)，首先会比较两者的ZXID，均为0，再比较myid，此时Server2的myid最大，于是更新自己的投票为(2, 0)，然后重新投票，对于Server2而言，其无须更新自己的投票，只是再次向集群中所有机器发出上一次投票信息即可。

**(4) 统计投票**。每次投票后，服务器都会统计投票信息，判断是否已经有过半机器接受到相同的投票信息，对于Server1、Server2而言，都统计出集群中已经有两台机器接受了(2, 0)的投票信息，此时便认为已经选出了Leader。

**(5) 改变服务器状态**。一旦确定了Leader，每个服务器就会更新自己的状态，如果是Follower，那么就变更为FOLLOWING，如果是Leader，就变更为LEADING。

##### 服务器运行期间无法和Leader保持连接。

(1) **变更状态**。Leader挂后，余下的非Observer服务器都会讲自己的服务器状态变更为LOOKING，然后开始进入Leader选举过程。

(2) **每个Server会发出一个投票**。在运行期间，每个服务器上的ZXID可能不同，此时假定Server1的ZXID为123，Server3的ZXID为122；在第一轮投票中，Server1和Server3都会投自己，产生投票(1, 123)，(3, 122)，然后各自将投票发送给集群中所有机器。

(3) **接收来自各个服务器的投票**。与启动时过程相同。

(4) **处理投票**。与启动时过程相同，此时，Server1将会成为Leader。

(5) **统计投票**。与启动时过程相同。

(6) **改变服务器的状态**。与启动时过程相同。

#### 数据同步过程

选完 leader 以后，zk 就进入状态同步过程。
1、leader 等待 server 连接；
2、follower 连接 leader，将最大的 zxid 发送给 leader；
3、leader 根据 follower 的 zxid 确定同步点；
4、完成同步后通知 follower 已经成为 uptodate 状态；
5、follower 收到 uptodate 消息后，又可以重新接受 client 的请求进行服务了。

#### zookeeper 写数据流程

1.Client向Zookeeper的Server1上写数据，发送一个写请求

2.如果Server1不是Leader，那么Server1会把接收到的请求进一步转发给Leader,这个Leader会把写请求广播给各个Leader,各个Server写成功后就会通知Leader

3.当Leader收到大多数Server数据写成功了，那么就说明数据写成功了，比如这里有三个节点，只有两个节点数据写成功了，就认为数据写成功了，写成功以后，Leader会告诉Server数据写成功了

4.Server1会通知Client数据写成功了，这时就认为整个写操作成功

## zookeeper协议

待补充。。。。