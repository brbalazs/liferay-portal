/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.item.selector.web.internal;

import com.liferay.asset.kernel.service.AssetVocabularyService;
import com.liferay.document.library.display.context.DLMimeTypeDisplayContext;
import com.liferay.document.library.item.selector.web.internal.constants.DLItemSelectorWebKeys;
import com.liferay.document.library.item.selector.web.internal.display.context.DLItemSelectorViewDisplayContext;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.item.selector.ItemSelectorReturnTypeResolverHandler;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.language.LanguageResources;
import com.liferay.staging.StagingGroupHelper;

import java.io.IOException;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.PortletURL;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Roberto Díaz
 */
public abstract class BaseDLItemSelectorView<T extends ItemSelectorCriterion>
	implements DLItemSelectorView<T> {

	@Override
	public String[] getExtensions() {
		return new String[0];
	}

	@Override
	public String[] getMimeTypes() {
		return new String[0];
	}

	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	public String getTitle(Locale locale) {
		ResourceBundleLoader resourceBundleLoader = getResourceBundleLoader();

		ResourceBundle resourceBundle = resourceBundleLoader.loadResourceBundle(
			locale);

		return ResourceBundleUtil.getString(
			resourceBundle, "documents-and-media");
	}

	@Override
	public boolean isVisible(ThemeDisplay themeDisplay) {
		return true;
	}

	@Override
	public void renderHTML(
			ServletRequest request, ServletResponse response, T t,
			PortletURL portletURL, String itemSelectedEventName, boolean search)
		throws IOException, ServletException {

		ServletContext servletContext = getServletContext();

		RequestDispatcher requestDispatcher =
			servletContext.getRequestDispatcher("/documents.jsp");

		DLItemSelectorViewDisplayContext dlItemSelectorViewDisplayContext =
			new DLItemSelectorViewDisplayContext(
				t, this, _itemSelectorReturnTypeResolverHandler,
				itemSelectedEventName, search, portletURL,
				_assetVocabularyService, _classNameLocalService,
				stagingGroupHelper);

		request.setAttribute(
			DLItemSelectorWebKeys.DL_ITEM_SELECTOR_VIEW_DISPLAY_CONTEXT,
			dlItemSelectorViewDisplayContext);

		request.setAttribute(
			DLItemSelectorWebKeys.DL_MIME_TYPE_DISPLAY_CONTEXT,
			dlMimeTypeDisplayContext);

		requestDispatcher.include(request, response);
	}

	@Reference(unbind = "-")
	public void setAssetVocabularyService(
		AssetVocabularyService assetVocabularyService) {

		_assetVocabularyService = assetVocabularyService;
	}

	@Reference(unbind = "-")
	public void setClassNameLocalService(
		ClassNameLocalService classNameLocalService) {

		_classNameLocalService = classNameLocalService;
	}

	@Reference(unbind = "-")
	public void setItemSelectorReturnTypeResolverHandler(
		ItemSelectorReturnTypeResolverHandler
			itemSelectorReturnTypeResolverHandler) {

		_itemSelectorReturnTypeResolverHandler =
			itemSelectorReturnTypeResolverHandler;
	}

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.document.library.item.selector.web)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	protected ResourceBundleLoader getResourceBundleLoader() {
		return LanguageResources.RESOURCE_BUNDLE_LOADER;
	}

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	protected volatile DLMimeTypeDisplayContext dlMimeTypeDisplayContext;

	@Reference
	protected StagingGroupHelper stagingGroupHelper;

	private AssetVocabularyService _assetVocabularyService;
	private ClassNameLocalService _classNameLocalService;
	private ItemSelectorReturnTypeResolverHandler
		_itemSelectorReturnTypeResolverHandler;
	private ServletContext _servletContext;

}