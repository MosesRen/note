# 一些平时的note记录
### import问题 

在同一个包下，一个类访问另一个类的静态成员变量无需import 只要直接 类名.成员名即可

### ceil()和floor()区别

ceil()对整形变量向左取整，返回类型为double型。不小于x的最大整数，如ceil(1.5) =2

floor()对整形变量向左取整，返回类型为double型。不大于x的最大整数，如floor(1.5) = 1;

round(x) = Math.floor(x+0.5) 四舍五入

### Stringbuffer 和Stringbuilder

buffer 线程安全，先提出，builder线程不安全，后提出为了改善效率。

### 日期和时间

获取时间Date();
获取日期Calendar().getInstance();

### java 引用传递

只存在值传递，只存在值传递！！！基础类型传值，引用了类型虽然传引用，但是传递的是传入的对象的一个拷贝的引用。修改并不影响原来对象。

### java 内部类

+ 成员内部类
    等同与成员变量
+ 局部内部类
    等同与局部变量，没有修饰符，内部可访问。
+ 匿名内部类
    在实现父类或者接口时提供相应的对象而不许需要增加额外的方法。
+ 静态内部类
    静态成员变量
**其他**：编译时得到两个class文件一个为外部类   Outter.class文件 另一个为内部类的字节码文件Outter$Inner.class

### 函数式接口(Functional Interface)

就是一个有且仅有一个抽象方法，但是可以有多个非抽象方法的接口。函数式接口可以被隐式转换为 lambda 表达式。

函数式接口可以有方法的默认实现，也可以与静态的方法，也可以定义java.lang.Object 的public方法

可以用lambda表示一个函数式接口的实现

**常用的函数式接口**

​	java.lang.Runnable,

​	java.awt.event.ActionListener, 

​	java.util.Comparator,

​	java.util.concurrent.Callable

​	java.util.function包下的接口，如Consumer、Predicate、Supplier等

### 接口和抽象类的区别是什么？

1. 接口的方法默认是 public，所有方法在接口中不能有实现(Java 8 开始接口方法可以有默认实现），而抽象类可以有非抽象的方法。
2. 接口中除了static、final变量，不能有其他变量，而抽象类中则不一定。
3. 一个类可以实现多个接口，但只能实现一个抽象类。接口自己本身可以通过extends关键字扩展多个接口。
4. 接口方法默认修饰符是public，抽象方法可以有public、protected和default这些修饰符（抽象方法就是为了被重写所以不能使用private关键字修饰！）。
5. 从设计层面来说，抽象是对类的抽象，是一种模板设计，而接口是对行为的抽象，是一种行为的规范。

### 在 Java 中定义一个不做事且没有参数的构造方法的作用

Java 程序在执行子类的构造方法之前，如果没有用 `super() `来调用父类特定的构造方法，则会调用父类中“没有参数的构造方法”。因此，如果父类中只定义了有参数的构造方法，而在子类的构造方法中又没有用 `super() `来调用父类中特定的构造方法，则编译时将发生错误，因为 Java 程序在父类中找不到没有参数的构造方法可供执行。解决办法是在父类里加上一个不做事且没有参数的构造方法。 　

### 构造方法有哪些特性？

1. 名字与类名相同。
2. 没有返回值，但不能用void声明构造函数。
3. 生成类的对象时自动执行，无需调用。

### == 与 equals(重要)

**==** : 它的作用是判断两个对象的地址是不是相等。即，判断两个对象是不是同一个对象(基本数据类型==比较的是值，引用数据类型==比较的是内存地址)。

**equals()** : 它的作用也是判断两个对象是否相等。但它一般有两种使用情况：

- 情况1：类没有覆盖 equals() 方法。则通过 equals() 比较该类的两个对象时，等价于通过“==”比较这两个对象。
- 情况2：类覆盖了 equals() 方法。一般，我们都覆盖 equals() 方法来比较两个对象的内容是否相等；若它们的内容相等，则返回 true (即，认为这两个对象相等)。

### List数组相互转换

java steam

```java
Integer [] myArray = { 1, 2, 3 };
List myList = Arrays.stream(myArray).collect(Collectors.toList());
//基本类型也可以实现转换（依赖boxed的装箱操作）
int [] myArray2 = { 1, 2, 3 };
List myList = Arrays.stream(myArray2).boxed().collect(Collectors.toList());
```

​	Guava

```java
List<String> il = ImmutableList.of("string", "elements");  // from varray
List<String> il = ImmutableList.copyOf(aStringArray);      // from array
```

### Collection.toArray()方法使用的坑&如何反转数组

该方法是一个泛型方法：`<T> T[] toArray(T[] a);` 如果`toArray`方法中没有传递任何参数的话返回的是`Object`类型数组。

```java
String [] s= new String[]{
    "dog", "lazy", "a", "over", "jumps", "fox", "brown", "quick", "A"
};
List<String> list = Arrays.asList(s);
Collections.reverse(list);
s=list.toArray(new String[0]);//没有指定类型的话会报错
```

由于JVM优化，`new String[0]`作为`Collection.toArray()`方法的参数现在使用更好，`new String[0]`就是起一个模板的作用，指定了返回数组的类型，0是为了节省空间，因为它只是为了说明返回的类型。

###  不要在 foreach 循环里进行元素的 remove/add 操作

如果要进行`remove`操作，可以调用迭代器的 `remove `方法而不是集合类的 remove 方法。因为如果列表在任何时间从结构上修改创建迭代器之后，以任何方式除非通过迭代器自身`remove/add`方法，迭代器都将抛出一个`ConcurrentModificationException`,这就是单线程状态下产生的 **fail-fast 机制**。

> **fail-fast 机制** ：多个线程对 fail-fast 集合进行修改的时，可能会抛出ConcurrentModificationException，单线程下也会出现这种情况，上面已经提到过。