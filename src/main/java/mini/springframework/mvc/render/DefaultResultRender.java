package mini.springframework.mvc.render;

import mini.springframework.mvc.RequestProcessorChain;

/**
 * 默认渲染器
 *
 * @author daffupman
 * @since 2020/6/14
 */
public class DefaultResultRender implements ResultRender {

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        requestProcessorChain.getResponse().setStatus(requestProcessorChain.getResponseCode());
    }
}
