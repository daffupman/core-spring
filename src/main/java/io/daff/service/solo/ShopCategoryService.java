package io.daff.service.solo;

import io.daff.entity.bo.HeadLine;
import io.daff.entity.bo.ShopCategory;

import java.util.List;

/**
 * @author daffupman
 * @since 2020/3/22
 */
public interface ShopCategoryService {

    Boolean addShopCategory(ShopCategory shopCategory);
    Boolean removeShopCategory(Integer shopCategoryId);
    Boolean modifyShopCategory(ShopCategory shopCategory);
    ShopCategory queryShopCategoryById(Integer shopCategoryId);
    List<ShopCategory> queryShopCategory(ShopCategory shopCategoryCondition,
                                     Integer pageIndex,
                                     Integer pageSize);
}
