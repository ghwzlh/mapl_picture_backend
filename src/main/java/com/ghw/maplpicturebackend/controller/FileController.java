package com.ghw.maplpicturebackend.controller;

import com.ghw.maplpicturebackend.Exception.BusinessException;
import com.ghw.maplpicturebackend.Exception.ErrorCode;
import com.ghw.maplpicturebackend.annotation.AuthCheck;
import com.ghw.maplpicturebackend.common.BaseResponse;
import com.ghw.maplpicturebackend.common.Constant;
import com.ghw.maplpicturebackend.common.ResultUtils;
import com.ghw.maplpicturebackend.manage.cosManage;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private cosManage cosManage;

    public FileController(com.ghw.maplpicturebackend.manage.cosManage cosManage) {
        this.cosManage = cosManage;
    }

    /**
     * 测试文件上传
     * @param multipartFile
     * @return
     */
    @PostMapping("/test/upload")
    @AuthCheck(mustRole = Constant.ADMIN_AUTH)
    public BaseResponse<String> testUploadFile(@RequestPart("file")MultipartFile multipartFile) {
        // 指定文件存放目录
        String filename = multipartFile.getOriginalFilename();
        String filepath = String.format("/test/%s", filename);
        File file = null;
        // 上传文件
        try {
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManage.putObject(filepath, file);
            // 返回访问地址
            return ResultUtils.success(filepath);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "上传失败");
        } finally {
            if (file != null) {
                boolean delete = file.delete();
                if(!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 测试文件下载
     *
     * @param filepath 文件路径
     * @param response 响应对象
     */
    @AuthCheck(mustRole = Constant.ADMIN_AUTH)
    @GetMapping("/test/download/")
    public void testDownloadFile(String filepath, HttpServletResponse response) throws IOException {
        COSObjectInputStream cosObjectInput = null;
        try {
            COSObject cosObject = cosManage.getObject(filepath);
            cosObjectInput = cosObject.getObjectContent();
            // 处理下载到的流
            byte[] bytes = IOUtils.toByteArray(cosObjectInput);
            // 设置响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filepath);
            // 写入响应
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("file download error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载失败");
        } finally {
            if (cosObjectInput != null) {
                cosObjectInput.close();
            }
        }
    }


}
