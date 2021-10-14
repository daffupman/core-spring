package mini.springframework.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author daffupman
 * @since 2020/3/24
 */
public class ValidationUtils {

    /**
     * String是否为空
     */
    public static Boolean isEmpty(String obj) {
        return obj == null || "".equals(obj);
    }

    /**
     * Array是否为空
     */
    public static Boolean isEmpty(Object[] obj) {
        return obj == null || obj.length == 0;
    }

    /**
     * 返回Collection集合是否为空
     */
    public static Boolean isEmpty(Collection<?> obj) {
        return obj == null || obj.isEmpty();
    }

    /**
     * Map是否为空
     */
    public static Boolean isEmpty(Map<?, ?> obj) {
        return obj == null || obj.isEmpty();
    }
}
