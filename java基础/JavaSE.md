# Java SE

## 基本类型缓存池

基本类型和对应的包装类之间的赋值可以直接使用自动装箱和拆箱，可以向操作对象一样的操作基本类型数据。

new Integer(123) 与 Integer.valueOf(123) 的区别在于：

- new Integer(123) 每次都会新建一个对象；
- Integer.valueOf(123) 会使用缓存池中的对象，多次调用会取得同一个对象的引用。
  例如：Integer a = 10;
  调用的就是Integer.valueof()方法(自动装箱调用这个方法)

所有整数类型的类都有类似的缓存机制，基本类型对应的缓冲池如下：

- boolean values true and false
- all byte values
- short values between -128 and 127
- int values between -128 and 127
- char in the range \u0000 to \u007F

## 修饰符访问权限

访问权限控制：

public > protected > default > private

所有->其他包的非子类无法访问->子类无法访问->仅限本类

## CLASs和object类

### Class类

Class类也是类的一种，内容是创建的类的类型信息，Class只能由JVM去创建，没有public构造函数。

获取Class对象的方法

```java
//使用forName()方法
Class obj= Class.forName("shapes");
//使用getClass()方法
Class obj=s1.getClass();
Class obj1=s1.getSuperclass();
```

### Objetc类

```java
1．clone方法
保护方法，实现对象的浅复制，只有实现了Cloneable接口才可以调用该方法，否则抛出CloneNotSupportedException异常。
2．getClass方法
final方法，获得运行时类型。
3．toString方法
该方法用得比较多，一般子类都有覆盖。
4．finalize方法
该方法用于释放资源。因为无法确定该方法什么时候被调用，很少使用。
5．equals方法
该方法是非常重要的一个方法。一般equals和==是不一样的，但是在Object中两者是一样的。子类一般都要重写这个方法。
6．hashCode方法
该方法用于哈希查找，重写了equals方法一般都要重写hashCode方法。这个方法在一些具有哈希功能的Collection中用到。
一般必须满足obj1.equals(obj2)==true。可以推出obj1.hash- Code()==obj2.hashCode()，但是hashCode相等不一定就满足equals。不过为了提高效率，应该尽量使上面两个条件接近等价。
7．wait方法
wait方法就是使当前线程等待该对象的锁，当前线程必须是该对象的拥有者，也就是具有该对象的锁。wait()方法一直等待，直到获得锁或者被中断。wait(long timeout)设定一个超时间隔，如果在规定时间内没有获得锁就返回。
调用该方法后当前线程进入睡眠状态，直到以下事件发生。
（1）其他线程调用了该对象的notify方法。
（2）其他线程调用了该对象的notifyAll方法。
（3）其他线程调用了interrupt中断该线程。
（4）时间间隔到了。
此时该线程就可以被调度了，如果是被中断的话就抛出一个InterruptedException异常。
8．notify方法
该方法唤醒在该对象上等待的某个线程。
9．notifyAll方法
该方法唤醒在该对象上等待的所有线程。
```

