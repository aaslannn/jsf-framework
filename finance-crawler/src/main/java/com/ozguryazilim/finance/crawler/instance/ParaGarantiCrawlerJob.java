package com.ozguryazilim.finance.crawler.instance;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.ozguryazilim.finance.crawler.AbstractCrawerJob;
import com.ozguryazilim.finance.crawler.CrawlerUtility;
import com.ozguryazilim.finance.crawler.entity.FinanceDataProviderType;
import com.ozguryazilim.finance.crawler.entity.FundType;

public class ParaGarantiCrawlerJob extends AbstractCrawerJob {

	Logger											logger		= Logger.getLogger(ParaGarantiCrawlerJob.class);

	private final static FinanceDataProviderType	TYPE		= FinanceDataProviderType.GARANTI;
	private final static String						DOVIZ_URL	= "http://www.paragaranti.com/pages/doviz.jsp";
	private final static String						ALTIN_URL	= "http://www.gold.org/";

	@Override
	protected InputStream getStream(String url) throws IOException {
		if (url == DOVIZ_URL) {
			WebDriver driver = CrawlerUtility.createWebDriver();

			driver.get(url);
			driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@src='/gonline/yatirim/doviz_kur']")));

			return CrawlerUtility.getStreamFromDriver(driver);
		}
		else {
			return CrawlerUtility.getStreamByURL(url);
		}
	}

	@Override
	protected Map<FundType, String[]> getDovizFundXPaths() {
		Map<FundType, String[]> fundXPaths = new HashMap<FundType, String[]>();
		fundXPaths.put(FundType.USD, new String[] { "html/body/table/tbody/tr[1]/td[6]", "html/body/table/tbody/tr[2]/td[5]" });
		fundXPaths.put(FundType.EUR, new String[] { "html/body/table/tbody/tr[3]/td[6]", "html/body/table/tbody/tr[4]/td[5]" });
		fundXPaths.put(FundType.AUD, new String[] { "html/body/table/tbody/tr[5]/td[6]", "html/body/table/tbody/tr[6]/td[5]" });
		fundXPaths.put(FundType.CAD, new String[] { "html/body/table/tbody/tr[7]/td[6]", "html/body/table/tbody/tr[8]/td[5]" });
		fundXPaths.put(FundType.CHF, new String[] { "html/body/table/tbody/tr[9]/td[6]", "html/body/table/tbody/tr[10]/td[5]" });
		fundXPaths.put(FundType.DKK, new String[] { "html/body/table/tbody/tr[11]/td[6]", "html/body/table/tbody/tr[12]/td[5]" });
		fundXPaths.put(FundType.GBP, new String[] { "html/body/table/tbody/tr[13]/td[6]", "html/body/table/tbody/tr[14]/td[5]" });
		fundXPaths.put(FundType.JPY, new String[] { "html/body/table/tbody/tr[15]/td[6]", "html/body/table/tbody/tr[16]/td[5]" });
		fundXPaths.put(FundType.NOK, new String[] { "html/body/table/tbody/tr[17]/td[6]", "html/body/table/tbody/tr[18]/td[5]" });
		fundXPaths.put(FundType.SAR, new String[] { "html/body/table/tbody/tr[19]/td[6]", "html/body/table/tbody/tr[20]/td[5]" });
		fundXPaths.put(FundType.SEK, new String[] { "html/body/table/tbody/tr[21]/td[6]", "html/body/table/tbody/tr[22]/td[5]" });
		return fundXPaths;
	}

	@Override
	protected Map<FundType, String[]> getAltinFundXPaths() {
		Map<FundType, String[]> fundXPaths = new HashMap<FundType, String[]>();
		fundXPaths.put(FundType.XAUONS, new String[] { ".//*[@id='spotpriceCellBid']", ".//*[@id='spotpriceCellAsk']" });
		return fundXPaths;
	}

	@Override
	protected String getAltinURL() {
		return ALTIN_URL;
	}

	@Override
	protected String getDovizURL() {
		return DOVIZ_URL;
	}

	@Override
	protected FinanceDataProviderType getProviderType() {
		return TYPE;
	}
}
