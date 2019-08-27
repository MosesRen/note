# redis study

redis的一些笔记（个人向）

详细文档参见 http://redisdoc.com/string/index.html

## redis原理

单线程，用队列处理并发访问，由于是内存型数据库 用来当作缓存，读写比较快。

### 与memcache比较

+ 

![redis区别memcache](..\img\redis区别memcache.jpg)

## redis 数据类型

- string

  **常用命令:** set,get,decr,incr,mget 等。

  string 类型是 Redis 最基本的数据类型，string 类型的值最大能存储 512MB
  
  ```shell
SET key value
  ```

- hash

  **常用命令：** hget,hset,hgetall 等。

  Redis hash 是一个键值(key=>value)对集合。

  Redis hash 是一个 string 类型的 field 和 value 的映射表，hash 特别适合用于存储对象。

  ```shell
  HSET key field value
  ```

- list

  **常用命令:** lpush,rpush,lpop,rpop,lrange等

  Redis 列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）。

  ```
  lpush key value
  ```

  

- set

  **常用命令：** sadd,spop,smembers,sunion 等

  Redis的Set是string类型的无序集合。集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是O(1)。

  ```shell
  sadd key member
  ```

- ```shell
  sadd key member
  ```

- zset(有序集合)

  **常用命令：** zadd,zrange,zrem,zcard等

  Redis zset 和 set 一样也是string类型元素的集合,且不允许重复的成员。不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。
  
  ```shell
  zadd key score member
  ```



## Redis 过期时间

在设置redis时可以设置过期时间

### 删除方式：定期删除+惰性删除

- **定期删除**：redis默认是每隔 100ms 就**随机抽取**一些设置了过期时间的key，检查其是否过期，如果过期就删除。注意这里是随机抽取的。为什么要随机呢？你想一想假如 redis 存了几十万个 key ，每隔100ms就遍历所有的设置过期时间的 key 的话，就会给 CPU 带来很大的负载！
- **惰性删除** ：定期删除可能会导致很多过期 key 到了时间并没有被删除掉。所以就有了惰性删除。假如你的过期 key，靠定期删除没有被删除掉，还停留在内存里，除非你的系统去查一下那个 key，才会被redis给删除掉。这就是所谓的惰性删除，也是够懒的哈！

## 内存淘汰机制

由于删除方式的原因，容易导致大量过期的key堆积未被删除，容易导致过期key堆积在内存中，使得内存不足，或超出上限。

这时候需要有内存淘汰机制来及时清理内存。

***noeviction:** 当内存不足以容纳新写入数据时，新写入操作会报错，这个一般没人用吧，实在是太恶心了。

**allkeys-lru：**当内存不足以容纳新写入数据时，在键空间中，移除最近最少使用的 key（这个是最常用的）。

**allkeys-random：**当内存不足以容纳新写入数据时，在键空间中，随机移除某个 key，这个一般没人用吧，为啥要随机，肯定是把最近最少使用的 key 给干掉啊。

**volatile-lru**：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，移除最近最少使用的 key（这个一般不太合适）。

**volatile-random**：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，随机移除某个 key。

**volatile-ttl**：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，有更早过期时间的 key 优先移除。

4.0版本后增加以下两种：

1. **volatile-lfu**：从已设置过期时间的数据集(server.db[i].expires)中挑选最不经常使用的数据淘汰
2. **allkeys-lfu**：当内存不足以容纳新写入数据时，在键空间中，移除最不经常使用的key

- valatile 过期数据
- allkeys 所有数据
- lru 最少使用
- lfu 最不经常使用

## redis 持久化

1. **快照（snapshotting）持久化（RDB）**

   redis 默认持久化方式

   ```
   save 900 1           #在900秒(15分钟)之后，如果至少有1个key发生变化，Redis就会自动触发BGSAVE命令创建快照。
   
   save 300 10          #在300秒(5分钟)之后，如果至少有10个key发生变化，Redis就会自动触发BGSAVE命令创建快照。
   
   save 60 10000        #在60秒(1分钟)之后，如果至少有10000个key发生变化，Redis就会自动触发BGSAVE命令创建快照。
   ```

2. **只追加文件（append-only file,AOF）**

与快照持久化相比，AOF持久化 的实时性更好，因此已成为主流的持久化方案。默认情况下Redis没有开启AOF（append only file）方式的持久化，可以通过appendonly参数开启：

```
appendonly yes
```

开启AOF持久化后每执行一条会更改Redis中的数据的命令，Redis就会将该命令写入硬盘中的AOF文件。AOF文件的保存位置和RDB文件的位置相同，都是通过dir参数设置的，默认的文件名是appendonly.aof。

在Redis的配置文件中存在三种不同的 AOF 持久化方式，它们分别是：

```
appendfsync always    #每次有数据修改发生时都会写入AOF文件,这样会严重降低Redis的速度
appendfsync everysec  #每秒钟同步一次，显示地将多个写命令同步到硬盘
appendfsync no        #让操作系统决定何时进行同步
```

为了兼顾数据和写入性能，用户可以考虑 appendfsync everysec选项 ，让Redis每秒同步一次AOF文件，Redis性能几乎没受到任何影响。而且这样即使出现系统崩溃，用户最多只会丢失一秒之内产生的数据。当硬盘忙于执行写入操作的时候，Redis还会优雅的放慢自己的速度以便适应硬盘的最大写入速度。

Redis 4.0 开始支持 RDB 和 AOF 的混合持久化（默认关闭，可以通过配置项 `aof-use-rdb-preamble` 开启）。

### redis 事务

​	 单线程和事务关系的思考：（为什么redis单线程还需要事务）

单线程只能保证单个命令是原子的，如果需要多个命令是原子性的，需要提供事务来实现。

MULTI 开启事务

EXEC 提交事务

DISCARD 丢弃事务

WATCH  watch 为redis提供了CAS 功能，在进行操作前监视那些键值。

### 缓存雪崩和缓存穿透

缓存雪崩：缓存同一时间大面积的失效，所以，后面的请求都会落到数据库上，造成数据库短时间内承受大量请求而崩掉。

+ 设定随机失效时间

### 缓存穿透

缓存穿透是指查询一个一定不存在的数据，由于缓存是不命中时被动写的，并且出于容错考虑，如果从存储层查不到数据则不写入缓存，这将导致这个不存在的数据每次请求都要到存储层去查询，失去了缓存的意义。在流量大时，可能DB就挂掉了，要是有人利用不存在的key频繁攻击我们的应用，这就是漏洞。

解决方案：有很多种方法可以有效地解决缓存穿透问题，最常见的是采用布隆过滤器，将所有可能存在的数据哈希到一个足够大的bitmap中，一个一定不存在的数据会被这个bitmap拦截掉，从而避免了对底层存储系统的查询压力。另外也有一个更为简单粗暴的方法（我们采用的就是这种），如果一个查询返回的数据为空（不管是数据不存在，还是系统故障），我们仍然把这个空结果进行缓存，但它的过期时间会很短，最长不超过五分钟。

### 数据一致性
test