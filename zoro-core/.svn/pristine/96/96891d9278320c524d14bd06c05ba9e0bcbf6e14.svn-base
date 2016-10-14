package com.ozguryazilim.crawling.crawler.api.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ozguryazilim.crawling.crawler.api.exception.CrawlerException;
import com.ozguryazilim.crawling.crawler.api.exception.CrawlerException.CrawlerExceptionType;

public class Persister
{
	private final static String	CHECK_TABLE	= "SHOW TABLES LIKE '%s'";

	/**
	 * Checks the existence of the given table name if exists return true
	 * otherwise returns false
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static boolean checkTableExists(String tableName) throws SQLException
	{
		ResultSet set = Persister.executeQuery(String.format(CHECK_TABLE, tableName));
		Connection connection = set.getStatement().getConnection();
		boolean result = false;
		try
		{
			if (set.next())
			{
				result = true;
			}
		}
		finally
		{
			connection.close();
		}

		return result;
	}

	/**
	 * Executes the given select query and returns the result set. Caller should
	 * close the connection!!!
	 * 
	 * @param query
	 * @return
	 */
	public static ResultSet executeQuery(String query) throws SQLException
	{
		Connection connection = DBConnection.connect();

		Statement statement = null;
		ResultSet resultSet = null;
		try
		{
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		}
		catch (SQLException e)
		{
			connection.close();
			e.printStackTrace();
		}

		return resultSet;
	}

	/**
	 * Executes the given DELETE, UPDATE or INSERT queries
	 * 
	 * @param query
	 */
	public static void executeUpdateQuery(String query)
	{
		Connection connection = DBConnection.connect();

		Statement statement = null;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(query);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

	}

	/**
	 * Takes input tableName and column names and returns the proper insert
	 * query for a prepared statement for them Ex. tableName=TABLE
	 * returnColumNames={a, b, c, d}, whereColumnNames(e,f,g) Result => SELECT
	 * a,b,c,d FROM TABLE WHERE e=? AND f=? AND g=?
	 * 
	 * @param tableName
	 * @param columnNames
	 * @return
	 */
	private static String getSelectQuerySimpleTemplate(String tableName, String[] returnColumnNames, String[] whereColumnNames)
	{
		String selectQuery = "SELECT ";

		// Insert column names
		for (String arg : returnColumnNames)
		{
			selectQuery += arg + ",";
		}
		selectQuery = selectQuery.substring(0, selectQuery.length() - 1) + " FROM " + tableName;

		if (whereColumnNames != null && whereColumnNames.length > 0)
		{
			selectQuery += " WHERE ";
			
			// Insert column names
			for (String arg : whereColumnNames)
			{
				selectQuery += arg + "=? AND ";
			}
			selectQuery = selectQuery.substring(0, selectQuery.length() - 4);
		}

		return selectQuery;
	}

	/**
	 * Takes input tableName and column names and returns the proper insert
	 * query for a prepared statement for them Ex. tableName=TABLE
	 * columNames={a, b, c, d} Result => INSERT INTO TABLE(a,b,c,d)
	 * VALUES(?,?,?,?)
	 * 
	 * @param tableName
	 * @param columnNames
	 * @return
	 */
	private static String getInsertTableQueryTemplate(String tableName, String[] columnNames, boolean ignored)
	{
		String insertQuery = "INSERT INTO " + tableName + "( ";
		if (ignored)
		{
			insertQuery = "INSERT IGNORE INTO " + tableName + "( ";
		}

		// Insert column names
		for (String arg : columnNames)
		{
			insertQuery += arg + ",";
		}
		insertQuery = insertQuery.substring(0, insertQuery.length() - 1) + ") VALUES( ";

		// Insert each ? for each value
		for (int i = 0; i < columnNames.length; i++)
		{
			insertQuery += "?,";
		}
		insertQuery = insertQuery.substring(0, insertQuery.length() - 1) + ") ";

		return insertQuery;
	}

	/**
	 * Constructs a update query template with given parameters
	 * 
	 * @param tableName
	 * @param setColumnNames
	 * @param whereColumnNames
	 * @return
	 */
	private static String getUpdateTableQueryTemplate(String tableName, String[] setColumnNames, String[] whereColumnNames)
	{
		String updatequery = "UPDATE " + tableName + " SET ";

		// Insert names of the columns to be set
		for (String arg : setColumnNames)
		{
			updatequery += arg + " = ?,";
		}
		updatequery = updatequery.substring(0, updatequery.length() - 1) + " WHERE ";

		// Insert names of the columns to be used in where clause
		for (String arg : whereColumnNames)
		{
			updatequery += arg + " = ? AND ";
		}
		updatequery = updatequery.substring(0, updatequery.length() - 4);

		return updatequery;
	}

	/**
	 * Takes input tableName and column names and returns the proper insert
	 * query for a prepared statment for them Ex. tableName=TABLE columNames={a,
	 * b, c, d} and updatedColumns = {e,f,g} Result => INSERT INTO
	 * TABLE(a,b,c,d) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE e = ?, f = ?, g =
	 * ?
	 * 
	 * @param tableName
	 * @param columnNames
	 * @param updatedColumns
	 * @return
	 */
	private static String getInsertTableUpdateOnDuplicateQueryTemplate(String tableName, String[] columnNames, String[] updatedColumns)
	{
		String insertQuery = getInsertTableQueryTemplate(tableName, columnNames, false);
		insertQuery += " ON DUPLICATE KEY UPDATE ";
		for (String column : updatedColumns)
		{
			insertQuery += column + " = ?,";
		}
		insertQuery = insertQuery.substring(0, insertQuery.length() - 1);

		return insertQuery;
	}

	/**
	 * Takes input tableName, returnColumnNames, whereColumnNames and values to
	 * create and execute the query and returns the number of affected rows
	 * 
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @return
	 * @throws CrawlerException
	 * @throws Exception
	 *             Thrown if the number of columns and the number of values are
	 *             not equal
	 */
	public static ResultSet executeSelectQuery(String tableName, String[] returnColumnNames, String[] whereColumnNames, Object[] values) throws CrawlerException
	{
		String query = getSelectQuerySimpleTemplate(tableName, returnColumnNames, whereColumnNames);
		Connection connection = DBConnection.connect();
		ResultSet queryResult = null;

		try
		{
			PreparedStatement statement = connection.prepareStatement(query);
			putValues(0, statement, values);

			queryResult = statement.executeQuery();
		}
		catch (SQLException e)
		{
			try
			{
				connection.close();
			}
			catch (SQLException e2)
			{
				e.printStackTrace();
			}
			e.printStackTrace();
		}

		return queryResult;
	}

	/**
	 * Takes input tableName, columnNames and values to create and execute the
	 * query and returns the number of affected rows
	 * 
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @return
	 * @throws CrawlerException
	 * @throws Exception
	 *             Thrown if the number of columns and the number of values are
	 *             not equal
	 */
	public static Object insertToTable(String tableName, String[] columnNames, Object[] values) throws CrawlerException
	{
		if (columnNames.length != values.length)
		{
			throw new CrawlerException(CrawlerExceptionType.INVALID_INSERT_CALL, "Number of columns and values are not equal. Invalid insert call!");
		}

		String query = getInsertTableQueryTemplate(tableName, columnNames, true);
		Connection connection = DBConnection.connect();
		Object result = null;

		try
		{
			PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			putValues(0, statement, values);

			int queryResult = statement.executeUpdate();

			if (queryResult > 0)
			{
				ResultSet set = statement.getGeneratedKeys();
				if (set.next())
				{
					result = set.getObject(1);
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Returns a prepared statement with the given query NOT: Connection must be
	 * closed after preparedStatement execution
	 * 
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public static PreparedStatement getPreparedStatment(String query) throws SQLException
	{
		Connection connection = DBConnection.connect();
		return connection.prepareStatement(query);
	}

	/**
	 * Executes the given query and closes the connection, returns the result
	 * set
	 * 
	 * @param statement
	 * @return
	 */
	public static ResultSet executePreparedStatement(PreparedStatement statement)
	{
		ResultSet set = null;
		try
		{
			set = statement.executeQuery();
		}
		catch (SQLException e)
		{
			try
			{
				statement.getConnection().close();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
		}

		return set;
	}

	/**
	 * Executes the given query and closes the connection, returns the result
	 * set
	 * 
	 * @param statement
	 * @return
	 * @throws SQLException
	 */
	public static int executePreparedUpdateStatement(PreparedStatement statement) throws SQLException
	{
		int result = 0;
		try
		{
			result = statement.executeUpdate();
		}
		finally
		{
			try
			{
				statement.getConnection().close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Gets a insert into on duplicate key update query with given inputs and
	 * executes the query
	 * 
	 * @param tableName
	 * @param columnNames
	 * @param values
	 * @param updateColumNames
	 * @param updateValues
	 * @return
	 * @throws Exception
	 *             Thrown if the number of columns and the number of values are
	 *             not equal
	 */
	public static int insertToOrUpdateTable(String tableName, String[] columnNames, Object[] values, String[] updateColumNames, Object[] updateValues)
			throws CrawlerException
	{
		if (columnNames.length != values.length || updateColumNames.length != updateValues.length)
		{
			throw new CrawlerException(CrawlerExceptionType.INVALID_INSERT_CALL, "Number of columns and values are not equal. Invalid insert call!");
		}

		String query = getInsertTableUpdateOnDuplicateQueryTemplate(tableName, columnNames, updateColumNames);
		Connection connection = DBConnection.connect();
		int result = 0;

		try
		{
			PreparedStatement statement = connection.prepareStatement(query);
			int currentIndex = putValues(0, statement, values);
			putValues(currentIndex, statement, updateValues);

			result = statement.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Puts the given value into the preparedstatement according to the each
	 * values class
	 * 
	 * @param statement
	 * @param values
	 * @throws SQLException
	 */
	private static int putValues(int currentIndex, PreparedStatement statement, Object[] values) throws SQLException
	{
		for (int i = 0; i < values.length; i++)
		{
			Object arg = values[i];
			statement.setObject(++currentIndex, arg);
		}

		return currentIndex;
	}

	public static int updateTable(String tableName, String[] setColumnNames, Object[] setValues, String[] whereColumnNames, Object[] whereValues) throws CrawlerException
	{
		if (setColumnNames.length != setValues.length || whereColumnNames.length != whereValues.length)
		{
			throw new CrawlerException(CrawlerExceptionType.INVALID_UPDATE_CALL, "Number of columns and values are not equal. Invalid update call!");
		}

		String query = getUpdateTableQueryTemplate(tableName, setColumnNames, whereColumnNames);
		Connection connection = DBConnection.connect();
		int result = 0;

		try
		{
			PreparedStatement statement = connection.prepareStatement(query);
			int currentIndex = putValues(0, statement, setValues);
			putValues(currentIndex, statement, whereValues);

			result = statement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return result;
	}
}
