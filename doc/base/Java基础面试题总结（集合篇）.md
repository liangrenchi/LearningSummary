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

### LinkedList 为什么不能实现 RandomAccess 接口？

RandomAccess是一个标识接口，表明该接口的类是否支持随机访问（即通过索引快速定位数据），但由于LinkedList是地址不连续的，只能通过指针来定位，因此不能实现RandomAccess接口。



**补充内容:RandomAccess 接口**

```java
public interface RandomAccess {
}
```

查看源码我们发现实际上 `RandomAccess` 接口中什么都没有定义。所以，在我看来 `RandomAccess` 接口不过是一个标识罢了。标识什么？ 标识实现这个接口的类具有随机访问功能。

在 `binarySearch（)` 方法中，它要判断传入的 list 是否 `RandomAccess` 的实例，如果是，调用`indexedBinarySearch()`方法，如果不是，那么调用`iteratorBinarySearch()`方法



```java
    public static <T>
    int binarySearch(List<? extends Comparable<? super T>> list, T key) {
        if (list instanceof RandomAccess || list.size()<BINARYSEARCH_THRESHOLD)
            return Collections.indexedBinarySearch(list, key);
        else
            return Collections.iteratorBinarySearch(list, key);
    }
```

`ArrayList` 实现了 `RandomAccess` 接口， 而 `LinkedList` 没有实现。为什么呢？我觉得还是和底层数据结构有关！`ArrayList` 底层是数组，而 `LinkedList` 底层是链表。数组天然支持随机访问，时间复杂度为 O(1)，所以称为快速随机访问。链表需要遍历到特定位置才能访问特定位置的元素，时间复杂度为 O(n)，所以不支持快速随机访问。，`ArrayList` 实现了 `RandomAccess` 接口，就表明了他具有快速随机访问功能。 `RandomAccess` 接口只是标识，并不是说 `ArrayList` 实现 `RandomAccess` 接口才具有快速随机访问功能的！



### ArrayList 与 LinkedList 区别?

**是否保证线程安全：** `ArrayList` 和 `LinkedList` 都是不同步的，也就是不保证线程安全；

**底层数据结构：** `ArrayList` 底层使用的是 **`Object` 数组**；`LinkedList` 底层使用的是 **双向链表** 数据结构（JDK1.6 之前为循环链表，JDK1.7 取消了循环。注意双向链表和双向循环链表的区别，下面有介绍到！）

**插入和删除是否受元素位置的影响：**

- `ArrayList` 采用数组存储，所以插入和删除元素的时间复杂度受元素位置的影响。 比如：执行`add(E e)`方法的时候， `ArrayList` 会默认在将指定的元素追加到此列表的末尾，这种情况时间复杂度就是 O(1)。但是如果要在指定位置 i 插入和删除元素的话（`add(int index, E element)`），时间复杂度就为 O(n)。因为在进行上述操作的时候集合中第 i 和第 i 个元素之后的(n-i)个元素都要执行向后位/向前移一位的操作。
- `LinkedList` 采用链表存储，所以在头尾插入或者删除元素不受元素位置的影响（`add(E e)`、`addFirst(E e)`、`addLast(E e)`、`removeFirst()`、 `removeLast()`），时间复杂度为 O(1)，如果是要在指定位置 `i` 插入和删除元素的话（`add(int index, E element)`，`remove(Object o)`,`remove(int index)`）， 时间复杂度为 O(n) ，因为需要先移动到指定位置再插入和删除。

**是否支持快速随机访问：** `LinkedList` 不支持高效的随机元素访问，而 `ArrayList`（实现了 `RandomAccess` 接口） 支持。快速随机访问就是通过元素的序号快速获取元素对象(对应于`get(int index)`方法)。

**内存空间占用：** `ArrayList` 的空间浪费主要体现在在 list 列表的结尾会预留一定的容量空间，而 LinkedList 的空间花费则体现在它的每一个元素都需要消耗比 ArrayList 更多的空间（因为要存放直接后继和直接前驱以及数据）。

我们在项目中一般是不会使用到 `LinkedList` 的，需要用到 `LinkedList` 的场景几乎都可以使用 `ArrayList` 来代替，并且，性能通常会更好！就连 `LinkedList` 的作者约书亚 · 布洛克（Josh Bloch）自己都说从来不会使用 `LinkedList` 。

另外，不要下意识地认为 `LinkedList` 作为链表就最适合元素增删的场景。我在上面也说了，`LinkedList` 仅仅在头尾插入或者删除元素的时候时间复杂度近似 O(1)，其他情况增删元素的平均时间复杂度都是 O(n) 。



**补充内容: 双向链表和双向循环链表**

**双向链表：** 包含两个指针，一个 prev 指向前一个节点，一个 next 指向后一个节点。

![双向链表](https://oss.javaguide.cn/github/javaguide/cs-basics/data-structure/bidirectional-linkedlist.png)

**双向循环链表：** 最后一个节点的 next 指向 head，而 head 的 prev 指向最后一个节点，构成一个环。

![双向循环链表](https://oss.javaguide.cn/github/javaguide/cs-basics/data-structure/bidirectional-circular-linkedlist.png)



### ArrayList的自动扩容机制

参考以下文章：https://javaguide.cn/java/collection/java-collection-questions-01.html#%E8%AF%B4%E4%B8%80%E8%AF%B4-arraylist-%E7%9A%84%E6%89%A9%E5%AE%B9%E6%9C%BA%E5%88%B6%E5%90%A7

