package com.ozguryazilim.crawling.crawler.api.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ozguryazilim.crawling.crawler.api.db.DBUtil;
import com.ozguryazilim.crawling.crawler.api.db.Persister;
import com.ozguryazilim.crawling.crawler.api.exception.CrawlerException;
import com.ozguryazilim.crawling.crawler.api.exception.CrawlerException.CrawlerExceptionType;
import com.ozguryazilim.crawling.crawler.api.helper.DOMHelper;
import com.ozguryazilim.crawling.crawler.api.helper.DateHelper;
import com.ozguryazilim.crawling.crawler.api.helper.PersistHelper;
import com.ozguryazilim.crawling.crawler.api.helper.StringHelper;
import com.ozguryazilim.crawling.crawler.api.helper.XPathHelper;
import com.ozguryazilim.crawling.crawler.api.xml.DataColumn;
import com.ozguryazilim.crawling.crawler.api.xml.DataPage;
import com.ozguryazilim.crawling.crawler.api.xml.DataPageModel;
import com.ozguryazilim.crawling.crawler.api.xml.Datum;
import com.ozguryazilim.crawling.crawler.api.xml.Site;
import com.thoughtworks.selenium.Selenium;

public abstract class AbstractDataPageReaderThread extends Thread implements IConstants
{
	protected Site									site					= null;
	protected List<Object>							codes					= null;
	protected int									startIndex;
	protected int									endIndex;
	protected static HashMap<String, DataColumn>	dataColumnMap			= new HashMap<String, DataColumn>();
	private static int								noOfUniqueDataColumn	= 0;
	private static final String[]					strToRemove				= { "&bull;" };
	private static Logger							logger					= Logger.getLogger(AbstractDataPageReaderThread.class);

	protected Selenium								selenium				= null;

	/**
	 * Initializes the data column map with the values in dataColumns
	 * 
	 * @param site
	 * @param codes
	 * @param startIndex
	 * @param endIndex
	 */
	public void initializeReader(Site site, List<Object> codes, int startIndex, int endIndex)
	{
		this.site = site;
		this.codes = codes;
		this.startIndex = startIndex;
		this.endIndex = endIndex;

		List<DataColumn> dataColumns = site.getDataPage().getDataColumns();
		initializeDataColumnMap(dataColumns);
	}

	public void initializeDataColumnMap(List<DataColumn> dataColumns)
	{
		dataColumnMap.clear();
		noOfUniqueDataColumn = 0;
		for (DataColumn column : dataColumns)
		{
			dataColumnMap.put(column.getName(), column);
			if (column.isUsableForUniqueness())
			{
				noOfUniqueDataColumn++;
			}
		}
	}

	/**
	 * Runs the thread, where each code will be processed
	 */
	@Override
	public void run()
	{
		try
		{
			beforeRun();

			Object code;
			for (int codeIndex = startIndex; codeIndex < endIndex; codeIndex++)
			{
				code = codes.get(codeIndex);
				try
				{
					getDataForCode(code);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				// If wanted an info log can be put here
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			afterRun();
		}
	}

	/**
	 * Executes any necessary codes before the run method
	 */
	protected void beforeRun()
	{

	}

	/**
	 * Wraps up the environment before thread terminates
	 */
	protected void afterRun()
	{

	}

	/**
	 * Read the URL which is constructed by the base URL from data page
	 * component of the site and the code itself
	 * 
	 * @param code
	 * @throws IOException
	 * @throws CrawlerException
	 * @throws ParseException
	 * @throws SQLException
	 */
	protected void getDataForCode(Object code) throws CrawlerException, IOException, ParseException, SQLException
	{
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();

		Document doc;
		XPathExpression expr;
		NodeList shows;
		try
		{
			doc = getDocumentForCode(code);

			// Check whether the current page is a not found page
			expr = xPath.compile(site.getDataPage().getNotFoundXPathExpression());
			shows = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			if (shows.getLength() > 0)
			{
				return;
			}

			DataPageModel model = null;
			try
			{
				model = findModelOfPage(doc, code);
			}
			catch (CrawlerException e)
			{
				e.printStackTrace();
				return;
			}

			// Check if the page corresponds to an existing model(if not null)
			// otherwise
			// get the data by using default
			// tokenizers on the retrieved page
			if (model != null)
			{
				processModelOnDoc(model, doc, code);
			}
			else
			{
				getDataUsingDefaultTokens(doc, code);
			}
		}
		catch (XPathExpressionException xe)
		{
			logger.fatal("X Path Expression execution failure for code " + code);
			throw new CrawlerException(CrawlerExceptionType.MALFORMED_XPATH_EXPRESSION, xe);
		}
		catch (StringIndexOutOfBoundsException se)
		{
			logger.fatal("X Path Expression execution failure for code " + code);
		}
	}

	/**
	 * Returns the document for the code object.
	 * 
	 * @param code
	 * @return
	 * @throws IOException
	 */
	protected Document getDocumentForCode(Object code) throws IOException
	{
		Document doc;
		String urlStr = site.getDataPage().getUrl() + code;
		InputStream stream = getStream(urlStr);
		doc = getDocument(stream);
		return doc;
	}

	/**
	 * Process the given model on the document and persist the retrieved column
	 * values to row with the given code
	 * 
	 * @param model
	 * @param doc
	 * @param code
	 * @throws XPathExpressionException
	 * @throws CrawlerException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public void processModelOnDoc(DataPageModel model, Object doc, Object code) throws XPathExpressionException, CrawlerException, ParseException, SQLException
	{
		if (model.getMultiResultTable() != null)
		{
			processModelOnMultiResult(model, doc, code);
		}
		else
		{
			XPathFactory factory = XPathFactory.newInstance();
			XPath xPath = factory.newXPath();
			XPathExpression expr;
			NodeList shows;
			List<Datum> data = model.getData();

			for (Datum datum : data)
			{
				expr = xPath.compile(datum.getXPathExpression());
				shows = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

				DataColumn currentColumn = dataColumnMap.get(datum.getColumnName());
				if (shows.getLength() == 1)
				{
					Node node = shows.item(0);
					Object value = getValue(datum, node);

					persistValue(currentColumn, code, value);
				}
				else if ((shows.getLength() == 0 && currentColumn.isUsableForUniqueness()) || shows.getLength() > 1)
				{
					throw new CrawlerException(CrawlerExceptionType.DATA_FETCHING_FAILED_FOR_GIVEN_MODEL, "Job Code :" + code + "\nFor datum " + datum.getColumnName()
							+ " model has " + shows.getLength() + " amount results for the model " + model.getName());
				}
				// Else the value is not required therefore ignore the
				// returned result
			}
		}
	}

	protected void processModelOnMultiResult(DataPageModel model, Object doc, Object code) throws XPathExpressionException, CrawlerException, ParseException,
			SQLException
	{
		selenium.open(site.getUrl() + code);
		waitPageLoadThenSleep();
		getTableValuesForCurrentPage(model, code);
	}

	private void getTableValuesForCurrentPage(DataPageModel model, Object code) throws XPathExpressionException,
			CrawlerException, ParseException, SQLException
	{
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		XPathExpression expr;
		NodeList shows;

		List<HashMap<String, Object>> columnValueMapList = new ArrayList<HashMap<String, Object>>();
		int prevResultCount = -1;
		boolean persist = true;
		try
		{
			Document doc = getDocument(new ByteArrayInputStream(selenium.getHtmlSource().getBytes(site.getEncoding())));

			List<Datum> data = model.getData();
			for (Datum datum : data)
			{
				expr = xPath.compile(datum.getXPathExpression());
				shows = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

				DataColumn currentColumn = dataColumnMap.get(datum.getColumnName());
				int nodeListLength = shows.getLength();
				if (nodeListLength != 0 && datum.isMultiResult())
				{
					if (prevResultCount != -1 && nodeListLength != prevResultCount)
					{
						persist = false;
						new CrawlerException(CrawlerExceptionType.DATA_FETCHING_FAILED_FOR_GIVEN_MODEL, "Job Code :" + code + "\nFor datum " + datum.getColumnName()
								+ " different amount of multiresults are returned. Prev amount :" +prevResultCount + "\t Current value :" + nodeListLength).printStackTrace();
						break;
					}
					prevResultCount = nodeListLength;
					
					for (int currentNode = 0; currentNode < nodeListLength; currentNode++)
					{
						HashMap<String, Object> columnNodeValues = null;
						if (columnValueMapList.size() <= currentNode)
						{
							columnValueMapList.add(new HashMap<String, Object>());
							columnNodeValues = columnValueMapList.get(currentNode);
						}
						else
						{
							columnNodeValues = columnValueMapList.get(currentNode);
						}
						Node node = shows.item(currentNode);
						Object value = getValue(datum, node);

						columnNodeValues.put(currentColumn.getColumnName(), value);

					}
				}
				else if (nodeListLength == 1 && !datum.isMultiResult())
				{
					Node node = shows.item(0);
					Object value = getValue(datum, node);

					persistValue(currentColumn, code, value);
				}
				else if ((nodeListLength == 0 && currentColumn.isUsableForUniqueness()) || nodeListLength > 1)
				{
					throw new CrawlerException(CrawlerExceptionType.DATA_FETCHING_FAILED_FOR_GIVEN_MODEL, "Job Code :" + code + "\nFor datum " + datum.getColumnName()
							+ " model has " + nodeListLength + " amount results for the model " + model.getName());
				}
			}
			
			if (persist)
			{
				persistMultiResult(model, code, columnValueMapList);				
			}

			if (goToNextPagination())
			{
				getTableValuesForCurrentPage(model, code);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new CrawlerException(CrawlerExceptionType.DATA_FETCHING_FAILED_FOR_GIVEN_MODEL, "Job Code :" + code + " model document encoding not supported.");
		}
	}

	private void persistMultiResult(DataPageModel model, Object code, List<HashMap<String, Object>> columnValueMapList) throws CrawlerException
	{
		for (HashMap<String, Object> columnNodeValueMap : columnValueMapList)
		{
			Object idValue = columnNodeValueMap.remove("FLD_ID");
			
			List<String> keyList = new ArrayList<String>();
			keyList.add(site.getJobCodeColumnName());
			keyList.addAll(columnNodeValueMap.keySet());

			String[] columnNames = keyList.toArray(new String[] {});
			Object[] columnValues = new Object[columnNames.length];
			columnValues[0] = code;
			for (int currentColumn = 1; currentColumn < columnNames.length; currentColumn++)
			{
				String column = columnNames[currentColumn];
				columnValues[currentColumn] = columnNodeValueMap.get(column);
			}

			List<String> allColumnNameList = new ArrayList<String>();
			allColumnNameList.add("FLD_ID");
			CollectionUtils.addAll(allColumnNameList, columnNames);
			
			List<Object> allColumnValueList = new ArrayList<Object>();
			allColumnValueList.add(idValue);
			CollectionUtils.addAll(allColumnValueList, columnValues);
			
			Persister.insertToOrUpdateTable(model.getMultiResultTable(), allColumnNameList.toArray(new String[]{}), allColumnValueList.toArray(), columnNames, columnValues);
		}
	}

	protected void waitPageLoadThenSleep()
	{
		// selenium.waitForPageToLoad(OtelPuanCrawlerThread.PAGE_WAIT_TIME);
		if (getSleepWaitTime() != 0)
		{
			try
			{
				Thread.sleep(getSleepWaitTime());
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected int getSleepWaitTime()
	{
		return 0;
	}

	/**
	 * Do something to go the next page
	 * 
	 * @param selenium
	 */
	public boolean goToNextPagination()
	{
		return false;
	}

	/**
	 * Persist the given value to the given column
	 * 
	 * @param currentColumn
	 * @param code
	 * @param value
	 * @throws CrawlerException
	 * @throws SQLException
	 */
	protected void persistValue(DataColumn currentColumn, Object code, Object value) throws CrawlerException, SQLException
	{
		if (currentColumn.isPersistable())
		{
			PersistHelper.persistAll(site, code, value, currentColumn);
		}
	}

	/**
	 * Clears the content of the given input value. Clearing operation done
	 * using the remove text list of the data page and static strToRemove field
	 * 
	 * @param inputValue
	 * @return
	 */
	private String clearContent(String inputValue)
	{
		for (String str : strToRemove)
		{
			inputValue = inputValue.replaceAll(str, "");
		}

		String textToRemove = site.getDataPage().getRemoveTextList();
		if (textToRemove != null)
		{
			String[] texts = StringUtils.split(textToRemove, ",");
			for (String str : texts)
			{
				inputValue = inputValue.replaceAll(str, "");
			}
		}

		return inputValue.replaceAll("\n", " ");
	}

	/**
	 * Use default tokens of the data columns to get the data from the given
	 * document
	 * 
	 * @param doc
	 * @param code
	 *            Used to give information during throwing an exception
	 * @throws XPathExpressionException
	 * @throws CrawlerException
	 * @throws ParseException
	 */
	private void getDataUsingDefaultTokens(Document doc, Object code) throws XPathExpressionException, CrawlerException, ParseException
	{
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();

		DataPage dataPage = site.getDataPage();
		List<DataColumn> columns = dataPage.getDataColumns();
		String tokenizerXPathExpression = dataPage.getTokenizerXPathExpression();
		String tokenizerSeperator = dataPage.getTokenizerSeparator();
		String tokenizerCaptureMethod = dataPage.getTokenizerCaptureMethod();

		// Get the default tokenizers from the required columns
		List<String> requiredTokens = new ArrayList<String>();
		String defaultToken;
		for (DataColumn column : columns)
		{
			defaultToken = column.getDefaultTokenizer();
			if (column.isUsableForUniqueness() && defaultToken != null && !StringHelper.trimExtended(defaultToken).equals(""))
			{
				requiredTokens.add(defaultToken);
			}
		}

		// For each node returned by the xpath exrpression check whether
		// the required tokens are exists in one of them and if it si
		// set it to the actual data
		XPathExpression expr = xPath.compile(tokenizerXPathExpression);
		NodeList shows = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		Node currentNode;
		String nodeValue = null;
		int noOfFoundToken = 0;
		String[] splittedTokens;
		for (int nodeIndex = 0; nodeIndex < shows.getLength(); nodeIndex++)
		{
			currentNode = shows.item(nodeIndex);
			nodeValue = getStringValueOfNodeForCaptureMethod(currentNode, tokenizerCaptureMethod);

			if (nodeValue.length() != 0)
			{
				processExcluding(dataPage, nodeValue);

				// For each required token, check whether current node includes
				// them
				for (String token : requiredTokens)
				{
					splittedTokens = token.split(tokenizerSeperator);

					// As each token could include more than one tokens get them
					// by using the given splitter
					for (String splittedToken : splittedTokens)
					{
						if (nodeValue.toLowerCase(AbstractCrawler.defaultLocale).contains(splittedToken.toLowerCase(AbstractCrawler.defaultLocale)))
						{
							noOfFoundToken++;
							break;
						}
					}
				}

				// Check whether current nodeValue includes all the
				// required tokens
				if (noOfFoundToken == requiredTokens.size())
				{
					break;
				}
			}

			noOfFoundToken = 0;
		}

		// Process the nodeValue to get each data from it
		if (noOfFoundToken == requiredTokens.size())
		{
			getDataFromStringAndPersist(nodeValue, code, columns, tokenizerSeperator);
		}
		else
		{
			throw new CrawlerException(CrawlerExceptionType.REQUIRED_DEFAULT_TOKENS_COULD_NOT_BE_FOUND, "Default tokens could not be found for the xpath expression "
					+ tokenizerXPathExpression + " and for the job code " + code + ". \nThere is nothing to do anymore! I appreciate the page for its un-genericness.");
		}
	}

	/**
	 * Excludes the ending and beginning of the string according to
	 * corresponding fields of the data page
	 * 
	 * @param dataPage
	 * @param nodeValue
	 */
	private void processExcluding(DataPage dataPage, String nodeValue)
	{
		String excludedEndingString = dataPage.getExcludeEndingStartingWith();
		String excludedBeginningString = dataPage.getExcludePartEndingWith();
		int excludeIndex = -1;

		nodeValue = clearContent(nodeValue);

		// Exclude ending
		if (excludedEndingString != null && excludedEndingString.trim().length() != 0)
		{
			excludeIndex = nodeValue.indexOf(excludedEndingString);
			if (excludeIndex != -1)
			{
				nodeValue = nodeValue.substring(0, excludeIndex);
			}
		}

		// Exclude beginning
		if (excludedBeginningString != null && excludedBeginningString.trim().length() != 0)
		{
			excludeIndex = nodeValue.indexOf(excludedBeginningString);
			if (excludeIndex != -1)
			{
				nodeValue = nodeValue.substring(excludeIndex + excludedBeginningString.length());
			}
		}
	}

	/**
	 * Gets the column data from the given input text nodeValue
	 * 
	 * @param nodeValue
	 * @param code
	 * @param columns
	 * @param tokenizerSeperator
	 * @throws CrawlerException
	 */
	public void getDataFromStringAndPersist(String nodeValue, Object code, List<DataColumn> columns, String tokenizerSeperator) throws CrawlerException
	{
		HashMap<Integer, String> indexOfTokens = new HashMap<Integer, String>();
		int[] indexArray = processNodeValueForTokenIndex(nodeValue, code, columns, tokenizerSeperator, indexOfTokens);

		// After finding each indexes, starting from the biggest index get
		// the tokens
		String datumvalue;
		String[] tokenAndColumnName;
		int index = -1;
		for (int currentIndex = 0; currentIndex < columns.size(); currentIndex++)
		{
			index = removeTheLargestInteger(indexArray);
			// If returned index is -1 we expect that there is no value
			// found for that column
			if (index != -1)
			{
				tokenAndColumnName = indexOfTokens.get(index).split(tokenizerSeperator);
				datumvalue = nodeValue.substring(index + tokenAndColumnName[0].length()).trim();
				nodeValue = nodeValue.substring(0, index);

				Object value = datumvalue;
				DataColumn currentColumn = dataColumnMap.get(tokenAndColumnName[1]);
				if (currentColumn != null)
				{
					try
					{
						value = stringWithColumnType2Object(datumvalue, currentColumn);

						persistValue(currentColumn, code, value);
					}
					catch (ParseException e)
					{
						System.out.println("Parse exception for code " + code);
						e.printStackTrace();
					}
					catch (SQLException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Process the nodeValue to get the indexes of tokens in defaultTokenizer
	 * field of each column, also fills the indexOfTokens map
	 * 
	 * @param nodeValue
	 * @param code
	 * @param columns
	 * @param tokenizerSeperator
	 * @param indexOfTokens
	 * @return
	 */
	private int[] processNodeValueForTokenIndex(String nodeValue, Object code, List<DataColumn> columns, String tokenizerSeperator, HashMap<Integer, String> indexOfTokens)
	{
		int[] indexArray = new int[columns.size()];

		// Initialize each index to to -1
		for (int j = 0; j < indexArray.length; j++)
		{
			indexArray[j] = -1;
		}

		// Firstly get each index of the tokens
		String[] splittedTokens;
		int index = -1;
		for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++)
		{
			DataColumn column = columns.get(columnIndex);

			splittedTokens = column.getDefaultTokenizer().split(tokenizerSeperator);
			for (String token : splittedTokens)
			{
				index = nodeValue.indexOf(token);
				if (index != -1)
				{
					indexArray[columnIndex] = index;
					indexOfTokens.put(index, token + tokenizerSeperator + column.getName());
					break;
				}
			}
		}

		return indexArray;
	}

	/**
	 * Removes the largest integer in the array and returns it
	 * 
	 * @param array
	 * @return
	 */
	private int removeTheLargestInteger(int[] array)
	{
		int indexOfLargestValue = 0;
		for (int i = 1; i < array.length; i++)
		{
			if (array[indexOfLargestValue] < array[i])
			{
				indexOfLargestValue = i;
			}
		}

		int largestValue = array[indexOfLargestValue];
		array[indexOfLargestValue] = -1;

		return largestValue;

	}

	/**
	 * By using the already defined capture functions, returns the string data
	 * for the given node
	 * 
	 * @param node
	 * @param captureMethod
	 * @return
	 * @throws CrawlerException
	 */
	protected String getStringValueOfNodeForCaptureMethod(Node node, String captureMethod) throws CrawlerException
	{
		String value = null;
		if (captureMethod.equals(DATA_CAPTURE_METHOD_SIMPLE))
		{
			value = XPathHelper.getInnerText(node);
		}
		else if (captureMethod.equals(DATA_CAPTURE_METHOD_ALL))
		{
			value = XPathHelper.getAllInnerText(node);
		}
		else if (captureMethod.equals(DATA_CAPTURE_METHOD_NEXT_RECURSIVE))
		{
			value = XPathHelper.getInnerTextOfNextSiblingRecursively(node);
		}
		else if (captureMethod.equals(DATA_CAPTURE_METHOD_EMBEDDED_SIMPLE))
		{
			value = XPathHelper.getAllInnerText(node);
		}
		else if (captureMethod.equals(DATA_CAPTURE_METHOD_EMBEDDED_ALL))
		{
			value = XPathHelper.getAllInnerText(node);
		}
		else
		{
			throw new CrawlerException(CrawlerExceptionType.UNEXPECTED_CAPTURE_METHOD, "Capture method is given as " + captureMethod
					+ " which does not comply with expected values!");
		}

		return StringHelper.trimExtended(value);
	}

	/**
	 * Firstly gets the string representation of the node and checks if the
	 * capture method is embedded inner text then separates the value and
	 * returns the second art of the string
	 * 
	 * @param datum
	 * @param node
	 * @return
	 * @throws CrawlerException
	 */
	protected String getStringValueOfDatum(Datum datum, Node node) throws CrawlerException
	{
		String captureMethod = datum.getCaptureMethod();
		String separator = datum.getSeparator();
		String value = getStringValueOfNodeForCaptureMethod(node, captureMethod);
		if (captureMethod.equals(DATA_CAPTURE_METHOD_EMBEDDED_SIMPLE))
		{
			int index = datum.getEmbedIndex();

			if (value.contains(separator) || value.split(separator).length > 0)
			{
				try
				{
					value = value.split(separator)[index];
				}
				catch (Exception e)
				{

				}
			}
			else
			{
				throw new CrawlerException(CrawlerExceptionType.INVALID_EMBEDDED_SEPERATOR_OR_VALUE, "For datum column " + datum.getColumnName() + " seperator "
						+ separator + " could not be used for string value " + value);
			}
		}

		return StringHelper.trimExtended(value);
	}

	/**
	 * Finds the value of the current datum from the page and returns it
	 * 
	 * @param datum
	 * @param node
	 * @return
	 * @throws CrawlerException
	 * @throws ParseException
	 */
	protected Object getValue(Datum datum, Node node) throws CrawlerException, ParseException
	{
		String value = getStringValueOfDatum(datum, node);

		DataColumn columnForDatum = dataColumnMap.get(datum.getColumnName());
		return stringWithColumnType2Object(value, columnForDatum);
	}

	/**
	 * Constructs an object from the given string and the column type
	 * 
	 * @param value
	 * @param columnType
	 * @return
	 * @throws CrawlerException
	 * @throws ParseException
	 */
	protected Object stringWithColumnType2Object(String value, DataColumn column) throws CrawlerException, ParseException
	{
		Object result = null;
		String columnType = column.getValueColumnType();
		if (columnType.equals(DBUtil.COLUMN_TYPE_INTEGER))
		{
			try
			{
				result = new Integer(value);
			}
			catch (NumberFormatException e)
			{
				result = 0;
			}
		}
		else if (columnType.equals(DBUtil.COLUMN_TYPE_TIMESTAMP))
		{
			result = DateHelper.parse(value);
		}
		else if (columnType.equals(DBUtil.COLUMN_TYPE_STRING))
		{
			result = value;
			
			if (columnType.equals(DBUtil.COLUMN_TYPE_STRING) && ((String) value).length() > column.getLength())
			{
				value = ((String) value).substring(0, column.getLength() - 1);
			}
		}
		else
		{
			throw new CrawlerException(CrawlerExceptionType.UNEXPECTED_COLUMN_TYPE_FOR_DATUM, "Unexpected column type " + columnType + " for the value " + value);
		}

		return result;
	}

	/**
	 * Finds the model of the current page to use the respective xpath
	 * expressions
	 * 
	 * @param doc
	 * @return
	 * @throws CrawlerException
	 */
	protected DataPageModel findModelOfPage(Document doc, Object code) throws CrawlerException
	{
		DataPage dataPage = site.getDataPage();
		List<DataPageModel> models = dataPage.getModels();
		if (models == null)
		{
			return null;
		}

		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		XPathExpression expr;
		NodeList shows;
		int notEmptyUniqueColumnCount = 0;
		List<DataPageModel> resultModels = new ArrayList<DataPageModel>();

		for (DataPageModel model : models)
		{
			List<Datum> data = model.getData();
			notEmptyUniqueColumnCount = 0;
			for (Datum datum : data)
			{
				if (dataColumnMap.get(datum.getColumnName()) != null && dataColumnMap.get(datum.getColumnName()).isUsableForUniqueness())
				{
					try
					{
						expr = xPath.compile(datum.getXPathExpression());
						shows = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

						if (shows.getLength() == 1 || (shows.getLength() > 0 && model.getMultiResultTable() != null))
						{
							Node node = shows.item(0);
							try
							{
								stringWithColumnType2Object(getStringValueOfDatum(datum, node), dataColumnMap.get(datum.getColumnName()));
								notEmptyUniqueColumnCount++;
							}
							catch (ParseException e)
							{
								// Do not increase the count as the value is not
								// an expected type
							}
						}
					}
					catch (XPathExpressionException xe)
					{
						throw new CrawlerException(CrawlerExceptionType.MALFORMED_XPATH_EXPRESSION, xe);
					}
				}
			}

			if (notEmptyUniqueColumnCount == noOfUniqueDataColumn)
			{
				resultModels.add(model);
			}
		}

		// Expected result is 1 model
		if (resultModels.size() == 1)
		{
			return resultModels.get(0);
		}
		else if (resultModels.size() == 0)
		{
			return null;
		}
		else
		{
			String extraInfo = "";
			for (DataPageModel eachModel : resultModels)
			{
				extraInfo += eachModel.getName() + " ";
			}

			throw new CrawlerException(CrawlerExceptionType.MORE_THAN_ONE_MODEL_RETURNED, "Job Code : " + code + "\nNumber of returned models is " + resultModels.size()
					+ " which should be one. Returned model names are " + extraInfo);
		}
	}

	protected Document getDocument(InputStream stream)
	{
		return DOMHelper.getDocument(stream, site.getDomParser(), site.getEncoding());
	}

	protected InputStream getStream(String urlStr) throws IOException
	{
		URL url = new URL(urlStr);
		return url.openStream();
	}
}
