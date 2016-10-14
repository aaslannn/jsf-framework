package com.ozguryazilim.crawling.crawler.api.helper;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.DOMAttrImpl;
import org.w3c.tidy.DOMElementImpl;
import org.w3c.tidy.DOMTextImpl;

public class XPathHelper
{
	/**
	 * Returns the text values of each child node recursively
	 * 
	 * @param parent
	 * @return
	 */
	public static String getAllInnerText(Node parent)
	{
		NodeList children = parent.getChildNodes();
		String postResult = "";
		if (children != null)
		{
			for (int i = 0; i < children.getLength(); i++)
			{
				Node currentChild = children.item(i);
				postResult += getAllInnerText(currentChild) + " ";
			}
		}

		return getInnerText(parent) + " " + postResult;
	}

	/**
	 * Returns the text of the node whether the data of text node or the
	 * textContent of the node
	 * 
	 * @param node
	 * @return
	 */
	public static String getInnerText(Node node)
	{
		String result = null;
		if (node instanceof DOMTextImpl)
		{
			result = ((DOMTextImpl) node).getData();
		}
		else if (node instanceof DOMAttrImpl)
		{
			result = node.getNodeValue();
		}
		else if (node.getNodeName().equals("img"))
		{
			result = node.getAttributes().getNamedItem("src").getNodeValue();
		}
		else
		{
			result = node.getTextContent();
		}

		if (result == null)
		{
			result = "";
		}

		return result;
	}

	/**
	 * Returns the traces each the next siblings recursively and concatenates
	 * the text of the nodes
	 * 
	 * @param node
	 * @return
	 */
	public static String getInnerTextOfNextSiblingRecursively(Node node)
	{
		Node next = node.getNextSibling();
		if (next != null && next instanceof DOMElementImpl)
		{
			String text = getAllInnerText(next);
			return text + getInnerTextOfNextSiblingRecursively(next);
		}

		return "";
	}
}
