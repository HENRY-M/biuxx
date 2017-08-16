package com.biuxx.utils.security.cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.biuxx.utils.security.cipher.holder.RSACerFileHolder;
import com.biuxx.utils.security.cipher.holder.RSAPfxFileHolder;


abstract class BiuxxRSACipher implements BiuxxCipher {
    
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected RSACerFileHolder pubKeyHolder;
    protected RSAPfxFileHolder priKeyHolder;
    
    BiuxxRSACipher(RSACerFileHolder pubKeyHolder, RSAPfxFileHolder priKeyHolder) {
        if(pubKeyHolder == null || priKeyHolder == null) {
            throw new IllegalArgumentException("pubKeyHolder and priKeyHolder cannot be null!");
        }
        
        this.pubKeyHolder = pubKeyHolder;
        this.priKeyHolder = priKeyHolder;
    }
}
