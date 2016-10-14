function createOverlayPanel(panelGroupId, widgetVarStr, forId)
{
	if (!widgetVarStr || widgetVarStr == '')
	{
		widgetVarStr = panelGroupId.replaceAll(':','_');
	}

	var overlayPanel = window[widgetVarStr];
	if (!overlayPanel)
	{
		overlayPanel = createOverlayPanelInstance(panelGroupId, widgetVarStr,
				forId);
	}
	
	addOnclickEvent(overlayPanel, forId);

	if (overlayPanel.isVisible)
	{
		overlayPanel.show();
	}
}

function hideOverlay(panelGroupId, widgetVarStr) {
	if (!widgetVarStr || widgetVarStr == '')
	{
		widgetVarStr = panelGroupId.replaceAll(':','_');
	}
	
	var overlayPanel = window[widgetVarStr];
	
	overlayPanel.hide();
	
	return false;
}

function createOverlayPanelInstance(panelGroupId, widgetVarStr, forId)
{
	var overlayPanel = {};

	overlayPanel.forElement = document.getElementById(forId);
	overlayPanel.panelGroupId = panelGroupId;
	overlayPanel.isVisible = false;
	overlayPanel.show = function()
	{
		$(document.getElementById(overlayPanel.panelGroupId)).show();
		overlayPanel.isVisible = true;
	};

	overlayPanel.hide = function()
	{
		$(document.getElementById(overlayPanel.panelGroupId)).hide();
		overlayPanel.isVisible = false;
	};

	overlayPanel.toggleView = function()
	{
		if (overlayPanel.isVisible)
		{
			overlayPanel.hide();
		}
		else
		{
			overlayPanel.show();
		}
	};

	window[widgetVarStr] = overlayPanel;

	return overlayPanel;
}

function addOnclickEvent(overlayPanel, forId)
{
	overlayPanel.forElement = document.getElementById(forId);

	$(overlayPanel.forElement).click(function()
	{
		overlayPanel.toggleView();
	});
}