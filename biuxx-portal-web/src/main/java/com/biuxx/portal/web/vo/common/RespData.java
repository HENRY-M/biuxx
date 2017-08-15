package com.biuxx.portal.web.vo.common;

import java.io.Serializable;

import com.biuxx.portal.web.constants.RespCodeConstants;

public class RespData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8452296373767750390L;

	private RespHead head;
	private Object data;

	public RespData(Object data) {
		super();
		this.head = new RespHead(RespCodeConstants.SUCCEED, "");
		this.data = data;
	}

	public RespData(RespHead head, Object data) {
		super();
		this.head = head;
		this.data = data;
	}

	public RespData(String code, String desc, Object data) {
		super();
		this.head = new RespHead(code, desc);
		this.data = data;
	}

	public RespHead getHead() {
		return head;
	}

	public void setHead(RespHead head) {
		this.head = head;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
