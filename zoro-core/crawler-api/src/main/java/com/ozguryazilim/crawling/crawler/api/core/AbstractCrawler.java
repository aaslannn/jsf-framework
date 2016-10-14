package com.ozguryazilim.crawling.crawler.api.core;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.ozguryazilim.crawling.crawler.api.db.DBUtil;
import com.ozguryazilim.crawling.crawler.api.db.Persister;
import com.ozguryazilim.crawling.crawler.api.exception.CrawlerException;
import com.ozguryazilim.crawling.crawler.api.exception.CrawlerException.CrawlerExceptionType;
import com.ozguryazilim.crawling.crawler.api.helper.CommonHelper;
import com.ozguryazilim.crawling.crawler.api.helper.DOMHelper;
import com.ozguryazilim.crawling.crawler.api.xml.DataColumn;
import com.ozguryazilim.crawling.crawler.api.xml.DataPageModel;
import com.ozguryazilim.crawling.crawler.api.xml.DataStream;
import com.ozguryazilim.crawling.crawler.api.xml.Datum;
import com.ozguryazilim.crawling.crawler.api.xml.Field;
import com.ozguryazilim.crawling.crawler.api.xml.IDataColumn;
import com.ozguryazilim.crawling.crawler.api.xml.Site;
import com.thoughtworks.selenium.Selenium;

public abstract class AbstractCrawler implements IConstants
{
	private static Logger							logger							= Logger.getLogger(AbstractCrawler.class);

	protected Site									site							= null;
	protected final String							ALL_JOB_CODE_QUERY				= "SELECT DISTINCT(%s) FROM %s";
	private final static int						DEFAULT_MAX_POOL_SIZE			= 5;
	private final static int						DEFAULT_POOLS_INITIAL_THREAD_NO	= DEFAULT_MAX_POOL_SIZE;
	protected Selenium								selenium						= null;
	public static Locale							defaultLocale					= new Locale("tr", "TR");
	private int										maxPoolSize						= DEFAULT_MAX_POOL_SIZE;
	private int										poolInitSize					= DEFAULT_POOLS_INITIAL_THREAD_NO;
	private Class<AbstractDataPageReaderThread>		dataPageReaderClass				= null;

	final protected ArrayBlockingQueue<Runnable>	queue							= new ArrayBlockingQueue<Runnable>(20);

	/**
	 * This function called for the generic crawling operation. Each site should
	 * have its own crawling functionality by supplying runJobCodeGetter
	 * function and implementing other abstract functions.
	 */
	protected void crawl() throws Exception
	{
		long startTime = System.currentTimeMillis();
		runJobCodeGetter();
		logger.info("Crawling for job codes of " + site.getName() + " has been completed in " + +(System.currentTimeMillis() - startTime) / 1000 / 60 + " m");

		// Use found job codes to get the data from their data pages
		runDataPageReaders(maxPoolSize, dataPageReaderClass);
		logger.info("Crawling for " + site.getName() + " has been completed in " + +(System.currentTimeMillis() - startTime) / 1000 / 60 + " m");
	}

	/**
	 * Abstract getInstance function which will be implemented by subclass'es to
	 * return their instances
	 * 
	 * @return an AbstractCrawler
	 */
	@SuppressWarnings("unchecked")
	protected void checkInstanceGettable(Site site, @SuppressWarnings("rawtypes") Class dataPageReaderClass) throws CrawlerException
	{
		if (site.getName().equals(getSiteName()))
		{
			this.site = site;
			this.dataPageReaderClass = dataPageReaderClass;
		}
		else
		{
			logger.error("The given site can not be crawled by current crawler instance!");
			throw new CrawlerException(CrawlerExceptionType.UNEXPECTED_SITE_FOR_CRAWLER, "The given site can not be crawled by current crawler instance!");
		}
	}

	/**
	 * @return the site
	 */
	public Site getSite()
	{
		return site;
	}

	/**
	 * @param site
	 *            the site to set
	 */
	public void setSite(Site site)
	{
		this.site = site;
	}

	/**
	 * @param maxPoolSize
	 *            the maxPoolSize to set
	 */
	public void setMaxPoolSize(int maxPoolSize)
	{
		this.maxPoolSize = maxPoolSize;
	}

	/**
	 * @return the maxPoolSize
	 */
	public int getMaxPoolSize()
	{
		return maxPoolSize;
	}

	/**
	 * @param poolInitSize
	 *            the poolInitSize to set
	 */
	public void setPoolInitSize(int poolInitSize)
	{
		this.poolInitSize = poolInitSize;
	}

	/**
	 * @return the poolInitSize
	 */
	public int getPoolInitSize()
	{
		return poolInitSize;
	}

	/**
	 * Function called before calling crawl function, override to add
	 * functionalities to be executed before the crawling operations
	 * 
	 * @throws Exception
	 */
	protected void beforeCrawl() throws Exception
	{
		checkAndCreateTable();
	}

	protected abstract String getJobQuery();

	protected abstract void runJobCodeGetter() throws Exception;

	/**
	 * Called after crawl function, override if there are functionalities which
	 * should be executed after crawling completes.
	 * 
	 * @throws Exception
	 */
	protected void afterCrawl() throws Exception
	{

	}

	/**
	 * Each site should have its own crawling functionality
	 */
	public void startCrawler() throws Exception
	{
		beforeCrawl();

		crawl();

		afterCrawl();
	}

	/**
	 * Gets the name of the site
	 * 
	 * @return
	 */
	protected abstract String getSiteName();

	/**
	 * Checks whether the main table exists if true ignores, otherwise creates
	 * each table
	 * 
	 * @throws Exception
	 */
	protected void checkAndCreateTable() throws Exception
	{
		List<String> createTableQueries = constructCreateTableQuery();
		for (String query : createTableQueries)
		{
			Persister.executeUpdateQuery(query);
		}
	}

	/**
	 * Constructs create table queries for each not created tables
	 * 
	 * @param site
	 * @throws Exception
	 */
	private List<String> constructCreateTableQuery() throws Exception
	{
		String query = DBUtil.startCreateTableQuery(site.getTable());
		DataPageModel firstModel = site.getDataPage().getModels().get(0);
		String modelTableQuery = null;
		if (firstModel != null && firstModel.getMultiResultTable() != null)
		{
			modelTableQuery = DBUtil.startCreateTableQuery(firstModel.getMultiResultTable());
			modelTableQuery += DBUtil.getAColumnQuery(site.getJobCodeColumnName(), site.getJobCodeColumnType(), site.getJobCodeColumnLength());
		}

		List<String> dataTableQueries = new ArrayList<String>();
		List<String> crossTableQueries = new ArrayList<String>();

		// If there is no identifier column
		if (site.getJobCodeColumnName() != null)
		{
			query += DBUtil.getAColumnQuery(site.getJobCodeColumnName(), site.getJobCodeColumnType(), site.getJobCodeColumnLength());
		}

		List<DataColumn> dataColumns = site.getDataPage().getDataColumns();
		DataStream stream = site.getDataStream();
		if (dataColumns != null && stream != null)
		{
			dataColumns.addAll(stream.getDataColumns());
		}

		if (dataColumns != null)
		{
			String secondaryColumnName;
			for (DataColumn column : dataColumns)
			{
				if (!column.isDummyColumn())
				{
					if (isColumnHasMultiResult(column.getColumnName()))
					{
						modelTableQuery += DBUtil.getAColumnQuery(column.getColumnName(), column.getValueColumnType(), column.getLength());
					}
					else
					{
						query = addDataColumn(column, query, dataTableQueries, crossTableQueries);

						// TODO correct this part
						secondaryColumnName = column.getSecondaryColumn();
						if (secondaryColumnName != null && !secondaryColumnName.equals(""))
						{
							query += DBUtil.getAColumnQuery(secondaryColumnName, column.getValueColumnType(), column.getLength());
						}
					}
				}
			}
		}

		List<Field> fields = site.getFields();
		if (fields != null)
		{
			for (IDataColumn field : fields)
			{
				if (!field.isDummyColumn())
				{
					query = addDataColumn(field, query, dataTableQueries, crossTableQueries);
				}
			}
		}

		query = addForeignAnaUniqueKeys(query);
		query = DBUtil.endCreateTableQuery(query);
		logger.debug(query);

		if (!Persister.checkTableExists(site.getTable()))
		{
			dataTableQueries.add(query);

			if (modelTableQuery != null)
			{
				modelTableQuery += DBUtil.getForeignKeyQuery(site.getJobCodeColumnName(), site.getTable(), site.getJobCodeColumnName());
				modelTableQuery += DBUtil.getPrimaryKeyQuery(new String[] { DBUtil.FLD_ID });
				modelTableQuery = DBUtil.endCreateTableQuery(modelTableQuery);

				dataTableQueries.add(modelTableQuery);
			}
		}

		dataTableQueries.addAll(crossTableQueries);
		return dataTableQueries;
	}

	private boolean isColumnHasMultiResult(String columnName)
	{
		for (DataPageModel model : site.getDataPage().getModels())
		{
			String multiResultTable = model.getMultiResultTable();
			if (multiResultTable != null && !multiResultTable.isEmpty())
			{
				for (Datum datum : model.getData())
				{
					if (datum.getColumnName().equals(columnName) && datum.isMultiResult())
					{
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Adds the site field id to foreign keys and the unique columns as unique
	 * keys
	 * 
	 * @param query
	 *            String
	 * @return updatedQuery
	 */
	private String addForeignAnaUniqueKeys(String query)
	{
		if (site.getUniqueColumns() != null)
		{
			List<DataColumn> uniqueColumns = site.getUniqueColumns().getColumns();
			if (uniqueColumns != null && uniqueColumns.size() != 0)
			{
				String[] uniqueKeys = new String[uniqueColumns.size()];
				for (int i = 0; i < uniqueColumns.size(); i++)
				{
					DataColumn uniqueColumn = uniqueColumns.get(i);
					uniqueKeys[i] = uniqueColumn.getColumnName();
				}
				query += DBUtil.getUniqueKeyQuery(uniqueKeys);
			}
		}

		if (site.getJobCodeColumnName() != null)
		{
			query += DBUtil.getPrimaryKeyQuery(new String[] { site.getJobCodeColumnName() });
		}

		return query;
	}

	private String addDataColumn(IDataColumn dataColumn, String query, List<String> dataTableQueries, List<String> crossTableQueries) throws SQLException, Exception
	{
		checkNecessaryFieldsExists(dataColumn);

		String type = null;
		if (dataColumn instanceof Field)
		{
			type = ((Field) dataColumn).getType();
		}

		String relation = dataColumn.getRelation();
		if (relation.equals(RELATION_MANY_TO_ONE))
		{
			String fieldDataTable = dataColumn.getFieldDataTable();
			String columnName = dataColumn.getColumnName();
			String idColumnType = dataColumn.getIdColumnType();
			int columnLength = dataColumn.getColumnLength();
			if (type != null && type.equals(FIELD_TYPE_RADIO_BUTTON))
			{
				idColumnType = DBUtil.COLUMN_TYPE_INTEGER;
			}

			if (!Persister.checkTableExists(fieldDataTable))
			{
				dataTableQueries.add(DBUtil.createDataTableQuery(fieldDataTable, idColumnType, columnLength,
						!(dataColumn instanceof Field) || ((Field) dataColumn).isIdAutoIncremented()));
			}

			query += DBUtil.getAColumnQuery(columnName, idColumnType, columnLength);
			query += DBUtil.getForeignKeyQuery(columnName, fieldDataTable, DBUtil.FLD_ID);
		}
		else if (relation.equals(RELATION_MANY_TO_MANY))
		{
			String fieldDataTable = dataColumn.getFieldDataTable();
			int columnLength = dataColumn.getColumnLength();
			String idColumnType = dataColumn.getIdColumnType();
			String crossTableName = dataColumn.getCrossTable();
			String inverseColumnName = dataColumn.getInverseColumnName();
			String columnName = dataColumn.getColumnName();

			if (!Persister.checkTableExists(fieldDataTable))
			{
				dataTableQueries.add(DBUtil.createDataTableQuery(fieldDataTable, idColumnType, columnLength,
						!(dataColumn instanceof Field) || ((Field) dataColumn).isIdAutoIncremented()));
			}

			if (!Persister.checkTableExists(crossTableName))
			{
				String siteJobCodeColumnType = site.getJobCodeColumnType();
				int siteJobCodeColumnLength = site.getJobCodeColumnLength();

				String crossTableQuery = DBUtil.startCreateTableQuery(crossTableName);
				crossTableQuery += DBUtil.getAColumnQuery(columnName, idColumnType, columnLength);
				crossTableQuery += DBUtil.getAColumnQuery(inverseColumnName, siteJobCodeColumnType, siteJobCodeColumnLength);
				crossTableQuery += DBUtil.getForeignKeyQuery(columnName, fieldDataTable, DBUtil.FLD_ID);
				crossTableQuery += DBUtil.getForeignKeyQuery(inverseColumnName, site.getTable(), site.getJobCodeColumnName());
				crossTableQuery += DBUtil.getPrimaryKeyQuery(new String[] { columnName, inverseColumnName });
				crossTableQuery = DBUtil.endCreateTableQuery(crossTableQuery);

				crossTableQueries.add(crossTableQuery);
			}
		}
		else if (relation.equals(RELATION_INTEGRATED))
		{
			String columnName = dataColumn.getColumnName();
			String columnType = dataColumn.getValueColumnType();
			int columnLength = dataColumn.getColumnLength();

			query += DBUtil.getAColumnQuery(columnName, columnType, columnLength);
		}

		return query;
	}

	/**
	 * For different types of relations check whether the required values for
	 * the corresponding relation is not null
	 * 
	 * @param field
	 * @throws CrawlerException
	 */
	private void checkNecessaryFieldsExists(IDataColumn field) throws CrawlerException
	{
		String fieldDataTable = field.getFieldDataTable();
		String crossTableName = field.getCrossTable();
		String inverseColumnName = field.getInverseColumnName();
		String columnName = field.getColumnName();
		String relation = field.getRelation();

		if (relation.equals(RELATION_MANY_TO_ONE))
		{
			if (fieldDataTable == null || columnName == null)
			{
				String message = "The fields necessary for a many to one relation are not found in " + field.getId() + " field (name:" + columnName + ")";
				logger.error(message);
				throw new CrawlerException(CrawlerExceptionType.NECCESSARY_FIELDS_ARE_MISSING, message);
			}
		}
		else if (relation.equals(RELATION_MANY_TO_MANY))
		{
			if (fieldDataTable == null || crossTableName == null || inverseColumnName == null || columnName == null)
			{
				String message = "The fields necessary for a many to many relation are not found in " + field.getId() + " field. (name:" + columnName + ")";
				logger.error(message);
				throw new CrawlerException(CrawlerExceptionType.NECCESSARY_FIELDS_ARE_MISSING, message);
			}
		}
		else if (relation.equals(RELATION_INTEGRATED))
		{
			if (columnName == null)
			{
				String message = "The fields necessary for a integrated relation are not found in " + field.getId() + " field (name:" + columnName + ")";
				logger.error(message);
				throw new CrawlerException(CrawlerExceptionType.NECCESSARY_FIELDS_ARE_MISSING, message);
			}
		}
	}

	protected List<Object> getAllJobCodes(String query) throws SQLException
	{
		ResultSet foundCodes = Persister.executeQuery(String.format(query, site.getJobCodeColumnName(), site.getTable()));
		List<Object> allCodesStr = new ArrayList<Object>();
		String siteColumnName = site.getJobCodeColumnName();
		try
		{
			while (foundCodes.next())
			{
				String code = foundCodes.getString(siteColumnName);
				allCodesStr.add(code);
			}
		}
		finally
		{
			foundCodes.getStatement().getConnection().close();
		}

		return allCodesStr;
	}

	protected void runDataPageReaders(int maxPoolSize, Class<AbstractDataPageReaderThread> readerClass) throws SQLException, InterruptedException,
			InstantiationException, IllegalAccessException
	{
		ThreadPoolExecutor executor = new ThreadPoolExecutor(poolInitSize, maxPoolSize, 40, TimeUnit.SECONDS, queue);
		List<Object> allCodesStr = getAllJobCodes(getJobQuery());
		logger.info("Total number of codes : " + allCodesStr.size());

		AbstractDataPageReaderThread readerThread;
		List<Integer[]> startEndIndexes = CommonHelper.getPartialIndexes(allCodesStr.size(), getMaxPoolSize());
		for (Integer[] indexes : startEndIndexes)
		{
			readerThread = readerClass.newInstance();
			readerThread.initializeReader(site, allCodesStr, indexes[0], indexes[1]);
			executor.execute(readerThread);
		}
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.HOURS);
	}

	protected Document getDocument(InputStream stream)
	{
		return DOMHelper.getDocument(stream, site.getDomParser(), site.getEncoding());
	}
}
