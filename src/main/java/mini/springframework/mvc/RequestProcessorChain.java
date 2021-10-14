package mini.springframework.mvc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mini.springframework.mvc.processor.RequestProcessor;
import mini.springframework.mvc.render.DefaultResultRender;
import mini.springframework.mvc.render.InternalErrorResultRender;
import mini.springframework.mvc.render.ResultRender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * 以责任链模式执行注册的请求处理器
 * 委派给特定的Render实例对处理后的结果进行渲染
 *
 * @author daffupman
 * @since 2020/6/14
 */
@Data
@Slf4j
public class RequestProcessorChain {

    // 请求处理器迭代器
    private Iterator<RequestProcessor> requestProcessorIterator;
    // 请求request
    private HttpServletRequest request;
    // 请求response
    private HttpServletResponse response;
    // http请求方法
    private String requestMethod;
    // http请求路径
    private String requestPath;
    // http响应状态码
    private int responseCode;
    // 请求结果渲染器
    private ResultRender resultRender;

    public RequestProcessorChain(Iterator<RequestProcessor> requestProcessorIterator, HttpServletRequest request, HttpServletResponse response) {
        this.requestProcessorIterator = requestProcessorIterator;
        this.request = request;
        this.response = response;
        this.requestMethod = request.getMethod();
        this.requestPath = request.getPathInfo();
        this.responseCode = HttpServletResponse.SC_OK;
    }

    /**
     * 以责任链模式执行请求链
     */
    public void doRequestProcessChain() {
        // 1、通过迭代器遍历注册的请求粗里粗气实现类列表
        try {
            // 2、知道某个请求处理器执行后返回false为止
            while (requestProcessorIterator.hasNext()) {
                if (!requestProcessorIterator.next().process(this)) {
                    break;
                }
            }
        } catch (Exception e) {
            // 3、期间如果出现异常，则交由内部异常渲染器处理
            this.resultRender = new InternalErrorResultRender(e.getMessage());
            log.error("doRequestProcessChain error: ", e);
        }
    }

    /**
     * 执行处理器
     */
    public void doRender() {
        // 如果请求处理器实现类均为选择合适的渲染器，则使用默认的
        if (this.resultRender == null) {
            this.resultRender = new DefaultResultRender();
        }
        // 调用渲染器的render方法对结果进行渲染
        try {
            this.resultRender.render(this);
        } catch (Exception e) {
            log.error("doRender error: ", e);
            throw new RuntimeException(e);
        }
    }
}
