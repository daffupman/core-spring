package mini.springframework.aop.annotation;

import java.lang.annotation.*;

/**
 * 切面标记注解
 *
 * @author daffupman
 * @since 2020/6/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 将Aspect的切面逻辑注入到value注解标记的类上
     */
    Class<? extends Annotation> value();
}
