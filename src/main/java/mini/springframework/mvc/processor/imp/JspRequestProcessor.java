package mini.springframework.mvc.processor.imp;

import mini.springframework.mvc.RequestProcessorChain;
import mini.springframework.mvc.processor.RequestProcessor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 * jsp资源请求处理
 *
 * @author daffupman
 * @since 2020/6/14
 */
public class JspRequestProcessor implements RequestProcessor {

    // jsp请求的RequestDispatcher的名称
    public static final String JSP_SERVLET = "jsp";
    // jsp请求资源路径前缀
    public static final String JSP_RESOURCE_PREFIX = "/templates/";

    // jsp的RequestDispatcher，处理jsp资源
    private RequestDispatcher jspServlet;

    public JspRequestProcessor(ServletContext servletContext) {
        jspServlet = servletContext.getNamedDispatcher(JSP_SERVLET);
        if (jspServlet == null) {
            throw new RuntimeException("There is no jsp servlet.");
        }
    }

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        if(isJspResource(requestProcessorChain.getRequestPath())) {
            jspServlet.forward(requestProcessorChain.getRequest(), requestProcessorChain.getResponse());
            return false;
        }
        return true;
    }

    private boolean isJspResource(String url) {
        return url.startsWith(JSP_RESOURCE_PREFIX );
    }
}
