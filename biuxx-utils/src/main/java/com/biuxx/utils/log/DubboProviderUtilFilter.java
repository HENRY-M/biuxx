package com.biuxx.utils.log;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;

public class DubboProviderUtilFilter extends DubboInteractionUtil implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(DubboProviderUtilFilter.class);

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

		LogUtil.registerProcessID(UUID.randomUUID().toString().replaceAll("-", ""));
		String interactionUUID = (String) invocation.getAttachment(INTERACTION_UUID);
		String methodName = invocation.getMethodName();

		InvocationAnnotations invocationAnnotations = getAnnotations(invoker.getInterface(), methodName, invocation.getParameterTypes());
//		Object[] preDealtArguments = preDealForLogging(invocation.getArguments(), invocationAnnotations.getArgumentAnnotations());
		try {
			logger.info(
					FunIMsgFormat.MsgStyle.DEFAULT_LOG.getFormat("receive service[{}] invocation[{}] with params[{}]"),
					new Object[] { methodName, interactionUUID, JSON.toJSONString(invocation.getArguments()) });

			Result result = invoker.invoke(invocation);
			Object resultObj = result.getValue();

//			Object preDealtResultObj = preDealForLogging(resultObj, invocationAnnotations.getResultAnnotations());
			logger.info(FunIMsgFormat.MsgStyle.DEFAULT_LOG.getFormat("service[{}] invocation[{}] return result[{}]"),
					new Object[] { methodName, interactionUUID, JSON.toJSONString(resultObj) });

			return result;
		} catch (RpcException e) {
			logger.error(FunIMsgFormat.MsgStyle.DEFAULT_LOG.getFormat("service[{}] invocation[{}] is abort"),
					methodName, interactionUUID);
			logger.error(FunIMsgFormat.HEAD, e);
			throw e;
		}
	}

}
