package mini.springframework.inject.annotation;

import java.lang.annotation.*;

/**
 * @author daffupman
 * @since 2020/3/26
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {

    String value() default "";

}
