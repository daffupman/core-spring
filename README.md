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
    
- AbstractApplicationContext.refresh()

    - 后置处理器PostProcessor
    
        本身也是一个注入到容器中的Bean（即需要加上@Bean注解），里面的方法会在特定的时机被容器调用。可以在不改变容器或Bean核心逻辑的情况下对Bean进行拓展，如：对Bean包装，属性设置，内容修改，对象代理，甚至替换掉整个bean。
        
        PostProcessor的种类：
        
        - 容器级别：BeanFactoryPostProcessor和BeanDefinitionRegistryPostProcessor
        - Bean级别：BeanPostProcessor
        
    - Aware接口及其子接口
    
    从Bean里获取到容器实例并对其操作。Aware接口里面没有定义任何方法，只是一个标记。其子类有：
    
    - ApplicationContextAware：提供的setApplicationContext方法将提供ApplicationContext类型的参数供该接口的实现类使用；
    - MessageSourceAware：
    - ApplicationEventPublisherAware：
    - BeanFactoryAware：
    - ResourceLoaderAware：
    - BeanNameAware：提供的setBeanName方法将提供Bean的名称给该接口的实现类使用；
    
    - 事件监听器模式
    
    回调函数：往组件注册自定义的方法以便组件在特定场景下调用。监听器会监听某个事件，一旦事件触发，会立刻作出响应。事件监听器的三要素：
    
    - 事件源（Event Source）
    - 事件监听器（Event Listener）:注册在事件源上，用于监听事件，处理或转发
    - 事件对象（Event Object）：负责事件源和事件监听器之间的信息传递
    
    Spring事件驱动模型由三大组成部分：
    
    - 事件：ApplicationEvent抽象类，继承jdk的EventObject，通过source获取事件源，一般是容器本身。ApplicationContextEvent抽象类为ApplicationEvent的子类，其子类有：
        
        - ContextClosedEvent：容器停止时触发的事件
        - ContextRefreshedEvent：容器刷新时触发的事件
        - ContextStoppedEvent：容器停止时触发的事件
        - ContextStartedEvent：容器启动时触发的事件
        
    - 事件监听器：ApplicationListener
    - 事件发布器：Publisher以及Multicaster
    
    执行流程：
    
    - prepareRefresh：刷新前的工作准备；
    - obtainFreshBeanFactory：获取子类刷新后的内部beanFactory实例；
    - prepareBeanFactory：为容器注册必要的系统级别的Bean；
    - postProcessBeanFactory：允许容器的子类去注册postProcessor；
    - invokeBeanFactoryPostProcessor：调用容器注册的容器基本的后置处理器；
    - registryBeanPostProcessors：向容器注册Bean级别的后置处理器；
    - initMessageSource：初始化国际化配置；
    - initApplicationEventMulticaster：初始化事件发布者组件；
    - onRefresh：在单例Bean初始化之前预留给子类初始化其他特殊bean的口子；
    - finishBeanFactoryInitialization：设置系统级别的服务，实例化所有非懒加载的单例；
    - finishRefresh：触发初始化完成的回调方法，并发布容器刷新完成的事件给监听者；
    - resetCommonCaches：重置Spring内核中的共用缓存。
    
## 依赖注入

Spring在Bean实例的创建过程中做了很多精细化控制。

### 依赖注入过程

#### AbstractBeanFactory#doGetBean：获取Bean实例

- 尝试从三级中缓存获取Bean
    
    Bean可以是普通的Bean，也可以是FactoryBean。如果是Bean实例则直接返回；如果是FactoryBean，则会调用getObject方法返回Bean实例。
    
- 循环依赖的判断

    如果中缓存中没有获取到Bean，那么会让容器去创建Bean。在创建之前会先判断是否有循环依赖，如果有循环依赖，那么会使用三级缓存来解决。

- 递归去父容器获取Bean实例

    如果当前容器中没有注册该Bean的BeanDefinition实例，父BeanFactory中去查找。

- 从当前容器获取Bean实例

    如果容器中有这个BeanDefinition，那么直接获取。

- 递归实例化显式依赖的Bean

    如果这个Bean设置了depends-on属性，即需要先实例化depends-on的属性。

- 根据不同的Scope采用不同的策略创建Bean实例

- 对Bean进行类型可选的检查

#### DefaultSingletonRegistry#getSingleton：获取单例实例。三级缓存解决循环依赖

#### AbstractAutowireCapableBeanFactory

##### createBean：创建Bean实例的准备

- Bean类型解析
- 处理方法覆盖
- Bean实例化前的后置处理
- doCreateBean

##### doCreateBean：创建Bean实例

- 创建无属性的Bean实例
    - 创建Bean实例（工厂方法、含参构造器注入、无参构造器注入）
        - BeanWrapper接口：提供了修改Bean属性的方法
    - 记录下被@Autowired或者@Value标记上的方法和成员变量
    - 是否允许提前暴露
    - 填充bean属性
    - initializeBean
    - 注册相关销毁逻辑
    - 返回创建好的实例

##### applyMergedBeanDefinitionPostProcessors：处理@Autowired以及@Value

##### populateBean：给Bean实例注入属性值（依赖注入）

总的理念： 调用Bean的Setter方法实例去给Bean设置上属性值；变量类型的转换，同时还要考虑处理集合类型的情况

- postProcessAfterInstantiation：在设置属性前区修改Bean状态，也可以控制是否继续给Bean设置属性值
- 注入属性到PropertyValues中（按名字装配、按类型装配）
- postProcessPropertyValues：对解析完但未设置的属性进行再处理
- 是否进行依赖检查
- 将PropertyValues中的属性值设置到BeanWrapper中

#### AutowiredAnnotationBeanPostProcessor#postProcessorProperties：Autowired的依赖注入逻辑

#### DefaultListableBeanFactory#doResolveDependency：依赖解析

#### DependencyDescriptor#injectionPoint：创建依赖实例

## AOP

关注点分离（Concern Separation）：不同的问题交给不同的部分去解决，每部分专注解决自己的问题。

单个Aspect的执行顺序：

![](https://raw.githubusercontent.com/daffupman/markdown-img/master/20200610225813.png)

多个Aspect的执行顺序：

![](https://raw.githubusercontent.com/daffupman/markdown-img/master/20200610225956.png)

- 织入：将Aspect模块化的横切关注点集成到OOP里
- 织入器：完成织入过程的执行者，如ajc

### Spring AOP实现原理

![代理模式](https://raw.githubusercontent.com/daffupman/markdown-img/master/20200612223735.png)

- JDK静态代理
- JDK动态代理

    在JVM加载阶段会完成：
    1. 通过带有包名的类来获取对应class文件的二进制字节流（ClassLoader#findClass）；
    2. 根据读取的字节流，将代表的静态存储结构转换为运行时数据结构；
    3. 在内存中生成一个代表该类的Class对象，作为方法区该类的数据访问入口。
    
    JDK提供的动态代理方式，可以在程序运行时动态生成类的字节码，并加载到JVM中。它也要求被代理类实现接口，但是代理类并不需要实现接口。
    
    核心接口和类：
    
    `java.lang.reflect.InvocationHandler`：实现此接口的类才可具有代理功能。`public Object invoke(Object proxy, Method method, Object[] args) throws Throwable`是该接口的唯一方法，第一个参数代表代理对象，第二参数为被代理对象的方法，第三个为方法的参数。
    
    `java.lang.reflect.Proxy`：用来创建动态代理类，其中最重要的一个方法是`public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)`。
    
- cglib动态代理
 
    对asm框架的封装，需要额外引入jar，代理对象时不要求被代理的对象实现接口。核心类：
    
    `net.sf.cglib.proxy.Callback`：该接口没有方法，只是一个标记；
    `net.sf.cglib.proxy.MethodInterceptor`：相当于InvocationHandler，该接口继承了Callback，包含一个方法`Object intercept(Object var1, Method var2, Object[] var3, MethodProxy var4)`,前三个参数与InvocationHandler#invoke方法的参数意思一样，最后一个参数是对Method对象的代理对象。
    `net.sf.cglib.proxy.Enhancer`：相当于Proxy，该类包含一个核心方法`public Object create()`,`public static Object create(Class type, Callback callback)`
    
- jdk动态代理和cglib动态代理
    
    - jdk动态代理：基于反射机制，要求业务类必须实现接口。jdk原生，支持更好。
    - cglib：基于asm机制实现，生成业务类的子类去作为代理类。被代理类无需实现接口，但需要额外的jar包
    
### 自研Aop

#### V1.0

目标：

1. 解决标记的问题，定义横切逻辑的骨架；
2. 定义Aspect横切逻辑以及被代理方法的执行顺序；
3. 将横切逻辑织入到被代理的对象以生成动态代理对象；

#### V2.0 AspectJ框架

AspectJ框架的织入时机：静态织入和LTW

- 编译时织入：利用ajc，将切面逻辑织入到类里生成class对象
- 编译后织入：利用ajc，修改javac编译出的class文件
- 类加载期织入：利用java agent，在类加载的时候织入切面逻辑

#### Spring AOP

总体流程：

- 注册解析AOP服务
- 解析和加载横切逻辑
- 将横切逻辑织入到目标Bean中

使用@EnableAspectJAutoProxy注解可以让spring具备aspectj的功能。该注解上标有 `@Import(AspectJAutoProxyRegistrar)`，引入的 `AspectJAutoProxyRegistrar` 就是Spring Aop的入口。

AspectJAutoProxyRegistrar实现了ImportBeanDefinitionRegistrar接口，重要方法为 `registerBeanDefinition` 。

`AutoProxyCreator`：