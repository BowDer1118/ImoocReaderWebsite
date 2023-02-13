package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.reader.entity.Book;
import com.imooc.reader.entity.Evaluation;
import com.imooc.reader.entity.MemberReadState;
import com.imooc.reader.mapper.BookMapper;
import com.imooc.reader.mapper.EvaluationMapper;
import com.imooc.reader.mapper.MemberReadStateMapper;
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
    @Resource
    private MemberReadStateMapper memberReadStateMapper;
    @Resource
    private EvaluationMapper evaluationMapper;

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

    /**
     * 依照書本id查詢
     * @param bookId 書本的id
     * @return 書本物件
     */
    @Override
    public Book selectById(Long bookId) {
        Book book=bookMapper.selectById(bookId);
        return book;
    }

    /**
     * 更新圖書的評論數量和評分
     */
    @Override
    public void updateEvaluation() {
        bookMapper.updateEvaluation();
    }

    /**
     * 新增一本書籍
     * @param book 書本物件
     * @return 新增完畢後的書本物件(MyBatis-Plus會自動添加bookId屬性
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Book createBook(Book book) {
        bookMapper.insert(book);
        return book;
    }

    /**
     * 更新書本的內容
     *
     * @param book 要更新的書本
     * @return 更新後的書本
     */
    @Override
    public Book updateBook(Book book) {
        //更新書本
        bookMapper.updateById(book);
        return book;
    }

    /**
     * 依照bookId刪除書本與相關的資料
     *
     * @param bookId 要刪除的書本id
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void deleteBookById(Long bookId) {
        //刪除book table中的資料
        bookMapper.deleteById(bookId);
        //刪除member_read_state table中的資料
        QueryWrapper<MemberReadState> mrsqueryWrapper=new QueryWrapper();
        //刪除Book的所有的評論狀態
        mrsqueryWrapper.eq("book_id",bookId);
        memberReadStateMapper.delete(mrsqueryWrapper);
        //刪除evaluation table中的資料
        QueryWrapper<Evaluation> eQueryWrapper=new QueryWrapper<>();
        eQueryWrapper.eq("book_id",bookId);
        evaluationMapper.delete(eQueryWrapper);
    }
}
