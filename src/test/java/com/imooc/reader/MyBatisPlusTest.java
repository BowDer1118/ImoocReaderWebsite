package com.imooc.reader;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.reader.entity.Test;
import com.imooc.reader.mapper.TestMapper;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MyBatisPlusTest {
    @Resource
    private TestMapper testMapper;

    @org.junit.Test
    //插入一個自訂的物件
    public void testInsert(){
        Test test=new Test();
        //只需要設定Content，id會由MyBatis-Plus進行自動增加的處理
        test.setContent("MyBatis-Plus插入測試");
        //使用繼承自BaseMapper的insert方法，插入我們指定的泛型的物件
        testMapper.insert(test);
    }

    @org.junit.Test
    public void testUpdate(){
        //使用selectById查找物件
        Test test=testMapper.selectById(59);
        test.setContent("更新後的字串");
        //更新物件
        testMapper.updateById(test);
    }

    @org.junit.Test
    public void testDelete(){
        int count=testMapper.deleteById(59);
        System.out.println("影響了"+count+"筆資料");
    }

    @org.junit.Test
    public void testSelect(){
        //查詢條件包裝器:用於設定查詢的條件
        QueryWrapper<Test> queryWrapper=new QueryWrapper<>();
        //eq是等於比較:會依照資料庫中的欄位進行比對(意思是:where id=54)
//        queryWrapper.eq("id",54);
        //gt是大於的意思
//        queryWrapper.gt("id",5);
        //可以多條件查詢
        //id>5且id<100
        queryWrapper.gt("id",5);
        queryWrapper.lt("id",100);
        //查詢多個物件
        List<Test> list=testMapper.selectList(queryWrapper);
        System.out.println("List中的元素:"+list);

    }
}
