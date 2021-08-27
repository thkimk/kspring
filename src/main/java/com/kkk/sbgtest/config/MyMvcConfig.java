package com.kkk.sbgtest.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

//@Configuration
//@EnableWebMvc
public class MyMvcConfig extends WebMvcConfigurerAdapter {

//    @Bean
    public LocaleResolver localeResolver() {

        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.KOREAN);
        cookieLocaleResolver.setCookieName("APPLICATION_LOCALE");
        return cookieLocaleResolver;
    }

//    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("locale");
        return lci;
    }

//    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TestInterceptor())
                .excludePathPatterns("/css/**", "/fonts/**", "/plugin/**", "/scripts/**");

        registry.addInterceptor(localeChangeInterceptor());
//        localeChangeInterceptor.setParamName("cookie_language");
//        localeChangeInterceptor.setHttpMethods("GET");
//        registry.addInterceptor(localeChangeInterceptor);
    }

//    @Bean
    public FilterRegistrationBean getFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new TestFilter());
        registrationBean.setOrder(Integer.MIN_VALUE);
        registrationBean.addUrlPatterns("/*");
        //registrationBean.setUrlPatterns(Arrays.asList("/board/*"));
        return registrationBean;
    }

}
