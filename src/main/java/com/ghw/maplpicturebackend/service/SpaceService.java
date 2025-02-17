package com.ghw.maplpicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ghw.maplpicturebackend.model.DTO.picture.PictureQueryRequest;
import com.ghw.maplpicturebackend.model.DTO.space.spaceAddRequest;
import com.ghw.maplpicturebackend.model.DTO.space.spaceQueryRequest;
import com.ghw.maplpicturebackend.model.VO.picture.PictureVO;
import com.ghw.maplpicturebackend.model.VO.space.SpaceVO;
import com.ghw.maplpicturebackend.model.entity.Picture;
import com.ghw.maplpicturebackend.model.entity.Space;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ghw.maplpicturebackend.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author lenovo
* @description 针对表【space(空间)】的数据库操作Service
* @createDate 2025-02-13 10:46:05
*/
public interface SpaceService extends IService<Space> {

    /**
     * 新建空间
     * @param spaceAddRequest
     * @param loginUser
     * @return
     */
    long addSpace(spaceAddRequest spaceAddRequest, User loginUser);


    /**
     * 检验参数
     * @param space
     * @param add
     */
    void validSpace(Space space, boolean add);

    /**
     * 根据空间级别，自动填充限额
     * @param space
     */
    void fillSpaceBySpaceLevel(Space space);

    /**
     * 构造查询语句
     *
     * @param spaceQueryRequest
     * @return
     */
    QueryWrapper<Space> getQueryWrapper(spaceQueryRequest spaceQueryRequest);

    /**
     * 获取空间封装类
     *
     * @param space
     * @param request
     * @return
     */
    SpaceVO getSpaceVO(Space space, HttpServletRequest request);

    /**
     * 分页获取空间封装
     */
    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request);

    /**
     * 检验空间权限
     * @param loginUser
     * @param space
     */
    void checkSpaceAuth(User loginUser, Space space);
}
