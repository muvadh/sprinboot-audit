package com.gridsig.audits.configuration;

import com.gridsig.audits.interceptor.RequestResponseInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestResponseInterceptor requestResponseInterceptor;

    public WebConfig(RequestResponseInterceptor requestResponseInterceptor) {
        this.requestResponseInterceptor = requestResponseInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestResponseInterceptor).addPathPatterns("/**");
    }
}
