## Java基础面试题总结（一）

### 静态方法为什么不能调用非静态成员

这个需要结合JVM的相关知识，主要原因如下：

1、静态方法属于类的，在类加载的时候就会分配内存，可以通过类名直接访问。而非静态成员属于实例对象，只有在对象实例化之后才存在，需要通过类的实例对象去访问。

2、在类的非静态成员不存在的时候静态方法就已经存在了，此时调用在内存中还不存在的非静成员，属于非法操作

```java
public class Example {
    // 定义一个字符型常量
    public static final char LETTER_A = 'A';

    // 定义一个字符串常量
    public static final String GREETING_MESSAGE = "Hello, world!";

    public static void main(String[] args) {
        // 输出字符型常量的值
        System.out.println("字符型常量的值为：" + LETTER_A);

        // 输出字符串常量的值
        System.out.println("字符串常量的值为：" + GREETING_MESSAGE);
    }
}

```

### 静态方法和实例方法有何不同

**1、调用方式**

在外部调用静态方法时，可以使用`类名.方法名`的方式，也可以换用`对象.方法名`的方式，而实例方法只有后面这种方式，也就是说，**调用静态方法可以无需创建对象**。

不过，需要注意的是一般不建议使用`对象.方法名`的方式来调用静态方法。这种方式非常容易造成混淆，静态方法不属于类的某个对象而是属于这个类。

因此，一般建议使用`类名.方法名`的方式来调用静态方法。

```java
public class Person {
    public void method() {
      //......
    }

    public static void staicMethod(){
      //......
    }
    public static void main(String[] args) {
        Person person = new Person();
        // 调用实例方法
        person.method();
        // 调用静态方法
        Person.staicMethod()
    }
}
```

**2、访问类成员是否存在限制**

静态方法在访问本类的成员时，只允许访问静态成员（即静态成员变法和静态方法），不允许访问实例成员（即实例成员变量和实例方法），而实例方法不存在这个限制

### 什么是可变长参数？

从Java5开始，Java支持定义可变长参数，所谓可变长参数就是允许在调用方法时传入不定长度的参数。就比如下面的这个`printVariable`方法就可以接受0个或者多个参数。

```java
public static void method1(String... args) {
   //......
}
```

另外，可变参数只能作为函数的最后一个参数，但其前面可以有也可以没有任何其他参数。

```java
public static void method2(String arg1, String... args) {
   //......
}
```

**遇到方法重载的情况怎么办呢？会优先匹配固定参数还是可变参数的方法呢？**

答案是会优先匹配固定参数的方法，因为固定参数的方法匹配度更高。

我们通过下面这个例子来证明一下。

```java
public class VariableLengthArgument {

    public static void printVariable(String... args) {
        for (String s : args) {
            System.out.println(s);
        }
    }

    public static void printVariable(String arg1, String arg2) {
        System.out.println(arg1 + arg2);
    }

    public static void main(String[] args) {
        printVariable("a", "b");
        printVariable("a", "b", "c", "d");
    }
}
```

输出

```
ab
a
b
c
d
```

另外，Java 的**可变参数编译后实际会被转换成一个数组**，我们看编译后生成的 `class`文件就可以看出来了。

```java
public class VariableLengthArgument {

    public static void printVariable(String... args) {
        String[] var1 = args;
        int var2 = args.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String s = var1[var3];
            System.out.println(s);
        }

    }
    // ......
}
```

### 对象的相等和引用相等的区别

- 对象的相等一般比较的是内存中存放的内容是否相等
- 引用相等一般比较的是他们指向的地址是否相等

这里举个例子

```java
String str1 = "hello";
String str2 = new String("hello");
String str3 = "hello";
// 使用 == 比较字符串的引用相等
System.out.println(str1 == str2);
System.out.println(str1 == str3);
// 使用 equals 方法比较字符串的相等
System.out.println(str1.equals(str2));
System.out.println(str1.equals(str3));
```

输出结果

```
false
true
true
true
```

从上面的代码输出结果可以看出

`str1`和`str2`不相等，而`str1`和`str3`相等，这里因为`==`运算符比较的是字符串的引用是否相等

`str1`、`str2`、`str3`三者的内容都相等。这是因为`equals`方法比较的是字符串的内容，即使这些字符串的对象引用不同，只要它们的内容相等，就认为它们是相等的。

### 构造方法有哪些特点？是否可被override？

构造方法特点如下：

- 名字和类名相同
- 没有返回值，但不能被void声明构造函数
- 生成类对象时自动执行，无需调用

构造方法不能被override（重写），但是可以overload（重载），所以你可以看到一个类中有多个构造函数的情况。

### 接口和抽象类有什么共同点和区别？

**共同点**：

- 都不能被实例化
- 都可以包含抽象方法
- 都可以有默认实现的方法（Java8可以用`default`关键字在接口中定义默认方法）

区别：

- 接口主要用于对类的行为**进行约束**，你实现了某个接口就具有了对应的行为。抽象类主要用于**代码复用**，引调的是所属关系。
- 一个类只能继承一个类，但是可以实现多个接口
- 接口中等等成员变量只能是`public static final`类型的，不能被修改且必须有初始值，而抽象类的变量默认`default`，可在子类中被重新定义，也可以重新赋值

### 深拷贝和浅拷贝区别了解吗？什么是引用拷贝？

**浅拷贝**：浅拷贝会在堆上创建一个新的对象（区别于引用拷贝的一点），不过，如果原对象内部属性是引用类型的话，浅拷贝会直接复制内部对象的引用地址，也就是说拷贝对象和原对象共用一个内部对象。

**深拷贝**：深拷贝会完全复制整个对象，包括这个对象所包含的内部对象



浅拷贝

浅拷贝的示例代码如下，我们这里实现了`Cloneable`接口，并重写了`clone()`方法

`clone()`方法的实现很简单，直接调用的是父类的`Object`的`clone()`方法

```java
public class Address implements Cloneable{
    private String name;
    // 省略构造函数、Getter&Setter方法
    @Override
    public Address clone() {
        try {
            return (Address) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

public class Person implements Cloneable {
    private Address address;
    // 省略构造函数、Getter&Setter方法
    @Override
    public Person clone() {
        try {
            Person person = (Person) super.clone();
            return person;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
```

测试：

```
Person person1 = new Person(new Address("武汉"));
Person person1Copy = person1.clone();
// true
System.out.println(person1.getAddress() == person1Copy.getAddress());
```



深拷贝

这里我们简单对 `Person` 类的 `clone()` 方法进行修改，连带着要把 `Person` 对象内部的 `Address` 对象一起复制。

```java
@Override
public Person clone() {
    try {
        Person person = (Person) super.clone();
        person.setAddress(person.getAddress().clone());
        return person;
    } catch (CloneNotSupportedException e) {
        throw new AssertionError();
    }
}
```

测试：

```java
Person person1 = new Person(new Address("武汉"));
Person person1Copy = person1.clone();
// false
System.out.println(person1.getAddress() == person1Copy.getAddress());
```

从输出结构就可以看出，虽然 `person1` 的克隆对象和 `person1` 包含的 `Address` 对象已经是不同的了。

**那什么是引用拷贝呢？** 简单来说，引用拷贝就是两个不同的引用指向同一个对象。

一张图来描述浅拷贝、深拷贝、引用拷贝：

![浅拷贝、深拷贝、引用拷贝示意图](https://oss.javaguide.cn/github/javaguide/java/basis/shallow&deep-copy.png)



### HashCode()有什么用？

`hashCode()`的作用是获取哈希码（`int`整数），也称为散列码。这个哈希码的作用是确定哈希表中的索引位置。

![hashCode() 方法](https://oss.javaguide.cn/github/javaguide/java/basis/java-hashcode-method.png)

`hashCode`定义在JDK的`Object`类中，这就意味着Java中的任何类都包含有`hashCode`函数。另外需要注意的是：`Object`的`hashCode()`方法是本地方法，也就是用C语言或者C++实现的。

### 为什么重写equals()时必须重写hashCode()方法？

因为两个相等的对象的 `hashCode` 值必须是相等。也就是说如果 `equals` 方法判断两个对象是相等的，那这两个对象的 `hashCode` 值也要相等。

如果重写 `equals()` 时没有重写 `hashCode()` 方法的话就可能会导致 `equals` 方法判断是相等的两个对象，`hashCode` 值却不相等。

**思考**：重写 `equals()` 时没有重写 `hashCode()` 方法的话，使用 `HashMap` 可能会出现什么问题。

**总结**：

- `equals` 方法判断两个对象是相等的，那这两个对象的 `hashCode` 值也要相等。
- 两个对象有相同的 `hashCode` 值，他们也不一定是相等的（哈希碰撞）。

更多关于 `hashCode()` 和 `equals()` 的内容可以查看：https://www.cnblogs.com/skywang12345/p/3324958.html

### Exception和Error有什么区别？

在Java中，所有的异常都有一个共同的祖先`java.lang`包中的`Throwable`类。`Throwable`类有两个重要的子类：

- `Exception`：程序本身可以处理的异常，可以通过`catch`来进行捕获。`Exception`又可以分为Checked Exception（受检查异常，必须处理）和Unchecked Exception（不受检查异常，可以不处理）。
- `Error`：`Error`属于程序无法处理的错误，不建议通过`catch`捕获。例如Java虚拟机运行错误、虚拟机内存不够错误、类定义错误等。这些异常发生时，Java虚拟机（JVM）一般会选择线程终止。

### Checked Exception 和 Unchecked Exception 有什么区别？

**Checked Exception** 即 受检查异常 ，Java 代码在编译过程中，如果受检查异常没有被 `catch`或者`throws` 关键字处理的话，就没办法通过编译。

比如下面这段 IO 操作的代码：

![img](https://oss.javaguide.cn/github/javaguide/java/basis/checked-exception.png)

除了`RuntimeException`及其子类以外，其他的`Exception`类及其子类都属于受检查异常 。常见的受检查异常有：IO 相关的异常、`ClassNotFoundException`、`SQLException`...。

**Unchecked Exception** 即 **不受检查异常** ，Java 代码在编译过程中 ，我们即使不处理不受检查异常也可以正常通过编译。

`RuntimeException` 及其子类都统称为非受检查异常，常见的有（建议记下来，日常开发中会经常用到）：

- `NullPointerException`(空指针错误)
- `IllegalArgumentException`(参数错误比如方法入参类型错误)
- `NumberFormatException`（字符串转换为数字格式错误，`IllegalArgumentException`的子类）
- `ArrayIndexOutOfBoundsException`（数组越界错误）
- `ClassCastException`（类型转换错误）
- `ArithmeticException`（算术错误）
- `SecurityException` （安全错误比如权限不够）
- `UnsupportedOperationException`(不支持的操作错误比如重复创建同一用户)
- ......

![img](https://oss.javaguide.cn/github/javaguide/java/basis/unchecked-exception.png)

### Throwable类常用方法有哪些？

- `String.getMessage()`：返回异常发生时的简要描述
- `String.toString()`：返回异常发生时的详细信息
- `String.getLocalizedMessage()`：返回异常对象的本地化信息。使用`Throwable`的子类覆盖这个方法，可以生成本地化信息。如果子类没有覆盖访方法，则该方法返回的结果相同
- `void printStackTrace()`：在控制台上打印`Throwable`对象封装的异常信息

### finally 中的代码一定会执行吗？

不一定的！在某些情况下，finally 中的代码不会被执行。

就比如说 finally 之前虚拟机被终止运行的话，finally 中的代码就不会被执行。

```java
try {
    System.out.println("Try to do something");
    throw new RuntimeException("RuntimeException");
} catch (Exception e) {
    System.out.println("Catch Exception -> " + e.getMessage());
    // 终止当前正在运行的Java虚拟机
    System.exit(1);
} finally {
    System.out.println("Finally");
}
```

输出：

```
Try to do something
Catch Exception -> RuntimeException
```

另外，在以下 2 种特殊情况下，`finally` 块的代码也不会被执行：

1. 程序所在的线程死亡。
2. 关闭 CPU。