package com.imooc.reader.task;

import com.imooc.reader.service.BookService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/*定義Spring Task定時任務*/
@Component
public class ComputeTask {
    @Resource
    private BookService bookService;
    //任務調度標籤:定時任務，並使用Crop表達式指定時間
    @Scheduled(cron = "0 * * * * ?")
    public void updateEvaluation(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime=simpleDateFormat.format(new Date());
        System.out.println("["+currentTime+"]執行自動更新圖書評分...");
        bookService.updateEvaluation();
        currentTime=simpleDateFormat.format(new Date());
        System.out.println("["+currentTime+"]已更新所有圖書評分");
    }
}
