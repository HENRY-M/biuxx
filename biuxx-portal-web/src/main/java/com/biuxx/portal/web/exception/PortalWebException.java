package com.biuxx.portal.web.exception;

public class PortalWebException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2282678771577042190L;

	private String code;
	private String desc;

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
