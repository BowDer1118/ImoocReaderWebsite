package com.imooc.reader.service;


import com.imooc.reader.entity.User;

public interface UserService {
    /**
     * 依照管理員名稱查詢User物件
     * @param username 管理員名稱
     * @return User物件
     */
    public User selectUserByUsername(String username);
}
