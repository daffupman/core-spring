package mini.springframework.aop;

import io.daff.controller.superadmin.HeadLineOperationController;
import mini.springframework.core.BeanContainer;
import mini.springframework.inject.DependencyInjector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author daffupman
 * @since 2020/6/13
 */
public class AspectWeaverTest {

    @DisplayName("测试aop织入")
    @Test
    public void testDoAop() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("io.daff");
        // aop需要先于di操作
        new AspectWeaver().doAop();
        new DependencyInjector().doIoc();

        HeadLineOperationController bean = (HeadLineOperationController) beanContainer.getBean(HeadLineOperationController.class);
        bean.addHeadLine(null, null);
    }
}
