package com.ozguryazilim.finance.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ozguryazilim.finance.crawler.entity.FinanceDataProviderType;
import com.ozguryazilim.finance.crawler.entity.FundType;
import com.ozguryazilim.zoro.core.db.entity.BaseEntity;

@Table(name = "FINANCE_INVESMENT")
@Entity
public class FinanceInvesment extends BaseEntity {

	private static final long		serialVersionUID	= 4950248904770242257L;

	@Column
	private FinanceDataProviderType	provider;

	@Column
	private FundType				fundType;

	@Column
	private String					username;

	@Column
	private double					amount;

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
	 * @return the fundType
	 */
	public FundType getFundType() {
		return fundType;
	}

	/**
	 * @param fundType
	 *            the fundType to set
	 */
	public void setFundType(FundType fundType) {
		this.fundType = fundType;
	}

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
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

}
