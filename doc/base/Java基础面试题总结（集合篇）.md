## Java基础面试题总结（集合篇）

### 集合概览

Java集合，也叫作容器，主要是由两大接口派生而来：一个是`Collection`接口，主要用于存放单一元素，另一个是`Map`接口，主要用于用放键值对。对于`Collection`接口，下面又有三个主要的子接口：`List`、`Set`、`Queue`。

Java集合框架如下图所示：

![Java 集合框架概览](https://oss.javaguide.cn/github/javaguide/java/collection/java-collection-hierarchy.png)

注：图中只列举了主要的继承派生关系，并没有列举所有关系。比如省略了`AbstractList`, `NavigableSet`等抽象类以及其他的一些辅助类，如想深入了解，可自行查看源码。

### 说说List、Set、Queue、Map四者的区别？

`List`（对付顺序的好帮手）：存储的元素是有序的、可重复的

`Set`（注重独一无二的性质）：存储的元素是无序的、不可重复的

`Queue`（实现排队功能的叫号机）：按特定的排队规则来确定先后顺序，存储的元素是有序的、可重复的

`Map`（用key来搜索的专家）：使用键值对(key-value)存储，类似于数学上的函数y=f(x)，“x”代表key，“y”代表value，key是无序的，不可重复的，value是无序的，可重复的，每个键最多映射到一个值。

### ArrayList和Array（数组的区别）？

`ArrayList`内部基于动态数组实现，比`Array`（静态数组）使用起来更加灵活

- `ArrayList`会根据实际存储的元素动态地扩容或缩容，而`Array`被创建之后就不能改变它的长度了
- `ArrayList`允许你使用泛形来确保类型安全，`Array`则不可以
- `ArrayList`中只能存储对象。对于基本类型数据，需要使用其对应的包装类（如Integer、Dubbo等）。`Array`可以直接存储基本类型数据，也可以存储对象
- `ArrayList`支持插入、删除、遍历等常见操作，并且提供丰富的API操作方法，比如`add()`、`remove()`等。`Array`只是一个固定长度的数组，只能按照下标访问其中的元素，不具备动态添加、删除元素的能力。
- `ArrayList`创建时不需要指定大小，而`Array`创建时必须指定大小

下面是二者使用的简单对比：

`Array`：

```java
 // 初始化一个 String 类型的数组
 String[] stringArr = new String[]{"hello", "world", "!"};
 // 修改数组元素的值
 stringArr[0] = "goodbye";
 System.out.println(Arrays.toString(stringArr));// [goodbye, world, !]
 // 删除数组中的元素，需要手动移动后面的元素
 for (int i = 0; i < stringArr.length - 1; i++) {
     stringArr[i] = stringArr[i + 1];
 }
 stringArr[stringArr.length - 1] = null;
 System.out.println(Arrays.toString(stringArr));// [world, !, null]
```

`ArrayList` ：

```java
// 初始化一个 String 类型的 ArrayList
 ArrayList<String> stringList = new ArrayList<>(Arrays.asList("hello", "world", "!"));
// 添加元素到 ArrayList 中
 stringList.add("goodbye");
 System.out.println(stringList);// [hello, world, !, goodbye]
 // 修改 ArrayList 中的元素
 stringList.set(0, "hi");
 System.out.println(stringList);// [hi, world, !, goodbye]
 // 删除 ArrayList 中的元素
 stringList.remove(0);
 System.out.println(stringList); // [world, !, goodbye]
```

### ArrayList 和 Vector 的区别?（了解即可）

- `ArrayList` 是 `List` 的主要实现类，底层使用 `Object[]`存储，适用于频繁的查找工作，线程不安全 。
- `Vector` 是 `List` 的古老实现类，底层使用`Object[]` 存储，线程安全。

### Vector 和 Stack 的区别?（了解即可）

- `Vector` 和 `Stack` 两者都是线程安全的，都是使用 `synchronized` 关键字进行同步处理。
- `Stack` 继承自 `Vector`，是一个后进先出的栈，而 `Vector` 是一个列表。

随着 Java 并发编程的发展，`Vector` 和 `Stack` 已经被淘汰，推荐使用并发集合类（例如 `ConcurrentHashMap`、`CopyOnWriteArrayList` 等）或者手动实现线程安全的方法来提供安全的多线程操作支持。

### ArrayList可以添加null值吗？

`ArrayList`中可以存储任何类型的对象，包括`null`值。不过，不建议向`ArrayList`中添加`null`值，`null`值无意义，会让代码难以维护，比如忘记做判空处理就会导致空指针异常。

示例代码：

```java
ArrayList<String> listOfStrings = new ArrayList<>();
listOfStrings.add(null);
listOfStrings.add("java");
System.out.println(listOfStrings);
```

输出：

```
[null, java]
```