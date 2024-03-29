package mini.springframework.mvc.render;

import com.google.gson.Gson;
import mini.springframework.mvc.RequestProcessorChain;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Json渲染器
 *
 * @author daffupman
 * @since 2020/6/14
 */
public class JsonResultRender implements ResultRender {

    private Object jsonData;

    public JsonResultRender(Object jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public void render(RequestProcessorChain requestProcessorChain) throws Exception {
        // 设置响应头
        requestProcessorChain.getResponse().setContentType("application/json");
        requestProcessorChain.getResponse().setCharacterEncoding("UTF-8");
        // 响应流写入经过gson格式化之后的处理结果
        try (PrintWriter writer = requestProcessorChain.getResponse().getWriter()) {
            Gson gson = new Gson();
            writer.write(gson.toJson(jsonData));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
