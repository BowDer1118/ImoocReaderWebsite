package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.reader.entity.Category;
import com.imooc.reader.mapper.CategoryMapper;
import com.imooc.reader.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/*實作CategoryService接口*/
//指定不使用Transaction和readOnly提高效能
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
@Service("categoryService") //具體的實現類的beanId通常會叫做接口名稱，讓Spring可以快速DI
public class CategoryServiceImpl implements CategoryService {
    @Resource
    CategoryMapper categoryMapper;

    /**
     * 查詢所有圖書分類
     * @return 圖書分類的List
     */

    @Override
    public List<Category> selectAll() {
        //查詢器:使用默認值，代表不加以任何條件，整個table的資料都取出來
        QueryWrapper queryWrapper=new QueryWrapper();
        List<Category> categoryList=categoryMapper.selectList(queryWrapper);
        return categoryList;
    }
}
