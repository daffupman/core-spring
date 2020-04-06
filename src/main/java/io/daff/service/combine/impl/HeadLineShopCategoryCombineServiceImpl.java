package io.daff.service.combine.impl;

import io.daff.entity.bo.HeadLine;
import io.daff.entity.bo.ShopCategory;
import io.daff.entity.dto.MainPageInfoDto;
import io.daff.service.combine.HeadLineShopCategoryCombineService;
import io.daff.service.solo.HeadLineService;
import io.daff.service.solo.ShopCategoryService;
import mini.springframework.core.annotation.Service;
import mini.springframework.inject.annotation.Autowired;

import java.util.List;

/**
 * @author daffupman
 * @since 2020/3/22
 */
@Service
public class HeadLineShopCategoryCombineServiceImpl implements HeadLineShopCategoryCombineService {

    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private ShopCategoryService shopCategoryService;

    @Override
    public MainPageInfoDto getMainPageInfo() {
        // 获取头条列表
        HeadLine headLineCondition = new HeadLine();
        headLineCondition.setEnableStatus(1);
        List<HeadLine> headLineList = headLineService.queryHeadLine(headLineCondition, 1, 4);

        // 获取店铺类别列表
        ShopCategory shopCategoryCondition = new ShopCategory();
        List<ShopCategory> shopCategoryList = shopCategoryService.queryShopCategory(shopCategoryCondition, 1, 100);

        // 合并两者并返回
        return mergeMainPageInfoResult(headLineList, shopCategoryList);
    }

    // 合并结果集
    private MainPageInfoDto mergeMainPageInfoResult(List<HeadLine> headLineList,
                                                    List<ShopCategory> shopCategoryList) {

        MainPageInfoDto result = new MainPageInfoDto();
        result.setHeadLineList(headLineList);
        result.setShopCategoryList(shopCategoryList);
        return result;
    }
}
