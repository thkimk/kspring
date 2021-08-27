package com.kkk.sbgtest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class TestHttpInterceptor implements ClientHttpRequestInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        logger.warn("## ClientHttpRequestInterceptor [intercept() - 1]");

        try (ClientHttpResponse response = execution.execute(request, body)) {
            logger.warn("## ClientHttpRequestInterceptor [intercept() - 2]");
            response.getHeaders().add("Foo", "bar");
            return response;
        }
    }

}