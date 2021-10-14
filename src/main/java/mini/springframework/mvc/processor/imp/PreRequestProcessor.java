package mini.springframework.mvc.processor.imp;

import lombok.extern.slf4j.Slf4j;
import mini.springframework.mvc.RequestProcessorChain;
import mini.springframework.mvc.processor.RequestProcessor;

/**
 * 请求预处理，包括编码以及路径处理
 *
 * @author daffupman
 * @since 2020/6/14
 */
@Slf4j
public class PreRequestProcessor implements RequestProcessor {

    @Override
    public boolean process(RequestProcessorChain requestProcessorChain) throws Exception {
        // 将请求的编码统一设置为UTF-8
        requestProcessorChain.getRequest().setCharacterEncoding("UTF-8");
        // 将请求路径末尾的/删除，为后续匹配Controller请求路径做准备
        String requestPath = requestProcessorChain.getRequestPath();
        // 对于请求http://localhost:8080/hello 它的requestPath为/
        if (requestPath.length() > 1 && requestPath.endsWith("/")) {
            // 不是访问根路径,且以/结尾,则去除/
            requestProcessorChain.setRequestPath(requestPath.substring(0, requestPath.length() - 2));
        }
        log.info("preprocess request {} {}", requestProcessorChain.getRequestMethod(), requestProcessorChain.getRequestPath());
        return true;
    }
}
