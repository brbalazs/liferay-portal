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

package com.liferay.commerce.shipment.web.internal.display.context;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountServiceUtil;
import com.liferay.commerce.address.CommerceAddressFormatter;
import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommercePortletKeys;
import com.liferay.commerce.constants.CommerceShipmentConstants;
import com.liferay.commerce.frontend.ClayCreationMenu;
import com.liferay.commerce.frontend.ClayCreationMenuActionItem;
import com.liferay.commerce.frontend.ClayMenuActionItem;
import com.liferay.commerce.frontend.model.HeaderActionModel;
import com.liferay.commerce.frontend.model.StepModel;
import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.inventory.service.CommerceInventoryWarehouseService;
import com.liferay.commerce.model.CommerceAddress;
import com.liferay.commerce.model.CommerceCountry;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.model.CommerceOrderItem;
import com.liferay.commerce.model.CommerceRegion;
import com.liferay.commerce.model.CommerceShipment;
import com.liferay.commerce.model.CommerceShipmentItem;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.commerce.service.CommerceAddressService;
import com.liferay.commerce.service.CommerceCountryService;
import com.liferay.commerce.service.CommerceOrderItemService;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.commerce.service.CommerceRegionService;
import com.liferay.commerce.service.CommerceShipmentItemService;
import com.liferay.commerce.service.CommerceShipmentService;
import com.liferay.commerce.shipment.web.internal.portlet.action.ActionHelper;
import com.liferay.commerce.util.CommerceUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.search.BaseModelSearchResult;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.webserver.WebServerServletTokenUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alessio Antonio Rendina
 * @author Alec Sloan
 */
public class CommerceShipmentDisplayContext
	extends BaseCommerceShipmentDisplayContext<CommerceShipment> {

	public CommerceShipmentDisplayContext(
		ActionHelper actionHelper, HttpServletRequest httpServletRequest,
		CommerceAddressFormatter commerceAddressFormatter,
		CommerceAddressService commerceAddressService,
		CommerceChannelService commerceChannelService,
		CommerceCountryService commerceCountryService,
		CommerceOrderItemService commerceOrderItemService,
		CommerceOrderLocalService commerceOrderLocalService,
		CommerceRegionService commerceRegionService,
		CommerceShipmentService commerceShipmentService,
		CommerceShipmentItemService commerceShipmentItemService,
		CommerceInventoryWarehouseService commerceInventoryWarehouseService) {

		super(
			actionHelper, httpServletRequest,
			CommerceShipment.class.getSimpleName());

		_commerceAddressFormatter = commerceAddressFormatter;
		_commerceAddressService = commerceAddressService;
		_commerceChannelService = commerceChannelService;
		_commerceCountryService = commerceCountryService;
		_commerceOrderItemService = commerceOrderItemService;
		_commerceOrderLocalService = commerceOrderLocalService;
		_commerceRegionService = commerceRegionService;
		_commerceShipmentService = commerceShipmentService;
		_commerceShipmentItemService = commerceShipmentItemService;
		_commerceInventoryWarehouseService = commerceInventoryWarehouseService;
	}

	public List<CommerceAccount> getCommerceAccountsWithShippableOrders()
		throws PortalException {

		List<CommerceOrder> commerceOrders = getCommerceOrders();

		Stream<CommerceOrder> stream = commerceOrders.stream();

		long[] commerceAccountIds = stream.mapToLong(
			CommerceOrder::getCommerceAccountId
		).toArray();

		commerceAccountIds = ArrayUtil.unique(commerceAccountIds);

		List<CommerceAccount> commerceAccounts = new ArrayList<>();

		for (long commerceAccountId : commerceAccountIds) {
			commerceAccounts.add(
				CommerceAccountServiceUtil.getCommerceAccount(
					commerceAccountId));
		}

		return commerceAccounts;
	}

	public String getCommerceAccountThumbnailURL(
			CommerceAccount commerceAccount, String pathImage)
		throws PortalException {

		StringBundler sb = new StringBundler(5);

		sb.append(pathImage);
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

	public List<CommerceChannel> getCommerceChannels() throws PortalException {
		return _commerceChannelService.searchCommerceChannels(
			cpRequestHelper.getCompanyId());
	}

	public List<CommerceCountry> getCommerceCountries() {
		return _commerceCountryService.getCommerceCountries(
			cpRequestHelper.getCompanyId(), true);
	}

	public int getCommerceInventoryWarehouseItemQuantity(
			long commerceOrderItemId, long commerceInventoryWarehouseId)
		throws PortalException {

		return _commerceOrderItemService.
			getCommerceInventoryWarehouseItemQuantity(
				commerceOrderItemId, commerceInventoryWarehouseId);
	}

	public List<CommerceInventoryWarehouse> getCommerceInventoryWarehouses()
		throws PortalException {

		if (_commerceInventoryWarehouses != null) {
			return _commerceInventoryWarehouses;
		}

		_commerceInventoryWarehouses =
			_commerceInventoryWarehouseService.getCommerceInventoryWarehouses(
				cpRequestHelper.getCompanyId(), _getGroupId(), true);

		return _commerceInventoryWarehouses;
	}

	public long getCommerceOrderId(long commerceShipmentId)
		throws PortalException {

		List<CommerceShipmentItem> commerceShipmentItems =
			_commerceShipmentItemService.getCommerceShipmentItems(
				commerceShipmentId, 0, 1, null);

		if (commerceShipmentItems.isEmpty()) {
			return 0;
		}

		CommerceShipmentItem commerceShipmentItem = commerceShipmentItems.get(
			0);

		CommerceOrderItem commerceOrderItem =
			_commerceOrderItemService.getCommerceOrderItem(
				commerceShipmentItem.getCommerceOrderItemId());

		return commerceOrderItem.getCommerceOrderId();
	}

	public List<CommerceOrderItem> getCommerceOrderItems(long commerceOrderId)
		throws PortalException {

		if (commerceOrderId <= 0) {
			return Collections.emptyList();
		}

		return _commerceOrderItemService.
			getAvailableForShipmentCommerceOrderItems(commerceOrderId);
	}

	public List<CommerceOrder> getCommerceOrders() throws PortalException {
		SearchContext searchContext = _buildSearchContext();

		BaseModelSearchResult<CommerceOrder> baseModelSearchResult =
			_commerceOrderLocalService.searchCommerceOrders(searchContext);

		return baseModelSearchResult.getBaseModels();
	}

	public String getCommerceOrderUrl(long commerceOrderId)
		throws PortalException {

		PortletURL portletURL = PortletURLFactoryUtil.create(
			httpServletRequest, CommercePortletKeys.COMMERCE_ORDER,
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter("mvcRenderCommandName", "editCommerceOrder");
		portletURL.setParameter(
			"commerceOrderId", String.valueOf(commerceOrderId));

		return portletURL.toString();
	}

	public List<CommerceRegion> getCommerceRegions(long commerceCountryId) {
		return _commerceRegionService.getCommerceRegions(
			commerceCountryId, true);
	}

	public String getCommerceShipmentStatusLabel(int status) {
		return LanguageUtil.get(
			cpRequestHelper.getLocale(),
			CommerceShipmentConstants.getShipmentStatusLabel(status));
	}

	public String getDescriptiveShippingAddress() throws PortalException {
		CommerceShipment commerceShipment = getCommerceShipment();

		if (commerceShipment.getCommerceAddressId() == 0) {
			return StringPool.BLANK;
		}

		return _commerceAddressFormatter.getDescriptiveAddress(
			getShippingAddress(), true);
	}

	public List<HeaderActionModel> getHeaderActionModels()
		throws PortalException {

		List<HeaderActionModel> headerActionModels = new ArrayList<>();

		CommerceShipment commerceShipment = getCommerceShipment();

		int[] shipmentStatuses = CommerceShipmentConstants.SHIPMENT_STATUSES;

		int currentShipmentStatus = commerceShipment.getStatus();

		if (currentShipmentStatus !=
				CommerceShipmentConstants.SHIPMENT_STATUS_DELIVERED) {

			int[] availableShipmentStatuses = new int[0];

			if (currentShipmentStatus ==
					CommerceShipmentConstants.
						SHIPMENT_STATUS_READY_TO_BE_SHIPPED) {

				availableShipmentStatuses = ArrayUtil.append(
					availableShipmentStatuses,
					CommerceShipmentConstants.SHIPMENT_STATUS_PROCESSING);
			}

			availableShipmentStatuses = ArrayUtil.append(
				availableShipmentStatuses,
				shipmentStatuses[currentShipmentStatus + 1]);

			for (int shipmentStatus : availableShipmentStatuses) {
				String label = CommerceShipmentConstants.getShipmentStatusLabel(
					shipmentStatus);

				PortletURL portletURL = PortalUtil.getControlPanelPortletURL(
					httpServletRequest, CommercePortletKeys.COMMERCE_SHIPMENT,
					PortletRequest.ACTION_PHASE);

				portletURL.setParameter(
					ActionRequest.ACTION_NAME, "editCommerceShipment");
				portletURL.setParameter(Constants.CMD, "transition");
				portletURL.setParameter(
					"redirect", PortalUtil.getCurrentURL(httpServletRequest));
				portletURL.setParameter(
					"commerceShipmentId",
					String.valueOf(getCommerceShipmentId()));
				portletURL.setParameter(
					"transitionName", String.valueOf(shipmentStatus));

				String buttonClass = "btn-primary";

				int availableStatusesLength = availableShipmentStatuses.length;

				if ((availableStatusesLength > 1) &&
					(shipmentStatus !=
						availableShipmentStatuses
							[availableStatusesLength - 1])) {

					buttonClass = "btn-secondary";
				}

				headerActionModels.add(
					new HeaderActionModel(
						buttonClass, null, portletURL.toString(),
						cpRequestHelper.getPortletName() + label, label));
			}
		}

		return headerActionModels;
	}

	public String getNavigation() {
		return ParamUtil.getString(
			cpRequestHelper.getRequest(), "navigation", "all");
	}

	public String[] getNavigationKeys() {
		int[] shipmentStatuses = CommerceShipmentConstants.SHIPMENT_STATUSES;

		String[] navigationKeys = new String[0];

		navigationKeys = ArrayUtil.append(navigationKeys, "all");

		for (int shipmentStatus : shipmentStatuses) {
			navigationKeys = ArrayUtil.append(
				navigationKeys,
				CommerceShipmentConstants.getShipmentStatusLabel(
					shipmentStatus));
		}

		return navigationKeys;
	}

	public int getNumberOfItemsShipped(long commerceShipmentId)
		throws PortalException {

		return _commerceShipmentItemService.getCommerceShipmentItemsCount(
			commerceShipmentId);
	}

	@Override
	public PortletURL getPortletURL() throws PortalException {
		PortletURL portletURL = super.getPortletURL();

		portletURL.setParameter("navigation", getNavigation());

		return portletURL;
	}

	@Override
	public SearchContainer<CommerceShipment> getSearchContainer()
		throws PortalException {

		if (searchContainer != null) {
			return searchContainer;
		}

		searchContainer = new SearchContainer<>(
			liferayPortletRequest, getPortletURL(), null, null);

		OrderByComparator<CommerceShipment> orderByComparator =
			CommerceUtil.getCommerceShipmentOrderByComparator(
				getOrderByCol(), getOrderByType());

		String emptyResultsMessage = "no-shipments-were-found";

		String navigation = getNavigation();

		if (!navigation.equals("all")) {
			emptyResultsMessage = LanguageUtil.format(
				cpRequestHelper.getRequest(), "no-x-shipments-were-found",
				navigation, true);
		}

		searchContainer.setEmptyResultsMessage(emptyResultsMessage);
		searchContainer.setOrderByCol(getOrderByCol());
		searchContainer.setOrderByComparator(orderByComparator);
		searchContainer.setOrderByType(getOrderByType());

		int total;
		List<CommerceShipment> results;

		Integer shipmentStatus = CommerceShipmentConstants.getShipmentStatus(
			navigation);

		if (!navigation.equals("all") && (shipmentStatus != null)) {
			total = _commerceShipmentService.getCommerceShipmentsCount(
				cpRequestHelper.getCompanyId(), shipmentStatus);
			results = _commerceShipmentService.getCommerceShipments(
				cpRequestHelper.getCompanyId(), shipmentStatus,
				searchContainer.getStart(), searchContainer.getEnd(),
				orderByComparator);
		}
		else {
			total = _commerceShipmentService.getCommerceShipmentsCount(
				cpRequestHelper.getCompanyId());
			results = _commerceShipmentService.getCommerceShipments(
				cpRequestHelper.getCompanyId(), searchContainer.getStart(),
				searchContainer.getEnd(), orderByComparator);
		}

		searchContainer.setTotal(total);
		searchContainer.setResults(results);

		return searchContainer;
	}

	public ClayCreationMenu getShipmentClayCreationMenu()
		throws PortalException, WindowStateException {

		ClayCreationMenu clayCreationMenu = new ClayCreationMenu();

		if (hasManageCommerceShipmentsPermission()) {
			PortletURL portletURL = getPortletURL();

			portletURL.setParameter(
				"redirect", PortalUtil.getCurrentURL(httpServletRequest));
			portletURL.setParameter(
				"mvcRenderCommandName", "addCommerceShipment");
			portletURL.setWindowState(LiferayWindowState.POP_UP);

			clayCreationMenu.addClayCreationMenuItem(
				new ClayCreationMenuActionItem(
					portletURL.toString(),
					LanguageUtil.format(
						cpRequestHelper.getRequest(), "add-x", "shipment"),
					ClayMenuActionItem.CLAY_MENU_ACTION_ITEM_TARGET_MODAL));
		}

		return clayCreationMenu;
	}

	public List<ClayMenuActionItem> getShipmentItemBulkActions()
		throws PortalException, WindowStateException {

		List<ClayMenuActionItem> bulkActions = new ArrayList<>();

		CommerceShipment commerceShipment = getCommerceShipment();

		if (hasManageCommerceShipmentsPermission() &&
			(commerceShipment.getStatus() ==
				CommerceShipmentConstants.SHIPMENT_STATUS_PROCESSING)) {

			bulkActions.add(new ClayMenuActionItem(null, null, null, null));
		}

		return bulkActions;
	}

	public ClayCreationMenu getShipmentItemClayCreationMenu()
		throws PortalException, WindowStateException {

		ClayCreationMenu clayCreationMenu = new ClayCreationMenu();

		CommerceShipment commerceShipment = getCommerceShipment();

		if (hasManageCommerceShipmentsPermission() &&
			(commerceShipment.getStatus() ==
				CommerceShipmentConstants.SHIPMENT_STATUS_PROCESSING)) {

			PortletURL portletURL = getPortletURL();

			portletURL.setParameter(
				"redirect", PortalUtil.getCurrentURL(httpServletRequest));
			portletURL.setParameter(
				"commerceShipmentId",
				String.valueOf(commerceShipment.getCommerceShipmentId()));
			portletURL.setParameter(
				"mvcRenderCommandName", "addCommerceShipmentItems");
			portletURL.setWindowState(LiferayWindowState.POP_UP);

			clayCreationMenu.addClayCreationMenuItem(
				new ClayCreationMenuActionItem(
					portletURL.toString(),
					LanguageUtil.format(
						cpRequestHelper.getRequest(), "add-x", "shipment-item"),
					ClayMenuActionItem.CLAY_MENU_ACTION_ITEM_TARGET_MODAL));
		}

		return clayCreationMenu;
	}

	public List<StepModel> getShipmentSteps() throws PortalException {
		CommerceShipment commerceShipment = getCommerceShipment();

		List<StepModel> steps = new ArrayList<>();

		for (int shipmentStatus : CommerceShipmentConstants.SHIPMENT_STATUSES) {
			StepModel step = new StepModel();

			step.setId(String.valueOf(shipmentStatus));
			step.setLabel(
				LanguageUtil.get(
					httpServletRequest,
					CommerceShipmentConstants.getShipmentStatusLabel(
						shipmentStatus)));

			if (commerceShipment.getStatus() == shipmentStatus) {
				step.setState("active");
			}
			else if (commerceShipment.getStatus() > shipmentStatus) {
				step.setState("completed");
			}
			else {
				step.setState("inactive");
			}

			steps.add(step);
		}

		return steps;
	}

	public CommerceAddress getShippingAddress() throws PortalException {
		CommerceShipment commerceShipment = getCommerceShipment();

		return _commerceAddressService.getCommerceAddress(
			commerceShipment.getCommerceAddressId());
	}

	public boolean hasOrderItemsAvailableToShip() throws PortalException {
		CommerceShipment commerceShipment = getCommerceShipment();

		int commerceOrderItemsCount =
			_commerceOrderItemService.getCommerceOrderItemsCount(
				commerceShipment.getCommerceAccountId(),
				CommerceOrderConstants.ORDER_STATUS_FULFILLED);

		if (commerceOrderItemsCount > 0) {
			return true;
		}

		return false;
	}

	private SearchContext _buildSearchContext() throws PortalException {
		SearchContext searchContext = new SearchContext();

		searchContext.setAttribute(
			"orderStatuses", CommerceOrderConstants.ORDER_STATUS_FULFILLED);

		searchContext.setAttribute(
			"useSearchResultPermissionFilter", Boolean.FALSE);

		searchContext.setAttribute(
			Field.STATUS, WorkflowConstants.STATUS_APPROVED);

		searchContext.setCompanyId(cpRequestHelper.getCompanyId());
		searchContext.setStart(QueryUtil.ALL_POS);
		searchContext.setEnd(QueryUtil.ALL_POS);

		long[] commerceChannelGroupIds = _getCommerceChannelGroupIds();

		if ((commerceChannelGroupIds != null) &&
			(commerceChannelGroupIds.length > 0)) {

			searchContext.setGroupIds(commerceChannelGroupIds);
		}

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		return searchContext;
	}

	private long[] _getCommerceChannelGroupIds() throws PortalException {
		List<CommerceChannel> commerceChannels = getCommerceChannels();

		Stream<CommerceChannel> stream = commerceChannels.stream();

		return stream.mapToLong(
			CommerceChannel::getGroupId
		).toArray();
	}

	private long _getGroupId() throws PortalException {
		CommerceShipment commerceShipment = getCommerceShipment();

		if (commerceShipment != null) {
			return commerceShipment.getGroupId();
		}

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.getCommerceOrder(
				ParamUtil.getLong(httpServletRequest, "commerceOrderId"));

		return commerceOrder.getGroupId();
	}

	private final CommerceAddressFormatter _commerceAddressFormatter;
	private final CommerceAddressService _commerceAddressService;
	private final CommerceChannelService _commerceChannelService;
	private final CommerceCountryService _commerceCountryService;
	private List<CommerceInventoryWarehouse> _commerceInventoryWarehouses;
	private final CommerceInventoryWarehouseService
		_commerceInventoryWarehouseService;
	private final CommerceOrderItemService _commerceOrderItemService;
	private final CommerceOrderLocalService _commerceOrderLocalService;
	private final CommerceRegionService _commerceRegionService;
	private final CommerceShipmentItemService _commerceShipmentItemService;
	private final CommerceShipmentService _commerceShipmentService;

}