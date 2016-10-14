package com.ozguryazilim.crawling.crawler.api.xml;

public interface IDataColumn
{
	/**
	 * @return the id
	 */
	public String getId();
	
	/**
	 * @return the idColumnType
	 */
	public String getIdColumnType();
	
	/**
	 * @return the valueColumnType
	 */
	public String getValueColumnType();
	
	/**
	 * @return the relation
	 */
	public String getRelation();
	
	/**
	 * @return the columnName
	 */
	public String getColumnName();
	
	/**
	 * @return the columnLength
	 */
	public int getColumnLength();
	
	/**
	 * @return the fieldDataTable
	 */
	public String getFieldDataTable();
	
	/**
	 * @return the crossTable
	 */
	public String getCrossTable();
	
	/**
	 * @return the inverseColumnName
	 */
	public String getInverseColumnName();
	
	/**
	 * @return the dummyColumn
	 */
	public boolean isDummyColumn();
}
