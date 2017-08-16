package com.biuxx.utils.security.cipher.holder;

public class RSAPfxFileHolder extends RSAFileHolderAdapter {

	private String password;

	public RSAPfxFileHolder(String password) {
		if (password == null || "".equals(password)) {
			throw new IllegalArgumentException("passord cannot be null");
		}
		this.password = password;
	}

	@Override
	public String getPfxPassword() {
		return password;
	}

}
