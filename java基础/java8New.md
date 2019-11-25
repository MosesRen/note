# Java 8 新特性总结

## 1.接口默认方法

可使用`default`关键字向接口添加非抽象方法的实现。

```java
interface Formula{

    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }

}
```

## 2.lambda表达式 

lambda运算符：所有的lambda表达式都是用新的lambda运算符 " => ",可以叫他，“转到”或者 “成为”。运算符将表达式分为两部分，左边指定输入参数，右边是lambda的主体。lambda实质是对接口的实现。

lambda表达式：

1.一个参数：param -> expr

2.多个参数：（param-list）-> expr

```java
// 1. 不需要参数,返回值为 5  
() -> 5  
  
// 2. 接收一个参数(数字类型),返回其2倍的值  
x -> 2 * x  
  
// 3. 接受2个参数(数字),并返回他们的差值  
(x, y) -> x – y  
  
// 4. 接收2个int型整数,返回他们的和  
(int x, int y) -> x + y  
  
// 5. 接受一个 string 对象,并在控制台打印,不返回任何值(看起来像是返回void)  
(String s) -> System.out.print(s)
```

lambda表达式作用域：

+ 局部变量，必须用final修饰，否则在使用后无法被修改，（隐形设为了final）。
+ 实例变量和静态变量，lambda均可以访问。
+ 默认接口方法：不适用于lambda表达式。

lambda 使用场景：

+ 

## 3.函数式接口

**“函数式接口”是指仅仅只包含一个抽象方法,但是可以有多个非抽象方法(也就是上面提到的默认方法)的接口。**使用`@FunctionalInterface` 注解进行声明,编译器会在编译的时候进行检查。

四大内置函数式接口	java.util.function包下:

| 函数式接口                     | 参数类型 | 返回类型    | 用途                                                        |
| ------------------------------ | -------- | ----------- | ----------------------------------------------------------- |
| **Consumer<T>(消费型接口)**    | **T**    | **void**    | **对类型为T的对象应用操作。void accept(T t)**               |
| **Supplier<T>(供给型接口)**    | **无**   | **T**       | **返回类型为T的对象。 T get();**                            |
| **Function<T, R>(函数型接口)** | **T**    | **R**       | **对类型为T的对象应用操作并返回R类型的对象。R apply(T t);** |
| **Predicate<T>(断言型接口)**   | **T**    | **boolean** | **确定类型为T的对象是否满足约束。boolean test(T t);**       |

#### Predicates

Predicate 接口是只有一个参数的返回布尔类型值的 **断言型** 接口。该接口包含多种默认方法来将 Predicate 组合成其他复杂的逻辑（比如：与，或，非）

#### Function

Function 接口接受一个参数并生成结果。

#### Supplier 

Supplier 接口产生给定泛型类型的结果。 与 Function 接口不同，Supplier 接口不接受参数。

#### Consumers

Consumer 接口表示要对单个输入参数执行的操作。

## 4.方法与构造函数引用

Java 8允许您通过`::`关键字传递方法或构造函数的引用。 上面的示例显示了如何引用静态方法。 但我们也可以引用对象方法：

```java
class Something {
    String startsWith(String s) {
        return String.valueOf(s.charAt(0));
    }
}
class Test{
    public static void main(String [] args) {
        Something something = new Something();
        Converter<String, String> converter = something::startsWith;
        String converted = converter.convert("Java");
        System.out.println(converted);    // "J"
    }
}

```

构造器的方法引用

接下来看看构造函数是如何使用`::`关键字来引用的，首先我们定义一个包含多个构造函数的简单类：

```java
class Person {
    String firstName;
    String lastName;

    Person() {}

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
```

接下来我们指定一个用来创建Person对象的对象工厂接口：

```java
interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}
```

这里我们使用构造函数引用来将他们关联起来，而不是手动实现一个完整的工厂：

```java
//代码三
PersonFactory<Person> personFactory = Person::new;
Person person = personFactory.create("Peter", "Parker");
```

理解（个人向）定义了一个返回P对象的接口，在代码三中采用Person的构造函数对赋值给了这个接口的对象，在下面create调用中，自动调用了构造方法。

## 5.Optionals

Optionals不是函数式接口，而是用于防止 NullPointerException 的漂亮工具。这是下一节的一个重要概念，让我们快速了解一下Optionals的工作原理。

Optional 是一个简单的容器，其值可能是null或者不是null。在Java 8之前一般某个函数应该返回非空对象但是有时却什么也没有返回，而在Java 8中，你应该返回 Optional 而不是 null。

```java
//of（）：为非null的值创建一个Optional
Optional<String> optional = Optional.of("bam");
// isPresent（）： 如果值存在返回true，否则返回false
optional.isPresent();           // true
//get()：如果Optional有值则将其返回，否则抛出NoSuchElementException
optional.get();                 // "bam"
//orElse（）：如果有值则将其返回，否则返回指定的其它值
optional.orElse("fallback");    // "bam"
//ifPresent（）：如果Optional实例有值则为其调用consumer，否则不做处理
optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
```

## 6.Steam

`java.util.Stream` 表示能应用在一组元素上一次执行的操作序列。Stream 操作分为中间操作或者最终操作两种，最终操作返回一特定类型的计算结果，而中间操作返回Stream本身，这样你就可以将多个操作依次串起来。Stream 的创建需要指定一个数据源，比如`java.util.Collection` 的子类，List 或者 Set， Map 不支持。Stream 的操作可以串行执行或者并行执行。

首先看看Stream是怎么用，首先创建实例代码的用到的数据List：

```java
List<String> stringList = new ArrayList<>();
stringList.add("ddd2");
stringList.add("aaa2");
stringList.add("bbb1");
stringList.add("aaa1");
stringList.add("bbb3");
stringList.add("ccc");
stringList.add("bbb2");
stringList.add("ddd1");
```

### Filter

filter 通过一个predicate接口来过滤元素，中间操作。

forEach需要一个函数来对过滤后的元素依次执行。forEach是一个最终操作

```java
// 测试 Filter(过滤)
stringList.stream()
    .filter((s) -> s.startsWith("a"))
    .forEach(System.out::println);
```

Sorted

排序是一个 **中间操作**，返回的是排序好后的 Stream。**如果你不指定一个自定义的 Comparator 则会使用默认排序。**

```java
// 测试 Sort (排序)
stringList
    .stream()
    .sorted()
    .filter((s) -> s.startsWith("a"))
    .forEach(System.out::println);// aaa1 aaa2
```

### Map

面的示例展示了将字符串转换为大写字符串。你也可以通过map来讲对象转换成其他类型，map返回的Stream类型是根据你map传递进去的函数的返回值决定的。

```java
// 测试 Map 操作
stringList
    .stream()
    .map(String::toUpperCase)
    .sorted((a, b) -> b.compareTo(a))
    .forEach(System.out::println);
```

#### Match

Stream提供了多种匹配操作，允许检测指定的Predicate是否匹配整个Stream。所有的匹配操作都是 **最终操作** ，并返回一个 boolean 类型的值。

```java
// 测试 Match (匹配)操作
boolean anyStartsWithA =
    stringList
	.stream()
    .anyMatch((s) -> s.startsWith("a"));
System.out.println(anyStartsWithA);      // true

boolean allStartsWithA =
    stringList
	.stream()
    .allMatch((s) -> s.startsWith("a"));

System.out.println(allStartsWithA);      // false

boolean noneStartsWithZ =
    stringList
    .stream()
    .noneMatch((s) -> s.startsWith("z"));

System.out.println(noneStartsWithZ);      // true
```

### Count

计数是一个 **最终操作**，返回Stream中元素的个数，**返回值类型是 long**。

```java
//测试 Count (计数)操作
long startsWithB =
    stringList
	.stream()
    .filter((s) -> s.startsWith("b"))
    .count();
System.out.println(startsWithB);    // 3
```

### Reduce

这是一个 **最终操作** ，允许通过指定的函数来讲stream中的多个元素规约为一个元素，规约后的结果是通过Optional 接口表示的：

```java
//测试 Reduce (规约)操作
Optional<String> reduced =
    stringList
    .stream()
    .sorted()
    .reduce((s1, s2) -> s1 + "#" + s2);
reduced.ifPresent(System.out::println);
```

## Parallel Streams(并行流)

前面提到过Stream有串行和并行两种，串行Stream上的操作是在一个线程中依次完成，而并行Stream则是在多个线程上同时执行。

```java
//并行排序
long t0 = System.nanoTime();

long count = values.parallelStream().sorted().count();
System.out.println(count);

long t1 = System.nanoTime();

long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
System.out.println(String.format("parallel sort took: %d ms", millis));
```

## Maps

前面提到过，Map 类型不支持 streams，不过Map提供了一些新的有用的方法来处理一些日常任务。Map接口本身没有可用的 `stream（）`方法，但是你可以在键，值上创建专门的流或者通过 `map.keySet().stream()`,`map.values().stream()`和`map.entrySet().stream()`。