package com.imooc.reader.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.imooc.reader.entity.Book;

//分頁功能
public interface BookService {
    /**
     * @param categoryId 前台當前要顯示的分類id
     * @param order 當前書籍排序的準則
     * @param page 要查詢第幾頁
     * @param rows 每一頁要有幾筆資料
     * @return 分頁物件
     */
    public IPage<Book> paging(Long categoryId,String order,Integer page,Integer rows);

    /**
     * 根據編號查詢Book物件
     * @param bookId 書本的id
     * @return Book物件
     */
    public Book selectById(Long bookId);
}
