package com.biuxx.schedule.jobs.impl;

import java.util.concurrent.TimeUnit;

import com.biuxx.schedule.jobs.AbstractBiuxxJob;

public class BiuxxWatchdogJobImpl extends AbstractBiuxxJob {

	@Override
	public void doJob() {
		logger.info("Job Start......");

		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			logger.error("", e);
		}
		
		logger.info("Job Stop.......");
	}

}
