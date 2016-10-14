package com.ozguryazilim.finance.crawler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ozguryazilim.zoro.core.db.entity.BaseEntity;

@Table(name = "FUND_HOLDER")
@Entity
public class FundValueHolder extends BaseEntity {

	private static final long	serialVersionUID	= 1316280559012083989L;

	@Column
	private double				bidValue;

	@Column
	private double				askValue;

	@ManyToOne
	@JoinColumn(name = "FINANCE_DATA_REF")
	private FinanceData			data;

	@Column(name = "fundType")
	private FundType			type;

	/**
	 * @return the data
	 */
	public FinanceData getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(FinanceData data) {
		this.data = data;
	}

	/**
	 * @return the bidValue
	 */
	public double getBidValue() {
		return bidValue;
	}

	/**
	 * @param bidValue
	 *            the bidValue to set
	 */
	public void setBidValue(double bidValue) {
		this.bidValue = bidValue;
	}

	/**
	 * @return the askValue
	 */
	public double getAskValue() {
		return askValue;
	}

	/**
	 * @param askValue
	 *            the askValue to set
	 */
	public void setAskValue(double askValue) {
		this.askValue = askValue;
	}

	/**
	 * @return the type
	 */
	public FundType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(FundType type) {
		this.type = type;
	}
}
