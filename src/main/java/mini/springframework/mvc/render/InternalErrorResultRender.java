package mini.springframework.mvc.render;

import mini.springframework.mvc.RequestProcessorChain;

import javax.servlet.http.HttpServletResponse;

/**
 * 内部异常渲染器
 *
 * @author daffupman
 * @since 2020/6/14
 */
public class InternalErrorResultRender implements ResultRender {

    private String errorMsg;

    public InternalErrorResultRender(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
    }
}
