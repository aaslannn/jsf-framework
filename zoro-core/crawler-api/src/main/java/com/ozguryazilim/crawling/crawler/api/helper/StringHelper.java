package com.ozguryazilim.crawling.crawler.api.helper;

public class StringHelper
{
	public static String trimExtended(String value)
	{
		return value.trim().replaceAll((char)160 + "", "");
	}
	
	/**
	 * Gets the link as the input and takes out the job code part of the link
	 * and returns he job code
	 * 
	 * @param longText
	 * @return code of the job which exists in the link
	 */
	public static String getInnerTextFromLongText(String longText, String startOfInnerText, String endOfInnerText)
	{
		if (longText == null)
		{
			return null;
		}

		int startIndexOfInnerText = 0;
		if (startOfInnerText != null)
		{
			startIndexOfInnerText = getStartIndexOfCode(longText, startOfInnerText);
		}
		
		int endIndexOfInnerText = longText.length();
		if (endOfInnerText != null)
		{
			endIndexOfInnerText = getEndIndexOfCode(longText, endOfInnerText, startIndexOfInnerText);
		}
		
		String result = longText.substring(startIndexOfInnerText);
		if (endIndexOfInnerText != -1)
		{
			result = longText.substring(startIndexOfInnerText, endIndexOfInnerText);
		}

		return result;
	}
	
	/**
	 * Returns the start index of the code in the given link according to the
	 * startOfCodeInLink attribute of the site.
	 * 
	 * @param longText
	 * @return
	 */
	private static int getStartIndexOfCode(String longText, String startText)
	{
		return longText.lastIndexOf(startText) + startText.length();
	}

	/**
	 * Returns the ending index of the code in the given link, according to the
	 * endOfCodeInLink attribute of site
	 * 
	 * @param longText
	 * @param startIndexOfCode
	 * @return
	 */
	private static int getEndIndexOfCode(String longText, String endOfInnerText, int startIndexOfInnerText)
	{
		if (endOfInnerText == null)
		{
			endOfInnerText = "&";
		}

		return longText.indexOf(endOfInnerText, startIndexOfInnerText);
	}
}
