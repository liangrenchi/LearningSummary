## Java基础面试题总结

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

