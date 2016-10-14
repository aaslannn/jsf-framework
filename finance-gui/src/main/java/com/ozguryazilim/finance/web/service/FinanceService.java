package com.ozguryazilim.finance.web.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.ozguryazilim.finance.crawler.entity.FinanceDataProviderType;
import com.ozguryazilim.finance.crawler.entity.FundType;
import com.ozguryazilim.finance.crawler.entity.FundValueHolder;
import com.ozguryazilim.finance.web.entity.FinanceInvesment;
import com.ozguryazilim.finance.web.entity.FinancePurchase;
import com.ozguryazilim.finance.web.gui.FinanceViewerViewController;
import com.ozguryazilim.zoro.core.db.DBEntityManager;

@ApplicationScoped
@Named
public class FinanceService implements Serializable {

	private static final long	serialVersionUID	= 5881249940579182196L;

	@Inject
	private DBEntityManager		entityManager;

	@SuppressWarnings("unchecked")
	public FinanceInvesment getInvesment(FinanceDataProviderType provider, String username, FundType fund) {
		List<FinanceInvesment> invesments = entityManager.executeParameterizedSelectQuery(
				"SELECT fi FROM FinanceInvesment fi WHERE fi.username = ? and fi.provider = ? and fi.fundType = ?", new Object[] { username, provider, fund });

		if (invesments.size() > 0) {
			return invesments.get(0);
		}

		FinanceInvesment financeInvesment = new FinanceInvesment();
		financeInvesment.setAmount(0);
		financeInvesment.setFundType(fund);
		financeInvesment.setProvider(provider);
		financeInvesment.setUsername(username);

		return financeInvesment;
	}

	@SuppressWarnings("unchecked")
	public List<FinanceInvesment> getInvesments(FinanceDataProviderType provider, String username) {
		List<FinanceInvesment> invesments = entityManager.executeParameterizedSelectQuery("SELECT fi FROM FinanceInvesment fi WHERE fi.username = ? and fi.provider = ?",
				new Object[] { username, provider });

		return invesments;
	}

	public double getFundAskRate(List<FundValueHolder> holders, FundType fund) {
		for (FundValueHolder holder : holders) {
			if (holder.getType() == fund) {
				return holder.getBidValue();
			}
		}

		return 0;
	}

	@SuppressWarnings("unchecked")
	public double getGain(FinanceDataProviderType provider, String username, FundType fund) {
		List<FinancePurchase> purchases = entityManager.executeParameterizedSelectQuery(
				"SELECT fp FROM FinancePurchase fp WHERE  username = ? AND boughtCurrency = ? AND buyActive = true", new Object[] { username, fund });

		double expentMoney = 0;
		double currentMoney = 0;
		List<FundValueHolder> fundRates = getFundRates(provider);
		for (FinancePurchase purchase : purchases) {
			expentMoney += purchase.getBoughtCurrencyAmount() * purchase.getRate();
			currentMoney += purchase.getBoughtCurrencyAmount() * getFundAskRate(fundRates, purchase.getBoughtCurrency());
		}

		return currentMoney - expentMoney;
	}

	@SuppressWarnings("unchecked")
	public List<FundValueHolder> getFundRates(FinanceDataProviderType provider) {
		List<FundValueHolder> latestRates = entityManager.executeParameterizedSelectQuery(FinanceViewerViewController.LATEST_VALUES_QUERY, new Object[] { provider, provider });
		return latestRates;
	}

	public void applyPurchase(FinanceDataProviderType provider, String username, FinancePurchase purchase) {
		FinanceInvesment invesment = getInvesment(provider, username, purchase.getBoughtCurrency());
		invesment.setAmount(invesment.getAmount() + purchase.getBoughtCurrencyAmount());

		// Inactivate all purchases as it has been 0
		if (Math.abs(invesment.getAmount()) < 0.001) {
			invesment.setAmount(0);

			entityManager.executeUpdateQuery("UPDATE FinancePurchase SET buyActive = false WHERE username = ? AND boughtCurrency = ? AND buyActive = true", new Object[] {
					username, purchase.getBoughtCurrency() });
			entityManager.executeUpdateQuery("UPDATE FinancePurchase SET sellActive = false WHERE username = ? AND soldCurrency = ? AND sellActive = true", new Object[] {
					username, purchase.getBoughtCurrency() });
		}

		if (purchase.getSoldCurrency() != null) {
			FinanceInvesment soldInvesment = getInvesment(provider, username, purchase.getSoldCurrency());
			soldInvesment.setAmount(soldInvesment.getAmount() - purchase.getSoldCurrencyAmount());

			// Inactivate all purchases as it has been 0
			if (Math.abs(soldInvesment.getAmount()) < 0.001) {
				soldInvesment.setAmount(0);

				entityManager.executeUpdateQuery("UPDATE FinancePurchase SET buyActive = false WHERE username = ? AND boughtCurrency = ? AND buyActive = true", new Object[] {
						username, purchase.getSoldCurrency() });
				entityManager.executeUpdateQuery("UPDATE FinancePurchase SET sellActive = false WHERE username = ? AND soldCurrency = ? AND sellActive = true", new Object[] {
						username, purchase.getSoldCurrency() });
			}

		}

		entityManager.save(invesment);
	}

	public double getActiveAmount(FinanceDataProviderType provider, String username, FundType fund) {
		return getActiveBoughtAmount(provider, username, fund) - getActiveSoldAmount(provider, username, fund);
	}

	@SuppressWarnings("unchecked")
	public double getActiveBoughtAmount(FinanceDataProviderType provider, String username, FundType fund) {
		List<Double> boughtPurchases = entityManager.executeParameterizedSelectQuery(
				"SELECT SUM(fp.boughtCurrencyAmount) FROM FinancePurchase fp WHERE  username = ? AND boughtCurrency = ? AND buyActive = true", new Object[] { username, fund });

		if (boughtPurchases.size() > 0) {
			return boughtPurchases.get(0);
		}

		return 0;
	}

	@SuppressWarnings("unchecked")
	public double getActiveSoldAmount(FinanceDataProviderType provider, String username, FundType fund) {
		List<Double> soldPurchases = entityManager.executeParameterizedSelectQuery(
				"SELECT SUM(fp.soldCurrencyAmount) FROM FinancePurchase fp WHERE  username = ? AND soldCurrency = ? AND sellActive = true", new Object[] { username, fund });

		if (soldPurchases.size() > 0 && soldPurchases.get(0) != null) {
			return soldPurchases.get(0);
		}

		return 0;
	}

}
