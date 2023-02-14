package com.imooc.reader.controller.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.imooc.reader.entity.Book;
import com.imooc.reader.service.BookService;
import com.imooc.reader.service.exception.BussinessException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static javax.management.Query.attr;

/**
 * 後台對前台所有功能的控制
 */
@Controller
@RequestMapping("/management/book") //限定URL的開頭
public class MBookController {
    @Resource
    private BookService bookService;
    @GetMapping("/index.html")
    public ModelAndView showBook(){
        return new ModelAndView("/management/book");
    }

    /**
     * 處理檔案上傳
     * @param file 用來處理並訪問上傳的文件
     * @param request 用以獲取當前web應用程式的根路徑
     * @return 一個JSON用來告知wangEditor文件上傳的處理結果
     */
    @PostMapping("/upload")
    @ResponseBody
    public Map upload(@RequestParam("img") MultipartFile file, HttpServletRequest request) throws IOException {
        Map map=new HashMap();
        //得到上傳目錄
        //getServletContext().getResource("/")得到的路徑為發布時，也就是out資料夾中的root路徑:out/artifacts/imooc_reader_Web_exploded
        String uploadPath=request.getServletContext().getResource("/").getPath()+"/upload/";
        //以時間為文件的名字
        String fileName= new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        //獲取檔案的副檔名
        String suffix=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //將上傳的檔案另存到指定的位置
        file.transferTo(new File(uploadPath+fileName+suffix));
        //回傳處理結果
        map.put("errno",0);
        map.put("data",new String[]{"/upload/"+fileName+suffix}); //回傳檔案上傳後的訪問URL
        return map;
    }

    /**
     * 負責處理新增一本書到資料庫
     * @param book 要新增的Book物件
     * @return 處理結果
     */
    @PostMapping("/create")
    @ResponseBody
    public Map createBook(Book book){
        Map map=new HashMap();
        try{
            //補全book屬性
            book.setEvaluationQuantity(0);
            book.setEvaluationScore(0f);
            //解析HTML片段(獲得Document物件)
            Document document=Jsoup.parse(book.getDescription());
            //透過select選取img標籤
            Element element=document.select("img").first();
            //獲取當前元素的屬性值
            String cover=element.attr("src");
            //補全Cover屬性
            book.setCover(cover);
            bookService.createBook(book);
            map.put("code","0");
            map.put("msg","success");
        }catch (BussinessException bussinessException){
            bussinessException.printStackTrace();
            map.put("code",bussinessException.getCode());
            map.put("msg",bussinessException.getMsg());
        }
        return map;
    }

    /**
     * 處理Lay-UI的List中，所需的分業務物件
     * @param page 要查詢的頁數
     * @param limit 每頁物件的數量
     * @return 處理結果+分頁中的物件+查詢的總數量
     */
    @GetMapping("/list")
    @ResponseBody
    public Map list(Integer page,Integer limit){
        if(page==null){
            page=1;
        }
        if(limit==null){
            limit=10;
        }
        IPage<Book> pageObject=bookService.paging(null,null,page,limit);
        Map map=new HashMap();
        map.put("code",0);
        map.put("msg","success");
        //回傳當前分頁的資料
        map.put("data",pageObject.getRecords());
        //回傳未分頁的總數
        map.put("count",pageObject.getTotal());
        return map;
    }


    /**
     * 提供依照id查詢書本的功能
     * @param bookId 書本id
     * @return 處理結果+書本物件
     */
    @GetMapping("/id/{id}")
    @ResponseBody
    public Map selectById(@PathVariable("id") Long bookId){
        Map map=new HashMap();
        Book book=bookService.selectById(bookId);
        map.put("code","0");
        map.put("msg","success");
        //將查詢到的book放入JSON的data中
        map.put("data",book);
        return map;
    }

    /**
     * 依照書本id更新書本
     * @param book 書本物件
     * @return 處理結果
     */
    @PostMapping("/update")
    @ResponseBody
    public Map updateBook(Book book){
        Map map=new HashMap();
        try{
            //先從資料庫撈取Book物件
            Book rawBook=bookService.selectById(book.getBookId());
            //將要更新的內容更新到rawBook中
            rawBook.setBookName(book.getBookName());
            rawBook.setSubTitle(book.getSubTitle());
            rawBook.setAuthor(book.getAuthor());
            rawBook.setCategoryId(book.getCategoryId());
            rawBook.setDescription(book.getDescription());
            //如果描述發生變化，同時必須更新cover
            Document document=Jsoup.parse(book.getDescription());
            String cover=document.select("img").first().attr("src");
            rawBook.setCover(cover);
            //更新資料
            bookService.updateBook(rawBook);
            map.put("code","0");
            map.put("msg","success");
        }catch (BussinessException bussinessException){
            map.put("code",bussinessException.getCode());
            map.put("msg",bussinessException.getMsg());
        }
        return map;
    }

    /**
     * 依照bookId刪除書本與相關資訊
     * @param bookId 要刪除的bookId
     * @return 處理結果
     */
    @GetMapping("/delete/{id}")
    @ResponseBody
    public Map deleteBook(@PathVariable("id") Long bookId){
        Map map=new HashMap();
        try{
           bookService.deleteBookById(bookId);
            map.put("code","0");
            map.put("msg","success");
        }catch (BussinessException bussinessException){
            map.put("code",bussinessException.getCode());
            map.put("msg",bussinessException.getMsg());
        }
        return map;
    }
}
