package mini.springframework.mvc.render;

import mini.springframework.mvc.RequestProcessorChain;
import mini.springframework.mvc.type.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author daffupman
 * @since 2020/6/14
 */
public class ViewResultRender implements ResultRender {

    public static final String VIEW_PATH = "/templates/";
    private ModelAndView modelAndView;

    public ViewResultRender(Object mv) {
        if (mv instanceof ModelAndView) {
            // 1、如果入参类型是ModelAndView，则直接赋值给成员变量
            this.modelAndView = ((ModelAndView) mv);
        } else if (mv instanceof String) {
            // 2、如果入参类型是String，则为视图，需要包装后才赋值给成员变量
            this.modelAndView = new ModelAndView().setView((String) mv);
        } else {
            // 3、其他情况，直接抛出异常
            throw new RuntimeException("illegal request result type");
        }

    }

    /**
     * 将请求处理结果按照视图路径转发至对应视图进行展示
     */
    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        HttpServletRequest request = requestProcessorChain.getRequest();
        HttpServletResponse response = requestProcessorChain.getResponse();
        String path = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        // 设置属性
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
        // JSP页面跳转
        request.getRequestDispatcher(VIEW_PATH + path).forward(request, response);
    }
}
