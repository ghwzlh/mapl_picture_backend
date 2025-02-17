package com.ghw.maplpicturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ghw.maplpicturebackend.model.entity.Tags;
import com.ghw.maplpicturebackend.service.TagsService;
import com.ghw.maplpicturebackend.mapper.TagsMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【tags(标签)】的数据库操作Service实现
* @createDate 2025-02-12 13:51:16
*/
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags>
    implements TagsService{

}




