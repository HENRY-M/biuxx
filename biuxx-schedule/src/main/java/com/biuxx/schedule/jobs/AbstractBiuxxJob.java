package com.biuxx.schedule.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBiuxxJob implements BiuxxJob {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void execute() {
        try {
            
            doJob();
        } catch (Throwable e) {
            logger.error("Unhandled Error!", e);
        }
    }
    
    public abstract void doJob();
}
