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
@XmlRootElement(name = "dataPageModel")
public class DataPageModel
{
	@XmlAttribute(name = "name", required = true)
	private String		name;

	@XmlAttribute(name = "multiResultTable", required = false)
	private String		multiResultTable;

	@XmlElement(name = "datum")
	private List<Datum>	data;

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
	 * @return the multiResultTable
	 */
	public String getMultiResultTable()
	{
		return multiResultTable;
	}

	/**
	 * @param multiResultTable
	 *            the multiResultTable to set
	 */
	public void setMultiResultTable(String multiResultTable)
	{
		this.multiResultTable = multiResultTable;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<Datum> data)
	{
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public List<Datum> getData()
	{
		return data;
	}

	public static DataPageModel getNewInstance()
	{
		return new DataPageModel();
	}
}
