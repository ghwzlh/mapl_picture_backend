package com.ghw.maplpicturebackend.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ghw.maplpicturebackend.Exception.BusinessException;
import com.ghw.maplpicturebackend.Exception.ErrorCode;
import com.ghw.maplpicturebackend.Exception.ThrowUtils;
import com.ghw.maplpicturebackend.Utils.PasswordUtils;
import com.ghw.maplpicturebackend.annotation.AuthCheck;
import com.ghw.maplpicturebackend.annotation.LogAdd;
import com.ghw.maplpicturebackend.common.BaseResponse;
import com.ghw.maplpicturebackend.common.Constant;
import com.ghw.maplpicturebackend.common.DeleteRequest;
import com.ghw.maplpicturebackend.common.ResultUtils;
import com.ghw.maplpicturebackend.model.DTO.User.*;
import com.ghw.maplpicturebackend.model.VO.User.LoginUserVO;
import com.ghw.maplpicturebackend.model.VO.User.UserVO;
import com.ghw.maplpicturebackend.model.entity.User;
import com.ghw.maplpicturebackend.model.enums.userRoleEnum;
import com.ghw.maplpicturebackend.service.SpaceService;
import com.ghw.maplpicturebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    private SpaceService spaceService;


    public UserController(UserService userService, SpaceService spaceService) {
        this.userService = userService;
        this.spaceService = spaceService;
    }

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return 用户ID
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody @Valid UserRegisterRequest userRegisterRequest ) {
        boolean present = Optional.ofNullable(userRegisterRequest).isPresent();
        if(!present) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        ThrowUtils.throwIf(!ObjectUtil.equals(userRegisterRequest.getUserPassword(), userRegisterRequest.getCheckPassword()), ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        long userId = userService.userRegister(userRegisterRequest);
        ThrowUtils.throwIf(ObjectUtil.isNull(userId), ErrorCode.OPERATION_ERROR, "注册用户失败", "register failed");
        return ResultUtils.success(userId);
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return 用户信息
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody @Valid UserLoginRequest userLoginRequest, HttpServletRequest request) {
        boolean present = Optional.ofNullable(userLoginRequest).isPresent();
        if(!present) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LoginUserVO loginUserVO = userService.userLogin(userLoginRequest, request);
        ThrowUtils.throwIf(loginUserVO == null, ErrorCode.NOT_FOUND_ERROR, "请求数据不存在", "userLoginVO is disappear");
        // todo: 如果私有空间未创建就自动创建私有空间
        // todo: 解耦
//        List<Space> list = spaceService.lambdaQuery().eq(Space::getUserId, loginUserVO.getId()).list();
//        if(!list.isEmpty()){
//            User user = new User();
//            BeanUtils.copyProperties(Objects.requireNonNull(loginUserVO), user);
//            spaceAddRequest spaceAddRequest = new spaceAddRequest();
//            spaceAddRequest.setSpaceLevel(spaceLevelEnum.COMMON.getValue());
//            spaceAddRequest.setSpaceName(userLoginRequest.getUserAccount() + "默认空间");
//            spaceService.addSpace(spaceAddRequest, user);
//        }
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户注销
     * @param request
     * @return 用户信息
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "参数错误", "request is null");
        boolean userLogout = userService.userLogout(request);
        ThrowUtils.throwIf(!userLogout, ErrorCode.OPERATION_ERROR, "用户注销失败", "user logout failed");
        return ResultUtils.success(userLogout);
    }

    /**
     * 获取用户信息
     * @param request
     * @return 用户信息
     */
    // @AuthCheck(mustRole = Constant.USER_AUTH)
    @GetMapping("/get/login")
    @LogAdd
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "参数错误", "request is null");
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR, "用户未登录", "user is not login");
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    /**
     * 创建用户
     */
    @LogAdd
    @PostMapping("/add")
    @AuthCheck(mustRole = Constant.ADMIN_AUTH)
    public BaseResponse<Long> addUser(@RequestBody @Valid UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR, "请求参数错误", "user add param is wrong");
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        String encryptPassword = PasswordUtils.getEncryptPassage(Constant.DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "添加用户失败", "add user failed");
        return ResultUtils.success(user.getId());
    }

    /**
     * 根据 id 获取用户（仅管理员）
     */
    @LogAdd
    @GetMapping("/get")
    @AuthCheck(mustRole = Constant.ADMIN_AUTH)
    public BaseResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR, "请求参数错误", "id is wrong");
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "该用户不存在", "the user is not exist");
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     */
    @LogAdd
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 删除用户
     */
    @LogAdd
    @PostMapping("/delete")
    @AuthCheck(mustRole = Constant.ADMIN_AUTH)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        ThrowUtils.throwIf(!b, ErrorCode.OPERATION_ERROR, "删除用户失败", "Delete user failed");
        return ResultUtils.success(b);
    }

    /**
     * 更新用户(仅管理员和自己)
     */
    @LogAdd
    @PostMapping("/update")
    @AuthCheck(mustRole = Constant.USER_AUTH)
    public BaseResponse<Boolean> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userUpdateRequest == null, ErrorCode.PARAMS_ERROR, "请求参数错误", "user update is null");
        User loginUser = userService.getLoginUser(request);
        // 只有本人或管理员才可以修改
        ThrowUtils.throwIf(!loginUser.getId().equals(Objects.requireNonNull(userUpdateRequest).getId()) || !userRoleEnum.ADMIN.equals(userRoleEnum.getEnumByValue(userUpdateRequest.getUserRole())), ErrorCode.NO_AUTH_ERROR, "你无权限修改", "no auth request");
        User user = new User();
        BeanUtils.copyProperties(Objects.requireNonNull(userUpdateRequest), user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新用户数据操作失败", "update user failed");
        return ResultUtils.success(true);
    }

    /**
     * 分页获取用户封装列表（仅管理员）
     *
     * @param userQueryRequest 查询请求参数
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = Constant.ADMIN_AUTH)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR, "请求参数错误", "query param is wrong");
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, pageSize),
                userService.getUserQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, pageSize, userPage.getTotal());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 分页获取用户封装列表（仅管理员且带有deleted字段）
     *
     * @param userQueryRequest 查询请求参数
     */
    @LogAdd
    @PostMapping("/list/page/inner/vo")
    @AuthCheck(mustRole = Constant.ADMIN_AUTH)
    public BaseResponse<Page<UserVO>> listUserHasDeletedVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR, "请求参数错误", "query param is wrong");
        Page<UserVO> userVOPage = userService.pageSelf(userQueryRequest);
        return ResultUtils.success(userVOPage);
    }

}