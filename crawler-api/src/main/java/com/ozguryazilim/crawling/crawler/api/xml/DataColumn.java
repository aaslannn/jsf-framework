package com.ozguryazilim.crawling.crawler.api.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", factoryMethod = "getNewInstance")
@XmlRootElement(name = "dataColumn")
public class DataColumn implements IDataColumn
{
	@XmlAttribute(name = "id", required = true)
	private String			id;
	
	@XmlAttribute(name = "idColumnType")
	private String			idColumnType;
	
	@XmlAttribute(name = "valueColumnType")
	private String			valueColumnType;

	@XmlAttribute(name = "name", required = true)
	private String	name;

	@XmlAttribute(name = "length")
	private int		length;

	@XmlAttribute(name = "secondaryColumn")
	private String	secondaryColumn;

	@XmlAttribute(name = "usableForUniqueness")
	private boolean	usableForUniqueness	= false;

	@XmlAttribute(name = "defaultTokenizer", required = true)
	private String	defaultTokenizer;

	@XmlAttribute(name = "dummyColumn")
	private boolean	dummyColumn			= false;

	@XmlAttribute(name = "persistable")
	private boolean	persistable			= true;

	@XmlAttribute(name = "tableName")
	private String			tableName;

	@XmlAttribute(name = "crossTable")
	private String			crossTable;

	@XmlAttribute(name = "inverseColumnName")
	private String			inverseColumnName;

	@XmlAttribute(name = "relation", required = true)
	private String			relation;

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
	 * @param idColumnType the idColumnType to set
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
	 * @param valueColumnType the valueColumnType to set
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
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the length
	 */
	public int getLength()
	{
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(int length)
	{
		this.length = length;
	}

	/**
	 * @param secondaryColumn
	 *            the secondaryColumn to set
	 */
	public void setSecondaryColumn(String secondaryColumn)
	{
		this.secondaryColumn = secondaryColumn;
	}

	/**
	 * @return the secondaryColumn
	 */
	public String getSecondaryColumn()
	{
		return secondaryColumn;
	}

	/**
	 * @param usableForUniqueness
	 *            the usableForUniqueness to set
	 */
	public void setUsableForUniqueness(boolean usableForUniqueness)
	{
		this.usableForUniqueness = usableForUniqueness;
	}

	/**
	 * @return the usableForUniqueness
	 */
	public boolean isUsableForUniqueness()
	{
		return usableForUniqueness;
	}

	/**
	 * @param defaultTokenizer
	 *            the defaultTokenizer to set
	 */
	public void setDefaultTokenizer(String defaultTokenizer)
	{
		this.defaultTokenizer = defaultTokenizer;
	}

	/**
	 * @return the defaultTokenizer
	 */
	public String getDefaultTokenizer()
	{
		return defaultTokenizer;
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
	 * @param persistable
	 *            the persistable to set
	 */
	public void setPersistable(boolean persistable)
	{
		this.persistable = persistable;
	}

	/**
	 * @return the persistable
	 */
	public boolean isPersistable()
	{
		return persistable;
	}

	/**
	 * @return the fieldDataTable
	 */
	public String getFieldDataTable()
	{
		return tableName;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName()
	{
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
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
	 * @param relation
	 *            the relation to set
	 */
	public void setRelation(String relation)
	{
		this.relation = relation;
	}

	@Override
	public int getColumnLength()
	{
		return length;
	}

	@Override
	public String getColumnName()
	{
		return name;
	}

	/**
	 * @return the relation
	 */
	public String getRelation()
	{
		return relation;
	}

	public static DataColumn getNewInstance()
	{
		return new DataColumn();
	}
}
