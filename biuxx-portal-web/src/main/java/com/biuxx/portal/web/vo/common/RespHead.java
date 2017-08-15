package com.biuxx.portal.web.vo.common;

import java.io.Serializable;

public class RespHead implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3601922380962096278L;
	
	private String code;
	private String desc;

	public RespHead(String code, String desc) {
		super();
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
