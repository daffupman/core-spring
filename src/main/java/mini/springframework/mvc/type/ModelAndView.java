package mini.springframework.mvc.type;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储处理完后的结果数据，以及显示该数据的视图
 *
 * @author daffupman
 * @since 2020/7/1
 */
public class ModelAndView {

    // 视图所在的路径
    private String view;
    // 页面的data数据
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView setView(String view) {
        this.view = view;
        return this;
    }

    public ModelAndView addViewData(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public String getView() {
        return view;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
