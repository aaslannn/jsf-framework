package com.ozguryazilim.crawling.crawler.api.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractDataStream
{
	@XmlElement(name = "dataColumn")
	private List<DataColumn>	dataColumns;

	@XmlAttribute(name = "tokenizerSeparator", required = true)
	private String				tokenizerSeparator;

	@XmlAttribute(name = "excludeEndingStartingWith")
	private String				excludeEndingStartingWith;

	@XmlAttribute(name = "excludePartEndingWith")
	private String				excludePartEndingWith;

	@XmlAttribute(name = "removeTextList")
	private String				removeTextList;

	/**
	 * @param dataColumns
	 *            the dataColumns to set
	 */
	public void setDataColumns(List<DataColumn> dataColumns)
	{
		this.dataColumns = dataColumns;
	}

	/**
	 * @return the dataColumns
	 */
	public List<DataColumn> getDataColumns()
	{
		return dataColumns;
	}

	/**
	 * @param tokenizerSeparator
	 *            the tokenizerSeparator to set
	 */
	public void setTokenizerSeparator(String tokenizerSeparator)
	{
		this.tokenizerSeparator = tokenizerSeparator;
	}

	/**
	 * @return the tokenizerSeparator
	 */
	public String getTokenizerSeparator()
	{
		return tokenizerSeparator;
	}

	/**
	 * @param excludeEndingStartingWith
	 *            the excludeEndingStartingWith to set
	 */
	public void setExcludeEndingStartingWith(String excludeEndingStartingWith)
	{
		this.excludeEndingStartingWith = excludeEndingStartingWith;
	}

	/**
	 * @return the excludeEndingStartingWith
	 */
	public String getExcludeEndingStartingWith()
	{
		return excludeEndingStartingWith;
	}

	/**
	 * @param excludePartEndingWith
	 *            the excludePartEndingWith to set
	 */
	public void setExcludePartEndingWith(String excludePartEndingWith)
	{
		this.excludePartEndingWith = excludePartEndingWith;
	}

	/**
	 * @return the excludePartEndingWith
	 */
	public String getExcludePartEndingWith()
	{
		return excludePartEndingWith;
	}

	/**
	 * @param removeTextList
	 *            the removeTextList to set
	 */
	public void setRemoveTextList(String removeTextList)
	{
		this.removeTextList = removeTextList;
	}

	/**
	 * @return the removeTextList
	 */
	public String getRemoveTextList()
	{
		return removeTextList;
	}
}
