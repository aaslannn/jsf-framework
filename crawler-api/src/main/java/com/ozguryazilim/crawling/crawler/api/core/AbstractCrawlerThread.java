package com.ozguryazilim.crawling.crawler.api.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ozguryazilim.crawling.crawler.api.db.DBUtil;
import com.ozguryazilim.crawling.crawler.api.db.Persister;
import com.ozguryazilim.crawling.crawler.api.exception.CrawlerException;
import com.ozguryazilim.crawling.crawler.api.exception.CrawlerException.CrawlerExceptionType;
import com.ozguryazilim.crawling.crawler.api.helper.DOMHelper;
import com.ozguryazilim.crawling.crawler.api.helper.StringHelper;
import com.ozguryazilim.crawling.crawler.api.xml.Field;
import com.ozguryazilim.crawling.crawler.api.xml.Site;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;

public abstract class AbstractCrawlerThread extends Thread implements IConstants
{
	private static Logger		logger					= Logger.getLogger(AbstractCrawlerThread.class);

	protected Site				site					= null;
	protected Selenium			selenium				= null;
	protected Field				field					= null;

	private final static String	LAST_UPDATE_TIME		= "SELECT %s FROM %s WHERE %s = %s";
	private final static String	UPDATE_LAST_UPDATE_TIME	= "UPDATE %s SET %s = ? WHERE %s = ?";

	public final static String	FIREFOX_PATH			= "*firefox /usr/lib64/firefox/firefox";

	/**
	 * Initializes the selenium
	 */
	protected void initSelenium()
	{
		try
		{
			selenium.open(site.getUrl());
		}
		catch (SeleniumException e)
		{
			// Try to open one more time
			logger.warn("Re-trying to open : " + site.getUrl());
			selenium.open(site.getUrl());
		}

		if (field != null)
		{
			String preJSCall = field.getPreJSCall();

			// Call the pre js call if exists
			if (preJSCall != null && StringHelper.trimExtended(preJSCall).length() != 0)
			{
				selenium.runScript(preJSCall);
			}
		}
	}

	/**
	 * Runs the simple "document.x.y.z.length = 0" like scripts
	 * 
	 * @param queryKey
	 * @param script
	 */
	protected void runSimleScript(String queryKey, String script)
	{
		selenium.runScript(site.getFormPrefix() + queryKey + script);
	}

	/**
	 * Evaluates the simple "document.x.y.z.value;" like scripts
	 * 
	 * @param queryKey
	 * @param script
	 * @return
	 */
	protected String getEvalSimpleScript(String queryKey, String script)
	{
		return selenium.getEval(getWindowPrefix() + queryKey + script);
	}

	/**
	 * Returns a string .options[index]
	 * 
	 * @param index
	 * @return
	 */
	protected String getOption(int index)
	{
		return ".options[" + index + "]";
	}

	/**
	 * Gets the value of the option from the client script
	 * 
	 * @param queryKey
	 * @param index
	 * @return
	 */
	protected String getOptionValue(String queryKey, int index)
	{
		return getEvalSimpleScript(queryKey, getOption(index) + ".value");
	}

	/**
	 * Gets the text of the option from the client script
	 * 
	 * @param queryKey
	 * @param index
	 * @return
	 */
	protected String getOptionText(String queryKey, int index)
	{
		return getEvalSimpleScript(queryKey, getOption(index) + ".text");
	}

	/**
	 * Sets the length of the options to 0
	 * 
	 * @param queryKey
	 */
	protected void resetOption(String queryKey)
	{
		runSimleScript(queryKey, ".options.length = 0;");
	}

	/**
	 * Returns the string representation of the boolean value
	 * 
	 * @param value
	 * @return
	 */
	protected String boolean2Str(boolean value)
	{
		String valueStr = "true";
		if (!value)
		{
			valueStr = "false";
		}

		return valueStr;
	}

	/**
	 * Calls prefix.queryKey[index].checked = value;
	 * 
	 * @param queryKey
	 * @param index
	 * @param value
	 */
	protected void setButtonChecked(String queryKey, int index, boolean value)
	{
		runSimleScript(queryKey, "[" + index + "].checked = " + boolean2Str(value) + ";");
	}

	/**
	 * Calls the prefix.queryKey.option[index].selected = value
	 * 
	 * @param queryKey
	 * @param index
	 * @param value
	 */
	protected void setOptionSelected(String queryKey, int index, boolean value)
	{
		runSimleScript(queryKey, getOption(index) + ".selected = " + boolean2Str(value) + ";");
	}

	/**
	 * Sets the checked attribute to the given value
	 * 
	 * @param queryKey
	 * @param value
	 */
	protected void setChecked(String queryKey, boolean value)
	{
		runSimleScript(queryKey, ".checked = " + boolean2Str(value) + ";");
	}

	/**
	 * Runs the query prefix.queryKey.option[index].value = value;
	 * 
	 * @param queryKey
	 * @param index
	 * @param value
	 */
	protected void setOptionValue(String queryKey, int index, String value)
	{
		runSimleScript(queryKey, getOption(index) + ".value = '" + value + "';");
	}

	/**
	 * Sets the value of the given component
	 * 
	 * @param queryKey
	 * @param value
	 */
	protected void setValue(String queryKey, String value)
	{
		runSimleScript(queryKey, ".value = '" + value + "';");
	}

	/**
	 * Returns the prefix to be used in getValue scripts
	 * 
	 * @return
	 */
	protected String getWindowPrefix()
	{
		return "window." + site.getFormPrefix();
	}

	protected boolean checkNeedsUpdate(Field field, Object optionId) throws CrawlerException, SQLException
	{
		String tableName = field.getFieldDataTable();
		String relation = field.getRelation();
		boolean result = false;
		if (!(relation.equals(RELATION_MANY_TO_ONE) || relation.equals(RELATION_MANY_TO_MANY)))
		{
			throw new CrawlerException(CrawlerExceptionType.INCOMPATIBLE_FIELD_RELATION, "Check needs update culd only be run on relation ManytoMany or ManytoOne");
		}

		String value = "";
		String idColumnType = field.getIdColumnType();
		if (idColumnType.equals(DBUtil.COLUMN_TYPE_INTEGER))
		{
			value = optionId + "";
		}
		else if (idColumnType.equals(DBUtil.COLUMN_TYPE_STRING))
		{
			value = "'" + optionId + "'";
		}

		ResultSet resultSet = Persister.executeQuery(String.format(LAST_UPDATE_TIME, DBUtil.FLD_LAST_UPDATE, tableName, DBUtil.FLD_ID, value));
		try
		{
			if (resultSet.next())
			{
				Timestamp lastUpdate = (Timestamp) resultSet.getTimestamp(DBUtil.FLD_LAST_UPDATE);
				if (lastUpdate == null || lastUpdate.equals(0))
				{
					result = true;
				}
				else
				{
					long diff = (new Date()).getTime() - lastUpdate.getTime();
					if (diff > ONE_DAY)
					{
						result = true;
					}
				}
			}
		}
		finally
		{
			resultSet.getStatement().getConnection().close();
		}

		return result;
	}

	protected void updateLastUpdateTime(Field field, Object optionId) throws SQLException
	{
		String query = String.format(UPDATE_LAST_UPDATE_TIME, field.getFieldDataTable(), DBUtil.FLD_LAST_UPDATE, DBUtil.FLD_ID);
		PreparedStatement statement = Persister.getPreparedStatment(query);
		statement.setTimestamp(1, new Timestamp(new Date().getTime()));
		statement.setObject(2, optionId);
		Persister.executePreparedUpdateStatement(statement);
	}

	/**
	 * Checks whether the current page is an expected page
	 * 
	 * @return
	 */
	protected abstract boolean isAValidPage();

	/**
	 * Returns the document using the HTML Cleaner or JTidy according to the
	 * page implementation
	 * 
	 * @param stream
	 * @return
	 */
	protected Document getDocument(InputStream stream)
	{
		return DOMHelper.getDocument(stream, site.getDomParser(), site.getEncoding());
	}

	/**
	 * Returns the links at the given stream
	 * 
	 * @param stream
	 * @return
	 */
	protected List<String> getCodesOfPage(InputStream stream)
	{
		return getLinksFromPage(stream, site.getPageLinksXPathExpression(), site.getStartOfCodeInLink(), site.getEndOfCodeInLink());
	}

	protected List<String> getLinksFromPage(InputStream stream, String linkXPathExpression, String startOfInnerText, String endOfInnerText)
	{
		List<String> result = new ArrayList<String>();
		XPathFactory factory = XPathFactory.newInstance();
		Document doc = getDocument(stream);
		XPathExpression expr;
		XPath xPath = factory.newXPath();
		try
		{
			expr = xPath.compile(linkXPathExpression);

			NodeList links = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < links.getLength(); i++)
			{
				Node item = links.item(i);
				if (item != null)
				{
					String link = item.getAttributes().getNamedItem("href").getNodeValue();
					String code = getJobCodeFromLink(link, startOfInnerText, endOfInnerText);

					if (code != null)
					{
						result.add(code);
					}
				}
			}
		}
		catch (XPathExpressionException e)
		{
			logger.fatal("XPath expression is not well formed : " + linkXPathExpression);
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Returns the links from the below table at the current page of selenium
	 * 
	 * @return links on the current page
	 */
	protected List<String> getLinks()
	{
		String allPage = selenium.getHtmlSource();

		return getCodesOfPage(new ByteArrayInputStream(allPage.getBytes()));
	}

	/**
	 * Gets the link as the input and takes out the job code part of the link
	 * and returns he job code
	 * 
	 * @param link
	 * @return code of the job which exists in the link
	 */
	protected String getJobCodeFromLink(String link, String startOfCode, String endOfCode)
	{
		return StringHelper.getInnerTextFromLongText(link, startOfCode, endOfCode);
	}

	/**
	 * Returns the total number of the pages
	 * 
	 * @return
	 */
	protected abstract int getTotalNumberOfPages();

	/**
	 * Calls wait for page load function for the given pageLoadWait time and
	 * after waiting load, calls for sleepWait milliseconds to sleep the tread
	 * 
	 * @param pageLoadWait
	 * @param sleepWait
	 * @throws CrawlerException
	 * @throws InterruptedException
	 */
	protected void waitPageLoadThenSleep(String pageLoadWait, long sleepWait) throws CrawlerException, InterruptedException
	{
		try
		{
			selenium.waitForPageToLoad(pageLoadWait);
			if (sleepWait != 0)
			{
				Thread.sleep(sleepWait);
			}
		}
		catch (SeleniumException se)
		{
			if (!isAValidPage())
			{
				throw new CrawlerException(CrawlerExceptionType.UNEXPECTED_PAGE_LOADED, se);
			}
		}
	}

	/**
	 * A function to be used if a stream is required from a urlStr
	 * 
	 * @param urlStr
	 * @return
	 */
	protected InputStream getStreamOfUrl(String urlStr)
	{
		URL url;
		InputStream result = null;
		try
		{
			url = new URL(urlStr);
			result = url.openStream();
		}
		catch (MalformedURLException e)
		{
			logger.fatal("URL is malformed : " + urlStr);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Returns the list of nodes which is found in the given url applying to
	 * given xpath expression
	 * 
	 * @param url
	 *            String
	 * @param xpath
	 *            String
	 * @return List<Node>
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	protected List<Node> getNodesFromURL(String url, String xpath) throws MalformedURLException, IOException, XPathExpressionException
	{
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();

		Document doc = getDocument(getStreamOfUrl(url));
		XPathExpression expr = xPath.compile(xpath);

		NodeList shows = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		List<Node> result = new ArrayList<Node>();
		for (int i = 0; i < shows.getLength(); i++)
		{
			Node node = shows.item(i);
			result.add(node);
		}

		return result;
	}
}
