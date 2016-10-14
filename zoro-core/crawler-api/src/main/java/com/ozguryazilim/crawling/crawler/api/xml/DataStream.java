package com.ozguryazilim.crawling.crawler.api.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", factoryMethod = "getNewInstance")
@XmlRootElement(name = "dataStream")
public class DataStream extends AbstractDataStream
{
	@XmlAttribute(name = "postUrl")
	private String	postUrl;

	@XmlAttribute(name = "streamLinkXPathExpression")
	private String	streamLinkXPathExpression;

	@XmlAttribute(name = "startOfCodeInLink")
	private String	startOfCodeInLink;

	@XmlAttribute(name = "endOfCodeInLink")
	private String	endOfCodeInLink;

	/**
	 * @return the postUrl
	 */
	public String getPostUrl()
	{
		return postUrl;
	}

	/**
	 * @param postUrl
	 *            the postUrl to set
	 */
	public void setPostUrl(String postUrl)
	{
		this.postUrl = postUrl;
	}

	/**
	 * @return the streamLinkXPathExpression
	 */
	public String getStreamLinkXPathExpression()
	{
		return streamLinkXPathExpression;
	}

	/**
	 * @param streamLinkXPathExpression
	 *            the streamLinkXPathExpression to set
	 */
	public void setStreamLinkXPathExpression(String streamLinkXPathExpression)
	{
		this.streamLinkXPathExpression = streamLinkXPathExpression;
	}

	/**
	 * @return the startOfCodeInLink
	 */
	public String getStartOfCodeInLink()
	{
		return startOfCodeInLink;
	}

	/**
	 * @param startOfCodeInLink
	 *            the startOfCodeInLink to set
	 */
	public void setStartOfCodeInLink(String startOfCodeInLink)
	{
		this.startOfCodeInLink = startOfCodeInLink;
	}

	/**
	 * @param endOfCodeInLink
	 *            the endOfCodeInLink to set
	 */
	public void setEndOfCodeInLink(String endOfCodeInLink)
	{
		this.endOfCodeInLink = endOfCodeInLink;
	}

	/**
	 * @return the endOfCodeInLink
	 */
	public String getEndOfCodeInLink()
	{
		return endOfCodeInLink;
	}

	public static DataStream getNewInstance()
	{
		return new DataStream();
	}
}
