## Java基础面试题总结（集合篇）

### 集合概览

Java集合，也叫作容器，主要是由两大接口派生而来：一个是`Collection`接口，主要用于存放单一元素，另一个是`Map`接口，主要用于用放键值对。对于`Collection`接口，下面又有三个主要的子接口：`List`、`Set`、`Queue`。

Java集合框架如下图所示：

![Java 集合框架概览](https://oss.javaguide.cn/github/javaguide/java/collection/java-collection-hierarchy.png)

注：图中只列举了主要的继承派生关系，并没有列举所有关系。比如省略了`AbstractList`, `NavigableSet`等抽象类以及其他的一些辅助类，如想深入了解，可自行查看源码。

