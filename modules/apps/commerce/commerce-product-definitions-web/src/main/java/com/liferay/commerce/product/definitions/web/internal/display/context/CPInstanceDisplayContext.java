/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.product.definitions.web.internal.display.context;

import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.currency.util.CommercePriceFormatter;
import com.liferay.commerce.frontend.ClayCreationMenu;
import com.liferay.commerce.frontend.ClayCreationMenuItem;
import com.liferay.commerce.product.definitions.web.display.context.BaseCPDefinitionsDisplayContext;
import com.liferay.commerce.product.definitions.web.portlet.action.ActionHelper;
import com.liferay.commerce.product.definitions.web.servlet.taglib.ui.CPDefinitionScreenNavigationConstants;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CPDefinitionOptionValueRel;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CPMeasurementUnit;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CPDefinitionOptionRelService;
import com.liferay.commerce.product.service.CPInstanceService;
import com.liferay.commerce.product.service.CPMeasurementUnitLocalService;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.util.CustomAttributesUtil;

import java.math.BigDecimal;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alessio Antonio Rendina
 * @author Marco Leo
 */
public class CPInstanceDisplayContext extends BaseCPDefinitionsDisplayContext {

	public CPInstanceDisplayContext(
		ActionHelper actionHelper, HttpServletRequest httpServletRequest,
		CommerceCurrencyLocalService commerceCurrencyLocalService,
		CommercePriceFormatter commercePriceFormatter,
		CPDefinitionOptionRelService cpDefinitionOptionRelService,
		CPInstanceService cpInstanceService, CPInstanceHelper cpInstanceHelper,
		CPMeasurementUnitLocalService cpMeasurementUnitLocalService) {

		super(actionHelper, httpServletRequest);

		_commerceCurrencyLocalService = commerceCurrencyLocalService;
		this.commercePriceFormatter = commercePriceFormatter;
		_cpDefinitionOptionRelService = cpDefinitionOptionRelService;
		_cpInstanceService = cpInstanceService;
		_cpInstanceHelper = cpInstanceHelper;
		_cpMeasurementUnitLocalService = cpMeasurementUnitLocalService;
	}

	public Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
			cpInstanceJsonParse(long cpInstanceId)
		throws PortalException {

		if (cpInstanceId <= 0) {
			return Collections.emptyMap();
		}

		CPInstance cpInstance = _cpInstanceService.getCPInstance(cpInstanceId);

		return _cpInstanceHelper.getCPDefinitionOptionRelsMap(
			cpInstance.getCPDefinitionId(), cpInstance.getJson());
	}

	public ClayCreationMenu getClayCreationMenu() throws Exception {
		ClayCreationMenu clayCreationMenu = new ClayCreationMenu();

		clayCreationMenu.addClayCreationMenuItem(
			new ClayCreationMenuItem(
				_getAddCPInstancePortletURL(),
				LanguageUtil.get(cpRequestHelper.getRequest(), "add-sku"),
				ClayCreationMenuItem.
					CLAY_CREATION_MENU_ITEM_TARGET_SIDE_PANEL));

		return clayCreationMenu;
	}

	public String getCommerceCurrencyCode() throws PortalException {
		CommerceCurrency commerceCurrency = getCommerceCurrency();

		if (commerceCurrency != null) {
			return commerceCurrency.getCode();
		}

		return StringPool.BLANK;
	}

	public List<CPDefinitionOptionRel> getCPDefinitionOptionRels()
		throws PortalException {

		CPDefinition cpDefinition = getCPDefinition();

		if (cpDefinition == null) {
			return Collections.emptyList();
		}

		return _cpDefinitionOptionRelService.getCPDefinitionOptionRels(
			cpDefinition.getCPDefinitionId(), true);
	}

	public List<CPDefinitionOptionValueRel> getCPDefinitionOptionValueRels(
			CPDefinitionOptionRel cpDefinitionOptionRel)
		throws PortalException {

		Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
			cpDefinitionOptionRelListMap = cpInstanceJsonParse(
				getCPInstanceId());

		if (cpDefinitionOptionRelListMap.isEmpty() ||
			!cpDefinitionOptionRelListMap.containsKey(cpDefinitionOptionRel)) {

			return Collections.emptyList();
		}

		return cpDefinitionOptionRelListMap.get(cpDefinitionOptionRel);
	}

	public List<CPDefinitionOptionValueRel> getCPDefinitionOptionValueRels(
			long cpDefinitionOptionValueRelId)
		throws PortalException {

		return actionHelper.getCPDefinitionOptionValueRels(
			cpDefinitionOptionValueRelId);
	}

	public CPInstance getCPInstance() throws PortalException {
		if (_cpInstance != null) {
			return _cpInstance;
		}

		_cpInstance = actionHelper.getCPInstance(
			cpRequestHelper.getRenderRequest());

		return _cpInstance;
	}

	public long getCPInstanceId() throws PortalException {
		CPInstance cpInstance = getCPInstance();

		if (cpInstance == null) {
			return 0;
		}

		return cpInstance.getCPInstanceId();
	}

	public String getCPMeasurementUnitName(int type) {
		CPMeasurementUnit cpMeasurementUnit =
			_cpMeasurementUnitLocalService.fetchPrimaryCPMeasurementUnit(
				cpRequestHelper.getCompanyId(), type);

		if (cpMeasurementUnit != null) {
			return cpMeasurementUnit.getName(cpRequestHelper.getLocale());
		}

		return StringPool.BLANK;
	}

	@Override
	public PortletURL getPortletURL() throws PortalException {
		PortletURL portletURL = super.getPortletURL();

		if (getCPDefinitionId() > 0) {
			portletURL.setParameter(
				"mvcRenderCommandName", "editProductDefinition");
		}
		else {
			portletURL.setParameter("mvcRenderCommandName", "viewInstances");
			portletURL.setParameter(
				"catalogNavigationItem", "view-all-instances");
		}

		portletURL.setParameter(
			"screenNavigationCategoryKey", getScreenNavigationCategoryKey());

		return portletURL;
	}

	@Override
	public String getScreenNavigationCategoryKey() {
		return CPDefinitionScreenNavigationConstants.CATEGORY_KEY_SKUS;
	}

	public boolean hasCustomAttributesAvailable() throws Exception {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return CustomAttributesUtil.hasCustomAttributes(
			themeDisplay.getCompanyId(), CPInstance.class.getName(),
			getCPInstanceId(), null);
	}

	public String renderOptions(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortalException {

		CPDefinition cpDefinition = getCPDefinition();

		return _cpInstanceHelper.renderCPInstanceOptions(
			getCPDefinitionId(), null, cpDefinition.isIgnoreSKUCombinations(),
			true, renderRequest, renderResponse);
	}

	public BigDecimal round(BigDecimal value) throws PortalException {
		CommerceCurrency commerceCurrency = getCommerceCurrency();

		if (commerceCurrency == null) {
			return value;
		}

		return commerceCurrency.round(value);
	}

	protected CommerceCurrency getCommerceCurrency() throws PortalException {
		CPDefinition cpDefinition = getCPDefinition();

		CommerceCatalog commerceCatalog = cpDefinition.getCommerceCatalog();

		return _commerceCurrencyLocalService.getCommerceCurrency(
			commerceCatalog.getCompanyId(),
			commerceCatalog.getCommerceCurrencyCode());
	}

	protected final CommercePriceFormatter commercePriceFormatter;

	private String _getAddCPInstancePortletURL() throws Exception {
		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			httpServletRequest, CPDefinition.class.getName(),
			PortletProvider.Action.MANAGE);

		portletURL.setParameter("mvcRenderCommandName", "editProductInstance");
		portletURL.setParameter(
			"cpDefinitionId", String.valueOf(getCPDefinitionId()));

		portletURL.setWindowState(LiferayWindowState.POP_UP);

		return portletURL.toString();
	}

	private final CommerceCurrencyLocalService _commerceCurrencyLocalService;
	private final CPDefinitionOptionRelService _cpDefinitionOptionRelService;
	private CPInstance _cpInstance;
	private final CPInstanceHelper _cpInstanceHelper;
	private final CPInstanceService _cpInstanceService;
	private final CPMeasurementUnitLocalService _cpMeasurementUnitLocalService;

}