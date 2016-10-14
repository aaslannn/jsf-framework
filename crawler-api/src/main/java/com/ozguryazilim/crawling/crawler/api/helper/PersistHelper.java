package com.ozguryazilim.crawling.crawler.api.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ozguryazilim.crawling.crawler.api.core.IConstants;
import com.ozguryazilim.crawling.crawler.api.db.DBUtil;
import com.ozguryazilim.crawling.crawler.api.db.Persister;
import com.ozguryazilim.crawling.crawler.api.exception.CrawlerException;
import com.ozguryazilim.crawling.crawler.api.xml.DataColumn;
import com.ozguryazilim.crawling.crawler.api.xml.IDataColumn;
import com.ozguryazilim.crawling.crawler.api.xml.Site;

public class PersistHelper implements IConstants
{
	private static Logger		logger			= Logger.getLogger(PersistHelper.class);
	private static final String	ID_SELECT_QUERY	= "SELECT %s FROM %s WHERE %s = ?";

	/**
	 * Persist the given optionId and the jobCodes to the db according to the
	 * the relation of the field
	 * 
	 * @param dataColumn
	 * @param optionId
	 * @param jobCodes
	 * @throws CrawlerException
	 * @throws Exception
	 */
	public static void persistOption(Site site, IDataColumn dataColumn, Object optionId, List<Object> jobCodes) throws CrawlerException
	{
		String relation = dataColumn.getRelation();
		if (relation.equals(RELATION_MANY_TO_MANY))
		{
			String crossTableName = dataColumn.getCrossTable();
			String columnName = dataColumn.getColumnName();
			String inverseColumnName = dataColumn.getInverseColumnName();
			for (Object code : jobCodes)
			{
				Persister.insertToTable(crossTableName, new String[] { inverseColumnName, columnName }, new Object[] { code, optionId });
			}
		}
		else if (relation.equals(RELATION_MANY_TO_ONE) || relation.equals(RELATION_INTEGRATED))
		{
			String columnName = dataColumn.getColumnName();
			for (Object code : jobCodes)
			{
				Persister.insertToOrUpdateTable(site.getTable(), new String[] { site.getJobCodeColumnName(), columnName }, new Object[] { code, optionId },
						new String[] { columnName }, new Object[] { optionId });
			}
		}

	}

	/**
	 * Persists the given job code and given column value information to the any
	 * table related
	 * 
	 * @param site
	 * @param jobCode
	 * @param value
	 * @param currentColumn
	 * @throws CrawlerException
	 * @throws SQLException
	 */
	public static void persistAll(Site site, Object jobCode, Object value, DataColumn currentColumn) throws CrawlerException, SQLException
	{
		List<Object> jobCodes = new ArrayList<Object>();
		jobCodes.add(jobCode);

		// If string value, check if the values length is lower than the max
		// length of column
		if (currentColumn.getValueColumnType().equals(DBUtil.COLUMN_TYPE_STRING))
		{
			String strValue = (String) value;
			if (currentColumn.getColumnLength() < strValue.length())
			{
				value = strValue.substring(0, currentColumn.getColumnLength() - 1);
				logger.warn("The value is bigger then max length. jobCode : " + jobCode + " \tcolumn :" + currentColumn.getName() + "\tvalue length : " + strValue.length());
			}
		}

		// Firstly persists to field data table then retrieves the id of the
		// persist data and use it to persist the value to main site table as
		// reference
		if (currentColumn.getRelation().equals(RELATION_MANY_TO_ONE) || currentColumn.getRelation().equals(RELATION_MANY_TO_MANY))
		{
			int id = -1;
			Object[] values = new Object[] { value };
			Persister.insertToTable(currentColumn.getTableName(), new String[] { DBUtil.FLD_VALUE }, values);
			String query = String.format(ID_SELECT_QUERY, DBUtil.FLD_ID, currentColumn.getTableName(), DBUtil.FLD_VALUE);

			PreparedStatement statement = Persister.getPreparedStatment(query);
			statement.setObject(1, value);
			ResultSet set = Persister.executePreparedStatement(statement);
			try
			{
				if (set.next())
				{
					id = set.getInt(DBUtil.FLD_ID);
				}
			}
			finally
			{
				set.getStatement().getConnection().close();
			}

			persistOption(site, currentColumn, id, jobCodes);
		}
		else
		{
			// Directly use the persist option function
			persistOption(site, currentColumn, value, jobCodes);
		}
	}
}
