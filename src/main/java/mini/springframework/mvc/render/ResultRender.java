package mini.springframework.mvc.render;

import mini.springframework.mvc.RequestProcessorChain;

/**
 * 渲染请求结果
 *
 * @author daffupman
 * @since 2020/6/14
 */
public interface ResultRender {

    // 执行渲染
    void render(RequestProcessorChain requestProcessorChain) throws Exception;
}
