package io.daff.service.solo.impl;

import io.daff.entity.bo.HeadLine;
import io.daff.entity.bo.ShopCategory;
import io.daff.service.solo.ShopCategoryService;
import mini.springframework.core.annotation.Service;

import java.util.List;

/**
 * @author daffupman
 * @since 2020/3/22
 */
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Override
    public Boolean addShopCategory(ShopCategory shopCategory) {
        return null;
    }

    @Override
    public Boolean removeShopCategory(Integer shopCategoryId) {
        return null;
    }

    @Override
    public Boolean modifyShopCategory(ShopCategory shopCategory) {
        return null;
    }

    @Override
    public ShopCategory queryShopCategoryById(Integer shopCategoryId) {
        return null;
    }

    @Override
    public List<ShopCategory> queryShopCategory(ShopCategory shopCategoryCondition,
                                            Integer pageIndex,
                                            Integer pageSize) {
        return null;
    }
}
