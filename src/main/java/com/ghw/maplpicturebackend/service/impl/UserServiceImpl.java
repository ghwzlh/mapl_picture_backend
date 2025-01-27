package com.ghw.maplpicturebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghw.maplpicturebackend.Exception.ErrorCode;
import com.ghw.maplpicturebackend.Exception.ThrowUtils;
import com.ghw.maplpicturebackend.Utils.PasswordUtils;
import com.ghw.maplpicturebackend.common.Constant;
import com.ghw.maplpicturebackend.model.DTO.User.UserLoginRequest;
import com.ghw.maplpicturebackend.model.DTO.User.UserQueryRequest;
import com.ghw.maplpicturebackend.model.DTO.User.UserRegisterRequest;
import com.ghw.maplpicturebackend.model.VO.User.LoginUserVO;
import com.ghw.maplpicturebackend.model.VO.User.UserVO;
import com.ghw.maplpicturebackend.model.entity.User;
import com.ghw.maplpicturebackend.model.enums.userRoleEnum;
import com.ghw.maplpicturebackend.service.UserService;
import com.ghw.maplpicturebackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
* @author lenovo
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-01-25 21:30:35
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
         String userAccount = userRegisterRequest.getUserAccount();
         String userPassword = userRegisterRequest.getUserPassword();
        // 校验参数
        // 已在controller层进行校验
        // 检查是否重复
        QueryWrapper<User> userQueryWrapperForDistinct = new QueryWrapper<>();
        userQueryWrapperForDistinct.eq("userAccount", userAccount);
        Long count = this.baseMapper.selectCount(userQueryWrapperForDistinct);
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "该账号已存在，请重新输入", "this userAccount already appear");
        // 密码加密加盐
        String encryptPassage = PasswordUtils.getEncryptPassage(userPassword);
        // 插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassage);
        user.setUserName(RandomUtil.randomString(10));
        user.setUserAvatar(Constant.AVATAR);
        user.setUserProfile("这个用户太懒，这里什么都没有");
        user.setShareCode(RandomUtil.randomNumbers(20));
        user.setUserRole(userRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "注册失败" , "System error, register failed");
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 校验信息
        // 已在controller层进行校验
        // 用户密码加密
        String userPassword = userLoginRequest.getUserPassword();
        String userAccount = userLoginRequest.getUserAccount();
        String encryptPassage = PasswordUtils.getEncryptPassage(userPassword);
        // 查询用户是否存在
        QueryWrapper<User> UserQueryWrapperForPresent = new QueryWrapper<>();
        UserQueryWrapperForPresent.eq("userAccount", userAccount);
        UserQueryWrapperForPresent.eq("userPassword", encryptPassage);
        User user = this.baseMapper.selectOne(UserQueryWrapperForPresent);
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "用户不存在或密码错误", "user Login failed, userAccount can not match userPassage");
        // 记录用户登录态
        HttpSession session = request.getSession();
        session.setAttribute(Constant.USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(Constant.USER_LOGIN_STATE);
        ThrowUtils.throwIf(attribute == null, ErrorCode.NOT_LOGIN_ERROR, "用户未登录", "user logout failed user not login");
        request.getSession().removeAttribute(Constant.USER_LOGIN_STATE);
        return true;
    }

    /**
     * 用户脱敏
     * @param user
     * @return
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "用户不存在", "user Login failed, userLoginVO can not created");
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    /**
     * 获取登录用户
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(Constant.USER_LOGIN_STATE);
        ThrowUtils.throwIf(attribute == null, ErrorCode.NOT_LOGIN_ERROR, "用户未登录", "user logout failed user not login");
        User loginUser = (User) attribute;
        ThrowUtils.throwIf(loginUser == null || loginUser.getId() == null, ErrorCode.NOT_LOGIN_ERROR, "用户未登录", "Object copy failed");
        User currentUser = this.getById(loginUser.getId());
        ThrowUtils.throwIf(currentUser == null, ErrorCode.NOT_LOGIN_ERROR, "用户未登录", "user id not login");
        return currentUser;
    }

    /**
     * 获取脱敏用户信息
     * @param user
     * @return
     */
    @Override
    public UserVO getUserVO(User user) {
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "用户不存在", "userVO can not created");
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取脱敏用户信息列表
     * @param userList
     * @return
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream()
                .map(this::getUserVO)
                .collect(Collectors.toList());
    }

    /**
     * 获取查询用户时的查询条件
     * @param userQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<User> getUserQueryWrapper(UserQueryRequest userQueryRequest) {
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        Integer deleted = userQueryRequest.getDeleted();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(ObjectUtil.isNotNull(id), "id", id);
        userQueryWrapper.eq(CharSequenceUtil.isNotBlank(userRole), "userRole", userRole);
        if(deleted != null && (deleted == 1 || deleted == 0)) {
            userQueryWrapper.eq("deleted", deleted);
        }
        userQueryWrapper.like(CharSequenceUtil.isNotBlank(userAccount), "userAccount", userAccount);
        userQueryWrapper.like(CharSequenceUtil.isNotBlank(userName), "userName", userName);
        userQueryWrapper.orderBy(CharSequenceUtil.isNotEmpty(sortField), "ascend".equals(sortOrder), sortField);
        return userQueryWrapper;
    }

    @Override
    public Page<UserVO> pageSelf(UserQueryRequest userQueryRequest) {
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        Integer deleted = userQueryRequest.getDeleted();
        int current = userQueryRequest.getCurrent();
        int pageSize = userQueryRequest.getPageSize();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        List<User> userList = userMapper.pageSelf(userName, userAccount, deleted, (current - 1) * pageSize, pageSize, sortField, sortOrder);
        long total = userList.size();
        List<UserVO> userVOList = getUserVOList(userList);
        Page<UserVO> userVOPage = new Page<>(current, pageSize, total);
        userVOPage.setRecords(userVOList);
        return userVOPage;
    }
}




