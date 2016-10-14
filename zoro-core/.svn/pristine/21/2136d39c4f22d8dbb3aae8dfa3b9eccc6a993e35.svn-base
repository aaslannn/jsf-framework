package com.ozguryazilim.crawling.crawler.api.xml.factory;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ozguryazilim.crawling.crawler.api.xml.Sites;

public class CrawlerFactory
{
	private static final String	SITES_XML	= "/com/ozguryazilim/crawling/crawler/xml/sites.xml";
	private static CrawlerFactory	instance	= null;
	private Sites					sites		= null;

	/**
	 * Reads the sites.xml and constructs the sites
	 * 
	 * @throws DocumentException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private CrawlerFactory()
	{
		Unmarshaller unmarshaller;
		JAXBContext context;
		try
		{
			context = JAXBContext.newInstance(CrawlerFactory.class);
			unmarshaller = context.createUnmarshaller();
			InputStream stream = CrawlerFactory.class.getResourceAsStream(SITES_XML);
			InputSource input = new InputSource(stream);
			input.setSystemId(CrawlerFactory.class.getResource(SITES_XML).toURI().toString());

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setXIncludeAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(input);

			setSites((Sites) unmarshaller.unmarshal(doc));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static CrawlerFactory getInstance()
	{
		if (instance == null)
		{
			instance = new CrawlerFactory();
		}

		return instance;
	}

	/**
	 * @param sites
	 *            the sites to set
	 */
	public void setSites(Sites sites)
	{
		this.sites = sites;
	}

	/**
	 * @return the sites
	 */
	public Sites getSites()
	{
		return sites;
	}

}
