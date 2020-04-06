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
    
    框架具备的基本功能：
    
    - 解析配置
    - 定位和注册对象
    - 注入对象
    - 提供通用的工具类
  
- ApplicationContext高级容器
  
  ApplicationContext是BeanFactory的实现。该接口继承了:
  - EnvironmentCapable：包含一个获取Environment对象的getEnvironment()方法，而Environment对象继承了PropertyResolver可获取容器启动时的一些参数（如：-Dparam1=value1）
  - ListableBeanFactory：通过列表的方式来管理Bean；
  - HierarchicalBeanFactory：支持多层级的容器来对每一层的Bean管理
  - ResourcePatternResolver：加载资源文件
  - MessageSource：管理Message
  - ApplicationEventPublisher：具备事件发布的能力，容器在启动时会自己注入一些listener
  
  有以下几个具体的实现：
  
  - 基于xml的容器：
    - FileSystemXmlApplicationContext：从文件系统加载配置；
    - ClassPathXmlApplicationContext：从classpath加载配置
    - XmlWebApplicationContext：用于web程序的容器
  - 目前流行的容器，他们refresh()方法功能大致相似：容器初始化、配置解析，BeanFactoryPostProcessor和BeanPostProcessor的注册和激活，国际化配置。
    - AnnotationConfigServletWebServerApplicationContext：
    - AnnotationConfigReactiveWebServerApplicationContext
    - AnnotationConfigApplicationContext
    
  > 模板方法模式
  > 围绕抽象类，实现通用逻辑，定义模板结构，部分逻辑由子类实现。可以做到复用代码，反向控制
  > ```java
  > public abstract class AbstractTemplate {
  >     // 模板方法：定义整个方法需要实现的业务骨架
  >     public void templateMethod() {
  >         concreteMethod();
  >         hookMethod();
  >         abstractMethod();
  >     }
  > 
  >     // 具体方法
  >     private void concreteMethod() {
  >         // TODO：模板内固定不变的代码
  >     }
  > 
  >     // 钩子方法
  >     protected void hookMethod() {
  >         // TODO：子类可以依据情况实现的方法
  >     }
  > 
  >     // 抽象方法
  >     protected abstract void abstractMethod() {
  >         // TODO：必须让子类实现的方法
  >     }
  > }
  > ```

- Bean与BeanDefinition

    Bean的本质是Java对象，这是这个对象的生命周期由容器管理。不需要为了创建Bean而在原来的java类上添加任何额外的限制。对java对象的控制体现在配置上。
    
    根据配置，生成用来描述Bean的BeanDefinition，常用属性：
    
    - @Scope：作用范围scope（singleton，prototype，request，session，global session）；
    - @Lazy：懒加载lazy-init，决定Bean实例是否延迟加载；
    - @Primary：按类型装配时，有多个实现类匹配，会优先使用设置为true的实现类；
    - @Configuration和@Bean：factory-bean和factory-method
    
- 容器初始化的工作内容

    配置文件 --(读取)--> Resource --(解析)--> BeanDefinition --(注册)--> 容器
    
    在Java中资源会被抽象成URL，可以通过解析URL中的协议（Protocol）来处理不同资源的操作逻辑，而Spring则将物理资源的访问抽象成Resource。Resource接口的父接口为InputStreamSource，该接口包含一个获取输入流的方法`getInputStream()`；EncodeResource类可设置编码。而AbstractResource抽象类提供了对Resource接口中大量方法的实现，可通过继承AbstractResource类覆盖相应的方法来自定义Resource处理。Spring可通过“classpath:”、“file:”等资源地址前缀自动选择相应的Resource，也支持自动解析Ant风格带通配符的资源地址
    > - ?：匹配任何单字符
    > - *：匹配0个或者任意数量的字符
    > - **：匹配0个或任意级目录
                                                                                                                                                                                                                                                                                                                                                                                                                                    
    ResourceLoader实现了不同的Resource加载策略，按需返回特定类型的Resource。-

    ResourceLoader的使用者是BeanDefinitionReader。BeanDefinitionReader会利用ResourceLoader或ResourcePatternResolver将配置信息解析成一个个BeanDefinition，再借助BeanDefinitionRegistry将BeanDefinition注册到容器里。BeanDefinitionReader接口定义了一系列用于加载BeanDefinition的方法
    
    - 解析配置
    - 定位与注册对象

- 组件扫描和自动装配

    - 组件扫描：自动发现应用容器中需要创建的Bean；
    - 自动装配：自动满足Bean之间的依赖；