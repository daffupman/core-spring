package io.daff.service.solo;

import io.daff.entity.bo.HeadLine;
import io.daff.entity.dto.Result;

import java.util.List;

/**
 * @author daffupman
 * @since 2020/3/22
 */
public interface HeadLineService {

    Boolean addHeadLine(HeadLine headLine);
    Boolean removeHeadLine(Integer headLineId);
    Boolean modifyHeadLine(HeadLine headLine);
    HeadLine queryHeadLineById(Integer headLineId);
    Result<List<HeadLine>> queryHeadLine(HeadLine headLineCondition,
                                         Integer pageIndex,
                                         Integer pageSize);
}
