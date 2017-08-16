package com.biuxx.utils.security.cipher;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class CipherBucket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6770967824174554756L;

	private String data;
	private String salt;
	private String sign;
	

	public CipherBucket(String data, String salt, String sign) {
		super();
		this.data = data;
		this.salt = salt;
		this.sign = sign;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
