/**
 * This class holds the most detailed information about the site to be crawled.
 * Each field which will be used to extract information from a web page.
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
@XmlRootElement(name = "field")
public class Field implements IDataColumn
{
	@XmlAttribute(name = "id", required = true)
	private String			id;

	@XmlAttribute(name = "idColumnType")
	private String			idColumnType;

	@XmlAttribute(name = "valueColumnType")
	private String			valueColumnType;

	@XmlAttribute(name = "type")
	private String			type;

	@XmlAttribute(name = "ignored")
	private boolean			ignored				= false;

	@XmlAttribute(name = "relation", required = true)
	private String			relation;

	@XmlAttribute(name = "columnName")
	private String			columnName;

	@XmlAttribute(name = "valueColumnLength")
	private int				valueColumnLength;

	@XmlElement(name = "option")
	private List<Option>	options;

	@XmlAttribute(name = "queryKey")
	private String			queryKey;

	@XmlAttribute(name = "fieldDataTable")
	private String			fieldDataTable;

	@XmlAttribute(name = "crossTable")
	private String			crossTable;

	@XmlAttribute(name = "inverseColumnName")
	private String			inverseColumnName;

	@XmlAttribute(name = "minvalue")
	private int				minvalue;

	@XmlAttribute(name = "maxValue")
	private int				maxValue;

	@XmlAttribute(name = "incrementBy")
	private int				incrementBy;

	@XmlAttribute(name = "preJSCall")
	private String			preJSCall;

	@XmlAttribute(name = "indexToSet")
	private int				indexToSet;

	@XmlAttribute(name = "dummyColumn")
	private boolean			dummyColumn			= false;

	@XmlAttribute(name = "idAutoIncremented")
	private boolean			idAutoIncremented	= false;

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param idColumnType
	 *            the idColumnType to set
	 */
	public void setIdColumnType(String idColumnType)
	{
		this.idColumnType = idColumnType;
	}

	/**
	 * @return the idColumnType
	 */
	public String getIdColumnType()
	{
		return idColumnType;
	}

	/**
	 * @param valueColumnType
	 *            the valueColumnType to set
	 */
	public void setValueColumnType(String valueColumnType)
	{
		this.valueColumnType = valueColumnType;
	}

	/**
	 * @return the valueColumnType
	 */
	public String getValueColumnType()
	{
		return valueColumnType;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param ignored
	 *            the ignored to set
	 */
	public void setIgnored(boolean ignored)
	{
		this.ignored = ignored;
	}

	/**
	 * @return the ignored
	 */
	public boolean isIgnored()
	{
		return ignored;
	}

	/**
	 * @param relation
	 *            the relation to set
	 */
	public void setRelation(String relation)
	{
		this.relation = relation;
	}

	/**
	 * @return the relation
	 */
	public String getRelation()
	{
		return relation;
	}

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
	 * @param columnLength
	 *            the columnLength to set
	 */
	public void setColumnLength(int columnLength)
	{
		this.valueColumnLength = columnLength;
	}

	/**
	 * @return the columnLength
	 */
	public int getColumnLength()
	{
		return valueColumnLength;
	}

	/**
	 * @param options
	 *            the options to set
	 */
	public void setOptions(List<Option> options)
	{
		this.options = options;
	}

	/**
	 * @return the options
	 */
	public List<Option> getOptions()
	{
		return options;
	}

	/**
	 * @param queryKey
	 *            the queryKey to set
	 */
	public void setQueryKey(String queryKey)
	{
		this.queryKey = queryKey;
	}

	/**
	 * @return the queryKey
	 */
	public String getQueryKey()
	{
		return queryKey;
	}

	/**
	 * @return the fieldDataTable
	 */
	public String getFieldDataTable()
	{
		return fieldDataTable;
	}

	/**
	 * @param fieldDataTable
	 *            the fieldDataTable to set
	 */
	public void setFieldDataTable(String fieldDataTable)
	{
		this.fieldDataTable = fieldDataTable;
	}

	/**
	 * @return the crossTable
	 */
	public String getCrossTable()
	{
		return crossTable;
	}

	/**
	 * @param crossTable
	 *            the crossTable to set
	 */
	public void setCrossTable(String crossTable)
	{
		this.crossTable = crossTable;
	}

	/**
	 * @return the inverseColumnName
	 */
	public String getInverseColumnName()
	{
		return inverseColumnName;
	}

	/**
	 * @param inverseColumnName
	 *            the inverseColumnName to set
	 */
	public void setInverseColumnName(String inverseColumnName)
	{
		this.inverseColumnName = inverseColumnName;
	}

	/**
	 * @return the minvalue
	 */
	public int getMinvalue()
	{
		return minvalue;
	}

	/**
	 * @param minvalue
	 *            the minvalue to set
	 */
	public void setMinvalue(int minvalue)
	{
		this.minvalue = minvalue;
	}

	/**
	 * @return the maxValue
	 */
	public int getMaxValue()
	{
		return maxValue;
	}

	/**
	 * @param maxValue
	 *            the maxValue to set
	 */
	public void setMaxValue(int maxValue)
	{
		this.maxValue = maxValue;
	}

	/**
	 * @param incrementBy
	 *            the incrementBy to set
	 */
	public void setIncrementBy(int incrementBy)
	{
		this.incrementBy = incrementBy;
	}

	/**
	 * @return the incrementBy
	 */
	public int getIncrementBy()
	{
		return incrementBy;
	}

	/**
	 * @return the preJSCall
	 */
	public String getPreJSCall()
	{
		return preJSCall;
	}

	/**
	 * @param preJSCall
	 *            the preJSCall to set
	 */
	public void setPreJSCall(String preJSCall)
	{
		this.preJSCall = preJSCall;
	}

	/**
	 * @param dummyColumn
	 *            the dummyColumn to set
	 */
	public void setDummyColumn(boolean dummyColumn)
	{
		this.dummyColumn = dummyColumn;
	}

	/**
	 * @return the dummyColumn
	 */
	public boolean isDummyColumn()
	{
		return dummyColumn;
	}

	/**
	 * @return the indexToSet
	 */
	public int getIndexToSet()
	{
		return indexToSet;
	}

	/**
	 * @param indexToSelect
	 *            the indexToSelect to set
	 */
	public void setIndexToSet(int indexToSet)
	{
		this.indexToSet = indexToSet;
	}

	public void setIdAutoIncremented(boolean idAutoIncremented)
	{
		this.idAutoIncremented = idAutoIncremented;
	}

	public boolean isIdAutoIncremented()
	{
		return idAutoIncremented;
	}

	public static Field getNewInstance()
	{
		return new Field();
	}
}
