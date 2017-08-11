package com.biuxx.portal.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "portal")
public class IndexController extends BaseController {

	@RequestMapping(value = "name.do", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map getCurrentUserAccount(HttpServletRequest request, String merId, String transAmt, String gwType)
			throws Exception {
		
		Map map = new HashMap();
		map.put("name", "Ethan");
		return map;
	}

}
