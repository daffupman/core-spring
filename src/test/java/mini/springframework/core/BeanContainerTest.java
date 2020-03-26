package mini.springframework.core;

import io.daff.controller.DispatcherServlet;
import io.daff.controller.frontend.MainPageController;
import io.daff.service.solo.HeadLineService;
import io.daff.service.solo.impl.HeadLineServiceImpl;
import mini.springframework.core.annotation.Controller;
import org.junit.jupiter.api.*;

/**
 * @author daffupman
 * @since 2020/3/24
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeanContainerTest {

    private static BeanContainer beanContainer;

    @BeforeAll
    static void init() {
        beanContainer = BeanContainer.getInstance();
    }

    @DisplayName("加载目标类及其实例到BeanContainer")
    @Test
    @Order(1)
    public void loadBeanTest() {
        Assertions.assertEquals(false, beanContainer.isLoaded());
        beanContainer.loadBeans("io.daff");
        Assertions.assertEquals(5, beanContainer.size());
        Assertions.assertEquals(true, beanContainer.isLoaded());
    }

    @DisplayName("根据类获取其实例：getBeanTest")
    @Test
    @Order(2)
    public void getBeanTest() {
        MainPageController controller = (MainPageController)beanContainer.getBean(MainPageController.class);
        Assertions.assertEquals(true, controller instanceof MainPageController);
        DispatcherServlet dispatcherServlet = (DispatcherServlet)beanContainer.getBean(DispatcherServlet.class);
        Assertions.assertEquals(null, dispatcherServlet);
    }

    @DisplayName("根据注解获取对应的实例：getClassesByAnnotationTest")
    @Test
    @Order(3)
    public void getClassesByAnnotationTest() {
        Assertions.assertEquals(true, beanContainer.isLoaded());
        Assertions.assertEquals(2, beanContainer.getClassesByAnnotation(Controller.class).size());
    }

    @DisplayName("根据接口获取实现类：getClassesBySuperTest")
    @Test
    @Order(4)
    public void getClassesBySuperTest() {
        Assertions.assertEquals(true, beanContainer.isLoaded());
        Assertions.assertEquals(true, beanContainer.getClassesBySuper(HeadLineService.class).contains(HeadLineServiceImpl.class));
    }
}
