package com.ozguryazilim.crawling.crawler.api.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", factoryMethod = "getNewInstance")
@XmlRootElement(name = "uniqueColumns")
public class UniqueColumns
{

	@XmlElement(name = "dataColumn")
	private List<DataColumn>	columns;

	/**
	 * @param columns
	 *            the columns to set
	 */
	public void setColumns(List<DataColumn> columns)
	{
		this.columns = columns;
	}

	/**
	 * @return the columns
	 */
	public List<DataColumn> getColumns()
	{
		return columns;
	}

	public static UniqueColumns getNewInstance()
	{
		return new UniqueColumns();
	}
}
