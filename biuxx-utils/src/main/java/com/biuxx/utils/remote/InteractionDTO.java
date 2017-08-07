package com.biuxx.utils.remote;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class InteractionDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6131815926294717585L;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
