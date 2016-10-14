package com.ozguryazilim.crawling.crawler.api.db;

import org.apache.commons.lang.StringUtils;

import com.ozguryazilim.crawling.crawler.api.helper.StringHelper;

public class DBUtil
{
	public static final String	COLUMN_TYPE_STRING				= "ctString";
	public static final String	COLUMN_TYPE_INTEGER				= "ctInt";
	public static final String	COLUMN_TYPE_INTEGER_INCREMENTED	= "ctIntAutoIncremented";
	public static final String	COLUMN_TYPE_SMALL_INTEGER		= "ctSmallInt";
	public static final String	COLUMN_TYPE_TIMESTAMP			= "ctTimestamp";

	public static final String	FLD_VALUE						= "FLD_VALUE";
	public static final String	FLD_ID							= "FLD_ID";
	public static final String	FLD_LAST_UPDATE					= "FLD_LAST_UPDATE";

	private static final String	FOREIGN_KEY						= " FOREIGN KEY (%s) REFERENCES %s(%s),";
	private static final String	PRIMARY_KEY						= " PRIMARY KEY (%s),";
	private static final String	UNIQUE_KEY						= " UNIQUE KEY (%s),";
	private static final String	AUTO_INCREMENT					= " AUTO_INCREMENT";

	private static final int	DEFAULT_VALUE_LENGTH			= 150;

	public static String getAColumnQuery(String columnName, String columnType, int columnLength)
	{
		String query = columnName + " ";
		if (columnType.equals(COLUMN_TYPE_STRING))
		{
			if (columnLength == 0)
			{
				columnLength = DEFAULT_VALUE_LENGTH;
			}
			if (columnLength <= 2000)
			{
				query += "VARCHAR( " + columnLength + " ),";
			}
			else
			{
				query += "TEXT,";
			}
		}
		else if (columnType.equals(COLUMN_TYPE_INTEGER))
		{
			query += "INT,";
		}
		else if (columnType.equals(COLUMN_TYPE_INTEGER_INCREMENTED))
		{
			query += "INT AUTO_INCREMENT,";
		}
		else if (columnType.equals(COLUMN_TYPE_SMALL_INTEGER))
		{
			query += "SMALLINT,";
		}
		else if (columnType.equals(COLUMN_TYPE_TIMESTAMP))
		{
			query += "TIMESTAMP NULL DEFAULT NULL,";
		}

		return query;
	}

	public static String getAutoIncrementedColumnQuery(String columnName)
	{
		String query = columnName + " ";
		query += "INT " + AUTO_INCREMENT + ",";

		return query;
	}

	public static String getForeignKeyQuery(String columnName, String referredTable, String referredColumn)
	{
		return String.format(FOREIGN_KEY, columnName, referredTable, referredColumn);
	}

	public static String getPrimaryKeyQuery(String[] columnName)
	{
		return String.format(PRIMARY_KEY, StringUtils.join(columnName, ','));
	}

	public static String getUniqueKeyQuery(String[] keys)
	{
		return String.format(UNIQUE_KEY, StringUtils.join(keys, ","));
	}

	public static String createDataTableQuery(String tableName, String idColumnType, int columnLength) throws Exception
	{
		return createDataTableQuery(tableName, idColumnType, columnLength, false);
	}

	public static String createDataTableQuery(String tableName, String idColumnType, int columnLength, boolean autoIncrement) throws Exception
	{
		if (!(idColumnType.equals(COLUMN_TYPE_INTEGER) || idColumnType.equals(COLUMN_TYPE_STRING)))
		{
			throw new Exception("Table could not be created as FLD_ID column type is " + idColumnType);
		}

		String query = startCreateTableQuery(tableName);
		if (autoIncrement && idColumnType.equals(COLUMN_TYPE_INTEGER))
		{
			query += getAutoIncrementedColumnQuery(FLD_ID);
		}
		else if (autoIncrement && !idColumnType.equals(COLUMN_TYPE_INTEGER))
		{
			// throw new
			// Exception("Table could not be created as FLD_ID column type is "
			// + idColumnType +
			// " to which auto increment could not be applied!");
			query += getAColumnQuery(FLD_ID, idColumnType, columnLength);
		}
		else
		{
			query += getAColumnQuery(FLD_ID, idColumnType, columnLength);
		}

		query += getAColumnQuery(FLD_VALUE, COLUMN_TYPE_STRING, DEFAULT_VALUE_LENGTH);
		query += getAColumnQuery(FLD_LAST_UPDATE, COLUMN_TYPE_TIMESTAMP, 0);
		query += getPrimaryKeyQuery(new String[] { FLD_ID });
		query += getUniqueKeyQuery(new String[] { FLD_VALUE });
		query = endCreateTableQuery(query);

		return query;
	}

	public static String startCreateTableQuery(String tableName)
	{
		return "CREATE TABLE " + tableName + " ( ";
	}

	public static String endCreateTableQuery(String query)
	{
		if (StringHelper.trimExtended(query).endsWith(","))
		{
			query = query.trim().substring(0, query.length() - 1);
		}

		return query + " ); ";
	}
}
