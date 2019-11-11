## 对spring5源码构建分析
### 源码构建问题
在源码初次构建的过程中可能会出现spring-oxm编译通不过，这是因为在test类中有的源码
依赖出现问题，最简单粗暴的方式就是直接将该模块的test删除，因为我们对于源码的分析
是使用不到test源码的。
### 项目说明
spring-ioc：是对spring ioc和aop进行源码分析
spring-mymvc：是对spring-web进行源码分析
其他模块是对spring5的内置模块