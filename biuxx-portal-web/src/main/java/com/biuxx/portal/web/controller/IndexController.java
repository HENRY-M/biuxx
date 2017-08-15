package com.biuxx.portal.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biuxx.portal.web.vo.common.RespData;

@Controller
@RequestMapping(value = "portal")
public class IndexController extends BaseController {

	@RequestMapping(value = "name.do", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public RespData getCurrentUserAccount(String name)
			throws Exception {
		
		Map map = new HashMap();
		map.put("Hello, ", name);
		
		RespData data = new RespData(map);
		return data;
	}

}
