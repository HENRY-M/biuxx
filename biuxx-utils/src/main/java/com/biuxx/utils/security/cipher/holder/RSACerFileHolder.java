package com.biuxx.utils.security.cipher.holder;

public class RSACerFileHolder extends RSAFileHolderAdapter {

	@Override
	public String getPfxPassword() {
		throw new UnsupportedOperationException("CerFileHolder do not support this method!");
	}

}
