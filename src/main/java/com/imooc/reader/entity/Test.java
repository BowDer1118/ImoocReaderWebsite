package com.imooc.reader.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

//對應資料庫的test表中的屬性(屬性名稱要與資料庫對應)
@TableName("test") //讓MyBatis-Plus知道當前Class和資料庫哪個table對應
public class Test {
    @TableId(type = IdType.AUTO) //說明當前屬性為主鍵，並使用AUTO方式自動增加編號
    @TableField("id")//讓MyBatis-Plus知道當前屬性要和table中哪個屬性對應
    private Integer id;
    @TableField("content") //如果屬性名稱與資料庫名稱相同或符合駝峰命名轉換規則，則TableField可以不寫
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
