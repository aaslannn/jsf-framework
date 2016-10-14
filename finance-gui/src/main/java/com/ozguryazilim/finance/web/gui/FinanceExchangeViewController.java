package com.ozguryazilim.finance.web.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import com.ozguryazilim.finance.crawler.entity.FundType;
import com.ozguryazilim.finance.web.entity.FinanceInvesment;
import com.ozguryazilim.finance.web.entity.FinancePurchase;
import com.ozguryazilim.finance.web.service.FinanceService;
import com.ozguryazilim.zoro.core.JSFViewController;
import com.ozguryazilim.zoro.core.auth.ZoroCredentials;
import com.ozguryazilim.zoro.core.util.JSFUtility;
import com.ozguryazilim.zoro.core.util.MessageUtility;

@Named(value = "financeExchangeViewController")
@ViewScoped
public class FinanceExchangeViewController extends JSFViewController<FinancePurchase> {

	private static final long			serialVersionUID	= -462096306644367666L;

	@Inject
	private ZoroCredentials				credentials;

	@Inject
	private FinanceViewerViewController	viewerController;

	@Inject
	private FinanceService				service;

	public void amountChanged() {
		if (entity.getBoughtCurrencyAmount() != 0 && entity.getSoldCurrencyAmount() != 0) {
			entity.setRate(entity.getSoldCurrencyAmount() / entity.getBoughtCurrencyAmount());
		}
	}

	public List<SelectItem> getCurrencyList() {
		List<SelectItem> items = new ArrayList<SelectItem>();

		for (FundType fund : FundType.values()) {
			items.add(new SelectItem(fund, fund.toString()));
		}

		return items;
	}

	@Override
	protected boolean isValid() {
		FinanceInvesment soldInvesment = service.getInvesment(viewerController.getSetting().getProvider(), credentials.getUsername(), entity.getSoldCurrency());
		if (soldInvesment.getAmount() < entity.getSoldCurrencyAmount()) {
			JSFUtility.addErrorMessage(null, MessageUtility.get("viewer", "errorAmountIsLessThan"));
			return false;
		}
		entity.setTime(new Date());
		entity.setProvider(viewerController.getSetting().getProvider());
		entity.setUsername(credentials.getUsername());

		return super.isValid();
	}

	@Override
	protected void afterAdd() {
		service.applyPurchase(viewerController.getSetting().getProvider(), credentials.getUsername(), entity);

		super.afterAdd();

		viewerController.calculateSummary();
	}

	public void setBuy(FundType type) {
		clearEntity();

		entity.setBoughtCurrency(type);
		entity.setSoldCurrency(FundType.TR);
	}

	public void setSell(FundType type) {
		clearEntity();

		entity.setSoldCurrency(type);
		entity.setBoughtCurrency(FundType.TR);
	}
}
