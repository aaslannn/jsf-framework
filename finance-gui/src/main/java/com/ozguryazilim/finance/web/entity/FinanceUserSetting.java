package com.ozguryazilim.finance.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ozguryazilim.finance.crawler.entity.FinanceDataProviderType;
import com.ozguryazilim.zoro.core.db.entity.BaseEntity;

@Table(name = "FINANCE_USER_SETTING")
@Entity
public class FinanceUserSetting extends BaseEntity {

	private static final long		serialVersionUID	= -4362180974467052978L;

	@Column
	private FinanceDataProviderType	provider			= FinanceDataProviderType.ISBANKASI;

	@Column
	private double					buyOrderRatio;

	@Column
	private double					sellNegativeOrderRatio;

	@Column
	private double					sellPositiveOrderRatio;

	@Column
	private FinanceAlgorithm		algorithm			= FinanceAlgorithm.STATIC;

	@Column
	private String					username;

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
	 * @return the buyOrderRatio
	 */
	public double getBuyOrderRatio() {
		return buyOrderRatio;
	}

	/**
	 * @param buyOrderRatio
	 *            the buyOrderRatio to set
	 */
	public void setBuyOrderRatio(double buyOrderRatio) {
		this.buyOrderRatio = buyOrderRatio;
	}

	/**
	 * @return the sellNegativeOrderRatio
	 */
	public double getSellNegativeOrderRatio() {
		return sellNegativeOrderRatio;
	}

	/**
	 * @param sellNegativeOrderRatio
	 *            the sellNegativeOrderRatio to set
	 */
	public void setSellNegativeOrderRatio(double sellNegativeOrderRatio) {
		this.sellNegativeOrderRatio = sellNegativeOrderRatio;
	}

	/**
	 * @return the sellPositiveOrderRatio
	 */
	public double getSellPositiveOrderRatio() {
		return sellPositiveOrderRatio;
	}

	/**
	 * @param sellPositiveOrderRatio
	 *            the sellPositiveOrderRatio to set
	 */
	public void setSellPositiveOrderRatio(double sellPositiveOrderRatio) {
		this.sellPositiveOrderRatio = sellPositiveOrderRatio;
	}

	/**
	 * @return the algorithm
	 */
	public FinanceAlgorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * @param algorithm
	 *            the algorithm to set
	 */
	public void setAlgorithm(FinanceAlgorithm algorithm) {
		this.algorithm = algorithm;
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
}
