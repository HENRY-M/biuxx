package com.biuxx.gateway.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "")
public class MonitorController extends BaseController {

	@RequestMapping(value = "monitor.do", method = { RequestMethod.GET })
	public void getCurrentUserAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "must-revalidate");
			response.setDateHeader("Expires", 0);
			response.getWriter().write("ok");
		} catch (IOException e) {
			logger.error("", e);
		}
	}

}
