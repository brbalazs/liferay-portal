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

package com.liferay.commerce.subscription.web.internal.display.context;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.constants.CommerceActionKeys;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.frontend.model.HeaderActionModel;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.model.CommerceSubscriptionEntry;
import com.liferay.commerce.payment.model.CommercePaymentMethodGroupRel;
import com.liferay.commerce.payment.service.CommercePaymentMethodGroupRelLocalService;
import com.liferay.commerce.product.constants.CPConstants;
import com.liferay.commerce.product.display.context.util.CPRequestHelper;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.product.util.CPSubscriptionType;
import com.liferay.commerce.product.util.CPSubscriptionTypeJSPContributor;
import com.liferay.commerce.product.util.CPSubscriptionTypeJSPContributorRegistry;
import com.liferay.commerce.product.util.CPSubscriptionTypeRegistry;
import com.liferay.commerce.service.CommerceOrderItemLocalService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.service.CommerceSubscriptionEntryLocalService;
import com.liferay.commerce.subscription.web.internal.display.context.util.CommerceSubscriptionDisplayContextHelper;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.webserver.WebServerServletTokenUtil;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.Format;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Luca Pellizzon
 * @author Alessio Antonio Rendina
 */
public class CommerceSubscriptionEntryDisplayContext {

	public CommerceSubscriptionEntryDisplayContext(
		CommerceChannelLocalService commerceChannelLocalService,
		CommercePaymentMethodGroupRelLocalService
			commercePaymentMethodGroupRelLocalService,
		CommerceSubscriptionEntryLocalService commerceSubscriptionEntryService,
		CommerceOrderLocalService commerceOrderLocalService,
		CommerceOrderItemLocalService commerceOrderItemLocalService,
		ConfigurationProvider configurationProvider,
		CPSubscriptionTypeJSPContributorRegistry
			cpSubscriptionTypeJSPContributorRegistry,
		CPSubscriptionTypeRegistry cpSubscriptionTypeRegistry,
		HttpServletRequest httpServletRequest) {

		_commerceChannelLocalService = commerceChannelLocalService;
		_commercePaymentMethodGroupRelLocalService =
			commercePaymentMethodGroupRelLocalService;
		_commerceSubscriptionEntryLocalService =
			commerceSubscriptionEntryService;
		_commerceOrderLocalService = commerceOrderLocalService;
		_commerceOrderItemLocalService = commerceOrderItemLocalService;
		_configurationProvider = configurationProvider;
		_cpSubscriptionTypeJSPContributorRegistry =
			cpSubscriptionTypeJSPContributorRegistry;
		_cpSubscriptionTypeRegistry = cpSubscriptionTypeRegistry;
		_httpServletRequest = httpServletRequest;

		_cpRequestHelper = new CPRequestHelper(httpServletRequest);

		_themeDisplay = _cpRequestHelper.getThemeDisplay();

		_commerceOrderDateFormatDateTime =
			FastDateFormatFactoryUtil.getDateTime(
				DateFormat.MEDIUM, DateFormat.MEDIUM, _themeDisplay.getLocale(),
				_themeDisplay.getTimeZone());

		_portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(
			httpServletRequest);

		_portalPreferenceNamespace = CommerceSubscriptionEntry.class.getName();

		_rowChecker = getRowChecker();
	}

	public String getCommerceAccountThumbnailURL() throws PortalException {
		if (_commerceSubscriptionEntry == null) {
			return StringPool.BLANK;
		}

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.getCommerceOrderItem(
				_commerceSubscriptionEntry.getCommerceOrderItemId());

		CommerceOrder commerceOrder = commerceOrderItem.getCommerceOrder();

		CommerceAccount commerceAccount = commerceOrder.getCommerceAccount();

		ThemeDisplay themeDisplay = _cpRequestHelper.getThemeDisplay();

		StringBundler sb = new StringBundler(5);

		sb.append(themeDisplay.getPathImage());
		sb.append("/organization_logo?img_id=");
		sb.append(commerceAccount.getLogoId());

		if (commerceAccount.getLogoId() > 0) {
			sb.append("&t=");
			sb.append(
				WebServerServletTokenUtil.getToken(
					commerceAccount.getLogoId()));
		}

		return sb.toString();
	}

	public String getCommerceOrderDateTime(CommerceOrder commerceOrder) {
		return _commerceOrderDateFormatDateTime.format(
			commerceOrder.getCreateDate());
	}

	public long getCommerceOrderId() throws PortalException {
		CommerceSubscriptionEntry commerceSubscriptionEntry =
			getCommerceSubscriptionEntry();

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.getCommerceOrderItem(
				commerceSubscriptionEntry.getCommerceOrderItemId());

		return commerceOrderItem.getCommerceOrderId();
	}

	public CommerceSubscriptionEntry getCommerceSubscriptionEntry()
		throws PortalException {

		if (_commerceSubscriptionEntry != null) {
			return _commerceSubscriptionEntry;
		}

		long commerceSubscriptionEntryId = ParamUtil.getLong(
			_httpServletRequest, "commerceSubscriptionEntryId");

		if (commerceSubscriptionEntryId > 0) {
			_commerceSubscriptionEntry =
				_commerceSubscriptionEntryLocalService.
					fetchCommerceSubscriptionEntry(commerceSubscriptionEntryId);
		}

		return _commerceSubscriptionEntry;
	}

	public DropdownItemList getCommerceSubscriptionEntryActionItemList(
			CommerceSubscriptionEntry commerceSubscriptionEntry,
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws PortalException {

		CommerceSubscriptionDisplayContextHelper
			commerceSubscriptionDisplayContextHelper =
				new CommerceSubscriptionDisplayContextHelper(
					commerceSubscriptionEntry, _configurationProvider,
					portletRequest, portletResponse);

		return commerceSubscriptionDisplayContextHelper.
			getCommerceSubscriptionEntryActionItemList();
	}

	public long getCommerceSubscriptionEntryId() throws PortalException {
		CommerceSubscriptionEntry commerceSubscriptionEntry =
			getCommerceSubscriptionEntry();

		if (commerceSubscriptionEntry != null) {
			return commerceSubscriptionEntry.getCommerceSubscriptionEntryId();
		}

		return 0;
	}

	public String getCommerceSubscriptionEntryRemainingCycles(
		CommerceSubscriptionEntry commerceSubscriptionEntry) {

		long maxSubscriptionCycles =
			commerceSubscriptionEntry.getMaxSubscriptionCycles();

		if (maxSubscriptionCycles == 0) {
			return LanguageUtil.get(_httpServletRequest, "unlimited");
		}

		long commerceSubscriptionCycleEntriesCount =
			commerceSubscriptionEntry.getCurrentCycle();

		long remainingCycles =
			maxSubscriptionCycles - commerceSubscriptionCycleEntriesCount;

		return String.valueOf(remainingCycles);
	}

	public CPSubscriptionType getCPSubscriptionType(String subscriptionType) {
		return _cpSubscriptionTypeRegistry.getCPSubscriptionType(
			subscriptionType);
	}

	public CPSubscriptionTypeJSPContributor getCPSubscriptionTypeJSPContributor(
		String subscriptionType) {

		return _cpSubscriptionTypeJSPContributorRegistry.
			getCPSubscriptionTypeJSPContributor(subscriptionType);
	}

	public List<CPSubscriptionType> getCPSubscriptionTypes() {
		return _cpSubscriptionTypeRegistry.getCPSubscriptionTypes();
	}

	public List<DropdownItem> getDropdownItems() {
		List<DropdownItem> headerDropdownItems = new ArrayList<>();

		DropdownItem headerDropdownItem1 = new DropdownItem();

		headerDropdownItem1.setLabel("First link");
		headerDropdownItem1.setHref("/first-link");
		headerDropdownItem1.setIcon("home");

		headerDropdownItems.add(headerDropdownItem1);

		DropdownItem headerDropdownItem2 = new DropdownItem();

		headerDropdownItem2.setLabel("Second link");
		headerDropdownItem2.setIcon("blogs");
		headerDropdownItem2.setHref("/second-link");
		headerDropdownItem2.setActive(true);

		headerDropdownItems.add(headerDropdownItem2);

		return headerDropdownItems;
	}

	public String getEditCommerceOrderURL(long commerceOrderId)
		throws PortalException {

		String orderId;

		if (commerceOrderId > 0) {
			orderId = String.valueOf(commerceOrderId);
		}
		else {
			orderId = String.valueOf(getCommerceOrderId());
		}

		PortletURL portletURL = PortletProviderUtil.getPortletURL(
			_httpServletRequest, _themeDisplay.getScopeGroup(),
			CommerceOrder.class.getName(), PortletProvider.Action.MANAGE);

		portletURL.setParameter("mvcRenderCommandName", "editCommerceOrder");
		portletURL.setParameter("redirect", _themeDisplay.getURLCurrent());
		portletURL.setParameter("commerceOrderId", orderId);

		return portletURL.toString();
	}

	public PortletURL getEditCommerceSubscriptionEntryURL() {
		PortletURL portletURL = getPortletURL();

		portletURL.setParameter(
			"mvcRenderCommandName", "editCommerceSubscriptionEntry");

		return portletURL;
	}

	public List<HeaderActionModel> getHeaderActionModels()
		throws PortalException {

		List<HeaderActionModel> headerActionModels = new ArrayList<>();

		if (_commerceSubscriptionEntry == null) {
			return headerActionModels;
		}

		PortletURL portletURL = getTransitionOrderPortletURL();

		HeaderActionModel headerActionModel;

		portletURL.setParameter("transitionName", "cancel");

		headerActionModel = new HeaderActionModel(
			null, null, portletURL.toString(), null, "cancel");

		headerActionModels.add(headerActionModel);

		portletURL.setParameter("transitionName", "save");

		headerActionModel = new HeaderActionModel(
			"btn-primary", null, portletURL.toString(), null, "save");

		headerActionModels.add(headerActionModel);

		return headerActionModels;
	}

	public String getKeywords() {
		if (_keywords != null) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(_httpServletRequest, "keywords");

		return _keywords;
	}

	public String getOrderByCol() {
		if (_orderByCol != null) {
			return _orderByCol;
		}

		_orderByCol = ParamUtil.getString(_httpServletRequest, "orderByCol");

		if (Validator.isNull(_orderByCol)) {
			_orderByCol = _portalPreferences.getValue(
				_portalPreferenceNamespace, "order-by-col", "create-date");
		}
		else {
			boolean saveOrderBy = ParamUtil.getBoolean(
				_httpServletRequest, "saveOrderBy");

			if (saveOrderBy) {
				_portalPreferences.setValue(
					_portalPreferenceNamespace, "order-by-col", _orderByCol);
			}
		}

		return _orderByCol;
	}

	public String getOrderByType() {
		if (_orderByType != null) {
			return _orderByType;
		}

		_orderByType = ParamUtil.getString(_httpServletRequest, "orderByType");

		if (Validator.isNull(_orderByType)) {
			_orderByType = _portalPreferences.getValue(
				_portalPreferenceNamespace, "order-by-type", "desc");
		}
		else {
			boolean saveOrderBy = ParamUtil.getBoolean(
				_httpServletRequest, "saveOrderBy");

			if (saveOrderBy) {
				_portalPreferences.setValue(
					_portalPreferenceNamespace, "order-by-type", _orderByType);
			}
		}

		return _orderByType;
	}

	public String getOrderPaymentMethodImage() throws PortalException {
		CommerceSubscriptionEntry commerceSubscriptionEntry =
			getCommerceSubscriptionEntry();

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.getCommerceOrderItem(
				commerceSubscriptionEntry.getCommerceOrderItemId());

		CommerceOrder commerceOrder = commerceOrderItem.getCommerceOrder();

		String paymentMethodKey = commerceOrder.getCommercePaymentMethodKey();

		CommerceChannel commerceChannel =
			_commerceChannelLocalService.getCommerceChannelByOrderGroupId(
				commerceOrder.getGroupId());

		CommercePaymentMethodGroupRel commercePaymentMethodGroupRel =
			_commercePaymentMethodGroupRelLocalService.
				fetchCommercePaymentMethodGroupRel(
					commerceChannel.getSiteGroupId(), paymentMethodKey);

		return commercePaymentMethodGroupRel.getImageURL(
			_cpRequestHelper.getThemeDisplay());
	}

	public String getOrderPaymentMethodName() throws PortalException {
		CommerceSubscriptionEntry commerceSubscriptionEntry =
			getCommerceSubscriptionEntry();

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.getCommerceOrderItem(
				commerceSubscriptionEntry.getCommerceOrderItemId());

		CommerceOrder commerceOrder = commerceOrderItem.getCommerceOrder();

		String paymentMethodKey = commerceOrder.getCommercePaymentMethodKey();

		CommerceChannel commerceChannel =
			_commerceChannelLocalService.getCommerceChannelByOrderGroupId(
				commerceOrder.getGroupId());

		CommercePaymentMethodGroupRel commercePaymentMethodGroupRel =
			_commercePaymentMethodGroupRelLocalService.
				fetchCommercePaymentMethodGroupRel(
					commerceChannel.getSiteGroupId(), paymentMethodKey);

		return commercePaymentMethodGroupRel.getName(
			_cpRequestHelper.getLocale());
	}

	public String getOrderPaymentStatus() throws PortalException {
		CommerceSubscriptionEntry commerceSubscriptionEntry =
			getCommerceSubscriptionEntry();

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.getCommerceOrderItem(
				commerceSubscriptionEntry.getCommerceOrderItemId());

		CommerceOrder commerceOrder = commerceOrderItem.getCommerceOrder();

		return CommerceOrderConstants.getPaymentStatusLabel(
			commerceOrder.getPaymentStatus());
	}

	public PortletURL getPortletURL() {
		LiferayPortletResponse liferayPortletResponse =
			_cpRequestHelper.getLiferayPortletResponse();

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		String redirect = ParamUtil.getString(_httpServletRequest, "redirect");

		if (Validator.isNotNull(redirect)) {
			portletURL.setParameter("redirect", redirect);
		}

		long commerceSubscriptionEntryId = ParamUtil.getLong(
			_httpServletRequest, "commerceSubscriptionEntryId");

		if (commerceSubscriptionEntryId > 0) {
			portletURL.setParameter(
				"commerceSubscriptionEntryId",
				String.valueOf(commerceSubscriptionEntryId));
		}

		String delta = ParamUtil.getString(_httpServletRequest, "delta");

		if (Validator.isNotNull(delta)) {
			portletURL.setParameter("delta", delta);
		}

		String deltaEntry = ParamUtil.getString(
			_httpServletRequest, "deltaEntry");

		if (Validator.isNotNull(deltaEntry)) {
			portletURL.setParameter("deltaEntry", deltaEntry);
		}

		String keywords = ParamUtil.getString(_httpServletRequest, "keywords");

		if (Validator.isNotNull(keywords)) {
			portletURL.setParameter("keywords", keywords);
		}

		portletURL.setParameter("navigation", getNavigation());

		String orderByCol = getOrderByCol();

		if (Validator.isNotNull(orderByCol)) {
			portletURL.setParameter("orderByCol", orderByCol);
		}

		String orderByType = getOrderByType();

		if (Validator.isNotNull(orderByType)) {
			portletURL.setParameter("orderByType", orderByType);
		}

		return portletURL;
	}

	public RowChecker getRowChecker() {
		if (_rowChecker == null) {
			_rowChecker = new EmptyOnClickRowChecker(
				_cpRequestHelper.getLiferayPortletResponse());
		}

		return _rowChecker;
	}

	public SearchContainer<CommerceSubscriptionEntry> getSearchContainer()
		throws PortalException {

		if (_searchContainer != null) {
			return _searchContainer;
		}

		String emptyResultsMessage = "there-are-no-subscriptions";

		String navigation = getNavigation();

		if (navigation.equals("active")) {
			emptyResultsMessage = "there-are-no-active-subscriptions";
		}
		else if (navigation.equals("suspended")) {
			emptyResultsMessage = "there-are-no-suspended-subscriptions";
		}
		else if (navigation.equals("cancelled")) {
			emptyResultsMessage = "there-are-no-cancelled-subscriptions";
		}
		else if (navigation.equals("completed")) {
			emptyResultsMessage = "there-are-no-completed-subscriptions";
		}
		else if (navigation.equals("never-ends")) {
			emptyResultsMessage = "there-are-no-unlimited-subscriptions";
		}

		_searchContainer = new SearchContainer<>(
			_cpRequestHelper.getLiferayPortletRequest(), getPortletURL(), null,
			emptyResultsMessage);

		_searchContainer.setOrderByCol(getOrderByCol());
		_searchContainer.setOrderByType(getOrderByType());

		_searchContainer.setRowChecker(_rowChecker);

		List<CommerceSubscriptionEntry> subscriptionEntries =
			_commerceSubscriptionEntryLocalService.
				getCommerceSubscriptionEntries(
					_cpRequestHelper.getCompanyId(),
					_cpRequestHelper.getUserId(), _searchContainer.getStart(),
					_searchContainer.getEnd(),
					_searchContainer.getOrderByComparator());

		_searchContainer.setResults(subscriptionEntries);

		int subscriptionEntriesCount =
			_commerceSubscriptionEntryLocalService.
				getCommerceSubscriptionEntriesCount(
					_cpRequestHelper.getCompanyId(),
					_cpRequestHelper.getUserId());

		_searchContainer.setTotal(subscriptionEntriesCount);

		return _searchContainer;
	}

	public CommerceCurrency getSubscriptionCurrency() throws PortalException {
		CommerceSubscriptionEntry commerceSubscriptionEntry =
			getCommerceSubscriptionEntry();

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.getCommerceOrderItem(
				commerceSubscriptionEntry.getCommerceOrderItemId());

		CommerceOrder commerceOrder = commerceOrderItem.getCommerceOrder();

		return commerceOrder.getCommerceCurrency();
	}

	public Calendar getSubscriptionEndDate() throws PortalException {
		CommerceSubscriptionEntry commerceSubscriptionEntry =
			getCommerceSubscriptionEntry();

		String subscriptionType =
			commerceSubscriptionEntry.getSubscriptionType();

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(commerceSubscriptionEntry.getStartDate());

		long maxSubscriptionCycles =
			commerceSubscriptionEntry.getMaxSubscriptionCycles();

		if (Objects.equals(
				subscriptionType, CPConstants.DAILY_SUBSCRIPTION_TYPE)) {

			calendar.add(Calendar.DAY_OF_YEAR, (int)maxSubscriptionCycles);

			return calendar;
		}
		else if (Objects.equals(
					subscriptionType, CPConstants.MONTHLY_SUBSCRIPTION_TYPE)) {

			calendar.add(Calendar.MONTH, (int)maxSubscriptionCycles);

			return calendar;
		}
		else if (Objects.equals(
					subscriptionType, CPConstants.YEARLY_SUBSCRIPTION_TYPE)) {

			calendar.add(Calendar.YEAR, (int)maxSubscriptionCycles);

			return calendar;
		}
		else if (Objects.equals(
					subscriptionType, CPConstants.WEEKLY_SUBSCRIPTION_TYPE)) {

			calendar.add(Calendar.WEEK_OF_YEAR, (int)maxSubscriptionCycles);

			return calendar;
		}

		return calendar;
	}

	public BigDecimal getSubscriptionTotalPrice() throws PortalException {
		CommerceSubscriptionEntry commerceSubscriptionEntry =
			getCommerceSubscriptionEntry();

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.getCommerceOrderItem(
				commerceSubscriptionEntry.getCommerceOrderItemId());

		BigDecimal finalPrice = commerceOrderItem.getFinalPrice();

		return finalPrice.multiply(
			BigDecimal.valueOf(
				commerceSubscriptionEntry.getMaxSubscriptionCycles()));
	}

	public BigDecimal getSubscriptionUnitPrice() throws PortalException {
		CommerceSubscriptionEntry commerceSubscriptionEntry =
			getCommerceSubscriptionEntry();

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemLocalService.getCommerceOrderItem(
				commerceSubscriptionEntry.getCommerceOrderItemId());

		return commerceOrderItem.getFinalPrice();
	}

	public PortletURL getTransitionOrderPortletURL() {
		LiferayPortletResponse liferayPortletResponse =
			_cpRequestHelper.getLiferayPortletResponse();

		PortletURL portletURL = liferayPortletResponse.createActionURL();

		portletURL.setParameter(ActionRequest.ACTION_NAME, "editCommerceOrder");
		portletURL.setParameter(Constants.CMD, ActionKeys.UPDATE);
		portletURL.setParameter(
			"commerceSubscriptionEntryId",
			String.valueOf(
				_commerceSubscriptionEntry.getCommerceSubscriptionEntryId()));
		portletURL.setParameter("redirect", _cpRequestHelper.getCurrentURL());

		return portletURL;
	}

	public boolean hasCommerceChannel() throws PortalException {
		CommerceContext commerceContext =
			(CommerceContext)_httpServletRequest.getAttribute(
				CommerceWebKeys.COMMERCE_CONTEXT);

		long commerceChannelId = commerceContext.getCommerceChannelId();

		if (commerceChannelId > 0) {
			return true;
		}

		return false;
	}

	public boolean hasManageCommerceSubscriptionEntryPermission() {
		return PortalPermissionUtil.contains(
			_cpRequestHelper.getPermissionChecker(),
			CommerceActionKeys.MANAGE_COMMERCE_SUBSCRIPTIONS);
	}

	public boolean isPaymentMethodActive(String engineKey)
		throws PortalException {

		CommercePaymentMethodGroupRel commercePaymentMethodGroupRel =
			_commercePaymentMethodGroupRelLocalService.
				fetchCommercePaymentMethodGroupRel(
					_cpRequestHelper.getScopeGroupId(), engineKey);

		if (commercePaymentMethodGroupRel == null) {
			return false;
		}

		return commercePaymentMethodGroupRel.isActive();
	}

	protected String getNavigation() {
		return ParamUtil.getString(_httpServletRequest, "navigation", "all");
	}

	private final CommerceChannelLocalService _commerceChannelLocalService;
	private final Format _commerceOrderDateFormatDateTime;
	private final CommerceOrderItemLocalService _commerceOrderItemLocalService;
	private final CommerceOrderLocalService _commerceOrderLocalService;
	private final CommercePaymentMethodGroupRelLocalService
		_commercePaymentMethodGroupRelLocalService;
	private CommerceSubscriptionEntry _commerceSubscriptionEntry;
	private final CommerceSubscriptionEntryLocalService
		_commerceSubscriptionEntryLocalService;
	private final ConfigurationProvider _configurationProvider;
	private final CPRequestHelper _cpRequestHelper;
	private final CPSubscriptionTypeJSPContributorRegistry
		_cpSubscriptionTypeJSPContributorRegistry;
	private final CPSubscriptionTypeRegistry _cpSubscriptionTypeRegistry;
	private final HttpServletRequest _httpServletRequest;
	private String _keywords;
	private String _orderByCol;
	private String _orderByType;
	private final String _portalPreferenceNamespace;
	private final PortalPreferences _portalPreferences;
	private RowChecker _rowChecker;
	private SearchContainer<CommerceSubscriptionEntry> _searchContainer;
	private final ThemeDisplay _themeDisplay;

}