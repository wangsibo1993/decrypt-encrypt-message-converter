package com.sibo.decryptEncrypt;

import com.sibo.decryptEncrypt.annotation.EncryptDecrypt;
import com.sibo.decryptEncrypt.annotation.ExcludeEncryptDecrypt;
import com.sibo.decryptEncrypt.annotation.RequestDecryptBody;
import com.sibo.decryptEncrypt.annotation.ResponseEncryptBody;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class DecryptEncryptMVCMethodProcessor extends RequestResponseBodyMethodProcessor {

    public DecryptEncryptMVCMethodProcessor(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return !parameter.hasMethodAnnotation(ExcludeEncryptDecrypt.class)
        && ((parameter.hasParameterAnnotation(RequestBody.class)
        && (parameter.hasMethodAnnotation(EncryptDecrypt.class)
        || AnnotatedElementUtils.hasAnnotation(parameter.getContainingClass(), EncryptDecrypt.class)))
        || parameter.hasParameterAnnotation(RequestDecryptBody.class));
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return !returnType.hasMethodAnnotation(ExcludeEncryptDecrypt.class)
        && ((AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseEncryptBody.class)
        || returnType.hasMethodAnnotation(ResponseEncryptBody.class))
        || ((AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), EncryptDecrypt.class)
        || returnType.hasMethodAnnotation(EncryptDecrypt.class))
        && (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class)
        || returnType.hasMethodAnnotation(ResponseBody.class))));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        mavContainer.setRequestHandled(true);
        ServletServerHttpRequest inputMessage = this.createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = this.createOutputMessage(webRequest);
        HttpHeaders httpHeaders = inputMessage.getHeaders();
        List<String> deviceIdList = httpHeaders.get("deviceId");
        if(!CollectionUtils.isEmpty(deviceIdList) && deviceIdList.size() ==1) {
            outputMessage.getHeaders().add("deviceId",deviceIdList.get(0));
        }
        this.writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
    }

    @Override
    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter methodParam, Type paramType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
        return super.readWithMessageConverters(webRequest, methodParam, paramType);
    }
}