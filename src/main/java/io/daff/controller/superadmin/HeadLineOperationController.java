package io.daff.controller.superadmin;

import io.daff.entity.bo.HeadLine;
import io.daff.entity.dto.Result;
import io.daff.service.solo.HeadLineService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author daffupman
 * @since 2020/3/22
 */
public class HeadLineOperationController {

    private HeadLineService headLineService;

    public Result<Boolean> addHeadLine(HttpServletRequest req, HttpServletResponse resp) {
        headLineService.addHeadLine(new HeadLine());
        return null;
    }
    public Result<Boolean> removeHeadLine(HttpServletRequest req, HttpServletResponse resp) {
        headLineService.removeHeadLine(1);
        return null;
    }
    public Result<Boolean> modifyHeadLine(HttpServletRequest req, HttpServletResponse resp) {
        headLineService.modifyHeadLine(new HeadLine());
        return null;
    }
    public Result<HeadLine> queryHeadLineById(HttpServletRequest req, HttpServletResponse resp) {
        headLineService.queryHeadLineById(1);
        return null;
    }
    public Result<List<HeadLine>> queryHeadLine(HttpServletRequest req, HttpServletResponse resp) {
        headLineService.queryHeadLine(new HeadLine(), 1, 10);
        return null;
    }
}
