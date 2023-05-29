## JVM面试题整理

### 运行时数据区域

![JVM运行时数据区](https://menuimg.oss-cn-beijing.aliyuncs.com/JVM%E8%BF%90%E8%A1%8C%E6%97%B6%E6%95%B0%E6%8D%AE%E5%8C%BA.png)

**线程私有的**：

- 虚拟机栈
- 本地方法栈
- 程序计数器

**线程共享的：**

- 堆
- 方法区
- 直接内存（非运行时数据区的一部分）

具体详解参考以下文章：https://javaguide.cn/java/jvm/memory-area.html

### 堆空间的基本结构

Java的自动内存管理主要是针对象象内存的回收和对象内存的分配。同时，java自动内存管理最核心的功能是堆内存中对象的分配与回收。

Java堆是垃圾收集器管理的主要区域，因此也被称作**GC堆**。

从垃圾回收的角度来说，由于现在收集器基本采用分代垃圾收集算法，所以Java堆被划分几个不同的区域，这样我们就可以根据各个区域的特点选择合适的垃圾收集算法。

在JDK 7版本及JDK 7版本之前，堆内存被通常分为下面三部分：

1. 新生代内存
2. 老年代
3. 永久代

下面所示的Eden区，两个Survivor区S0和S1都属于新生代，中间一层属于老年代，最下面一层属于永久代。

![堆内存结构](https://oss.javaguide.cn/github/javaguide/java/jvm/hotspot-heap-structure.png)

JDK 8版本之后PermGen（永久）已被Metaspace（元空间）取代，元空间使用的是直接内存。

### 内存分配和回收原则

**对象优先在Eden区分配**

大多数情况下，对象在新生代中Eden区分配。当Eden区没有足够空间分配时，虚拟机将发起一次Minor GC。

测试代码

```java
public class GCTest {
	public static void main(String[] args) {
		byte[] allocation1, allocation2;
		allocation1 = new byte[30900*1024];
	}
}
```

通过以下方式运行：

![img](https://oss.javaguide.cn/github/javaguide/java/jvm/25178350.png)

添加的参数：`-XX:+PrintGCDetails`

![img](https://oss.javaguide.cn/github/javaguide/java/jvm/run-with-PrintGCDetails.png)

运行结果 (红色字体描述有误，应该是对应于 JDK1.7 的永久代)：

![img](https://oss.javaguide.cn/github/javaguide/java/jvm/28954286.jpg)

从上图我们可以看出Eden区内存几乎已经被分配完全（即使程序什么也不做，新生代也会使用2000多K内存）。

假如我们再为`allocation2`分配内存会出现什么情况呢？

```java
allocation2 = new byte[900*1024];
```

![img](https://oss.javaguide.cn/github/javaguide/java/jvm/28128785.jpg)

当Eden区没有足够空间进行分配时，虚拟机将发起一次Minor GC。GC期间虚拟机又发现`allocation1`已经无法存入Survivor空间，所以只好通过**分配担保机制**把新生代的对象提前转移到老年代中去，老年代上的空间足够存放`allocation1`，所以不会出现Full GC。执行Minor GC后，后面分配的对象如果能够存在Eden区的话，还是会在Eden区分配内存。可以执行如下代码验证。

```java
public class GCTest {

	public static void main(String[] args) {
		byte[] allocation1, allocation2,allocation3,allocation4,allocation5;
		allocation1 = new byte[32000*1024];
		allocation2 = new byte[1000*1024];
		allocation3 = new byte[1000*1024];
		allocation4 = new byte[1000*1024];
		allocation5 = new byte[1000*1024];
	}
}
```



