package com.biuxx.utils.security.cipher;

import com.biuxx.utils.security.cipher.holder.RSACerFileHolder;
import com.biuxx.utils.security.cipher.holder.RSAPfxFileHolder;

public final class BiuxxCipherFactory {

	public static BiuxxCipher buildRSACipher(BiuxxCipherType cipherType, RSACerFileHolder cer, RSAPfxFileHolder pfx) throws SecurityCipherException {
		
		if (cipherType == null) {
            throw new IllegalArgumentException("BiuxxCipherType cannot be null!");
        }

        if (cer == null) {
            throw new IllegalArgumentException("cer cannot be null!");
        }

        if (pfx == null) {
            throw new IllegalArgumentException("pfx cannot be null!");
        }
		
		switch(cipherType) {
		case RSA_ENCRYPT_AND_SIGN:
			return new EncryptAndSignRSACipherImpl(cer, pfx);
		case RSA_SIGN:
			return new SignRSACipherImpl(cer, pfx);
		default:
			throw new UnsupportedOperationException("Unsupported BiuxxCipherType:" + cipherType.name()); 
		}
	}
}
