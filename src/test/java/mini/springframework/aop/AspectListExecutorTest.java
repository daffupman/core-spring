package mini.springframework.aop;

import mini.springframework.aop.aspect.AspectInfo;
import mini.springframework.aop.mock.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author daffupman
 * @since 2020/6/13
 */
public class AspectListExecutorTest {

    @DisplayName("测试Aspect中的排序功能")
    @Test
    public void testSorted() {
        List<AspectInfo> aspectInfoList = Arrays.asList(
                new AspectInfo(4, new MockAspect2()),
                new AspectInfo(1, new MockAspect5()),
                new AspectInfo(3, new MockAspect3()),
                new AspectInfo(2, new MockAspect4()),
                new AspectInfo(5, new MockAspect1())
        );

        AspectListExecutor aspectListExecutor = new AspectListExecutor(
                AspectListExecutorTest.class,
                aspectInfoList
        );

        aspectListExecutor.getSortedAspectInfoList().forEach(each -> {
            System.out.println(each.getOrderIndex() + " : " + each.getAspectObject().getClass());
        });
    }
}
