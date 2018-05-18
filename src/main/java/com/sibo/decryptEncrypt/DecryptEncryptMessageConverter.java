package com.sibo.decryptEncrypt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class DecryptEncryptMessageConverter extends FastJsonHttpMessageConverter implements InitializingBean {

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream in = inputMessage.getBody();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String str = result.toString(StandardCharsets.UTF_8.name());
        try {
            //todo 自己指定解密方法
            String DeString = decryptOperate(str);
            return JSON.parseObject(DeString,type);
        } catch (Exception e) {
            throw new HttpMessageNotReadableException("解密异常",e);
        }
    }

    @Override
    public void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        String cSrc = JSON.toJSONString(obj);
        String enString;
        try {
            //todo 自己指定加密方法
            enString = encryptOperate(cSrc);
        }catch (Exception e) {
            throw new HttpMessageNotReadableException("加密异常",e);
        }
        FastJsonConfig fastJsonConfig = getFastJsonConfig();
        HttpHeaders headers = outputMessage.getHeaders();
        ByteArrayOutputStream outnew = new ByteArrayOutputStream();
        int len = JSON.writeJSONString(outnew,
                fastJsonConfig.getCharset(), //
                enString, //
                fastJsonConfig.getSerializeConfig(), //
                fastJsonConfig.getSerializeFilters(), //
                fastJsonConfig.getDateFormat(), //
                JSON.DEFAULT_GENERATE_FEATURE, //
                fastJsonConfig.getSerializerFeatures());

        if (fastJsonConfig.isWriteContentLength()) {
            headers.setContentLength(len);
        }

        OutputStream out = outputMessage.getBody();
        outnew.writeTo(out);
        outnew.close();
    }

    @Override
    public void afterPropertiesSet() {
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        this.setSupportedMediaTypes(supportedMediaTypes);
    }


    //todo 假装解密
    private String decryptOperate(String str){
        return str.substring(0,str.length()-1);
    }
    private String encryptOperate(String str){
        return str+"0";
    }
}