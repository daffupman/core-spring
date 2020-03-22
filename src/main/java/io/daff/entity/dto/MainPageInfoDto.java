package io.daff.entity.dto;

import io.daff.entity.bo.HeadLine;
import io.daff.entity.bo.ShopCategory;
import lombok.Data;

import java.util.List;

/**
 * @author daffupman
 * @since 2020/3/22
 */
@Data
public class MainPageInfoDto {

    private List<HeadLine> headLineList;
    private List<ShopCategory> shopCategoryList;
}
