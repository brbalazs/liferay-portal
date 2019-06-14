/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.bom.admin.web.internal.display.context;

import com.liferay.commerce.bom.admin.web.internal.display.context.util.CommerceBOMAdminRequestHelper;
import com.liferay.commerce.bom.model.CommerceBOMFolder;
import com.liferay.commerce.bom.model.CommerceBOMFolderConstants;
import com.liferay.commerce.bom.service.CommerceBOMFolderService;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletQName;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.servlet.taglib.ui.BreadcrumbEntry;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alessio Antonio Rendina
 */
public class CommerceBOMAdminDisplayContext {

	public CommerceBOMAdminDisplayContext(
		ModelResourcePermission<CommerceBOMFolder>
			commerceBOMFolderModelResourcePermission,
		CommerceBOMFolderService commerceBOMFolderService,
		HttpServletRequest httpServletRequest) {

		_commerceBOMFolderModelResourcePermission =
			commerceBOMFolderModelResourcePermission;
		_commerceBOMFolderService = commerceBOMFolderService;

		_commerceBOMAdminRequestHelper = new CommerceBOMAdminRequestHelper(
			httpServletRequest);
	}

	public CommerceBOMFolder getCommerceBOMFolder() throws PortalException {
		long commerceBOMFolderId = ParamUtil.getLong(
			_commerceBOMAdminRequestHelper.getRequest(), "commerceBOMFolderId");

		if (commerceBOMFolderId > 0) {
			return _commerceBOMFolderService.getCommerceBOMFolder(
				commerceBOMFolderId);
		}

		return null;
	}

	public long getCommerceBOMFolderId() throws PortalException {
		CommerceBOMFolder commerceBOMFolder = getCommerceBOMFolder();

		if (commerceBOMFolder == null) {
			return CommerceBOMFolderConstants.DEFAULT_COMMERCE_BOM_FOLDER_ID;
		}

		return commerceBOMFolder.getCommerceBOMFolderId();
	}

	public String getKeywords() {
		if (Validator.isNotNull(_keywords)) {
			return _keywords;
		}

		_keywords = ParamUtil.getString(
			_commerceBOMAdminRequestHelper.getRequest(), "keywords");

		return _keywords;
	}

	public String getOrderByCol() {
		return ParamUtil.getString(
			_commerceBOMAdminRequestHelper.getRequest(),
			SearchContainer.DEFAULT_ORDER_BY_COL_PARAM, "name");
	}

	public String getOrderByType() {
		return ParamUtil.getString(
			_commerceBOMAdminRequestHelper.getRequest(),
			SearchContainer.DEFAULT_ORDER_BY_TYPE_PARAM, "asc");
	}

	public List<BreadcrumbEntry> getPortletBreadcrumbEntries(
			CommerceBOMFolder commerceBOMFolder)
		throws Exception {

		List<BreadcrumbEntry> breadcrumbEntries = new ArrayList<>();

		BreadcrumbEntry breadcrumbEntry = new BreadcrumbEntry();

		breadcrumbEntry.setTitle(
			LanguageUtil.get(
				_commerceBOMAdminRequestHelper.getRequest(), "home"));

		PortletURL portletURL = getPortletURL();

		portletURL.setParameter(
			"commerceBOMFolderId",
			String.valueOf(
				CommerceBOMFolderConstants.DEFAULT_COMMERCE_BOM_FOLDER_ID));

		breadcrumbEntry.setURL(portletURL.toString());

		breadcrumbEntries.add(breadcrumbEntry);

		if (commerceBOMFolder == null) {
			return breadcrumbEntries;
		}

		List<CommerceBOMFolder> ancestorCommerceBOMFolders =
			commerceBOMFolder.getAncestors();

		Collections.reverse(ancestorCommerceBOMFolders);

		for (CommerceBOMFolder ancestorCommerceBOMFolder :
				ancestorCommerceBOMFolders) {

			BreadcrumbEntry commerceBOMFolderBreadcrumbEntry =
				new BreadcrumbEntry();

			commerceBOMFolderBreadcrumbEntry.setTitle(
				ancestorCommerceBOMFolder.getName());

			portletURL.setParameter(
				"commerceBOMFolderId",
				String.valueOf(
					ancestorCommerceBOMFolder.getCommerceBOMFolderId()));

			commerceBOMFolderBreadcrumbEntry.setURL(portletURL.toString());

			breadcrumbEntries.add(commerceBOMFolderBreadcrumbEntry);
		}

		if (commerceBOMFolder.getCommerceBOMFolderId() !=
				CommerceBOMFolderConstants.DEFAULT_COMMERCE_BOM_FOLDER_ID) {

			BreadcrumbEntry commerceBOMFolderBreadcrumbEntry =
				new BreadcrumbEntry();

			CommerceBOMFolder unescapedCommerceBOMFolder =
				commerceBOMFolder.toUnescapedModel();

			commerceBOMFolderBreadcrumbEntry.setTitle(
				unescapedCommerceBOMFolder.getName());

			portletURL.setParameter(
				"commerceBOMFolderId",
				String.valueOf(commerceBOMFolder.getCommerceBOMFolderId()));

			commerceBOMFolderBreadcrumbEntry.setURL(portletURL.toString());

			breadcrumbEntries.add(commerceBOMFolderBreadcrumbEntry);
		}

		return breadcrumbEntries;
	}

	public PortletURL getPortletURL() throws PortalException {
		LiferayPortletResponse liferayPortletResponse =
			_commerceBOMAdminRequestHelper.getLiferayPortletResponse();

		PortletURL portletURL = liferayPortletResponse.createRenderURL();

		HttpServletRequest httpServletRequest =
			PortalUtil.getOriginalServletRequest(
				_commerceBOMAdminRequestHelper.getRequest());

		String backURL = ParamUtil.getString(
			httpServletRequest,
			PortletQName.PUBLIC_RENDER_PARAMETER_NAMESPACE + "backURL");

		if (Validator.isNotNull(backURL)) {
			portletURL.setParameter(
				PortletQName.PUBLIC_RENDER_PARAMETER_NAMESPACE + "backURL",
				backURL);
		}

		String redirect = ParamUtil.getString(
			_commerceBOMAdminRequestHelper.getRequest(), "redirect");

		if (Validator.isNotNull(redirect)) {
			portletURL.setParameter("redirect", redirect);
		}

		String delta = ParamUtil.getString(
			_commerceBOMAdminRequestHelper.getRequest(), "delta");

		if (Validator.isNotNull(delta)) {
			portletURL.setParameter("delta", delta);
		}

		String deltaEntry = ParamUtil.getString(
			_commerceBOMAdminRequestHelper.getRequest(), "deltaEntry");

		if (Validator.isNotNull(deltaEntry)) {
			portletURL.setParameter("deltaEntry", deltaEntry);
		}

		String keywords = getKeywords();

		if (Validator.isNotNull(keywords)) {
			portletURL.setParameter("keywords", keywords);
		}

		portletURL.setParameter(
			"commerceBOMFolderId", String.valueOf(getCommerceBOMFolderId()));

		return portletURL;
	}

	public RowChecker getRowChecker() {
		if (_rowChecker == null) {
			_rowChecker = new EmptyOnClickRowChecker(
				_commerceBOMAdminRequestHelper.getLiferayPortletResponse());
		}

		return _rowChecker;
	}

	public SearchContainer<CommerceBOMFolder> getSearchContainer()
		throws PortalException {

		if (_searchContainer != null) {
			return _searchContainer;
		}

		_searchContainer = new SearchContainer<>(
			_commerceBOMAdminRequestHelper.getLiferayPortletRequest(),
			getPortletURL(), null, null);

		_searchContainer.setEmptyResultsMessage("no-folders-were-found");

		_searchContainer.setOrderByCol(getOrderByCol());
		_searchContainer.setOrderByComparator(null);
		_searchContainer.setOrderByType(getOrderByType());
		_searchContainer.setRowChecker(getRowChecker());

		int total = _commerceBOMFolderService.getCommerceBOMFoldersCount(
			_commerceBOMAdminRequestHelper.getCompanyId(),
			getCommerceBOMFolderId());

		_searchContainer.setTotal(total);

		List<CommerceBOMFolder> results =
			_commerceBOMFolderService.getCommerceBOMFolders(
				_commerceBOMAdminRequestHelper.getCompanyId(),
				getCommerceBOMFolderId(), _searchContainer.getStart(),
				_searchContainer.getEnd());

		_searchContainer.setResults(results);

		return _searchContainer;
	}

	public boolean hasPermissions(long commerceBOMFolderId, String actionId)
		throws PortalException {

		return _commerceBOMFolderModelResourcePermission.contains(
			_commerceBOMAdminRequestHelper.getPermissionChecker(),
			commerceBOMFolderId, actionId);
	}

	private final CommerceBOMAdminRequestHelper _commerceBOMAdminRequestHelper;
	private final ModelResourcePermission<CommerceBOMFolder>
		_commerceBOMFolderModelResourcePermission;
	private final CommerceBOMFolderService _commerceBOMFolderService;
	private String _keywords;
	private RowChecker _rowChecker;
	private SearchContainer<CommerceBOMFolder> _searchContainer;

}