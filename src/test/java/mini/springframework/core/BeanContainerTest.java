package mini.springframework.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author daffupman
 * @since 2020/3/24
 */
public class BeanContainerTest {

    private static BeanContainer beanContainer;

    @BeforeAll
    static void init() {
        beanContainer = BeanContainer.getInstance();
    }

    @DisplayName("加载目标类及其实例到BeanContainer")
    @Test
    public void loadBeanTest() {
        Assertions.assertEquals(false, beanContainer.isLoaded());
        beanContainer.loadBeans("io.daff");
        Assertions.assertEquals(5, beanContainer.size());
        Assertions.assertEquals(true, beanContainer.isLoaded());
    }
}
