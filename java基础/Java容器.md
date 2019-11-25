

# java 容器

## 容器类型

主要有两种Collection 和 Map，Collection主要是集合，而Map是键值对映射。

### 为什么要有hashCode

**我们以“HashSet如何检查重复”为例子来说明为什么要有hashCode：**

当你把对象加入HashSet时，HashSet会先计算对象的hashcode值来判断对象加入的位置，同时也会与其他已经加入的对象的hashcode值作比较，如果没有相符的hashcode，HashSet会假设对象没有重复出现。但是如果发现有相同hashcode值的对象，这时会调用equals（）方法来检查hashcode相等的对象是否真的相同。如果两者相同，HashSet就不会让其加入操作成功。如果不同的话，就会重新散列到其他位置。（摘自我的Java启蒙书《Head fist java》第二版）。这样我们就大大减少了equals的次数，相应就大大提高了执行速度。

### hashCode（）与equals（）的相关规定

1. 如果两个对象相等，则hashcode一定也是相同的
2. 两个对象相等,对两个对象分别调用equals方法都返回true
3. 两个对象有相同的hashcode值，它们也不一定是相等的
4. **因此，equals方法被覆盖过，则hashCode方法也必须被覆盖**
5. hashCode()的默认行为是对堆上的对象产生独特值。如果没有重写hashCode()，则该class的两个对象无论如何都不会相等（即使这两个对象指向相同的数据）

### 为什么两个对象有相同的hashcode值，它们也不一定是相等的？

在这里解释一位小伙伴的问题。以下内容摘自《Head Fisrt Java》。

因为hashCode() 所使用的杂凑算法也许刚好会让多个对象传回相同的杂凑值。越糟糕的杂凑算法越容易碰撞，但这也与数据值域分布的特性有关（所谓碰撞也就是指的是不同的对象得到相同的 hashCode）。

我们刚刚也提到了 HashSet,如果 HashSet 在对比的时候，同样的 hashcode 有多个对象，它会使用 equals() 来判断是否真的相同。也就是说 hashcode 只是用来缩小查找成本。

### Iterator接口

所有实现了Collection接口的容器类都有一个iterator()方法用以返回一个实现了Iterator接口的对象，这个对象可以是多种类型，不同的Collection实现类型遍历方式不同（用于遍历集合类）。

Iterator对象称作迭代器，用以方便的实现对容器内元素的遍历操作。

一句话总结，Iterator就是一个统一的遍历Collection中的元素的接口。

Iterator接口定义了如下方法

```java
//判断游标右边是否有元素
boolean hasNext();
//返回游标右边的元素并将游标移动到下一个位置
E next();
//删除游标左边的元素，在执行完next之后该操作只能执行一次
default void remove()
```

Iterator对象的remove方法是在迭代过程中删除元素的唯一安全的方法。

增强的for循环对于遍历array或Collection的时候相当简便

缺陷：1.数组不能方便的访问下标值

​           2.集合与使用Iterator相比，不能方便的删除结合中的内容，在内部也是调用Iterator

### Map类集合

| Map集合类        | key          | value        | Super       | JDK  | 说明                          |
| ---------------- | ------------ | ------------ | ----------- | ---- | ----------------------------- |
| HashTable        | 不允许为null | 不允许为null | Dictionary  | 1.0  | 线程安全（过时）              |
| ConcurrenHashMap | 不允许为null | 不允许为null | AbstractMap | 1.5  | 锁分段技术或CAS（JDK8及以上） |
| TreeMap          | 不允许为null | 允许为null   | AbstractMap | 1.2  | 线程不安全（有序）            |
| HashMap          | 允许为null   | 允许为null   | AbstractMap | 1.2  | 线程不安全（resize死链问题）  |

HashMap均可以为null，而tree由于需要比较key，所以key不能为null，其他的都不允许为空。

TreeMap 底层采用红黑树。

TreeSet底层实现是采用TreeMap，而HashSet底层是采用HashMap实现

#### 红黑树

一种平衡二叉查找树,查找树(左节点上的值小于根节点，右节点上的值大于根节点),平衡查找树(左右子树的高度差的绝对值最大为1)

1）节点要么为红色，要么为黑色。（不然为啥叫红黑树；））

2）根节点为黑色。

3）叶子节点为黑色。 （这两个简直送分，最上面和最下面都是黑的）

4）每个红色节点的左右孩子都是黑色。 （保证了从根节点到叶子节点不会出现连续两个红色节点）

5）从任意节点到其每个叶子节点的所有路径，都包含相同数目的黑色节点。（4,5是使得红黑树为平衡树的关键）

### 并发容器

#### HashTable

在所有put操作的时候都用synchronized进行加锁。

#### ConcurrentHashMap

是由 Segment 数组、HashEntry 组成，和 HashMap 一样，仍然是数组加链表

采用volatile修饰value和链表的Entry保证多线程的可见性。

1.7版本：

采用segment进行分段，每段只能同时有一个线程操作，put时，先通过key定位到对应的segment，然后竞争时自旋获取对应段的锁。由于value属性是采用volatile修饰，因此get时无需加锁。

扩容的优化：原来是采用头插法进行链表的复制，高并发的情况下，可能会出现环链表。

1.8版本：

其中抛弃了原有的 Segment 分段锁，而采用了 `CAS + synchronized` 来保证并发安全性。

扩容机制修改了原来的头插法，改为了将原来的node分为保留的和新加的桶里，原来位置的元素只可能在i位置和i+oldcCap位置(hash的特性)

