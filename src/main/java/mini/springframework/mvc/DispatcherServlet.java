package mini.springframework.mvc;

import lombok.extern.slf4j.Slf4j;
import mini.springframework.aop.AspectWeaver;
import mini.springframework.core.BeanContainer;
import mini.springframework.inject.DependencyInjector;
import mini.springframework.mvc.processor.RequestProcessor;
import mini.springframework.mvc.processor.imp.ControllerRequestProcessor;
import mini.springframework.mvc.processor.imp.JspRequestProcessor;
import mini.springframework.mvc.processor.imp.PreRequestProcessor;
import mini.springframework.mvc.processor.imp.StaticResourceRequestProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 仿SpringMVC的DispatcherServlet
 * - 拦截所有请求
 * - 解析请求
 * - 派发给对应的Controller里面的方法进行处理
 *
 * @author daffupman
 * @since 2020/3/22
 */
@WebServlet("/*") // 会处理所有的请求
@Slf4j
public class DispatcherServlet extends HttpServlet {

    List<RequestProcessor> PROCESSOR = new ArrayList<>();

    @Override
    public void init() {
        // 只执行一次，一般是对常驻内存的变量做处理

        // 1、初始化容器
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("io.daff");
        new AspectWeaver().doAop();
        new DependencyInjector().doIoc();

        // 2、初始化请求处理器责任链(按顺序添加)
        PROCESSOR.add(new PreRequestProcessor());
        PROCESSOR.add(new StaticResourceRequestProcessor(getServletContext()));
        PROCESSOR.add(new JspRequestProcessor(getServletContext()));
        PROCESSOR.add(new ControllerRequestProcessor());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 创建责任链实例
        RequestProcessorChain requestProcessorChain = new RequestProcessorChain(PROCESSOR.iterator(), req, resp);
        // 通过责任链模式来依次调用请求处理进行处理
        requestProcessorChain.doRequestProcessChain();
        // 对处理结果进行渲染
        requestProcessorChain.doRender();
    }
}
