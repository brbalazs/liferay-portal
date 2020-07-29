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

package com.liferay.commerce.pricing.web.internal.display.context;

import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyService;
import com.liferay.commerce.currency.util.comparator.CommerceCurrencyPriorityComparator;
import com.liferay.commerce.frontend.ClayCreationMenu;
import com.liferay.commerce.frontend.ClayCreationMenuActionItem;
import com.liferay.commerce.frontend.ClayMenuActionItem;
import com.liferay.commerce.frontend.clay.data.set.ClayHeadlessDataSetActionTemplate;
import com.liferay.commerce.frontend.model.HeaderActionModel;
import com.liferay.commerce.price.list.constants.CommercePriceListActionKeys;
import com.liferay.commerce.price.list.model.CommercePriceList;
import com.liferay.commerce.price.list.service.CommercePriceListService;
import com.liferay.commerce.pricing.model.CommercePriceModifier;
import com.liferay.commerce.pricing.service.CommercePriceModifierService;
import com.liferay.commerce.pricing.type.CommercePriceModifierType;
import com.liferay.commerce.pricing.type.CommercePriceModifierTypeRegistry;
import com.liferay.commerce.pricing.web.internal.servlet.taglib.ui.CommercePriceListScreenNavigationConstants;
import com.liferay.commerce.product.display.context.util.CPRequestHelper;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CommerceCatalogService;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.util.CustomAttributesUtil;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionURL;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.RenderURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alessio Antonio Rendina
 */
public class CommercePriceListDisplayContext
	extends BaseCommercePriceListDisplayContext {

	public CommercePriceListDisplayContext(
		CommerceCatalogService commerceCatalogService,
		CommerceCurrencyService commerceCurrencyService,
		ModelResourcePermission<CommercePriceList>
			commercePriceListModelResourcePermission,
		CommercePriceListService commercePriceListService,
		CommercePriceModifierService commercePriceModifierService,
		CommercePriceModifierTypeRegistry commercePriceModifierTypeRegistry,
		HttpServletRequest httpServletRequest) {

		super(
			commerceCatalogService, commercePriceListModelResourcePermission,
			commercePriceListService, httpServletRequest);

		_commerceCurrencyService = commerceCurrencyService;
		_commercePriceModifierService = commercePriceModifierService;
		_commercePriceModifierTypeRegistry = commercePriceModifierTypeRegistry;
	}

	public String getAddCommercePriceListRenderURL() throws Exception {
		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		portletURL.setParameter("mvcRenderCommandName", "addCommercePriceList");
		portletURL.setWindowState(LiferayWindowState.POP_UP);

		return portletURL.toString();
	}

	public String getAddCommercePriceModifierRenderURL() throws Exception {
		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		portletURL.setParameter(
			"mvcRenderCommandName", "addCommercePriceModifier");
		portletURL.setParameter(
			"commercePriceListId", String.valueOf(getCommercePriceListId()));
		portletURL.setWindowState(LiferayWindowState.POP_UP);

		return portletURL.toString();
	}

	public ClayCreationMenu getClayCreationPriceListMenu() throws Exception {
		ClayCreationMenu clayCreationMenu = new ClayCreationMenu();

		if (hasPermission(
				CommercePriceListActionKeys.ADD_COMMERCE_PRICE_LIST)) {

			clayCreationMenu.addClayCreationMenuActionItem(
				new ClayCreationMenuActionItem(
					getAddCommercePriceListRenderURL(),
					LanguageUtil.get(
						httpServletRequest, "create-new-price-list"),
					ClayMenuActionItem.
						CLAY_MENU_ACTION_ITEM_TARGET_MODAL_LARGE));
		}

		return clayCreationMenu;
	}

	public List<ClayHeadlessDataSetActionTemplate>
			getClayHeadlessDataSetActionPriceListTemplates()
		throws PortalException {

		RenderResponse renderResponse =
			commercePricingRequestHelper.getRenderResponse();

		RenderURL portletURL = renderResponse.createRenderURL();

		portletURL.setParameter(
			"mvcRenderCommandName", "editCommercePriceList");
		portletURL.setParameter(
			"redirect", commercePricingRequestHelper.getCurrentURL());
		portletURL.setParameter("commercePriceListId", "{id}");
		portletURL.setParameter(
			"screenNavigationCategoryKey",
			CommercePriceListScreenNavigationConstants.CATEGORY_KEY_DETAILS);

		return getClayHeadlessDataSetActionTemplates(
			portletURL.toString(), false);
	}

	public List<ClayHeadlessDataSetActionTemplate>
			getClayHeadlessDataSetActionPriceModifierCategoryTemplates()
		throws PortalException {

		List<ClayHeadlessDataSetActionTemplate>
			clayHeadlessDataSetActionTemplates = new ArrayList<>();

		clayHeadlessDataSetActionTemplates.add(
			new ClayHeadlessDataSetActionTemplate(
				null, "trash", "delete",
				LanguageUtil.get(httpServletRequest, "delete"), "delete",
				"delete", "headless"));

		return clayHeadlessDataSetActionTemplates;
	}

	public List<ClayHeadlessDataSetActionTemplate>
			getClayHeadlessDataSetActionPriceModifierCPDefinitionTemplates()
		throws PortalException {

		List<ClayHeadlessDataSetActionTemplate>
			clayHeadlessDataSetActionTemplates = new ArrayList<>();

		clayHeadlessDataSetActionTemplates.add(
			new ClayHeadlessDataSetActionTemplate(
				null, "trash", "delete",
				LanguageUtil.get(httpServletRequest, "delete"), "delete",
				"delete", "headless"));

		return clayHeadlessDataSetActionTemplates;
	}

	public List<ClayHeadlessDataSetActionTemplate>
			getClayHeadlessDataSetActionPriceModifierPricingClassTemplates()
		throws PortalException {

		List<ClayHeadlessDataSetActionTemplate>
			clayHeadlessDataSetActionTemplates = new ArrayList<>();

		clayHeadlessDataSetActionTemplates.add(
			new ClayHeadlessDataSetActionTemplate(
				null, "trash", "delete",
				LanguageUtil.get(httpServletRequest, "delete"), "delete",
				"delete", "headless"));

		return clayHeadlessDataSetActionTemplates;
	}

	public List<CommerceCatalog> getCommerceCatalogs() throws PortalException {
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return commerceCatalogService.searchCommerceCatalogs(
			themeDisplay.getCompanyId(), null, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	public List<CommerceCurrency> getCommerceCurrencies()
		throws PortalException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		return _commerceCurrencyService.getCommerceCurrencies(
			themeDisplay.getCompanyId(), true, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, new CommerceCurrencyPriorityComparator(true));
	}

	public CommercePriceModifier getCommercePriceModifier()
		throws PortalException {

		if (_commercePriceModifier != null) {
			return _commercePriceModifier;
		}

		long commercePriceModifierId = ParamUtil.getLong(
			httpServletRequest, "commercePriceModifierId");

		if (commercePriceModifierId > 0) {
			_commercePriceModifier =
				_commercePriceModifierService.getCommercePriceModifier(
					commercePriceModifierId);
		}

		return _commercePriceModifier;
	}

	public long getCommercePriceModifierId() throws PortalException {
		CommercePriceModifier commercePriceModifier =
			getCommercePriceModifier();

		if (commercePriceModifier == null) {
			return 0;
		}

		return commercePriceModifier.getCommercePriceModifierId();
	}

	public List<CommercePriceModifierType> getCommercePriceModifierTypes() {
		return _commercePriceModifierTypeRegistry.
			getCommercePriceModifierTypes();
	}

	public List<HeaderActionModel> getHeaderActionModels()
		throws PortalException {

		List<HeaderActionModel> headerActionModels = new ArrayList<>();

		CPRequestHelper cpRequestHelper = new CPRequestHelper(
			httpServletRequest);

		RenderResponse renderResponse = cpRequestHelper.getRenderResponse();

		RenderURL cancelURL = renderResponse.createRenderURL();

		HeaderActionModel cancelHeaderActionModel = new HeaderActionModel(
			null, cancelURL.toString(), null, "cancel");

		headerActionModels.add(cancelHeaderActionModel);

		CommercePriceList commercePriceList = getCommercePriceList();

		ActionURL actionURL = renderResponse.createActionURL();

		actionURL.setParameter(
			ActionRequest.ACTION_NAME, "editCommercePriceList");

		String saveButtonLabel = "save";

		if ((commercePriceList == null) || commercePriceList.isDraft() ||
			commercePriceList.isApproved() || commercePriceList.isExpired() ||
			commercePriceList.isScheduled()) {

			saveButtonLabel = "save-as-draft";
		}

		HeaderActionModel saveAsDraftHeaderActionModel = new HeaderActionModel(
			null, renderResponse.getNamespace() + "fm", actionURL.toString(),
			null, saveButtonLabel);

		headerActionModels.add(saveAsDraftHeaderActionModel);

		String publishButtonLabel = "publish";

		if (WorkflowDefinitionLinkLocalServiceUtil.hasWorkflowDefinitionLink(
				cpRequestHelper.getCompanyId(),
				cpRequestHelper.getScopeGroupId(),
				CommercePriceList.class.getName())) {

			publishButtonLabel = "submit-for-publication";
		}

		String additionalClasses = "btn-primary";

		if ((commercePriceList != null) && commercePriceList.isPending()) {
			additionalClasses = additionalClasses + " disabled";
		}

		HeaderActionModel publishHeaderActionModel = new HeaderActionModel(
			additionalClasses, renderResponse.getNamespace() + "fm",
			actionURL.toString(),
			renderResponse.getNamespace() + "publishButton",
			publishButtonLabel);

		headerActionModels.add(publishHeaderActionModel);

		return headerActionModels;
	}

	public long getParentCommercePriceListId() throws PortalException {
		CommercePriceList commercePriceList = getCommercePriceList();

		if (commercePriceList == null) {
			return 0;
		}

		return commercePriceList.getParentCommercePriceListId();
	}

	public String getPriceListsApiUrl(String portletName) {
		StringBundler sb = new StringBundler(7);

		sb.append("/o/headless-commerce-admin-pricing/v2.0/price-lists");
		sb.append("?filter=type eq ");
		sb.append(StringPool.BACK_SLASH);
		sb.append(StringPool.APOSTROPHE);
		sb.append(getCommercePriceListType(portletName));
		sb.append(StringPool.BACK_SLASH);
		sb.append(StringPool.APOSTROPHE);

		return sb.toString();
	}

	public String getPriceModifierCategoriesApiUrl() throws PortalException {
		return "/o/headless-commerce-admin-pricing/v2.0/price-modifiers/" +
			getCommercePriceModifierId() +
				"/price-modifier-categories?nestedFields=category";
	}

	public String getPriceModifierCPDefinitionApiUrl() throws PortalException {
		return "/o/headless-commerce-admin-pricing/v2.0/price-modifiers/" +
			getCommercePriceModifierId() +
				"/price-modifier-products?nestedFields=product";
	}

	public String getPriceModifierPricingClassesApiUrl()
		throws PortalException {

		return "/o/headless-commerce-admin-pricing/v2.0/price-modifiers/" +
			getCommercePriceModifierId() +
				"/price-modifier-product-groups?nestedFields=productGroup";
	}

	public ClayCreationMenu getPriceModifiersClayCreationMenu()
		throws Exception {

		ClayCreationMenu clayCreationMenu = new ClayCreationMenu();

		if (hasPermission(getCommercePriceListId(), ActionKeys.UPDATE)) {
			clayCreationMenu.addClayCreationMenuActionItem(
				new ClayCreationMenuActionItem(
					getAddCommercePriceModifierRenderURL(),
					LanguageUtil.get(httpServletRequest, "add-price-modifier"),
					ClayMenuActionItem.
						CLAY_MENU_ACTION_ITEM_TARGET_MODAL_LARGE));
		}

		return clayCreationMenu;
	}

	public boolean hasCustomAttributesAvailable(String className, long classPK)
		throws Exception {

		return CustomAttributesUtil.hasCustomAttributes(
			commercePricingRequestHelper.getCompanyId(), className, classPK,
			null);
	}

	public boolean isSelectedCatalog(CommerceCatalog commerceCatalog)
		throws PortalException {

		CommercePriceList commercePriceList = getCommercePriceList();

		if (commerceCatalog.getGroupId() == commercePriceList.getGroupId()) {
			return true;
		}

		return false;
	}

	private final CommerceCurrencyService _commerceCurrencyService;
	private CommercePriceModifier _commercePriceModifier;
	private final CommercePriceModifierService _commercePriceModifierService;
	private final CommercePriceModifierTypeRegistry
		_commercePriceModifierTypeRegistry;

}