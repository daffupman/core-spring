package mini.springframework.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求方法参数名称
 *
 * @author daffupman
 * @since 2020/6/14
 */
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    // 请求路径
    String value() default "";
    // 该参数是否必须
    boolean required() default true;
}
