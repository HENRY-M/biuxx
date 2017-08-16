package com.biuxx.utils.security.cipher;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.biuxx.utils.security.cipher.holder.RSACerFileHolder;
import com.biuxx.utils.security.cipher.holder.RSAPfxFileHolder;

abstract class AbstractSignRSACipher extends BiuxxRSACipher {

	AbstractSignRSACipher(RSACerFileHolder pubKeyHolder, RSAPfxFileHolder priKeyHolder) {
        super(pubKeyHolder, priKeyHolder);
    }

    @Override
    public CipherBucket enBucket(Map<String, String> params) throws SecurityCipherException {
        try {
            logger.debug("params={}", params);
            String data = new String(Base64.encodeBase64(JSON.toJSONString(params).getBytes(CHARSET)), CHARSET);

            String md5Input = buildMd5Params(params);
            logger.debug("md5Input={}", md5Input);
            String md5String = DigestUtils.md5Hex(md5Input);

            long timestamp = System.currentTimeMillis();
            String signInput = buildSignParams(md5String, timestamp);
            String sign = this.doSignature(signInput);
            return new CipherBucket(data, String.valueOf(timestamp), sign);
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
            logger.debug("packet = {}", packet);
            String data = new String(Base64.decodeBase64(packet.getData()), CHARSET);

            Map<String, String> params = JSON.parseObject(data, new TypeReference<Map<String, String>>() {
            });
            logger.debug("params={}", params);
            String md5Input = buildMd5Params(params);
            logger.debug("md5Input={}", md5Input);
            String md5String = DigestUtils.md5Hex(md5Input);
            long timestamp = Long.parseLong(packet.getSalt());
            String signInput = buildSignParams(md5String, timestamp);

            boolean verified = this.verifySignature(signInput, packet.getSign());
            logger.debug("verified={}", verified);
            if (!verified) {
                throw new SecurityCipherException("Unverified signature!");
            }
            return params;
        } catch (SecurityCipherException ce) {
            logger.error("", ce);
            throw ce;
        } catch (Exception e) {
            logger.error("", e);
            throw new SecurityCipherException("Unhandled deBucket Exception:" + e.getMessage());
        }
    }

    abstract String doSignature(String paramString) throws SecurityCipherException;

    abstract boolean verifySignature(String paramString, String signString) throws SecurityCipherException;

    private String buildSignParams(String md5String, long timestamp) {
        return md5String + "@" + timestamp;
    }

    private String buildMd5Params(Map<String, String> map) {
        StringBuilder result = new StringBuilder(256);
        Map<String, String> sortedMap = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        sortedMap.putAll(map);

        Map.Entry<String, String> me = null;
        String val = null;
        for (Iterator<Map.Entry<String, String>> it = sortedMap.entrySet().iterator(); it.hasNext();) {
            me = it.next();
            val = me.getValue();
            if (val != null) {
                val = val.trim();
                if (!"".equals(val)) {
                    result.append(val);
                }
            }
        }
        return result.toString();
    }

}
