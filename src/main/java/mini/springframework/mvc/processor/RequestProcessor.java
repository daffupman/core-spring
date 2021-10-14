package mini.springframework.mvc.processor;

import mini.springframework.mvc.RequestProcessorChain;

/**
 * 请求执行器
 *
 * @author daffupman
 * @since 2020/6/14
 */
public interface RequestProcessor {

    /**
     * 以责任链的方式依次执行RequestProcessor实现类的process方法
     * 返回true，接着执行下一个RequestProcessor实现类，返回false，停止执行，直接返回处理的结果。
     */
    boolean process(RequestProcessorChain requestProcessorChain) throws Exception;
}
