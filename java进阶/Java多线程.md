# Java并发

主要的问题：

1. 什么是多线程
2. 如何实现多线程
3. 多线程的问题及解决方法

## 1.什么是多线程

1. **进程与线程**：进程是资源分配的最小单位，线程是程序执行的最小单位（资源调度的最小单位）

   *一个程序至少有一个进程,一个进程至少有一个线程*。

   进程，是计算机中的程序关于某数据集合上的一次运行活动，是系统进行资源分配和调度的基本单位，是操作系统结构的基础。它的执行需要系统分配资源创建实体之后，才能进行。

   线程是进程的一个实体,是CPU调度和分派的基本单位,它是比进程更小的能独立运行的基本单位.线程自己基本上不拥有系统资源,只拥有一点在运行中必不可少的资源(如[程序计数器](https://www.baidu.com/s?wd=程序计数器&tn=SE_PcZhidaonwhc_ngpagmjz&rsv_dl=gh_pc_zhidao),一组寄存器和栈),但是它可与同属一个进程的其他的线程共享进程所拥有的全部资源.

   通俗一点说，进程就是程序的一次执行，而线程可以理解为进程中的执行的一段程序片段。

   **区别**

   1）地址空间和其它资源（如打开文件）：进程间相互独立，同一进程的各线程间共享。某进程内的线程在其它进程不可见。

   2）通信：进程间通信IPC(Inter-Process Communication)，线程间可以直接读写进程数据段（如全局变量）来进行通信——需要进程同步和互斥手段的辅助，以保证数据的一致性。

   3）调度和切换：线程上下文切换比进程上下文切换要快得多。

   4）

2. 线程的状态转换

   ![](../img/线程状态.png)

## 并发程序的同步方式

进程：无名管道(pipe)、有名管道(FIFO)、信号、共享内存、消息队列、信号量、套接字(socket)
线程：互斥量、读写锁、自旋锁、线程信号、条件变量

## 2.如何实现多线程

+ 继承Thread

  ```java
  class thread extends Thread{
      String name;
      public thread(String name) {
          this.name = name;
      }
  
      public void run() {
          System.out.println(this.name);
      }
  }
  public class ThreadTest {
      public static void main(String[] args) {
          thread thread1 = new thread("1");
          thread thread2 = new thread("2");
          thread1.start();
          thread2.start();
          System.out.println("main");
      }
  }
  ```

+ 实现runnable接口

  ```java
  class thread implements Runnable{
      String name;
      public thread(String name) {
          this.name = name;
      }
  
      @Override
      public void run() {
          System.out.println(this.name);
      }
  }
  public class ThreadTest {
      public static void main(String[] args) {
          new Thread(new thread("1")).start();
          new Thread(new thread("2")).start();
          System.out.println("main");
      }
  }
  ```

  运行时区别，要将runnable对象传入一个Thread对象的构造函数内

+ 实现Callable接口和Future，FutureTask

  可以在线程执行完毕后获取返回的结果。

  Future 是线程池提交了callable任务后的返回的对象

  采用Future实现多线程：

  ```java
  import java.util.concurrent.*;
  class myCallable implements Callable<String> {
      String name;
      public myCallable(String name) {
          this.name = name;
      }
      @Override
      public String call() throws Exception {
          System.out.println(this.name);
          return this.name;
      }
  }
  public class ThreadTest {
      public static void main(String[] args) {
          //创建线程池
          ExecutorService es = Executors.newSingleThreadExecutor();
          Future<String> future1 = es.submit(new myCallable("1"));
          Future<String> future2 = es.submit(new myCallable("2"));
          try {
              Thread.sleep(1000);
              System.out.println("return future1:"+future1.get());
              System.out.println("return future2:"+future2.get());
          }catch (Exception e) {
              e.printStackTrace();
          }
          System.out.println("main");
      }
  }
  ```

  FutureTask 接口实现了RunnableFuture接口，而RunnableFuture接口继承了Runnable 和Future接口因此FutureTask也可以直接提交给Executor执行。 当然也可以调用线程直接执行（FutureTask.run()）

  采用futuretask实现多线程：

  ```java
  import java.util.concurrent.*;
  class myCallable implements Callable<String> {
      String name;
      public myCallable(String name) {
          this.name = name;
      }
  
      @Override
      public String call() throws Exception {
          System.out.println(this.name);
          return this.name;
      }
  }
  public class ThreadTest {
      public static void main(String[] args) {
          FutureTask futureTask1= new FutureTask(new myCallable("1"));
          FutureTask futureTask2= new FutureTask(new myCallable("2"));
          new Thread(futureTask1).start();
          new Thread(futureTask2).start();
          try {
              Thread.sleep(1000);
              System.out.println("1:"+futureTask1.get());
              System.out.println("2:"+futureTask2.get());
          } catch (Exception e){
              e.printStackTrace();
          }
          System.out.println("main");
      }
  }
  ```

## 3.线程间同步与线程安全

区别进程里面的 ***同步/异步 阻塞/非阻塞*** 概念：

**1.同步与异步**
同步和异步关注的是**消息通信机制** (synchronous communication/ asynchronous communication)
所谓同步，就是在发出一个*调用*时，在没有得到结果之前，该*调用*就不返回。但是一旦调用返回，就得到返回值了。
换句话说，就是由*调用者*主动等待这个*调用*的结果。

而异步则是相反，**调用在发出之后，这个调用就直接返回了，所以没有返回结果**。换句话说，当一个异步过程调用发出后，调用者不会立刻得到结果。而是在*调用*发出后，*被调用者*通过状态、通知来通知调用者，或通过回调函数处理这个调用。

**2.阻塞非阻塞**

阻塞和非阻塞关注的是**程序在等待调用结果（消息，返回值）时的状态.**

阻塞调用是指调用结果返回之前，当前线程会被挂起。调用线程只有在得到结果之后才会返回。
非阻塞调用指在不能立刻得到结果之前，该调用不会阻塞当前线程。

### Java 线程间同步

由于多个线程可能会共享相同的内存空间和资源，因此在进行多线程时，要保证多个线程合理访问资源，防止造成冲突和错误。

### 线程安全

需要线程满足执行控制和内存可见

执行控制：线程按照人为的设想进行并发和要求执行。

内存可见：线程对内存的操作和修改对其他线程是可见的。

### 线程同步方法

+ Synchronized同步
  
  Java语言的关键字，当它用来修饰一个方法或者一个代码块的时候，能够保证在同一时刻最多只有一个线程执行该段代码。
  
  synchronized：可见性，原子性
  
  需要注意的地方，synchronized是对象锁，对整个对象的同步代码进行加锁，未获得锁的线程，所有对同步代码块的访问的请求都被阻塞。无论该线程请求的是不是加锁的线程所访问的代码块，所有同步代码块都被加锁。
  
  普通方法：只有获取了该对象的锁的可以访问，不影响其他对象的访问。
  
  静态方法：只有获取了类的锁的线程可访问，所有对象均被block。
  
  其他未被Synchronized修饰的方法，可以直接访问。
  
  Synchronized关键字不能继承。 父类使用了 synchronized的方法，子类在继承的时候默认是不同步的
  
  在定义接口方法时不能使用synchronized关键字
  
  构造方法不能使用synchronized关键字，但可以使用synchronized代码块来进行同步。 
  
  Synchronized也可以对类进行同步控制。
  
  + 同步方法
  
  ```java
  import java.util.concurrent.*;
  
  /**
   * Description:
   * User: jehuRen
   * Date: 2019-08-26
   * Time: 14:56
   */
  class myRunnable implements Runnable{
      String name;
      public myRunnable(String name) {
          this.name = name;
      }
  
      @Override
      public void run() {
          System.out.println(this.name);
          System.out.println(Thread.currentThread().getId());
      }
  }
  public class ThreadTest {
      public static void main(String[] args) {
          Runnable runnable = new myRunnable("1");
          new Thread(runnable).start();
          new Thread(runnable).start();
          System.out.println("main");
      }
  }
  ```
  
  + 同步代码块
  
  ```java
  //synchronized 块：通过 synchronized关键字来声明synchronized 块。语法如下：  
  synchronized(syncObject) {  
  //允许访问控制的代码  
  }  
  //synchronized 块是这样一个代码块，其中的代码必须获得对象 syncObject （如前所述，可以是类实例或类）的锁方能执行
  ```
  
  synchronized实现机制：
  
  32位JVM对象头:Mark Word（标记字段）、Klass Pointer（类型指针）
  
  **Mark Word**：默认存储对象的HashCode，分代年龄和锁标志位信息。这些信息都是与对象自身定义无关的数据，所以Mark Word被设计成一个非固定的数据结构以便在极小的空间内存存储尽量多的数据。它会根据对象的状态复用自己的存储空间，也就是说在运行期间Mark Word里存储的数据会随着锁标志位的变化而变化。
  
  **Klass Point**：对象指向它的类元数据的指针，虚拟机通过这个指针来确定这个对象是哪个类的实例。
  
+ 使用**volatile**进行同步



+ **使用可重入锁实现线程同步**
+ **使用局部变量实现线程同步**
+ **使用阻塞队列实现线程同步**
+ **使用原子变量实现线程同步**

