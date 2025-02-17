package com.ghw.maplpicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ghw.maplpicturebackend.model.DTO.spaceuser.SpaceUserAddRequest;
import com.ghw.maplpicturebackend.model.DTO.spaceuser.SpaceUserQueryRequest;
import com.ghw.maplpicturebackend.model.VO.spaceuser.SpaceUserVO;
import com.ghw.maplpicturebackend.model.entity.SpaceUser;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author lenovo
* @description 针对表【space_user(空间用户关联)】的数据库操作Service
* @createDate 2025-02-14 14:08:37
*/
public interface SpaceUserService extends IService<SpaceUser> {

    /**
     * 团队空间增加用户
     * @param spaceUserAddRequest
     * @return
     */
    long addSpaceUser(SpaceUserAddRequest spaceUserAddRequest);

    /**
     * 查询空间信息
     * @param spaceUser
     * @param request
     * @return
     */
    SpaceUserVO getSpaceUserVO(SpaceUser spaceUser, HttpServletRequest request);

    /**
     *  查询封装类列表
     * @param spaceUserList
     * @return
     */
    List<SpaceUserVO> getSpaceUserVOList(List<SpaceUser> spaceUserList);

    /**
     * 校验参数
     * @param spaceUser
     * @param add
     */
    void validSpaceUser(SpaceUser spaceUser, boolean add);

    /**
     * 拼接查询语句
     * @param spaceUserQueryRequest
     * @return
     */
    QueryWrapper<SpaceUser> getQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest);
}
