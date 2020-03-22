package io.daff.controller;

import io.daff.controller.frontend.MainPageController;
import io.daff.controller.superadmin.HeadLineOperationController;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 仿SpringMVC的DispatcherServlet
 * - 拦截所有请求
 * - 解析请求
 * - 派发给对应的Controller里面的方法进行处理
 *
 * @author daffupman
 * @since 2020/3/22
 */
@WebServlet("/") // 不要使用指定成/*，否则这个Servlet处理.jsp请求，会一直转发这个请求
@Slf4j
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("request path is: " + req.getServletPath());
        log.debug("request method is: " + req.getMethod());
        String path = req.getServletPath();
        String method = req.getMethod();
        if ("/frontend/getmainpageinfo".equals(path) && "GET".equalsIgnoreCase(method)) {
            new MainPageController().getMainPageInfo(req, resp);
        } else if("/superadmin/addheadline".equals(path) && "POST".equalsIgnoreCase(method)) {
            new HeadLineOperationController().addHeadLine(req, resp);
        }

    }
}
