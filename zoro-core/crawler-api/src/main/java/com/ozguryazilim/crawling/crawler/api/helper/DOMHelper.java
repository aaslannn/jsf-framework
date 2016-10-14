package com.ozguryazilim.crawling.crawler.api.helper;

import java.io.InputStream;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

public class DOMHelper
{
	private static final String	HTML_CLEANER	= "HTML_CLEANER";
	private static final String	JTIDY			= "JTIDY";

	public static Document getDocument(InputStream stream, String domParserName, String encoding)
	{
		Document document = null;
		if (domParserName.equals(HTML_CLEANER))
		{
			document = getDocumentByHTMLCleaner(stream, encoding);
		}
		else if (domParserName.equals(JTIDY))
		{
			document = getDocumentByJTidy(stream, encoding);
		}

		return document;
	}

	private static Document getDocumentByHTMLCleaner(InputStream stream, String encoding)
	{
		final HtmlCleaner cleaner = new HtmlCleaner();
		CleanerProperties props = cleaner.getProperties();
		Document doc = null;
		try
		{
			TagNode node = cleaner.clean(stream, encoding);
			doc = new DomSerializer(props, true).createDOM(node);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return doc;
	}

	private static Document getDocumentByJTidy(InputStream stream, String encoding)
	{
		Document doc = null;
		Tidy tidy = new Tidy();
		tidy.setShowWarnings(false);
		tidy.setShowErrors(0);
		tidy.setQuiet(true);
		tidy.setMakeClean(true);
		tidy.setInputEncoding(encoding);

		doc = tidy.parseDOM(stream, null);

		return doc;
	}
}
