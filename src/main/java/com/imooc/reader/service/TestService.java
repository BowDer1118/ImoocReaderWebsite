package com.imooc.reader.service;

import com.imooc.reader.mapper.TestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TestService {
    @Resource
    public TestMapper testMapper;

    @Transactional
    public void batchImport(){
        for(int i=0;i<5;i++){
//            if(i==3){
//                throw new RuntimeException("預期外異常");
//            }
            testMapper.insertSample();
        }
    }
}
