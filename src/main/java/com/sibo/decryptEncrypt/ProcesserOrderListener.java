package com.sibo.decryptEncrypt;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProcesserOrderListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(event.getApplicationContext().getParent() == null) {

            RequestMappingHandlerAdapter requestMappingHandlerAdapter = (RequestMappingHandlerAdapter)event.getApplicationContext().getBean("requestMappingHandlerAdapter");

            int requestResponseBodyMethodProcessorResolverIndex = 0;
            List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>(requestMappingHandlerAdapter.getArgumentResolvers());
            for(int i=0;i<resolvers.size();i++) {
                if(resolvers.get(i) instanceof RequestResponseBodyMethodProcessor
                        && !(resolvers.get(i) instanceof DecryptEncryptMVCMethodProcessor)) {
                    requestResponseBodyMethodProcessorResolverIndex = i;
                    continue;
                }
                if(resolvers.get(i) instanceof DecryptEncryptMVCMethodProcessor) {
                    if(requestResponseBodyMethodProcessorResolverIndex < i) {
                        HandlerMethodArgumentResolver requestResponseBodyMethodProcessor = resolvers.get(requestResponseBodyMethodProcessorResolverIndex);
                        resolvers.set(requestResponseBodyMethodProcessorResolverIndex,resolvers.get(i));
                        resolvers.set(i,requestResponseBodyMethodProcessor);
                    }
                }
            }
            requestMappingHandlerAdapter.setArgumentResolvers(resolvers);
            int requestResponseBodyMethodProcessorHandlerIndex = 0;
            List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(requestMappingHandlerAdapter.getReturnValueHandlers());
            for(int i=0;i<handlers.size();i++) {
                if(handlers.get(i) instanceof RequestResponseBodyMethodProcessor
                && !(handlers.get(i) instanceof DecryptEncryptMVCMethodProcessor)) {
                    requestResponseBodyMethodProcessorHandlerIndex = i;
                    continue;
                }
                if(handlers.get(i) instanceof DecryptEncryptMVCMethodProcessor) {
                    if(requestResponseBodyMethodProcessorHandlerIndex < i) {
                        HandlerMethodReturnValueHandler requestResponseBodyMethodProcessor = handlers.get(requestResponseBodyMethodProcessorHandlerIndex);
                        handlers.set(requestResponseBodyMethodProcessorHandlerIndex,handlers.get(i));
                        handlers.set(i,requestResponseBodyMethodProcessor);
                    }
                }
            }
            requestMappingHandlerAdapter.setReturnValueHandlers(handlers);
        }
    }
}