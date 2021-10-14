package mini.springframework.mvc.render;

import mini.springframework.mvc.RequestProcessorChain;

import javax.servlet.http.HttpServletResponse;

/**
 * 资源找不到时的渲染器
 *
 * @author daffupman
 * @since 2020/6/14
 */
public class ResourceNotFoundResultRender implements ResultRender {

    private String httpMethod;
    private String httpPath;

    public ResourceNotFoundResultRender(String method, String path) {
        this.httpMethod = method;
        this.httpPath = path;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "获取不到对应的请求资源：请求路径[" + httpPath + "]，请求的方法[" + httpMethod + "]"
        );
    }
}
