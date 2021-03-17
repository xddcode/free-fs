package com.free.fs.common.shiro;

import com.free.fs.common.constant.CommonConstant;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 加密解密工具类
 *
 * @author dinghao
 * @date 2021/3/12
 */
public class EndecryptUtil {

    /**
     * md5加密(没有盐)
     *
     * @param password 要加密的字符串
     */
    public static String encrytMd5(String password) {
        return new Md5Hash(password).toHex();
    }

    /**
     * 指定盐salt进行md5加密
     *
     * @param password       要加密的字符串
     * @param salt           盐
     * @param hashIterations 散列次数
     * @return
     */
    public static String encrytMd5(String password, String salt, int hashIterations) {
        return new Md5Hash(password, salt, hashIterations).toHex();
    }

    public static String encrytMd5(String password, int hashIterations) {
        return encrytMd5(password, CommonConstant.DEFAULT_SALT, hashIterations);
    }

    public static void main(String[] args) {
        System.out.println(encrytMd5("fs", CommonConstant.DEFAULT_SALT, CommonConstant.DEFAULT_HASH_COUNT));
    }
}
