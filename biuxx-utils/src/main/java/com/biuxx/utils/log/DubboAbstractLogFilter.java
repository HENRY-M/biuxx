package com.biuxx.utils.log;

import com.alibaba.dubbo.rpc.Filter;

public abstract class DubboAbstractLogFilter implements Filter {

	protected static final String INTERACTION_UUID = "interaction_uuid";
}
