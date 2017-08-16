package com.biuxx.utils.security.cipher;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.biuxx.utils.security.cipher.holder.RSACerFileHolder;
import com.biuxx.utils.security.cipher.holder.RSAPfxFileHolder;
import com.biuxx.utils.security.tools.RSATool;

class SignRSACipherImpl extends AbstractSignRSACipher {

	private PublicKey pubKey = null;
	private PrivateKey priKey = null;

	SignRSACipherImpl(RSACerFileHolder pubKeyHolder, RSAPfxFileHolder priKeyHolder) throws SecurityCipherException {
		super(pubKeyHolder, priKeyHolder);

		try {
			InputStream cerInputStream = pubKeyHolder.newInputStream();
			pubKey = RSATool.getPubKeyFromCRTInputStream(cerInputStream);
			pubKeyHolder.releaseInputStream(cerInputStream);

			InputStream pfxInputStream = priKeyHolder.newInputStream();
			priKey = RSATool.getPvkformPfxByInputStream(pfxInputStream, priKeyHolder.getPfxPassword());
			priKeyHolder.releaseInputStream(pfxInputStream);
		} catch (Exception e) {
			logger.error("", e);
			throw new SecurityCipherException("Unhandled error:" + e.getMessage());
		}
	}

	@Override
	String doSignature(String paramString) throws SecurityCipherException {
		try {
			return RSATool.doSignature(paramString, priKey, CHARSET);
		} catch (Exception e) {
			throw new SecurityCipherException("Unhandled RSA Error:" + e.getMessage());
		}
	}

	@Override
	boolean verifySignature(String paramString, String signString) throws SecurityCipherException {
		try {
			return RSATool.verifySignature(paramString, signString, pubKey, CHARSET);
		} catch (Exception e) {
			throw new SecurityCipherException("Unhandled RSA Error:" + e.getMessage());
		}
	}

}
