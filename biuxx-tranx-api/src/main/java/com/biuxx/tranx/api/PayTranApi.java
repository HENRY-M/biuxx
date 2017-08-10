package com.biuxx.tranx.api;

import com.biuxx.tranx.api.dto.PayTranDTO;

public interface PayTranApi {

	public PayTranDTO getPayTranByOrderId(String orderId);
}
