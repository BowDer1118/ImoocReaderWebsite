package com.imooc.reader.controller;


import com.imooc.reader.entity.Evaluation;
import com.imooc.reader.entity.Member;
import com.imooc.reader.service.MemberService;
import com.imooc.reader.service.exception.BussinessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

//處理用戶註冊與用戶登入
@Controller
public class MemberController {
    @Resource
    private MemberService memberService;

    @GetMapping("/register.html")
    public ModelAndView showRegister(){
        ModelAndView modelAndView=new ModelAndView("/register");
        return modelAndView;
    }


    /**
     * 接收來自前台的註冊請求
     * @param vc 驗證碼
     * @param username 用戶名稱
     * @param password 用戶密碼
     * @param nickname 用戶暱稱
     * @param request 用戶的request(會由Spring自動注入)
     * @return JSON物件:表示驗證碼驗證結果和註冊結果
     */
    @PostMapping("/registe")
    @ResponseBody
    public Map register(String vc, String username, String password, String nickname, HttpServletRequest request){
        Map map=new HashMap();
        //正確的驗證碼
        String verifyCode=(String)request.getSession().getAttribute("kaptchaVerifyCode");
        //比對驗證碼
        //如果沒有給予驗證碼或正確的驗證碼
        if(vc==null||verifyCode==null||!vc.equalsIgnoreCase(verifyCode)){ //比對失敗
            map.put("code","VC01"); //VC01代表驗證碼1號錯誤
            map.put("msg","驗證碼錯誤"); //輸出錯誤訊息
            return map;
        }
        //執行會員創建工作
        try{
            memberService.createMember(username, password, nickname);
            map.put("code","0");
            map.put("msg","success");
        }catch (BussinessException bussinessException){
            bussinessException.printStackTrace();
            map.put("code",bussinessException.getCode());
            map.put("msg",bussinessException.getMsg());
        }
        return map;
    }

    @GetMapping("/login.html")
    public ModelAndView showLogin(){
        return new ModelAndView("/login");
    }

    /**
     * 負責會員登入處理
     * @param username 用戶名稱
     * @param password 用戶密碼
     * @param vc 驗證碼
     * @param session 當前request的session物件(由Spring自動注入)
     * @return JSON物件:表示會員登入結果
     */
    @PostMapping("/check_login")
    @ResponseBody
    public Map checkLogin(String username, String password, String vc, HttpSession session){
        Map map=new HashMap();
        //正確的驗證碼
        String verifyCode=(String)session.getAttribute("kaptchaVerifyCode");
        //比對驗證碼
        if(vc==null||verifyCode==null||!vc.equalsIgnoreCase(verifyCode)){ //比對失敗
            map.put("code","VC01"); //VC01代表驗證碼1號錯誤
            map.put("msg","驗證碼錯誤"); //輸出錯誤訊息
            return map;
        }
        //執行登入
        try{
            Member member=memberService.checkLogin(username, password);
            map.put("code","0");
            map.put("msg","success");
            session.setAttribute("loginMember",member);
        }catch (BussinessException bussinessException){
            bussinessException.printStackTrace();
            map.put("code",bussinessException.getCode());
            map.put("msg",bussinessException.getMsg());
        }
        return map;
    }

    /**
     * 更新會員的閱讀紀錄
     * @param memberId 會員id
     * @param bookId 書本id
     * @param readState 閱讀狀態的編號
     * @return 處理結果
     */
    @PostMapping("/update_read_state")
    @ResponseBody
    public Map updateReadState(Long memberId,Long bookId,Integer readState){
        Map map=new HashMap();
        try{
            memberService.updateMemberReadState(memberId,bookId,readState);
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
     * 負責處理發布評論的Post Request
     * @param memberId 會員id
     * @param bookId 書本id
     * @param score 評論分數
     * @param content 評論內容
     * @return 處理結果
     */
    @PostMapping("/evaluate")
    @ResponseBody
    public Map evaluate(Long memberId,Long bookId,Integer score,String content){
        Map map=new HashMap();
        try{
            memberService.evaluate(memberId, bookId, score, content);
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
     * 負責更新短評的點讚數量
     * @param evaluationId 評論的id
     * @return 處理結果+更新後的評論物件
     */
    @PostMapping("/enjoy")
    @ResponseBody
    public Map enjoy(Long evaluationId){
        Map map=new HashMap();
        try{
            Evaluation evaluation=memberService.enjoy(evaluationId);
            map.put("code","0");
            map.put("msg","success");
            map.put("evaluation",evaluation);
        }catch (BussinessException bussinessException){
            bussinessException.printStackTrace();
            map.put("code",bussinessException.getCode());
            map.put("msg",bussinessException.getMsg());
        }
        return map;
    }
}
