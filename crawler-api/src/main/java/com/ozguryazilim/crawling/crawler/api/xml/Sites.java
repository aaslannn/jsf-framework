/**
 * Sites class is used the list of sites. From the xml file this class is the
 * root node. Overall crawling work will be done over this class.
 * 
 * @author aaslannn
 */
package com.ozguryazilim.crawling.crawler.api.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "sites")
public class Sites
{
	@XmlElement(name = "site")
	private List<Site>	sites;

	/**
	 * @param sites
	 *            the sites to set
	 */
	public void setSites(List<Site> sites)
	{
		this.sites = sites;
	}

	/**
	 * @return the sites
	 */
	public List<Site> getSites()
	{
		return sites;
	}

}
