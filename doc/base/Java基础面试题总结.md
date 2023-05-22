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