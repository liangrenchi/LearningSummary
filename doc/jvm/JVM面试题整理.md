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

#### 程序计数器

