package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.reader.entity.Book;
import com.imooc.reader.mapper.BookMapper;
import com.imooc.reader.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

//查詢不需要Transaction
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
@Service("bookService")
public class BookServiceImpl implements BookService {
    @Resource
    private BookMapper bookMapper;
    /**
     * @param categoryId 前台當前要顯示的分類id
     * @param order 當前書籍排序的準則
     * @param page 要查詢第幾頁
     * @param rows 每一頁要有幾筆資料
     * @return 分頁物件
     */
    @Override
    public IPage<Book> paging(Long categoryId,String order,Integer page, Integer rows) {
        //分頁物件:指定要查詢第幾頁，每頁多少筆資訊
        IPage<Book> iPage=new Page<>(page,rows);
        //查詢器
        QueryWrapper<Book> queryWrapper=new QueryWrapper<>();
        //過濾分類
        if(categoryId!=null &&categoryId!=-1){
            queryWrapper.eq("category_id",categoryId);
        }
        //排序規則
        if(order!=null){
            //依照數量
            if(order.equals("quantity")){
                queryWrapper.orderByDesc("evaluation_quantity");
            }else if(order.equals("score")){
                queryWrapper.orderByDesc("evaluation_score");
            }
        }
        //返回第page頁，分頁物件
        IPage<Book> pageObject=bookMapper.selectPage(iPage,queryWrapper);
        return pageObject;
    }

    @Override
    public Book selectById(Long bookId) {
        Book book=bookMapper.selectById(bookId);
        return book;
    }
}
