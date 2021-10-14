package mini.springframework.mvc.processor.imp;

import lombok.extern.slf4j.Slf4j;
import mini.springframework.mvc.RequestProcessorChain;
import mini.springframework.mvc.processor.RequestProcessor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 * 负责对静态资源请求的处理，包括但不限于图片，css以及js文件等
 *
 * @author daffupman
 * @since 2020/6/14
 */
@Slf4j
public class StaticResourceRequestProcessor implements RequestProcessor {

    public static final String TOMCAT_DEFAULT_SERVLET = "default";
    public static final String STATIC_RESOURCE_PREFIX = "/static/";
    // tomcat默认请求派发器
    private RequestDispatcher defaultDispatcher;

    public StaticResourceRequestProcessor(ServletContext servletContext) {
        this.defaultDispatcher = servletContext.getNamedDispatcher(TOMCAT_DEFAULT_SERVLET);
        if (this.defaultDispatcher == null) {
            throw new RuntimeException("There is no default tomcat servlet.");
        }
        log.info("The default servlet for static resource is {}", TOMCAT_DEFAULT_SERVLET);
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        // 通过请求路径判断是否是请求的静态资源  静态资源路径：webapp/static
        if(isStaticResource(requestProcessorChain.getRequestPath())) {
            // 如果是静态资源，则将请求转发给default servlet处理
            defaultDispatcher.forward(requestProcessorChain.getRequest(), requestProcessorChain.getResponse());
            return false;
        }
        return true;
    }

    /**
     * 通过请求路径的前缀判断是否是静态资源 /static/
     */
    private boolean isStaticResource(String requestPath) {
        return requestPath.startsWith(STATIC_RESOURCE_PREFIX);
    }
}
