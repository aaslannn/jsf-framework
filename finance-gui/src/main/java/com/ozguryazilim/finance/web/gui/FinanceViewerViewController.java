package com.ozguryazilim.finance.web.gui;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import com.ozguryazilim.finance.crawler.entity.FinanceDataProviderType;
import com.ozguryazilim.finance.crawler.entity.FundType;
import com.ozguryazilim.finance.crawler.entity.FundValueHolder;
import com.ozguryazilim.finance.web.entity.FinanceAlgorithm;
import com.ozguryazilim.finance.web.entity.FinanceInvesment;
import com.ozguryazilim.finance.web.entity.FinancePurchase;
import com.ozguryazilim.finance.web.entity.FinanceSummary;
import com.ozguryazilim.finance.web.entity.FinanceUserSetting;
import com.ozguryazilim.finance.web.service.FinanceService;
import com.ozguryazilim.zoro.core.JSFLazyDataModel;
import com.ozguryazilim.zoro.core.JSFViewController;
import com.ozguryazilim.zoro.core.auth.ZoroCredentials;
import com.ozguryazilim.zoro.core.db.DBEntityManager;
import com.ozguryazilim.zoro.core.util.MessageUtility;

@Named(value = "financeViewerViewController")
@ViewScoped
public class FinanceViewerViewController extends JSFViewController<FundValueHolder> {

	public static final String	LATEST_VALUES_QUERY	= "SELECT valueHolder FROM FundValueHolder valueHolder JOIN valueHolder.data as data WHERE data.type = ? and data.time >= (SELECT max(fData.time) FROM FinanceData fData WHERE fData.type = ? ) ORDER BY valueHolder.type";

	private static final long	serialVersionUID	= 3723252338624109893L;

	private FinanceUserSetting	setting				= null;

	private double				addedTLAmount		= 0;

	private FinanceSummary		summary;

	@Inject
	private DBEntityManager		entityManager;

	@Inject
	private ZoroCredentials		credentials;

	@Inject
	private FinanceService		service;

	@SuppressWarnings("unchecked")
	@Override
	protected void entityChanged() {

		List<FinanceUserSetting> result = entityManager.executeParameterizedSelectQuery("SELECT setting FROM FinanceUserSetting setting WHERE setting.username = ?",
				new Object[] { credentials.getUsername() });

		if (result.size() == 1) {
			setSetting(result.get(0));
		}
		else {
			setting = new FinanceUserSetting();
			setting.setBuyOrderRatio(0.2);
			setting.setSellNegativeOrderRatio(0.2);
			setting.setSellPositiveOrderRatio(1);
			setting.setUsername(credentials.getUsername());
		}

		calculateSummary();

		super.entityChanged();
	}

	public void calculateSummary() {
		summary = new FinanceSummary();

		// Calculate total currency in hand value
		List<FinanceInvesment> invesmentsOfUser = service.getInvesments(setting.getProvider(), credentials.getUsername());
		List<FundValueHolder> latestRates = service.getFundRates(setting.getProvider());
		double totalInvesmentValue = 0;
		double trInvesmentValue = 0;
		for (FinanceInvesment invesment : invesmentsOfUser) {
			if (invesment.getFundType() != FundType.TR) {
				double rate = service.getFundAskRate(latestRates, invesment.getFundType());
				totalInvesmentValue += rate * invesment.getAmount();
			}
			else {
				trInvesmentValue = invesment.getAmount();
			}
		}
		summary.setProcessedInvesment(totalInvesmentValue);
		summary.setWaitingInvesment(trInvesmentValue);
		summary.setInvestedAmount(service.getActiveSoldAmount(setting.getProvider(), credentials.getUsername(), FundType.TR) + trInvesmentValue);
		summary.setTotalInvesment(trInvesmentValue + totalInvesmentValue);
		summary.setTotalIncome(summary.getTotalInvesment() - summary.getInvestedAmount());

	}

	@Override
	public LazyDataModel<FundValueHolder> getDataModel() {
		JSFLazyDataModel<FundValueHolder> dataModel = (JSFLazyDataModel<FundValueHolder>) super.getDataModel();
		dataModel.setCustomQuery(LATEST_VALUES_QUERY, new Object[] { setting.getProvider(), setting.getProvider() });
		return dataModel;
	}

	public void saveSetting() {
		entityManager.save(setting);

		calculateSummary();
	}

	public List<SelectItem> getAlgorithmList() {
		List<SelectItem> items = new ArrayList<SelectItem>();

		for (FinanceAlgorithm algorithm : FinanceAlgorithm.values()) {
			items.add(new SelectItem(algorithm, MessageUtility.get("viewer", "algorithm." + algorithm.toString().toLowerCase())));
		}

		return items;
	}

	public List<SelectItem> getProviderList() {
		List<SelectItem> items = new ArrayList<SelectItem>();

		for (FinanceDataProviderType provider : FinanceDataProviderType.values()) {
			items.add(new SelectItem(provider, MessageUtility.get("viewer", "provider." + provider.toString().toLowerCase())));
		}

		return items;
	}

	public void addTRInvesment() {
		FinancePurchase purchase = new FinancePurchase();
		purchase.setProvider(setting.getProvider());
		purchase.setBoughtCurrency(FundType.TR);
		purchase.setBoughtCurrencyAmount(addedTLAmount);
		purchase.setUsername(credentials.getUsername());

		entityManager.save(purchase);

		service.applyPurchase(setting.getProvider(), credentials.getUsername(), purchase);

		addedTLAmount = 0;

		calculateSummary();
	}

	public FinanceInvesment getInvesmentOfUser(FundType fund) {
		return service.getInvesment(setting.getProvider(), credentials.getUsername(), fund);
	}

	public double getGainOfUser(FundType fund) {
		return service.getGain(setting.getProvider(), credentials.getUsername(), fund);
	}

	public String getOrder(FundType type) {
		return "AL";
	}

	public void buy(FundType type) {

	}

	public void sell(FundType type) {

	}

	/**
	 * @return the setting
	 */
	public FinanceUserSetting getSetting() {
		return setting;
	}

	/**
	 * @param setting
	 *            the setting to set
	 */
	public void setSetting(FinanceUserSetting setting) {
		this.setting = setting;
	}

	/**
	 * @return the summary
	 */
	public FinanceSummary getSummary() {
		return summary;
	}

	/**
	 * @param summary
	 *            the summary to set
	 */
	public void setSummary(FinanceSummary summary) {
		this.summary = summary;
	}

	/**
	 * @return the addedTLAmount
	 */
	public double getAddedTLAmount() {
		return addedTLAmount;
	}

	/**
	 * @param addedTLAmount
	 *            the addedTLAmount to set
	 */
	public void setAddedTLAmount(double addedTLAmount) {
		this.addedTLAmount = addedTLAmount;
	}
}
