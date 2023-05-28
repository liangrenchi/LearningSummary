---
title: Java内存区域与内存溢出异常
date: 2023/4/17 
categories: JVM
tags: java,jvm
comments: true
photo: https://img2.baidu.com/it/u=1734247813,400897296&fm=253&fmt=auto&app=138&f=PNG?w=1111&h=500
---

Java与C++之间有一堵由内存动态分配和垃圾收集技术所围成的“高墙”，墙外面的人想进去，墙里面的人却想出来。

<!--more-->

### JVM内存数据区域

![image-20230417222255787](https://menuimg.oss-cn-beijing.aliyuncs.com/image-20230417222255787.png)

**程序计数器**：当前线程所执行字节码行号指示器，用于记录正在执行虚拟机字节指令地址，**线程私有**。

**Java虚拟机栈**：存放基本数据类型、对象引用、方法出口等，**线程私有**。

> 如果线程请求的栈深度大于虚拟机所允许的深度，将抛出StackOverflowError异常；如果虚拟机栈可以动态扩展（当前大部分的Java虚拟机都可动态扩展，只不过Java虚拟机规范中也允许固定长度的虚拟机栈），如果扩展时无法申请到足够的内存，就会抛出OutOfMemoryError异常。 ----《深入理解Java虚拟机：JVM高级特性与最佳实践（第2版）》

**本地方法栈**：和虚拟机栈相似，只不过它服务于Native方法，**线程私有**。

**堆**：java内存最大一块，所有对象实例，数组都存放在java堆，GC回收的地方，**线程共享**。

**方法区**：存放已被加载类信息、常量、静态变量、即时编译器编译后代码数据等。（即永久化），回收目标主要常量池回收和类型缷载，**线程共享**。

### Java堆溢出

Java堆用于存储对象实例，只要不断地创建对象，并且对象数量到达最大堆的容量限制后就会产生内存异常。

代码限制Java堆的大小为20MB，不可扩展（将堆的最小值-Xms参数与最大值-Xmx参数设置为一样即可避免堆自动扩展），通过参数-XX:+HeapDumpOnOutOfMemoryError可以让虚拟机在出现内存溢出异常时Dump出当前的内存堆转储快照以便事后进行分析

```java
/＊＊
 ＊ VM Args：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 ＊ @author zzm
 ＊/
public class HeapOOM {
    static class OOMObject {
    }
    public static void main(String[] args) {
            List<OOMObject> list = new ArrayList<OOMObject>();
            while (true) {
                      list.add(new OOMObject());
            }
    }
}
```

运行结果：

```java
java.lang.OutOfMemoryError: Java heap space
Dumping heap to java_pid3404.hprof ...
Heap dump file created [22045981 bytes in 0.663 secs]
```

### 虚拟机桟和本地方法栈溢出

- 如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出StackOverflowError异常（方法递归调用）
- 如果虚拟机在扩展栈时无法申请到足够的内存空间，则抛出OutOfMemoryError异常。（线程过多）
- 参数 -Xss 去调整JVM栈大小

### 方法区和运行时常量池溢出

由于运行时常量池是方法区的一部分，因此这两个区域的溢出测试就放在一起进行。

String.intern()是一个Native方法，它的作用是：如果字符串常量池中已经包含一个等于此String对象的字符串，则返回代表池中这个字符串的String对象；否则，将此String对象包含的字符串添加到常量池中，并且返回此String对象的引用。

在JDK 1.6及之前的版本中，由于常量池分配在永久代内，我们可以通过-XX:PermSize和-XX:MaxPermSize限制方法区大小。

```java
/＊＊
 ＊ VM Args：-XX:PermSize=10M -XX:MaxPermSize=10M
 ＊ @author zzm
 ＊/
public class RuntimeConstantPoolOOM {
  public static void main(String[] args) {
            //使用List保持着常量池引用，避免Full GC回收常量池行为
            List<String> list = new ArrayList<String>();
            //10MB的PermSize在integer范围内足够产生OOM了
            int i = 0;
            while (true) {
                    list.add(String.valueOf(i++).intern());
            }
  }
}
```

运行结果：

```java
Exception in thread "main" java.lang.OutOfMemoryError: PermGen space
  at java.lang.String.intern(Native Method)
  at org.fenixsoft.oom.RuntimeConstantPoolOOM.main(RuntimeConstantPoolOOM.java:18)
```

