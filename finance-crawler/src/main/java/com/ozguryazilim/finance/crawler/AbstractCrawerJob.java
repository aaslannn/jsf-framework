package com.ozguryazilim.finance.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import com.ozguryazilim.finance.crawler.entity.FinanceData;
import com.ozguryazilim.finance.crawler.entity.FinanceDataProviderType;
import com.ozguryazilim.finance.crawler.entity.FundType;
import com.ozguryazilim.finance.crawler.entity.FundValueHolder;
import com.ozguryazilim.zoro.core.db.DBEntityManager;

public abstract class AbstractCrawerJob {
	private XPathFactory	factory	= XPathFactory.newInstance();
	protected XPath			xPath	= factory.newXPath();
	protected Document		doc		= null;
	Logger					logger	= Logger.getLogger(AbstractCrawerJob.class);

	public void run() {

		try {
			FinanceData data = fetchData();

			EntityManager entityManager = DBEntityManager.createAEntityManager();
			entityManager.getTransaction().begin();
			entityManager.persist(data);
			entityManager.getTransaction().commit();
			logger.debug("Read finance data from " + getProviderType() + " job!");
		}
		catch (IOException e) {
			logger.error("Cannot access url " + getAltinURL() + " for " + getProviderType() + " job!");
			e.printStackTrace();
		}
		catch (XPathExpressionException e) {
			logger.error("Xpath expression exception " + getProviderType() + " job!");
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (Throwable e) {
			logger.error("Unexpected exception for " + getProviderType() + " job!");
			e.printStackTrace();
		}
	}

	protected FinanceData fetchData() throws IOException, XPathExpressionException {
		startup(getDovizURL());

		FinanceData data = new FinanceData();
		data.setType(getProviderType());

		Map<FundType, String[]> fundXPaths = getDovizFundXPaths();
		processFunds(data, fundXPaths);

		Map<FundType, String[]> altinFundXPaths = getAltinFundXPaths();
		if (altinFundXPaths != null && altinFundXPaths.size() > 0) {
			startup(getAltinURL());

			processFunds(data, altinFundXPaths);
		}

		return data;
	}

	private void processFunds(FinanceData data, Map<FundType, String[]> fundXPaths) throws XPathExpressionException {
		if (fundXPaths != null) {
			for (FundType fundType : fundXPaths.keySet()) {

				String[] xPath = fundXPaths.get(fundType);
				if (xPath != null) {
					String bidValue = getValue(xPath[0]);
					String askValue = null;
					if (xPath.length > 1) {
						askValue = getValue(xPath[1]);
					}

					FundValueHolder fundValueHolder = new FundValueHolder();
					fundValueHolder.setType(fundType);
					fundValueHolder.setBidValue(Double.parseDouble(bidValue));
					fundValueHolder.setAskValue(Double.parseDouble(askValue));
					fundValueHolder.setData(data);

					data.getFunds().add(fundValueHolder);
				}
			}
		}
	}

	protected void startup(String url) throws IOException {

		InputStream stream = getStream(url);

		Tidy tidy = new Tidy();
		tidy.setInputEncoding("windows-1254");
		tidy.setShowWarnings(false);
		tidy.setShowErrors(0);
		tidy.setQuiet(true);
		doc = tidy.parseDOM(stream, null);
	}

	protected InputStream getStream(String url) throws IOException {
		return CrawlerUtility.getStreamByURL(url);
	}

	protected String getValue(String xpath) throws XPathExpressionException, IllegalArgumentException {

		XPathExpression expr = xPath.compile(xpath);

		NodeList shows = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

		if (shows.getLength() != 1) {
			throw new IllegalArgumentException("Given xpath returns " + shows.getLength() + " value for xpath: " + xpath + " for job " + getProviderType());
		}
		else {
			Node node = shows.item(0);

			return getNodeValue(node);
		}
	}

	protected String getNodeValue(Node node) {
		String value = CrawlerUtility.getAllInnerText(node);

		if (value.indexOf(".") > value.indexOf(",")) {
			return value.replaceAll(",", "");
		}

		return value.replaceAll("\\.", "").replaceAll(",", "\\.");
	}

	protected abstract FinanceDataProviderType getProviderType();

	protected abstract String getAltinURL();

	protected abstract String getDovizURL();

	protected abstract Map<FundType, String[]> getDovizFundXPaths();

	protected abstract Map<FundType, String[]> getAltinFundXPaths();
}
