package com.ozguryazilim.finance.web.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ozguryazilim.finance.crawler.entity.FinanceDataProviderType;
import com.ozguryazilim.finance.crawler.entity.FundType;
import com.ozguryazilim.zoro.core.db.entity.BaseEntity;

@Table(name = "FINANCE_PURCHASE")
@Entity
public class FinancePurchase extends BaseEntity {

	private static final long		serialVersionUID	= 7919723484999678413L;

	@Column
	private String					username;

	@Column
	private FinanceDataProviderType	provider;

	@Column
	private FundType				boughtCurrency;

	@Column
	private FundType				soldCurrency;

	@Column
	private double					boughtCurrencyAmount;

	@Column
	private double					soldCurrencyAmount;

	@Column
	private double					rate;

	@Column
	private Date					time;

	@Column
	private boolean					buyActive			= true;

	@Column
	private boolean					sellActive			= true;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the provider
	 */
	public FinanceDataProviderType getProvider() {
		return provider;
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	public void setProvider(FinanceDataProviderType provider) {
		this.provider = provider;
	}

	/**
	 * @return the boughtCurrency
	 */
	public FundType getBoughtCurrency() {
		return boughtCurrency;
	}

	/**
	 * @param boughtCurrency
	 *            the boughtCurrency to set
	 */
	public void setBoughtCurrency(FundType boughtCurrency) {
		this.boughtCurrency = boughtCurrency;
	}

	/**
	 * @return the soldCurrency
	 */
	public FundType getSoldCurrency() {
		return soldCurrency;
	}

	/**
	 * @param soldCurrency
	 *            the soldCurrency to set
	 */
	public void setSoldCurrency(FundType soldCurrency) {
		this.soldCurrency = soldCurrency;
	}

	/**
	 * @return the boughtCurrencyAmount
	 */
	public double getBoughtCurrencyAmount() {
		return boughtCurrencyAmount;
	}

	/**
	 * @param boughtCurrencyAmount
	 *            the boughtCurrencyAmount to set
	 */
	public void setBoughtCurrencyAmount(double boughtCurrencyAmount) {
		this.boughtCurrencyAmount = boughtCurrencyAmount;
	}

	/**
	 * @return the soldCurrencyAmount
	 */
	public double getSoldCurrencyAmount() {
		return soldCurrencyAmount;
	}

	/**
	 * @param soldCurrencyAmount
	 *            the soldCurrencyAmount to set
	 */
	public void setSoldCurrencyAmount(double soldCurrencyAmount) {
		this.soldCurrencyAmount = soldCurrencyAmount;
	}

	/**
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the buyActive
	 */
	public boolean isBuyActive() {
		return buyActive;
	}

	/**
	 * @param buyActive
	 *            the buyActive to set
	 */
	public void setBuyActive(boolean buyActive) {
		this.buyActive = buyActive;
	}

	/**
	 * @return the sellActive
	 */
	public boolean isSellActive() {
		return sellActive;
	}

	/**
	 * @param sellActive
	 *            the sellActive to set
	 */
	public void setSellActive(boolean sellActive) {
		this.sellActive = sellActive;
	}
}
