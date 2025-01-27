package com.ghw.maplpicturebackend.mapper;

import com.ghw.maplpicturebackend.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author lenovo
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2025-01-25 21:30:35
* @Entity com.ghw.maplpicturebackend.model.entity.User
*/
public interface UserMapper extends BaseMapper<User> {

    List<User> pageSelf(@Param("userName") String userName,
                        @Param("userAccount") String userAccount,
                        @Param("deleted") Integer deleted,
                        @Param("current") int current,
                        @Param("pageSize") int pageSize,
                        @Param("sortField") String sortField,
                        @Param("sortOrder") String sortOrder);
}




