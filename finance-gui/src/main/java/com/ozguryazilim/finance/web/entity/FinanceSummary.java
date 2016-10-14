package com.ozguryazilim.finance.web.entity;

public class FinanceSummary {

	private double	investedAmount		= 0;

	private double	waitingInvesment	= 0;

	private double	processedInvesment	= 0;

	private double	totalIncome			= 0;

	private double	totalInvesment		= 0;

	/**
	 * @return the investedAmount
	 */
	public double getInvestedAmount() {
		return investedAmount;
	}

	/**
	 * @param investedAmount
	 *            the investedAmount to set
	 */
	public void setInvestedAmount(double investedAmount) {
		this.investedAmount = investedAmount;
	}

	/**
	 * @return the waitingInvesment
	 */
	public double getWaitingInvesment() {
		return waitingInvesment;
	}

	/**
	 * @param waitingInvesment
	 *            the waitingInvesment to set
	 */
	public void setWaitingInvesment(double waitingInvesment) {
		this.waitingInvesment = waitingInvesment;
	}

	/**
	 * @return the processedInvesment
	 */
	public double getProcessedInvesment() {
		return processedInvesment;
	}

	/**
	 * @param processedInvesment
	 *            the processedInvesment to set
	 */
	public void setProcessedInvesment(double processedInvesment) {
		this.processedInvesment = processedInvesment;
	}

	/**
	 * @return the totalIncome
	 */
	public double getTotalIncome() {
		return totalIncome;
	}

	/**
	 * @param totalIncome
	 *            the totalIncome to set
	 */
	public void setTotalIncome(double totalIncome) {
		this.totalIncome = totalIncome;
	}

	/**
	 * @return the totalInvesment
	 */
	public double getTotalInvesment() {
		return totalInvesment;
	}

	/**
	 * @param totalInvesment
	 *            the totalInvesment to set
	 */
	public void setTotalInvesment(double totalInvesment) {
		this.totalInvesment = totalInvesment;
	}
}
