package com.ozguryazilim.finance.crawler.instance;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;

import com.ozguryazilim.finance.crawler.AbstractCrawerJob;
import com.ozguryazilim.finance.crawler.CrawlerUtility;
import com.ozguryazilim.finance.crawler.entity.FinanceDataProviderType;
import com.ozguryazilim.finance.crawler.entity.FundType;

public class IsBankasiCrawlerJob extends AbstractCrawerJob {

	Logger											logger		= Logger.getLogger(IsBankasiCrawlerJob.class);

	private final static FinanceDataProviderType	TYPE		= FinanceDataProviderType.ISBANKASI;
	private final static String						DOVIZ_URL	= "https://www.isbank.com.tr/Internet/MainPageEnter.aspx?src=DovizKurlari.ascx";
	private final static String						ALTIN_URL	= "https://www.isbank.com.tr/internet/mainpageenter.aspx?src=AltinFiyatlari.ascx";

	@Override
	protected InputStream getStream(String url) throws IOException {
		WebDriver driver = CrawlerUtility.createWebDriver();

		driver.get(url);
		WebElement iframe = driver.findElement(By.id("anasayfaaspx"));
		driver.switchTo().frame(iframe);

		return CrawlerUtility.getStreamFromDriver(driver);
	}

	@Override
	protected void startup(String url) throws IOException {

		InputStream stream = getStream(url);

		final HtmlCleaner cleaner = new HtmlCleaner();
		CleanerProperties props = cleaner.getProperties();
		props.setAllowHtmlInsideAttributes(true);
		props.setOmitUnknownTags(false);
		props.setTreatUnknownTagsAsContent(true);
		props.setOmitDeprecatedTags(false);
		try {
			TagNode node = cleaner.clean(stream, "UTF-8");
			doc = new DomSerializer(props, true).createDOM(node);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String getNodeValue(Node node) {
		String value = CrawlerUtility.getInnerText(node);

		return value;
	}

	@Override
	protected Map<FundType, String[]> getDovizFundXPaths() {
		Map<FundType, String[]> fundXPaths = new HashMap<FundType, String[]>();
		fundXPaths.put(FundType.USD, new String[] { "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[2]/td[2]", "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[2]/td[3]" });
		fundXPaths.put(FundType.EUR, new String[] { "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[3]/td[2]", "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[3]/td[3]" });
		fundXPaths.put(FundType.AUD, new String[] { ".//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[4]/td[2]", ".//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[4]/td[3]" });
		fundXPaths.put(FundType.DKK, new String[] { "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[5]/td[2]", "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[5]/td[3]" });
		fundXPaths.put(FundType.GBP, new String[] { "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[6]/td[2]", "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[6]/td[3]" });
		fundXPaths.put(FundType.SEK, new String[] { "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[7]/td[2]", "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[7]/td[3]" });
		fundXPaths.put(FundType.CHF, new String[] { "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[8]/td[2]", "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[8]/td[3]" });
		fundXPaths.put(FundType.JPY, new String[] { "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[9]/td[2]", "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[9]/td[3]" });
		fundXPaths.put(FundType.CAD, new String[] { "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[10]/td[2]", "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[10]/td[3]" });
		fundXPaths.put(FundType.KWD, new String[] { "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[11]/td[2]", "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[11]/td[3]" });
		fundXPaths.put(FundType.NOK, new String[] { "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[12]/td[2]", "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[12]/td[3]" });
		fundXPaths.put(FundType.SAR, new String[] { "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[13]/td[2]", "//*[@id='_ctl0_DovizKurlari_GridKurlar']/tbody/tr[13]/td[3]" });
		return fundXPaths;
	}

	@Override
	protected Map<FundType, String[]> getAltinFundXPaths() {
		Map<FundType, String[]> fundXPaths = new HashMap<FundType, String[]>();
		fundXPaths.put(FundType.XAU, new String[] { "//*[@id='_ctl0_AltinFiyatlari_lblGeneral']/table[1]/tbody/tr[3]/td[3]",
				"//*[@id='_ctl0_AltinFiyatlari_lblGeneral']/table[1]/tbody/tr[3]/td[2]" });
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
