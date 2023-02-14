package com.imooc.reader.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.reader.entity.Test;

//使用MyBatis-Plus時，Mapper類必須繼承BaseMapper並指定要使用哪個Class進行Mapping
public interface TestMapper extends BaseMapper<Test> {
    public void insertSample();
}
