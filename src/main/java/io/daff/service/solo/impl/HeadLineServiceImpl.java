package io.daff.service.solo.impl;

import io.daff.entity.bo.HeadLine;
import io.daff.entity.dto.Result;
import io.daff.service.solo.HeadLineService;
import lombok.extern.slf4j.Slf4j;
import mini.springframework.core.annotation.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author daffupman
 * @since 2020/3/22
 */
@Service
@Slf4j
public class HeadLineServiceImpl implements HeadLineService {

    @Override
    public Boolean addHeadLine(HeadLine headLine) {
        log.info("执行HeadLineServiceImpl#addHeadLine方法");
        return null;
    }

    @Override
    public Boolean removeHeadLine(Integer headLineId) {
        return null;
    }

    @Override
    public Boolean modifyHeadLine(HeadLine headLine) {
        return null;
    }

    @Override
    public HeadLine queryHeadLineById(Integer headLineId) {
        return null;
    }

    @Override
    public Result<List<HeadLine>> queryHeadLine(HeadLine headLineCondition, Integer pageIndex, Integer pageSize) {
        List<HeadLine> headLineList = new ArrayList<>();
        HeadLine headLine1 = new HeadLine();
        headLine1.setLineId(1L);
        headLine1.setLineName("头条1");
        headLine1.setLineLink("www.baidu.com");
        headLine1.setLineImg("头条图片1地址");
        headLineList.add(headLine1);
        HeadLine headLine2 = new HeadLine();
        headLine2.setLineId(2L);
        headLine2.setLineName("头条2");
        headLine2.setLineLink("www.google.com");
        headLine2.setLineImg("头条图片2地址");
        headLineList.add(headLine2);

        Result<List<HeadLine>> result = new Result<>();
        result.setData(headLineList);
        result.setCode(200);
        return result;
    }
}
