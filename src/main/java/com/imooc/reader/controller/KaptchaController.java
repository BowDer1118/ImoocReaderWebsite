package com.imooc.reader.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class KaptchaController {
    @Resource(name = "kaptchaProducer") //注入applicationContext中配置的bean
    private Producer kaptchaProducer;

    /**
     * 生成驗證碼圖片
     * @param request 客戶端的請求(由Spring IoC自動注入)
     * @param response 伺服器產生的響應(由Spring IoC自動注入)
     */
    @GetMapping("/verify_code")
    public void createVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*通知瀏覽器，對於伺服器的Response圖片不可緩存*/
        //設定response的過期時間:立即過期
        response.setDateHeader("Expires",0);
        //設定緩存策略:因為要產生新的驗證碼，所以指定不存儲、不緩存，且一定要重新驗證
        response.setHeader("Cache-Control","no-store,no-cache,must-revalidate");
        //用於IE5.0以後的指令:一樣用於指定緩存策略(兼容性問題)
        response.setHeader("Cache-Control","post-check=0,pre-check=0");
        response.setHeader("Pragma","no-cache");
        /*設定response為一張png圖片*/
        response.setContentType("image/png");
        //生成驗證碼字串
        String verifyCode=kaptchaProducer.createText();
        //將驗證碼保存到Session中
        request.getSession().setAttribute("kaptchaVerifyCode",verifyCode);
        System.out.println("驗證碼:"+request.getSession().getAttribute("kaptchaVerifyCode"));
        //依照驗證碼創建驗證碼圖片(為一張binary的圖片)
        BufferedImage image=kaptchaProducer.createImage(verifyCode);
        //使用OutputStream輸出binary資料(需要拋出異常)
        ServletOutputStream out=response.getOutputStream();
        //使用ImageIO將圖片寫入到輸出流中，發給客戶端
        ImageIO.write(image,"png",out);
        //立即輸出圖片
        out.flush();
        //關閉輸出流
        out.close();
    }
}
