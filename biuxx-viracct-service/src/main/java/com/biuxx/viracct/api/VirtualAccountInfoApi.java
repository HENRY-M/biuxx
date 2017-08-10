package com.biuxx.viracct.api;

import com.biuxx.viracct.api.dto.VirtualAccountDTO;

public interface VirtualAccountInfoApi {

	public VirtualAccountDTO getVirtualAccountByVirAcctId(String virAcctId);
}
