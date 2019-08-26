# 一些平时的note记录
1. import问题 在同一个包下，一个类访问另一个类的静态成员变量无需import 只要直接 类名.成员名即可
2. Spring 容器启动过程
   + 1、资源定位：找到配置文件（载入配置文件/Springboot采用自动扫描）
   + 2、BeanDefinition载入和解析（解析成BeanDefiniton）
   + 3、BeanDefinition注册（添加到map(key,Definition)）
   + 4、bean的实例化和依赖注入(getBean()方法进行实例化)

3. @refrence与@resource 

    前者是dubbo注解，后者是spring 的。后者@resource很简单就是注入资源，与@Autowired比较接近，只不过是按照变量名（beanid）注入。@reference也是注入，但是一般用来注入分布式的远程服务对象，需要配合dubbo配置使用。他们的区别就是一个是本地spring容器，另一个是把远程服务对象当做spring容器中的对象一样注入。
4. ceil()和floor()区别
   
    ceil()对整形变量向左取整，返回类型为double型。不小于x的最大整数，如ceil(1.5) =2

    floor()对整形变量向左取整，返回类型为double型。不大于x的最大整数，如floor(1.5) = 1;

    round(x) = Math.floor(x+0.5) 四舍五入
5. Stringbuffer 和Stringbuilder
    buffer 线程安全，先提出，builder线程不安全，后提出为了改善效率。
6. 日期和时间
    获取时间Date();
    获取日期Calendar().getInstance();
7. java 引用传递
    只存在值传递，只存在值传递！！！基础类型传值，引用了类型虽然传引用，但是传递的是传入的对象的一个拷贝的引用。修改并不影响原来对象。

**java 内部类**

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

### 常用的函数式接口

java.lang.Runnable,

java.awt.event.ActionListener, 

java.util.Comparator,

java.util.concurrent.Callable

java.util.function包下的接口，如Consumer、Predicate、Supplier等


