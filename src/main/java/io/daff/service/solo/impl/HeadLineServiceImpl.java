package io.daff.service.solo.impl;

import io.daff.entity.bo.HeadLine;
import io.daff.service.solo.HeadLineService;
import lombok.extern.slf4j.Slf4j;
import mini.springframework.core.annotation.Service;

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
    public List<HeadLine> queryHeadLine(HeadLine headLineCondition, Integer pageIndex, Integer pageSize) {
        return null;
    }
}
