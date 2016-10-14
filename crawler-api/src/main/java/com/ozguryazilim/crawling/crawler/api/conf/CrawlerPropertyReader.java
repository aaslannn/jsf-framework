package com.ozguryazilim.crawling.crawler.api.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class CrawlerPropertyReader
{
	private static final String	CRAWLER_PROPERTIES	= "crawler.properties";
	public static final String	DB_USER				= "db_user";
	public static final String	DB_PASS				= "db_password";
	public static final String	DB_DRIVER			= "db_driver";
	public static final String	DB_URL				= "db_url";

	public static void read() throws IOException
	{
		Properties properties = new Properties();
		InputStream stream = ClassLoader.getSystemResourceAsStream(CRAWLER_PROPERTIES);  
		properties.load(stream);
		
		CrawlerConfig.dbUser = (String) properties.get(DB_USER);
		CrawlerConfig.dbPassword = (String) properties.get(DB_PASS);
		CrawlerConfig.dbJdbcDriver = (String) properties.get(DB_DRIVER);
		CrawlerConfig.dbUrl = (String) properties.get(DB_URL);
		
	}
}
