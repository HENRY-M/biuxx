package com.biuxx.utils.log;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;
import com.biuxx.utils.log.format.BiuxxLogFormat;

public class DubboProviderLogFilter extends DubboAbstractLogFilter {

	private static final Logger logger = LoggerFactory.getLogger(DubboProviderLogFilter.class);

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

		DubboLogger.registerProcessID(UUID.randomUUID().toString().replaceAll("-", ""));
		String interactionUUID = (String) invocation.getAttachment(INTERACTION_UUID);
		String methodName = invocation.getMethodName();

		try {
			logger.info(
					BiuxxLogFormat.Style.DEFAULT.getFormat("receive service[{}] invocation[{}] with params[{}]"),
					new Object[] { methodName, interactionUUID, JSON.toJSONString(invocation.getArguments()) });

			Result result = invoker.invoke(invocation);
			Object resultObj = result.getValue();

			logger.info(BiuxxLogFormat.Style.DEFAULT.getFormat("serivce[{}] invocation[{}] return result[{}]"),
					new Object[] { methodName, interactionUUID, JSON.toJSONString(resultObj) });

			return result;
		} catch (RpcException e) {
			logger.error(BiuxxLogFormat.Style.DEFAULT.getFormat("serivce[{}] invocation[{}] is abort"),
					methodName, interactionUUID);
			logger.error(BiuxxLogFormat.HEAD, e);
			throw e;
		}
	}

}
