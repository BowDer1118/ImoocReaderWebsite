package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.reader.entity.User;
import com.imooc.reader.mapper.UserMapper;
import com.imooc.reader.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    /**
     * 依照管理員名稱查詢User物件
     *
     * @param username 管理員名稱
     * @return User物件
     */
    @Override
    public User selectUserByUsername(String username) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        //依照username查詢
        queryWrapper.eq("username",username);
        User user=userMapper.selectOne(queryWrapper);
        return user;
    }
}
