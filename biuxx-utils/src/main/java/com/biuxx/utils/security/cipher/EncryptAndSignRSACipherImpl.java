package com.biuxx.utils.security.cipher;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.biuxx.utils.security.cipher.holder.RSACerFileHolder;
import com.biuxx.utils.security.cipher.holder.RSAPfxFileHolder;
import com.biuxx.utils.security.tools.AESTool;
import com.biuxx.utils.security.tools.RSATool;

class EncryptAndSignRSACipherImpl extends AbstractEncryptAndSignRSACipher {

    private PublicKey pubKey = null;
    private PrivateKey priKey = null;
    
    EncryptAndSignRSACipherImpl(RSACerFileHolder pubKeyHolder, RSAPfxFileHolder priKeyHolder) throws SecurityCipherException {
        super(pubKeyHolder, priKeyHolder);
        
        try {
            InputStream cerInputStream = pubKeyHolder.newInputStream();
            pubKey = RSATool.getPubKeyFromCRTInputStream(cerInputStream);
            pubKeyHolder.releaseInputStream(cerInputStream);
            
            InputStream pfxInputStream = priKeyHolder.newInputStream();
            priKey = RSATool.getPvkformPfxByInputStream(pfxInputStream, priKeyHolder.getPfxPassword());
            priKeyHolder.releaseInputStream(pfxInputStream);
        }
        catch(Exception e) {
            logger.error("", e);
            throw new SecurityCipherException("Unhandled error:" + e.getMessage());
        }
    }

    @Override
    String encryptWithAES(String param, String key) throws SecurityCipherException {
        try {
            return AESTool.encryptToBase64(param, key);
        } catch (Throwable e) {
            throw new SecurityCipherException("Unhandled RSA Error:" + e.getMessage());
        }
    }

    @Override
    String decryptWithAES(String param, String key) throws SecurityCipherException {
        try {
            return AESTool.decryptFromBase64(param, key);
        } catch (Throwable e) {
            throw new SecurityCipherException("Unhandled RSA Error:" + e.getMessage());
        }
    }

    @Override
    String encryptWithRSA(String param) throws SecurityCipherException {
        try {
            return RSATool.encrypt(pubKey, param, CHARSET);
        } catch (Throwable e) {
            throw new SecurityCipherException("Unhandled RSA Error:" + e.getMessage());
        }
    }

    @Override
    String decryptWithRSA(String param) throws SecurityCipherException {
        try {
            return RSATool.decrypt(priKey, param, CHARSET);
        } catch (Throwable e) {
            throw new SecurityCipherException("Unhandled RSA Error:" + e.getMessage());
        }
    }

    @Override
    String doSignature(String param) throws SecurityCipherException {
        try {
            return RSATool.doSignature(param, priKey, CHARSET);
        } catch (Throwable e) {
            throw new SecurityCipherException("Unhandled RSA Error:" + e.getMessage());
        }
    }

    @Override
    boolean verifySignature(String param, String signString) throws SecurityCipherException {
        try {
            return RSATool.verifySignature(param, signString, pubKey, CHARSET);
        } catch (Throwable e) {
            throw new SecurityCipherException("Unhandled RSA Error:" + e.getMessage());
        }
    }

}
