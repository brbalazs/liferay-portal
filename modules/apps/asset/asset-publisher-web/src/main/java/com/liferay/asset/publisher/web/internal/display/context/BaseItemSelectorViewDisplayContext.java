/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.publisher.web.internal.display.context;

import com.liferay.asset.publisher.constants.AssetPublisherWebKeys;
import com.liferay.asset.publisher.util.AssetPublisherHelper;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletURLUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.site.item.selector.criterion.SiteItemSelectorCriterion;

import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Eudaldo Alonso
 */
public abstract class BaseItemSelectorViewDisplayContext
	implements ItemSelectorViewDisplayContext {

	public BaseItemSelectorViewDisplayContext(
		HttpServletRequest request, AssetPublisherHelper assetPublisherHelper,
		SiteItemSelectorCriterion siteItemSelectorCriterion,
		String itemSelectedEventName, PortletURL portletURL) {

		this.request = request;
		_assetPublisherHelper = assetPublisherHelper;
		_siteItemSelectorCriterion = siteItemSelectorCriterion;
		_itemSelectedEventName = itemSelectedEventName;
		this.portletURL = portletURL;
	}

	@Override
	public String getDisplayStyle() {
		String displayStyle = ParamUtil.getString(request, "displayStyle");

		PortalPreferences portalPreferences =
			PortletPreferencesFactoryUtil.getPortalPreferences(request);

		if (Validator.isNull(displayStyle)) {
			displayStyle = portalPreferences.getValue(
				AssetPublisherWebKeys.ITEM_SELECTOR, "display-style", "icon");
		}
		else {
			portalPreferences.setValue(
				AssetPublisherWebKeys.ITEM_SELECTOR, "display-style",
				displayStyle);

			request.setAttribute(
				WebKeys.SINGLE_PAGE_APPLICATION_CLEAR_CACHE, Boolean.TRUE);
		}

		return displayStyle;
	}

	public long getGroupId() {
		if (_groupId != null) {
			return _groupId;
		}

		_groupId = ParamUtil.getLong(request, "groupId");

		return _groupId;
	}

	@Override
	public String getItemSelectedEventName() {
		return _itemSelectedEventName;
	}

	@Override
	public PortletRequest getPortletRequest() {
		return (PortletRequest)request.getAttribute(
			JavaConstants.JAVAX_PORTLET_REQUEST);
	}

	@Override
	public PortletResponse getPortletResponse() {
		return (PortletResponse)request.getAttribute(
			JavaConstants.JAVAX_PORTLET_RESPONSE);
	}

	@Override
	public PortletURL getPortletURL() throws PortletException {
		PortletURL portletURL = PortletURLUtil.clone(
			this.portletURL,
			PortalUtil.getLiferayPortletResponse(getPortletResponse()));

		long plid = ParamUtil.getLong(request, "plid");
		long groupId = ParamUtil.getLong(request, "groupId");
		boolean privateLayout = ParamUtil.getBoolean(request, "privateLayout");
		String portletResource = ParamUtil.getString(
			request, "portletResource");

		portletURL.setParameter("plid", String.valueOf(plid));
		portletURL.setParameter("groupId", String.valueOf(groupId));
		portletURL.setParameter("privateLayout", String.valueOf(privateLayout));
		portletURL.setParameter("portletResource", portletResource);

		return portletURL;
	}

	@Override
	public long[] getSelectedGroupIds() {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		String portletResource = ParamUtil.getString(
			request, "portletResource");

		long plid = ParamUtil.getLong(request, "plid");

		Layout layout = LayoutLocalServiceUtil.fetchLayout(plid);

		if (layout == null) {
			return new long[0];
		}

		PortletPreferences portletPreferences =
			themeDisplay.getStrictLayoutPortletSetup(layout, portletResource);

		return _assetPublisherHelper.getGroupIds(
			portletPreferences, themeDisplay.getScopeGroupId(),
			themeDisplay.getLayout());
	}

	@Override
	public SiteItemSelectorCriterion getSiteItemSelectorCriterion() {
		return _siteItemSelectorCriterion;
	}

	@Override
	public boolean isShowSearch() {
		return true;
	}

	protected final PortletURL portletURL;
	protected final HttpServletRequest request;

	private final AssetPublisherHelper _assetPublisherHelper;
	private Long _groupId;
	private final String _itemSelectedEventName;
	private final SiteItemSelectorCriterion _siteItemSelectorCriterion;

}