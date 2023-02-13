package com.imooc.reader.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.imooc.reader.entity.Book;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 更新圖書的評論數量和評分
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateEvaluation();

    /**
     * 新增一本書籍
     * @param book 書本物件
     * @return 新增完畢後的書本物件(MyBatis-Plus會自動添加bookId屬性
     */
    public Book createBook(Book book);

    /**
     * 更新書本的內容
     * @param book 要更新的書本
     * @return 更新後的書本
     */
    public Book updateBook(Book book);

    /**
     * 依照bookId刪除書本與相關的資料
     * @param bookId 要刪除的書本id
     */
    public void deleteBookById(Long bookId);
}
