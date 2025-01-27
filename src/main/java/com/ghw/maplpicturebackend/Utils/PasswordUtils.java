package com.ghw.maplpicturebackend.Utils;

import com.ghw.maplpicturebackend.common.Constant;
import org.springframework.util.DigestUtils;

/**
 * 密码工具类
 */
public class PasswordUtils {

    /**
     * 加密
     * @param userPassage
     * @return
     */
    public static String getEncryptPassage(String userPassage) {
        return DigestUtils.md5DigestAsHex((Constant.SALT + userPassage).getBytes());
    }


}
