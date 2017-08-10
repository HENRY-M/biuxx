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

public class DubboConsumerLogFilter extends DubboAbstractLogFilter {

	private static final Logger logger = LoggerFactory.getLogger(DubboConsumerLogFilter.class);

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

		String interactionUUID = UUID.randomUUID().toString().replaceAll("-", "");
		String methodName = invocation.getMethodName();
		try {
			logger.info(
					BiuxxLogFormat.Style.DEFAULT
							.getFormat("invoke rpc-service[{}] invocation[{}] with params[{}]"),
					new Object[] { methodName, interactionUUID, JSON.toJSONString(invocation.getArguments()) });

			invocation.getAttachments().put(INTERACTION_UUID, interactionUUID);

			Result result = invoker.invoke(invocation);
			Object resultObj = result.getValue();

			logger.info(
					BiuxxLogFormat.Style.DEFAULT.getFormat("rpc-service[{}] invocation[{}] return result[{}]"),
					new Object[] { methodName, interactionUUID, JSON.toJSONString(resultObj) });

			return result;
		} catch (RpcException e) {
			logger.error(BiuxxLogFormat.Style.DEFAULT.getFormat("rpc-serivce[{}] invocation[{}] is abort"),
					methodName, interactionUUID);
			logger.error(BiuxxLogFormat.HEAD, e);
			throw e;
		}
	}

}
