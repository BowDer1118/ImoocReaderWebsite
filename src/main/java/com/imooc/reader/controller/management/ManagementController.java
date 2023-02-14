package com.imooc.reader.controller.management;

import com.imooc.reader.entity.User;
import com.imooc.reader.service.UserService;
import com.imooc.reader.service.utils.MD5Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 後臺系統的控制器
 */
@Controller
@RequestMapping("/management")
public class ManagementController {
    @Resource
    private UserService userService;

    /**
     * 檢查Session是否有管理員資訊
     * 顯示後臺管理首頁或登入頁面
     *
     * @param request 用來檢查Session中是否有管理員資訊
     * @return 顯示後臺管理首頁(或登入頁面)
     */
    @GetMapping("/index.html")
    public ModelAndView showIndex(HttpServletRequest request){
        //檢查Session是否有管理員資料
        User user=(User)request.getSession().getAttribute("user");
        //沒有管理員資料:強制跳轉到登入畫面
        if(user==null){
            return new ModelAndView("/management/login");
        }
        return new ModelAndView("/management/index");
    }

    /**
     * 顯示後台系統的登入畫面
     * @return 顯示後臺系統的登入畫面
     */
    @GetMapping("/login")
    public ModelAndView showLogin(){
        return new ModelAndView("/management/login");
    }

    /**
     * 執行後台的登錄檢查
     * @param username 管理員名稱
     * @param password 管理員密碼
     * @param request 使用Session
     * @return 登陸檢查結果+登陸成功後的跳轉地址
     */
    @PostMapping("/check_login")
    @ResponseBody
    public Map checkLogin(String username, String password, HttpServletRequest request){
        Map map=new HashMap();
        //比對資料庫中的管理員資訊
        User user= userService.selectUserByUsername(username);
        //比對管理員名稱
        if(user==null){ //管理員不存在
            map.put("code","ADMIN_NOT_FOUND");
            map.put("message","管理員名稱不存在");
            return map;
        }
        //比對管理員密碼
        String md5= MD5Utils.md5Digest(password,user.getSalt());
        //密碼比對失敗
        if(!md5.equals(user.getPassword())){
            map.put("code","PASSWORD_ERROR");
            map.put("message","管理員密碼不正確");
            return map;
        }
        //登錄成功
        //保存管理員資料到Session中
        request.getSession().setAttribute("user",user);
        //輸出處理結果
        map.put("code","0");
        map.put("message","success");
        //輸出管理頁面的跳轉地址
        map.put("redirect_url","/management/index.html");
        return map;
    }
}
