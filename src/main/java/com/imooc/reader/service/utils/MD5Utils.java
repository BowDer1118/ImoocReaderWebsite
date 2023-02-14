package com.imooc.reader.service.utils;

import org.apache.commons.codec.digest.DigestUtils;

/*
    MD5的工具類
* */
public class MD5Utils {
    /**
     * 將密碼進行混淆處理再進行MD5加密
     * @param source 密碼明文
     * @param salt 混淆的鹽值
     * @return 加密完成的字串
     */
    public static String md5Digest(String source,Integer salt){
        //根據鹽值進行混淆
        char[] charArray=source.toCharArray();
        for(int i=0;i<charArray.length;i++){
            charArray[i]=(char)(charArray[i]+salt);
        }
        String target=new String(charArray);
        //執行MD5加密
        String md5=DigestUtils.md5Hex(target);
        return md5;
    }
}
