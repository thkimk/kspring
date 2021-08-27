package com.kkk.sbgtest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // controller로 보내기 전에 처리하는 인터셉터
    // 반환이 false라면 controller로 요청을 안함
    // 매개변수 Object는 핸들러 정보를 의미한다. ( RequestMapping , DefaultServletHandler )
    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response,
            Object obj) throws Exception {

        MDC.put("preHandle", "value1");
        logger.warn("## TestInterceptor [preHandle()]");
        return true;
    }

    // controller의 handler가 끝나면 처리됨
    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response,
            Object obj, ModelAndView mav)
            throws Exception {
        logger.warn("## TestInterceptor [postHandle()]");
    }

    // view까지 처리가 끝난 후에 처리됨
    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response,
            Object obj, Exception e)
            throws Exception {
        logger.warn("## TestInterceptor [afterCompletion()]");
    }
}