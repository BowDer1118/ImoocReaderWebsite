package com.imooc.reader.service.impl;

import com.imooc.reader.entity.User;
import com.imooc.reader.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserServiceImplTest {
    @Resource
    private UserService userService;
    @Test
    public void selectUserByUsername() {
        User user=userService.selectUserByUsername("admin");
        System.out.println(user);
        user=userService.selectUserByUsername("admin1");
        System.out.println(user);
    }
}