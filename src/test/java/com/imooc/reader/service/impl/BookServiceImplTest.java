package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.imooc.reader.entity.Book;
import com.imooc.reader.mapper.BookMapper;
import com.imooc.reader.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class BookServiceImplTest {
    @Resource
    private BookService bookService;
    @Test
    public void paging() {
        IPage<Book> iPage=bookService.paging(2L,"quantity",1,10);
//        System.out.println("總頁數:"+iPage.getPages());
//        System.out.println("總紀錄數:"+iPage.getTotal());

        //獲取分頁的所有元素
        List<Book> bookList=iPage.getRecords();
        for(Book b:bookList){
            System.out.println(b.getBookId()+":"+b.getBookName());
        }

    }
}