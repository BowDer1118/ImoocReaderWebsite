package com.imooc.reader.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.imooc.reader.entity.*;
import com.imooc.reader.service.BookService;
import com.imooc.reader.service.CategoryService;
import com.imooc.reader.service.EvaluationService;
import com.imooc.reader.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class BookController {
    @Resource
    private CategoryService categoryService; //提供查詢分類的服務
    @Resource
    private BookService bookService; //提供查詢圖書分頁的服務
    @Resource
    private EvaluationService evaluationService; //提供查詢評論的服務
    @Resource
    private MemberService memberService; //提供會員功能
    /**
     * 負責Response主畫面
     * @return 主畫面
     */
    @GetMapping("/bookshop") //將主頁設定在bookshop
    public ModelAndView showIndex(){
        //設定View為index.ftl
        ModelAndView modelAndView=new ModelAndView("/index");
        //從資料庫獲取category的資訊
        List<Category> categoryList=categoryService.selectAll();
        //將資料傳給ModelAndView
        modelAndView.addObject(categoryList);
        return modelAndView;
    }

    /**
     * 利用分頁技術提供圖書資訊，利用JSON序列化提供資訊
     * @param categoryId 前台當前要顯示的分類id
     * @param order 當前書籍排序的準則
     * @param p 要請求的第幾頁
     * @return 第p頁圖書資訊的JSON物件
     */
    @GetMapping("/books")
    @ResponseBody
    public IPage<Book> selectBook(Long categoryId,String order,Integer p){
        //如果沒有給予頁數，默認給第1頁
        if(p==null){
            p=1;
        }
        IPage<Book> iPage=bookService.paging(categoryId,order,p,10);
        return iPage;
    }

    /**
     * 用於顯示書本的詳細資訊頁面
     * @param bookId 書本的id編號
     * @param session 獲取當前request的session
     * @return 書本詳細資訊的網頁
     */
    @GetMapping("/book/{bookId}") //獲取URL中的bookId
    public ModelAndView showDetail(@PathVariable(name = "bookId")Long bookId, HttpSession session){
        ModelAndView modelAndView=new ModelAndView("/detail");
        //查詢Book物件
        Book book=bookService.selectById(bookId);
        modelAndView.addObject("book",book);
        //查詢關於書本的有效評論
        List<Evaluation> evaluationList=evaluationService.selectByBookId(bookId);
        modelAndView.addObject("evaluationList",evaluationList);
        //獲取會員的session
        Member member=(Member) session.getAttribute("loginMember");
//        System.out.println("****");
//        System.out.println(member);
//        System.out.println("****");
        //判斷會員是否已經登入
        if(member!=null){
            //依照會員的id與書本的id查詢對應的會員閱讀狀態
            MemberReadState memberReadState=memberService.selectMemberReadState(member.getMemberId(),bookId);
//            System.out.println("****");
//            System.out.println(memberReadState);
//            System.out.println("****");
            //曾經評價過
            if(memberReadState!=null){
                //將狀態保存到modelAndView中
                modelAndView.addObject("memberReadState",memberReadState);
            }
        }
        return modelAndView;
    }
}
