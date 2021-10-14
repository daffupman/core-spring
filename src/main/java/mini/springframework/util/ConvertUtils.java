package mini.springframework.util;

/**
 * @author daffupman
 * @since 2020/6/14
 */
public class ConvertUtils {

    public static Object primitiveNull(Class<?> type) {
        if (type == int.class || type == double.class
                ||type == short.class ||type == long.class
                ||type == byte.class ||type == float.class) {
            return 0;
        } else if(type == boolean.class) {
            return false;
        }
        return null;
    }

    /**
     * String类型转换成对应的参数类型
     */
    public static Object convert(Class<?> type, String requestValue) {
        if(isPrimitive(type)) {
            if (ValidationUtils.isEmpty(requestValue)) {
                return primitiveNull(type);
            }
            if (type.equals(int.class)||type.equals(Integer.class)) {
                return Integer.parseInt(requestValue);
            } else if(type.equals(String.class)) {
                return requestValue;
            } else if(type.equals(Double.class) || type.equals(double.class)) {
                return Double.parseDouble(requestValue);
            } else if(type.equals(Float.class) || type.equals(float.class)) {
                return Float.parseFloat(requestValue);
            } else if (type.equals(Long.class) || type.equals(long.class)) {
                return Long.parseLong(requestValue);
            } else if(type.equals(Short.class) || type.equals(short.class)) {
                return Short.parseShort(requestValue);
            } else if(type.equals(Byte.class) || type.equals(byte.class)) {
                return Byte.parseByte(requestValue);
            }
            return requestValue;
        } else {
            throw new RuntimeException("could not support non primitive type conversion yet");
        }
    }

    private static boolean isPrimitive(Class<?> type) {
        return type == boolean.class
                || type == Boolean.class
                || type == short.class
                || type == Short.class
                || type == int.class
                || type == Integer.class
                || type == double.class
                || type == Double.class
                || type == byte.class
                || type == Byte.class
                || type == long.class
                || type == Long.class
                || type == float.class
                || type == Float.class
                || type == String.class
                || type == char.class
                || type == Character.class;
    }
}
