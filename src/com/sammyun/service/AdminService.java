/*
 * Copyright 2012-2014 sammyun.com.cn. All rights reserved.
 * Support: http://www.sammyun.com.cn
 * License: http://www.sammyun.com.cn/license
 */
package com.sammyun.service;

import java.util.List;

import com.sammyun.entity.Admin;
import com.sammyun.entity.dict.DictSchool;

/**
 * Service - 管理员
 * 
 * @author Sencloud Team

 */
public interface AdminService extends BaseService<Admin, Long>
{

    /**
     * 判断用户名是否存在
     * 
     * @param username 用户名(忽略大小写)
     * @return 用户名是否存在
     */
    boolean usernameExists(String username);

    /**
     * 根据用户名查找管理员
     * 
     * @param username 用户名(忽略大小写)
     * @return 管理员，若不存在则返回null
     */
    Admin findByUsername(String username);

    /**
     * 根据ID查找权限
     * 
     * @param id ID
     * @return 权限,若不存在则返回null
     */
    List<String> findAuthorities(Long id);

    /**
     * 判断管理员是否登录
     * 
     * @return 管理员是否登录
     */
    boolean isAuthenticated();

    /**
     * 获取当前登录管理员
     * 
     * @return 当前登录管理员,若不存在则返回null
     */
    Admin getCurrent();

    /**
     * 获取当前登录用户名
     * 
     * @return 当前登录用户名,若不存在则返回null
     */
    String getCurrentUsername();

    /**
     * 获取当前用户相关学校
     * 
     * @param admin
     * @return DictShcool
     */
    DictSchool getSchoolByAdmin(Admin admin);

    /**
     * 获取当前管理员所在学校
     * 
     * @return 当前登录管理员,若不存在则返回null
     */
    DictSchool getCurrentDictSchool();
}
