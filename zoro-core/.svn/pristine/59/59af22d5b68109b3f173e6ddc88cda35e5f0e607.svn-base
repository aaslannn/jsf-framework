package com.ozguryazilim.crawling.crawler.api.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

import com.ozguryazilim.crawling.crawler.api.conf.CrawlerConfig;

public class DBConnection
{
	private static final int		initialPoolSize	= 4;
	private static BasicDataSource	dataSource		= null;

	public static Connection connect()
	{
		if (dataSource == null)
		{
			dataSource = new BasicDataSource();
			dataSource.setDriverClassName(CrawlerConfig.dbJdbcDriver);
			dataSource.setPassword(CrawlerConfig.dbPassword);
			dataSource.setUsername(CrawlerConfig.dbUser);
			dataSource.setUrl(CrawlerConfig.dbUrl);
			dataSource.setInitialSize(initialPoolSize);
		}
		Connection conn = null;

		try
		{
			conn = dataSource.getConnection();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return conn;
	}

}
