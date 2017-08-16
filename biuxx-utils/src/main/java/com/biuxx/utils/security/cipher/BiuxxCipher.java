package com.biuxx.utils.security.cipher;

import java.util.Map;

public interface BiuxxCipher {
	
	public static final String CHARSET = "UTF-8";

	public CipherBucket enBucket(Map<String, String> data) throws SecurityCipherException;
	
	public Map<String, String> deBucket(CipherBucket data) throws SecurityCipherException;
}
