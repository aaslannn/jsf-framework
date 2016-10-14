package com.ozguryazilim.finance.crawler.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "FINANCE_DATA")
@Entity
public class FinanceData implements Serializable {

	private static final long		serialVersionUID	= -4573055091769442652L;

	@Column
	@Id
	@GeneratedValue
	private int						id;

	@OneToMany(orphanRemoval = true, cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "data")
	private List<FundValueHolder>	funds				= new ArrayList<FundValueHolder>();

	@Column
	private Date					time				= new Date();

	@Column
	private FinanceDataProviderType	type;

	/**
	 * @return the funds
	 */
	public List<FundValueHolder> getFunds() {
		return funds;
	}

	/**
	 * @param funds
	 *            the funds to set
	 */
	public void setFunds(List<FundValueHolder> funds) {
		this.funds = funds;
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
	 * @return the type
	 */
	public FinanceDataProviderType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(FinanceDataProviderType type) {
		this.type = type;
	}
}
