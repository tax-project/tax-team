package com.dkm.user.utils;

import com.alibaba.fastjson.JSONObject;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author: HuangJie
 * @Date: 2020/3/30 15:33
 * @Version: 1.0V
 */
public class BodyUtils {

    public static JSONObject bodyJson(HttpServletRequest request) {
        try {
            ServletInputStream inputStream = request.getInputStream();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] temp = new byte[1024 ];
            int read;
            while ((read=inputStream.read(temp))!=-1){
                stream.write(temp,0,read);
            }
            inputStream.close();
            stream.close();
            return JSONObject.parseObject(stream.toString());
        } catch (IOException e) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"请求Body内的数据获取异常");
        }
    }
}
