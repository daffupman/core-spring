package io.daff.controller.frontend;

import io.daff.entity.dto.MainPageInfoDto;
import io.daff.entity.dto.Result;
import io.daff.service.combine.HeadLineShopCategoryCombineService;
import lombok.Getter;
import mini.springframework.core.annotation.Controller;
import mini.springframework.inject.annotation.Autowired;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author daffupman
 * @since 2020/3/22
 */
@Controller
@Getter
public class MainPageController {

    @Autowired("HeadLineShopCategoryCombineServiceImpl")
    private HeadLineShopCategoryCombineService headLineShopCategoryCombineService;

    // 获取主页所需的数据
    public Result<MainPageInfoDto> getMainPageInfo(HttpServletRequest req, HttpServletResponse resp) {
        MainPageInfoDto result = headLineShopCategoryCombineService.getMainPageInfo();
        return new Result<>(result);
    }
}
