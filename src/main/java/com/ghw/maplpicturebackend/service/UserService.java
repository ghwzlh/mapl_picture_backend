package com.ghw.maplpicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ghw.maplpicturebackend.model.DTO.User.UserLoginRequest;
import com.ghw.maplpicturebackend.model.DTO.User.UserQueryRequest;
import com.ghw.maplpicturebackend.model.DTO.User.UserRegisterRequest;
import com.ghw.maplpicturebackend.model.VO.User.LoginUserVO;
import com.ghw.maplpicturebackend.model.VO.User.UserVO;
import com.ghw.maplpicturebackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author lenovo
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-01-25 21:30:35
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @param userLoginRequest
     * @return
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 用户注销
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏用户信息
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取当前用户信息
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取脱敏后的用户信息
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏后的用户列表
     * @param userList
     * @return
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 获取查询时的QueryWrapper
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getUserQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 自己实现分页（带有deleted字段）
     */
    Page<UserVO> pageSelf(UserQueryRequest userQueryRequest);

    /**
     * 判断是否是admin
     * @param user
     * @return
     */
    boolean isAdmin(User user);
}
