package com.imooc.reader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {
    //測試FreeMarker模板引擎是否能使用
    @GetMapping("/test/t1")
    public ModelAndView test1(){
        //傳入的參數是指向的view也就是WEB-INF/ftl下的test.ftl
        return new ModelAndView("/test");
    }
    //測試JSON序列化輸出中文顯示是否正常
    @GetMapping("/test/t2")
    @ResponseBody
    public Map test2(){
        Map result=new HashMap();
        result.put("test","測試文本");
        return result;
    }
}
