package mini.springframework.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * @author daffupman
 * @since 2020/3/22
 */
public class ClassUtilsTest {

    @DisplayName("提取目标类方法:extractPackageClass")
    @Test
    public void testExtractPackageClass() {
        Set<Class<?>> classSet = ClassUtils.extractClassFromPackage("io.daff.entity");
        System.out.println(classSet);
        Assertions.assertEquals(6, classSet.size());
    }
}
