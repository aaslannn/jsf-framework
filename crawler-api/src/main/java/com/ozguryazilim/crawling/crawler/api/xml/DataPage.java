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
@XmlRootElement(name = "dataPage")
public class DataPage extends DataStream
{
	@XmlAttribute(name = "url", required = true)
	private String				url;

	@XmlAttribute(name = "notFoundXPathExpression", required = true)
	private String				notFoundXPathExpression;

	@XmlElement(name = "dataPageModel")
	private List<DataPageModel>	models;

	@XmlAttribute(name = "tokenizerXPathExpression", required = true)
	private String				tokenizerXPathExpression;

	@XmlAttribute(name = "tokenizerCaptureMethod", required = true)
	private String				tokenizerCaptureMethod;

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
	 * @param notFoundXPathExpression
	 *            the notFoundXPathExpression to set
	 */
	public void setNotFoundXPathExpression(String notFoundXPathExpression)
	{
		this.notFoundXPathExpression = notFoundXPathExpression;
	}

	/**
	 * @return the notFoundXPathExpression
	 */
	public String getNotFoundXPathExpression()
	{
		return notFoundXPathExpression;
	}

	/**
	 * @param models
	 *            the models to set
	 */
	public void setModels(List<DataPageModel> models)
	{
		this.models = models;
	}

	/**
	 * @return the models
	 */
	public List<DataPageModel> getModels()
	{
		return models;
	}

	/**
	 * @param tokenizerXPathExpression
	 *            the tokenizerXPathExpression to set
	 */
	public void setTokenizerXPathExpression(String tokenizerXPathExpression)
	{
		this.tokenizerXPathExpression = tokenizerXPathExpression;
	}

	/**
	 * @return the tokenizerXPathExpression
	 */
	public String getTokenizerXPathExpression()
	{
		return tokenizerXPathExpression;
	}

	/**
	 * @param tokenizerCaptureMethod
	 *            the tokenizerCaptureMethod to set
	 */
	public void setTokenizerCaptureMethod(String tokenizerCaptureMethod)
	{
		this.tokenizerCaptureMethod = tokenizerCaptureMethod;
	}

	/**
	 * @return the tokenizerCaptureMethod
	 */
	public String getTokenizerCaptureMethod()
	{
		return tokenizerCaptureMethod;
	}

	public static DataPage getNewInstance()
	{
		return new DataPage();
	}
}
