package com.ozguryazilim.crawling.crawler.api.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.ozguryazilim.crawling.crawler.api.core.AbstractDataPageReaderThread;

public class DateHelper
{
	private static Logger					logger		= Logger.getLogger(DateHelper.class);

	private static HashMap<String, String>	monthMap	= new HashMap<String, String>();

	static
	{
		monthMap.put("Ocak", "01");
		monthMap.put("Şubat", "02");
		monthMap.put("Mart", "03");
		monthMap.put("Nisan", "04");
		monthMap.put("Mayıs", "05");
		monthMap.put("Haziran", "06");
		monthMap.put("Temmuz", "07");
		monthMap.put("Ağustos", "08");
		monthMap.put("Eylül", "09");
		monthMap.put("Ekim", "10");
		monthMap.put("Kasım", "11");
		monthMap.put("Aralık", "12");

	}

	public static Date parse(String value) throws ParseException
	{
		Date result = null;
		DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("tr", "TR"));
		try
		{
			result = formatter.parse(value);
		}
		catch (ParseException e)
		{
			try
			{
				String[] tokens = value.split(" ");
				String monthInt = monthMap.get(tokens[1]);

				result = formatter.parse(tokens[0] + "." + monthInt + "." + tokens[2]);
				logger.info("Parsed date in second try : " + value);
			}
			catch (Exception ex)
			{
				logger.error("Cannot parse given date : " + value);
				
				throw new ParseException(ex.getMessage(), 0);
			}
		}

		return result;
	}
}
