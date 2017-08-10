package com.biuxx.viracct.api;

import javax.annotation.Resource;

import com.biuxx.viracct.api.dto.VirtualAccountDTO;
import com.biuxx.viracct.service.VirtualAccountInfoService;

public class VirtualAccountInfoApiImpl implements VirtualAccountInfoApi {

	@Resource(name = "virtualAccountInfoService")
	private VirtualAccountInfoService virtualAccountInfoService;
	
	@Override
	public VirtualAccountDTO getVirtualAccountByVirAcctId(String virAcctId) {
		// TODO Auto-generated method stub
		return null;
	}

}
