package com.ozguryazilim.finance.crawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.DOMAttrImpl;
import org.w3c.tidy.DOMElementImpl;
import org.w3c.tidy.DOMTextImpl;
import org.w3c.tidy.Tidy;

public class CrawlerUtility extends Object {
	public static final String	URL		= "http://www.gold.org/";
	public static final String	XPATH	= "//*[@id='spotpriceCellAsk']";

	/*
	 * public static void main(String[] args) throws IOException {
	 * ExcelExtractor excelExc = new ExcelExtractor(new POIFSFileSystem());
	 * excelExc.getMetadataTextExtractor(); HSSFWorkbook workBook = new
	 * HSSFWorkbook(new FileInputStream(new File("2005YOPTercihSayilari.xls")));
	 * HSSFSheet sheet = workBook.getSheetAt(0); HSSFRow row = sheet.getRow(0);
	 * System.out.println(""); }
	 */

	// xPath test
	public static void main(String[] args) throws Exception {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();

		InputStream stream = getStreamByURL(URL);

		Tidy tidy = new Tidy();
		tidy.setInputEncoding("windows-1254");
		tidy.setShowWarnings(false);
		tidy.setShowErrors(0);
		tidy.setQuiet(true);
		// Writer out = new NullWriter();
		// PrintWriter dummyOut = new PrintWriter(out);
		// tidy.setErrout(dummyOut);

		// Document doc = tidy.parseDOM(stream, null);

		final HtmlCleaner cleaner = new HtmlCleaner();
		CleanerProperties props = cleaner.getProperties();
		props.setAllowHtmlInsideAttributes(true);
		props.setOmitUnknownTags(false);
		props.setTreatUnknownTagsAsContent(true);
		props.setOmitDeprecatedTags(false);
		Document doc = null;
		try {
			TagNode node = cleaner.clean(stream, "UTF-8");
			doc = new DomSerializer(props, true).createDOM(node);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// XPathExpression expr =
		// xPath.compile("//span[@class='xyzd_ilan_metni']");
		// XPathExpression expr =
		// xPath.compile("//self::node()[starts-with(.,'Kısa')]");
		// XPathExpression expr =
		// xPath.compile("//span[substring(., string-length(.) - 5) = 'Tarihi']/following-sibling::node()[1]");
		// XPathExpression expr =
		// xPath.compile("//self::node()[starts-with(.,'Metre')]");
		XPathExpression expr = xPath.compile(XPATH);

		// BufferedReader in = new BufferedReader(
		// new InputStreamReader(
		// url.openStream(),"ISO-8859-9"));
		//
		// String inputLine;
		//
		// while ((inputLine = in.readLine()) != null)
		// System.out.println(inputLine);
		//
		// in.close();

		NodeList shows = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

		for (int i = 0; i < shows.getLength(); i++) {
			Node node = shows.item(i);

			String allInnerText = getInnerText(node);
			System.out.println("Node text : \n" + allInnerText.replaceAll("\\.", "").replaceAll(",", "\\.") + "\n");

		}

		System.out.println("Size : " + shows.getLength());
	}

	public static String getAllInnerText(Node parent) {
		NodeList children = parent.getChildNodes();
		String postResult = "";
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node currentChild = children.item(i);
				postResult += getAllInnerText(currentChild) + " ";
			}
		}

		return getInnerText(parent) + postResult;
	}

	public static String getInnerText(Node node) {
		String result = null;
		if (node instanceof DOMTextImpl) {
			result = ((DOMTextImpl) node).getData();
		}
		else if (node instanceof DOMAttrImpl) {
			result = ((DOMAttrImpl) node).getNodeValue();
		}
		else if (node.getNodeName().equals("img")) {
			result = node.getAttributes().getNamedItem("src").getNodeValue();
		}
		else {
			result = node.getTextContent();
		}

		if (result == null) {
			result = "";
		}

		return result;
	}

	public static String getInnerTextOfNextSiblingRecursively(Node node) {
		Node next = node.getNextSibling();
		if (next != null && next instanceof DOMElementImpl) {
			String text = getAllInnerText(next);
			return text + getInnerTextOfNextSiblingRecursively(next);
		}
		else if (next != null) {
			return getInnerTextOfNextSiblingRecursively(next);
		}

		return "";
	}

	public static String getAttrOfNextSiblingRecursively(Node node) {
		Node next = node.getNextSibling();
		if (next != null && next instanceof DOMElementImpl) {
			String text = getAllInnerText(next);
			return text + getAttrOfNextSiblingRecursively(next);
		}

		return "";
	}

	// Selenium test
	public static InputStream getStreamBySelenium(String url) throws Exception {
		WebDriver driver = createWebDriver();

		driver.get(url);

		WebElement iframe = driver.findElement(By.id("anasayfaaspx"));
		driver.switchTo().frame(iframe);

		return getStreamFromDriver(driver);
	}

	public static InputStream getStreamFromDriver(WebDriver driver) {
		String htmlSource = driver.getPageSource();

		driver.close();
		InputStream stream = new ByteArrayInputStream(htmlSource.getBytes());

		return stream;
	}

	public static WebDriver createWebDriver() {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", true);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/space/Programs/phantomjs/bin/phantomjs");
		WebDriver driver = new PhantomJSDriver(caps);
		return driver;
	}

	public static InputStream getStreamByURL(String urlStr) throws IOException {

		URL url = new URL(urlStr);
		URLConnection conn = url.openConnection();
		// setting these timeouts ensures the client does not deadlock
		// indefinitely
		// when the server has problems.
		conn.setConnectTimeout(1000);
		conn.setReadTimeout(10000);
		return conn.getInputStream();
	}
}