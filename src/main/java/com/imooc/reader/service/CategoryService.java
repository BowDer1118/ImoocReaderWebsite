package com.imooc.reader.service;

import com.imooc.reader.entity.Category;

import java.util.List;

/*定義CategoryService提供了什麼服務*/
public interface CategoryService {
    public List<Category> selectAll();
}
