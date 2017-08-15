package com.biuxx.gateway.web.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.biuxx.utils.log.DubboLogger;
import com.biuxx.utils.log.format.BiuxxLogFormat;

public class LogInterceptor extends HandlerInterceptorAdapter {
	private static Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String interactionUUID = UUID.randomUUID().toString().replaceAll("-", "");
		DubboLogger.registerProcessID(interactionUUID);
		logger.info(BiuxxLogFormat.Style.DEFAULT.getFormat("access url[{}] invocation[{}] version[{}] with params[{}]"),
				request.getRequestURL().toString(), interactionUUID, request.getHeader("User-Agent"),
				JSON.toJSONString(request.getParameterMap()));

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
