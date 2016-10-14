/**
 * Site class holds the general information about a site to crawl. The table
 * referenced, URL and the mainly used document.form id prefix is given here. A
 * Site instance hold information about the fields to be crawled and persist to
 * the DB.
 * 
 * @author aaslannn
 */

package com.ozguryazilim.crawling.crawler.api.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", factoryMethod = "getNewInstance")
@XmlRootElement(name = "site")
public class Site
{
	@XmlAttribute(name = "name")
	private String			name;

	@XmlAttribute(name = "url")
	private String			url;

	@XmlAttribute(name = "secondaryUrl")
	private String			secondaryUrl;

	@XmlAttribute(name = "table")
	private String			table;

	@XmlAttribute(name = "jobCodeColumnName")
	private String			jobCodeColumnName;

	@XmlAttribute(name = "jobCodeColumnType")
	private String			jobCodeColumnType;

	@XmlAttribute(name = "jobCodeColumnLength")
	private int				jobCodeColumnLength;

	@XmlAttribute(name = "formPrefix")
	private String			formPrefix;

	@XmlElement(name = "field")
	private List<Field>		fields;

	@XmlElement(name = "dataPage")
	private DataPage		dataPage;

	@XmlElement(name = "dataStream")
	private DataStream		dataStream;

	@XmlAttribute(name = "pageLinksXPathExpression", required = true)
	private String			pageLinksXPathExpression;

	@XmlAttribute(name = "startOfCodeInLink", required = true)
	private String			startOfCodeInLink;

	@XmlAttribute(name = "endOfCodeInLink")
	private String			endOfCodeInLink;

	@XmlAttribute(name = "encoding")
	private String			encoding;

	@XmlAttribute(name = "domParser")
	private String			domParser;

	@XmlElement(name = "uniqueColumns")
	private UniqueColumns	uniqueColumns;

	@XmlAttribute(name = "crawl")
	private boolean			crawl	= true;

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param secondaryUrl
	 *            the secondaryUrl to set
	 */
	public void setSecondaryUrl(String secondaryUrl)
	{
		this.secondaryUrl = secondaryUrl;
	}

	/**
	 * @return the secondaryUrl
	 */
	public String getSecondaryUrl()
	{
		return secondaryUrl;
	}

	/**
	 * @return the table
	 */
	public String getTable()
	{
		return table;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public void setTable(String table)
	{
		this.table = table;
	}

	/**
	 * @param jobCodeColumnName
	 *            the jobCodeColumnName to set
	 */
	public void setJobCodeColumnName(String jobCodeColumnName)
	{
		this.jobCodeColumnName = jobCodeColumnName;
	}

	/**
	 * @return the jobCodeColumnName
	 */
	public String getJobCodeColumnName()
	{
		return jobCodeColumnName;
	}

	/**
	 * @param jobCodeColumnType
	 *            the jobCodeColumnType to set
	 */
	public void setJobCodeColumnType(String jobCodeColumnType)
	{
		this.jobCodeColumnType = jobCodeColumnType;
	}

	/**
	 * @return the jobCodeColumnType
	 */
	public String getJobCodeColumnType()
	{
		return jobCodeColumnType;
	}

	/**
	 * @param jobCodeColumnLength
	 *            the jobCodeColumnLength to set
	 */
	public void setJobCodeColumnLength(int jobCodeColumnLength)
	{
		this.jobCodeColumnLength = jobCodeColumnLength;
	}

	/**
	 * @return the jobCodeColumnLength
	 */
	public int getJobCodeColumnLength()
	{
		return jobCodeColumnLength;
	}

	/**
	 * @param formPrefix
	 *            the formPrefix to set
	 */
	public void setFormPrefix(String formPrefix)
	{
		this.formPrefix = formPrefix;
	}

	/**
	 * @return the formPrefix
	 */
	public String getFormPrefix()
	{
		return formPrefix;
	}

	/**
	 * @return the fields
	 */
	public List<Field> getFields()
	{
		return fields;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(List<Field> fields)
	{
		this.fields = fields;
	}

	/**
	 * @param dataPage
	 *            the dataPage to set
	 */
	public void setDataPage(DataPage dataPage)
	{
		this.dataPage = dataPage;
	}

	/**
	 * @return the dataPage
	 */
	public DataPage getDataPage()
	{
		return dataPage;
	}

	/**
	 * @param dataStream
	 *            the dataStream to set
	 */
	public void setDataStream(DataStream dataStream)
	{
		this.dataStream = dataStream;
	}

	/**
	 * @return the dataStream
	 */
	public DataStream getDataStream()
	{
		return dataStream;
	}

	/**
	 * @param pageLinksXPathExpression
	 *            the pageLinksXPathExpression to set
	 */
	public void setPageLinksXPathExpression(String pageLinksXPathExpression)
	{
		this.pageLinksXPathExpression = pageLinksXPathExpression;
	}

	/**
	 * @return the pageLinksXPathExpression
	 */
	public String getPageLinksXPathExpression()
	{
		return pageLinksXPathExpression;
	}

	/**
	 * @param startOfCodeInLink
	 *            the startOfCodeInLink to set
	 */
	public void setStartOfCodeInLink(String startOfCodeInLink)
	{
		this.startOfCodeInLink = startOfCodeInLink;
	}

	/**
	 * @return the startOfCodeInLink
	 */
	public String getStartOfCodeInLink()
	{
		return startOfCodeInLink;
	}

	/**
	 * @param endOfCodeInLink
	 *            the endOfCodeInLink to set
	 */
	public void setEndOfCodeInLink(String endOfCodeInLink)
	{
		this.endOfCodeInLink = endOfCodeInLink;
	}

	/**
	 * @return the endOfCodeInLink
	 */
	public String getEndOfCodeInLink()
	{
		return endOfCodeInLink;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding()
	{
		return encoding;
	}

	/**
	 * @param domParser
	 *            the domParser to set
	 */
	public void setDomParser(String domParser)
	{
		this.domParser = domParser;
	}

	/**
	 * @return the domParser
	 */
	public String getDomParser()
	{
		return domParser;
	}

	/**
	 * @param uniqueColumns
	 *            the uniqueColumns to set
	 */
	public void setUniqueColumns(UniqueColumns uniqueColumns)
	{
		this.uniqueColumns = uniqueColumns;
	}

	/**
	 * @return the uniqueColumns
	 */
	public UniqueColumns getUniqueColumns()
	{
		return uniqueColumns;
	}

	/**
	 * @param crawl the crawl to set
	 */
	public void setCrawl(boolean crawl)
	{
		this.crawl = crawl;
	}

	/**
	 * @return the crawl
	 */
	public boolean isCrawl()
	{
		return crawl;
	}

	public static Site getNewInstance()
	{
		return new Site();
	}
}
