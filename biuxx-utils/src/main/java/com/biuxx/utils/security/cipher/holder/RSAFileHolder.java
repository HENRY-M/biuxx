package com.biuxx.utils.security.cipher.holder;

import java.io.InputStream;

import com.biuxx.utils.security.cipher.SecurityCipherException;

public interface RSAFileHolder {

	void init(String keypath) throws SecurityCipherException;

	void init(byte[] keyBytes) throws SecurityCipherException;

	InputStream newInputStream();

	void releaseInputStream(InputStream keyStream);

	String getPfxPassword();
}
