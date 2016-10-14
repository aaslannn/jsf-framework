package com.ozguryazilim.crawling.crawler.api.exception;

@SuppressWarnings("serial")
public class CrawlerException extends Exception
{
	public enum CrawlerExceptionType
	{
		UNEXPECTED_SITE_FOR_CRAWLER,
		UNEXPECTED_PAGE_LOADED,
		INVALID_INSERT_CALL,
		INVALID_UPDATE_CALL,
		INCOMPATIBLE_FIELD_RELATION,
		NECCESSARY_FIELDS_ARE_MISSING,
		MALFORMED_XPATH_EXPRESSION,
		DATA_FETCHING_FAILED_FOR_GIVEN_MODEL,
		UNEXPECTED_CAPTURE_METHOD,
		UNEXPECTED_COLUMN_TYPE_FOR_DATUM,
		MORE_THAN_ONE_MODEL_RETURNED,
		INVALID_EMBEDDED_SEPERATOR_OR_VALUE,
		REQUIRED_DEFAULT_TOKENS_COULD_NOT_BE_FOUND,
		CAN_NOT_INSERT_AND_FETCH_THE_PRIMARY_KEY
	}
	
	private CrawlerExceptionType type;
	
	public CrawlerException(CrawlerExceptionType type)
	{
		this.setType(type);
	}
	
	public CrawlerException(CrawlerExceptionType type, String errorMessage)
	{
		super(errorMessage);
		
		this.setType(type);
	}
	
	public CrawlerException(CrawlerExceptionType type, StackTraceElement[] stackTrace)
	{
		super();
		setStackTrace(stackTrace);
		
		this.type = type;
	}
	
	public CrawlerException(CrawlerExceptionType type, Exception e)
	{
		super(e);
		
		this.type = type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(CrawlerExceptionType type)
	{
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public CrawlerExceptionType getType()
	{
		return type;
	}
}
