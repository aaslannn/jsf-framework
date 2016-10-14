package com.ozguryazilim.finance.crawler.job;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.event.Observes;
import javax.inject.Singleton;

import org.jboss.solder.servlet.WebApplication;
import org.jboss.solder.servlet.event.Initialized;

@Singleton
public class CrawlerJobManager {
	private ScheduledExecutorService	scheduler;

	@PostConstruct
	public void init() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new CrawlerJob(), 0, 3, TimeUnit.MINUTES);
	}

	@PreDestroy
	public void destroy() {
		scheduler.shutdownNow();
	}

	/**
	 * At startup of web application logs the message, it is used to initialize
	 * the bean at startup
	 * 
	 * @param webapp
	 */
	public void startup(@Observes @Initialized WebApplication webapp) {
		Logger.getLogger(CrawlerJobManager.class.getName()).info("CrawlerJobManager initialized at " + new Date(webapp.getStartTime()));
	}
}
