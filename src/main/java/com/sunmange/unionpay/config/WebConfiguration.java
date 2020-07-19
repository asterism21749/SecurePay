package com.sunmange.unionpay.config;

import com.sunmange.unionpay.interceptor.IPInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/*import org.springframework.web.servlet.config.annotation.InterceptorRegistry;*/


@Configuration("unionpayWebConfig")
@Primary
public class WebConfiguration extends WebMvcConfigurerAdapter {

    //将自定义的拦截器定义为一个bean
    @Bean
    public HandlerInterceptor getMyInterceptor() {
        return new IPInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则, 这里假设拦截 /** 后面的全部链接
        // excludePathPatterns 用户排除拦截
        List<String> patterns = new ArrayList<String>();
        patterns.add("/securepay/purchase");
        registry.addInterceptor(getMyInterceptor()).addPathPatterns(patterns);
        super.addInterceptors(registry);
    }

}
