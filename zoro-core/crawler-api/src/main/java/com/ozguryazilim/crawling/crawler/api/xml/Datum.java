package com.ozguryazilim.crawling.crawler.api.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", factoryMethod = "getNewInstance")
@XmlRootElement(name = "datum")
public class Datum
{

	@XmlAttribute(name = "columnName", required = true)
	private String	columnName;

	@XmlAttribute(name = "xPathExpression", required = true)
	private String	xPathExpression;

	@XmlAttribute(name = "captureMethod", required = true)
	private String	captureMethod;

	@XmlAttribute(name = "separator")
	private String	separator;

	@XmlAttribute(name = "embedIndex")
	private int		embedIndex	= 1;

	@XmlAttribute(name = "multiResult")
	private boolean	multiResult	= false;

	/**
	 * @return the columnName
	 */
	public String getColumnName()
	{
		return columnName;
	}

	/**
	 * @param columnName
	 *            the columnName to set
	 */
	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	/**
	 * @return the xPathExpression
	 */
	public String getXPathExpression()
	{
		return xPathExpression;
	}

	/**
	 * @param xPathExpression
	 *            the xPathExpression to set
	 */
	public void setXPathExpression(String xPathExpression)
	{
		this.xPathExpression = xPathExpression;
	}

	/**
	 * @return the captureMethod
	 */
	public String getCaptureMethod()
	{
		return captureMethod;
	}

	/**
	 * @param captureMethod
	 *            the captureMethod to set
	 */
	public void setCaptureMethod(String captureMethod)
	{
		this.captureMethod = captureMethod;
	}

	/**
	 * @param seperator
	 *            the separator to set
	 */
	public void setSeparator(String separator)
	{
		this.separator = separator;
	}

	/**
	 * @return the separator
	 */
	public String getSeparator()
	{
		return separator;
	}

	/**
	 * @param embedIndex
	 *            the embedIndex to set
	 */
	public void setEmbedIndex(int embedIndex)
	{
		this.embedIndex = embedIndex;
	}

	/**
	 * @return the embedIndex
	 */
	public int getEmbedIndex()
	{
		return embedIndex;
	}

	/**
	 * @return the multiResult
	 */
	public boolean isMultiResult()
	{
		return multiResult;
	}

	/**
	 * @param multiResult the multiResult to set
	 */
	public void setMultiResult(boolean multiResult)
	{
		this.multiResult = multiResult;
	}

	public static Datum getNewInstance()
	{
		return new Datum();
	}
}
