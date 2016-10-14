package com.ozguryazilim.crawling.crawler.api.main;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import com.ozguryazilim.crawling.crawler.api.conf.CrawlerPropertyReader;
import com.ozguryazilim.crawling.crawler.api.core.AbstractCrawler;
import com.ozguryazilim.crawling.crawler.api.xml.Site;
import com.ozguryazilim.crawling.crawler.api.xml.Sites;
import com.ozguryazilim.crawling.crawler.api.xml.factory.CrawlerFactory;

public abstract class CrawlerMain
{
	private static final String	LOG4J_PROPERTIES	= "log4j.properties";

	protected static void initialize() throws IOException
	{
		initializeLogging();

		readConfiguration();
	}

	/**
	 * Reads the sites xml and starts each crawler
	 * 
	 * @throws Exception
	 */
	protected void startCrawlers() throws Exception
	{
		Sites sites = CrawlerFactory.getInstance().getSites();
		List<Site> siteList = sites.getSites();

		for (Site site : siteList)
		{
			if (site.isCrawl())
			{
				AbstractCrawler crawler = getCrawlerForSite(site);
				if (crawler != null)
				{
					crawler.startCrawler();
				}
			}
		}

		/*
		 * DefaultExcelCrawler crawler = new DefaultExcelCrawler();
		 * crawler.startCrawler();
		 */
	}

	/**
	 * Returns the crawler with specific site name
	 * 
	 * @param site
	 *            Site
	 * @return AbstractCrawler
	 * @throws Exception
	 */
	protected abstract AbstractCrawler getCrawlerForSite(Site site) throws Exception;

	private static void initializeLogging()
	{
		PropertyConfigurator.configure(ClassLoader.getSystemResource(LOG4J_PROPERTIES));
	}

	private static void readConfiguration() throws IOException
	{
		CrawlerPropertyReader.read();
	}
}
