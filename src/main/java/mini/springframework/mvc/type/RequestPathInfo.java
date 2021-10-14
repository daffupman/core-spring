package mini.springframework.mvc.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装http请求路径和请求方法
 *
 * @author daffupman
 * @since 2020/6/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPathInfo {

    // HttpServletRequest中的请求方法
    private String httpMethod;
    // HttpServletRequest中的请求路径
    private String httpPath;
}
