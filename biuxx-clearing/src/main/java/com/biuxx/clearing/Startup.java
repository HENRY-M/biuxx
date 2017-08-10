package com.biuxx.clearing;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.biuxx.utils.log.DubboLogger;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;


public class Startup {
	
	private static Logger logger = LoggerFactory.getLogger(Startup.class);

	private static ClassPathXmlApplicationContext applicationContext;
	
	public static void main(String[] args) {

	    String nodeName = StringUtils.trimToEmpty(System.getProperty("node.name"));
	    
	    String resPath = getResoucesPath(Startup.class);
	    
        DubboLogger.registerLocationInfo("BIUXX", "CLEARING", nodeName);
	    
	    String logBackPathName = resPath + "logback.xml";
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        try {
            configurator.doConfigure(logBackPathName);
        } catch (JoranException e) {
            e.printStackTrace();
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(lc);

        String ctxFile = resPath + "applicationContext.xml";
		logger.info("Loading Spring applicationContext.xml! -->" + ctxFile);
		applicationContext = new ClassPathXmlApplicationContext("file:" + ctxFile);
		logger.info("Loading Spring applicationContext.xml Succeed! -->" + ctxFile);

		applicationContext.start();

		logger.info("Start server succeed!");
		
		while (true) {
			try {
				TimeUnit.SECONDS.sleep(10);
				logger.info("I am runing at this time:" + new Date().toString());
			} catch (InterruptedException e) {
				logger.error("Run Interrupted!....", e);
			} catch (Exception e) {
				logger.error("Run Exception!....", e);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static String getResoucesPath(Class c) {
		try {
			URL url = c.getProtectionDomain().getCodeSource().getLocation();

			String filePath =  URLDecoder.decode(url.getPath(), "UTF-8");

			if (filePath.endsWith(".jar")) {
				filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
			} else if (filePath.endsWith("classes/")) {
				filePath = filePath.substring(0, filePath.lastIndexOf("classes/"));
			}

			return filePath + "resources/";
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
