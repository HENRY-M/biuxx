package com.biuxx.utils.security.cipher;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.biuxx.utils.security.cipher.holder.RSACerFileHolder;
import com.biuxx.utils.security.cipher.holder.RSAPfxFileHolder;
import com.biuxx.utils.security.tools.RandomTool;

abstract class AbstractEncryptAndSignRSACipher extends BiuxxRSACipher {

	AbstractEncryptAndSignRSACipher(RSACerFileHolder pubKeyHolder, RSAPfxFileHolder priKeyHolder) {
        super(pubKeyHolder, priKeyHolder);
    }

    @Override
    public CipherBucket enBucket(Map<String, String> params) throws SecurityCipherException {
        try {
            String jp = JSON.toJSONString(params);
            logger.debug("Param Json String:[{}]", jp);

            String key = RandomTool.getRandom(16);
            String data = encryptWithAES(jp, key);
            logger.debug("AES Encrypt: data=[{}], key=[{}]", data, key);

            String salt = encryptWithRSA(key);
            logger.debug("AESKEY Encrypt With RSA, Encrypt result=[{}]", salt);

            String sign = doSignature(salt);
            logger.debug("AESKEY Sign With RSA,sign result=[{}]", sign);

            return new CipherBucket(data, salt, sign);
        } catch (SecurityCipherException ce) {
            logger.error("", ce);
            throw ce;
        } catch (Exception e) {
            logger.error("", e);
            throw new SecurityCipherException("Unhandled enBucket Exception:" + e.getMessage());
        }
    }

    @Override
    public Map<String, String> deBucket(CipherBucket packet) throws SecurityCipherException {
        try {
            logger.debug("deBucket Started!, packet = [{}]", packet);

            boolean verified = this.verifySignature(packet.getSalt(), packet.getSign());
            logger.debug("Verify Result, result=[{}]", verified);
            if (!verified) {
                throw new SecurityCipherException("Unverified signature!");
            }

            String key = this.decryptWithRSA(packet.getSalt());
            logger.debug("Decrypt AESKey with RSA Result, result=[{}]", key);

            String jp = this.decryptWithAES(packet.getData(), key);
            logger.debug("Decrypt Data with RSA Result, result=[{}]", jp);

            return JSON.parseObject(jp, new TypeReference<Map<String, String>>() {
            });
        } catch (SecurityCipherException ce) {
            logger.error("", ce);
            throw ce;
        } catch (Exception e) {
            logger.error("", e);
            throw new SecurityCipherException("Unhandled deBucket Exception:" + e.getMessage());
        }
    }

    abstract String encryptWithAES(String param, String key) throws SecurityCipherException;

    abstract String decryptWithAES(String param, String key) throws SecurityCipherException;

    abstract String encryptWithRSA(String param) throws SecurityCipherException;

    abstract String decryptWithRSA(String param) throws SecurityCipherException;

    abstract String doSignature(String param) throws SecurityCipherException;

    abstract boolean verifySignature(String param, String signString) throws SecurityCipherException;
}
