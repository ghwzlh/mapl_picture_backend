package com.ghw.maplpicturebackend.api.imagesearch.sub;

import com.ghw.maplpicturebackend.Exception.BusinessException;
import com.ghw.maplpicturebackend.Exception.ErrorCode;
import com.ghw.maplpicturebackend.api.imagesearch.model.ImageSearchResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 程序员ghost
 * @version 1.0
 * @date 2025-02-10 09:18
 * @description :
 */@Slf4j
public class GetImageListApi {

    /**
     * 获取图片列表
     *
     * @param query 搜索关键词（中文）
     * @return 图片搜索结果列表
     */
    public static List<ImageSearchResult> getImageList(String query) {
        try {
            // 先将中文关键词翻译为英文
            PexelsImageSearch imageSearch = new PexelsImageSearch();
            String translatedQuery = imageSearch.translateChineseToEnglish(query);

            // 使用 Pexels API 搜索图片
            List<String> imageUrls = imageSearch.searchPictures(translatedQuery, 30);

            // 将图片的URL转为ImageSearchResult对象
            return convertToImageSearchResults(imageUrls);
        } catch (Exception e) {
            log.error("获取图片列表失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取图片列表失败");
        }
    }

    /**
     * 将图片URL列表转换为ImageSearchResult列表
     *
     * @param imageUrls 图片URL列表
     * @return ImageSearchResult列表
     */
    private static List<ImageSearchResult> convertToImageSearchResults(List<String> imageUrls) {
        List<ImageSearchResult> imageSearchResults = new ArrayList<>();
        for (String url : imageUrls) {
            ImageSearchResult result = new ImageSearchResult();
            // 使用Pexels提供的原图URL作为缩略图
            result.setThumbUrl(url);
            // 这里假设来源地址与缩略图相同
            result.setFromUrl(url);
            imageSearchResults.add(result);
        }
        return imageSearchResults;
    }
}



