package com.sibo.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.sibo.decryptEncrypt.DecryptEncryptMessageConverter;
import com.sibo.decryptEncrypt.DecryptEncryptMVCMethodProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebAppMvcConfig extends WebMvcConfigurationSupport {

    private DecryptEncryptMVCMethodProcessor decryptEncryptMVCMethodProcessor;

    @Resource
    private DecryptEncryptMessageConverter decryptEncryptMessageConverter;

    @Resource
    private FastJsonHttpMessageConverter fastJsonHttpMessageConverter;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(getRequestDecryptResponseEncryptBodyMethodProcessor());
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(getRequestDecryptResponseEncryptBodyMethodProcessor());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){
        converters.add(fastJsonHttpMessageConverter);
    }

    private DecryptEncryptMVCMethodProcessor getRequestDecryptResponseEncryptBodyMethodProcessor() {
        if(decryptEncryptMVCMethodProcessor == null) {
            List<HttpMessageConverter<?>> converters = new ArrayList<>();
            converters.add(decryptEncryptMessageConverter);
            decryptEncryptMVCMethodProcessor = new DecryptEncryptMVCMethodProcessor(converters);
        }
        return decryptEncryptMVCMethodProcessor;
    }
}
