package mini.springframework.mvc.annotation;

import mini.springframework.mvc.type.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识Controller的方法与请求路径和请求方法的映射关系
 *
 * @author daffupman
 * @since 2020/6/14
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    // 请求路径
    String value() default "";
    RequestMethod method() default RequestMethod.GET;
}
