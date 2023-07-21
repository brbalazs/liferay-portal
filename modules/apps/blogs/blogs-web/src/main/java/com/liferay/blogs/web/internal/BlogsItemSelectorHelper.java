/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.blogs.web.internal;

import com.liferay.blogs.constants.BlogsPortletKeys;
import com.liferay.blogs.item.selector.criterion.BlogsItemSelectorCriterion;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType;
import com.liferay.item.selector.criteria.image.criterion.ImageItemSelectorCriterion;
import com.liferay.item.selector.criteria.upload.criterion.UploadItemSelectorCriterion;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Roberto Díaz
 */
@Component(service = BlogsItemSelectorHelper.class)
public class BlogsItemSelectorHelper {

	public String getItemSelectorURL(
		RequestBackedPortletURLFactory requestBackedPortletURLFactory,
		ThemeDisplay themeDisplay, String itemSelectedEventName) {

		if (_itemSelector == null) {
			return null;
		}

		List<ItemSelectorReturnType> desiredItemSelectorReturnTypes =
			new ArrayList<>();

		desiredItemSelectorReturnTypes.add(
			new FileEntryItemSelectorReturnType());

		BlogsItemSelectorCriterion blogsItemSelectorCriterion =
			new BlogsItemSelectorCriterion();

		blogsItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			desiredItemSelectorReturnTypes);

		ImageItemSelectorCriterion imageItemSelectorCriterion =
			new ImageItemSelectorCriterion();

		imageItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			desiredItemSelectorReturnTypes);

		List<ItemSelectorReturnType>
			uploadCriterionDesiredItemSelectorReturnTypes = new ArrayList<>();

		uploadCriterionDesiredItemSelectorReturnTypes.add(
			new FileEntryItemSelectorReturnType());

		PortletURL uploadURL = requestBackedPortletURLFactory.createActionURL(
			BlogsPortletKeys.BLOGS);

		uploadURL.setParameter(
			ActionRequest.ACTION_NAME, "/blogs/upload_cover_image");

		String[] extensions = PropsUtil.getArray(
			PropsKeys.BLOGS_IMAGE_EXTENSIONS);

		UploadItemSelectorCriterion uploadItemSelectorCriterion =
			new UploadItemSelectorCriterion(
				BlogsPortletKeys.BLOGS, uploadURL.toString(),
				LanguageUtil.get(themeDisplay.getLocale(), "blog-images"),
				PropsValues.BLOGS_IMAGE_MAX_SIZE, extensions);

		uploadItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			uploadCriterionDesiredItemSelectorReturnTypes);

		PortletURL itemSelectorURL = _itemSelector.getItemSelectorURL(
			requestBackedPortletURLFactory, itemSelectedEventName,
			blogsItemSelectorCriterion, imageItemSelectorCriterion,
			uploadItemSelectorCriterion);

		return itemSelectorURL.toString();
	}

	@Reference(
		cardinality = ReferenceCardinality.OPTIONAL,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile ItemSelector _itemSelector;

}