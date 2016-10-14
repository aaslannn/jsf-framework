package com.ozguryazilim.finance.crawler.job;

import java.util.GregorianCalendar;

import com.ozguryazilim.finance.crawler.instance.IsBankasiCrawlerJob;
import com.ozguryazilim.finance.crawler.instance.ParaGarantiCrawlerJob;

public class CrawlerJob implements Runnable {
	private final static IsBankasiCrawlerJob	isBankasiCrawlerJob		= new IsBankasiCrawlerJob();

	private final static ParaGarantiCrawlerJob	paraGarantiCrawlerJob	= new ParaGarantiCrawlerJob();

	public void run() {
		int dayOfWeek = GregorianCalendar.getInstance().get(GregorianCalendar.DAY_OF_WEEK);
		int hourOfDay = GregorianCalendar.getInstance().get(GregorianCalendar.HOUR_OF_DAY);

		if (((dayOfWeek != GregorianCalendar.SATURDAY && dayOfWeek != GregorianCalendar.SUNDAY) && (hourOfDay >= 8 && hourOfDay <= 18))) {
			isBankasiCrawlerJob.run();
			paraGarantiCrawlerJob.run();
		}
	}

}