/**
 * Option class holds the data to be held in a different table than the main
 * crawled information. This class holds a id&value pair where the id is
 * referred from whether main table or a table which is referred by main table.
 * 
 * @author aaslannn
 */
package com.ozguryazilim.crawling.crawler.api.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", factoryMethod = "getNewInstance")
@XmlRootElement(name = "option")
public class Option
{
	@XmlAttribute(name = "id")
	private String	id;

	@XmlAttribute(name = "value")
	private String	value;

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	public static Option getNewInstance()
	{
		return new Option();
	}

}
