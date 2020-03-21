# 深入理解Spring框架

## Spring的概览

### 用于构造Java应用程序性的轻量级框架

- 可以采用Spring来构造任何程序，而不局限于Web程序；

- 轻量级：最少的侵入，与应用程序低耦合；

- 基于pojo，构建出稳健而强大的应用；
    
    ![Spring架构](https://raw.githubusercontent.com/daffupman/markdown-img/master/20200316005643.png)
    
  Spring最基础核心模块：
  
  - spring-core
    - 包含框架基本的核心工具类，其他组件都要使用到这个里面的类；
    - 定义并提供资源的访问方式；
    - 为IoC和DI提供最基础的服务；
  - spring-beans
    - 面向Bean编程（BOP）；
    - Bean的定义，解析和创建；
    - 核心接口BeanFactory；
  - spring-context
    - 为spring提供运行时环境，保存对象的状态；
    - 拓展BeanFactory，添加Bean的生命周期控制，框架事件体系，资源透明化，事件监听，远程访问等；
    - 核心接口ApplicationContext，ApplicationContext实例化后会自动地为所有的单实例Bean进行实例化并装配相关依赖，使之处于待用状态；
  - spring-aop
    - 最小化的动态代理实现；
    - 两种代理模式：jdk动态和cglib，这两种只能使用运行时织入，仅支持方法级编织，仅支持方法执行切入点；
    - 基于core和beans模块，可选aspectj包（使用注解时），common-pool2和jamon；
    - spring-aspectj和instrument构成完整的aspectj
    
- core-spring项目的架构
    ![](https://raw.githubusercontent.com/daffupman/markdown-img/master/20200321172239.png)
    