/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.message.boards.web.internal.display.context;

import com.liferay.message.boards.constants.MBCategoryConstants;
import com.liferay.message.boards.display.context.MBHomeDisplayContext;
import com.liferay.message.boards.model.MBCategory;
import com.liferay.message.boards.web.internal.display.context.util.MBRequestHelper;
import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Iván Zaera
 */
public class DefaultMBHomeDisplayContext implements MBHomeDisplayContext {

	public DefaultMBHomeDisplayContext(
		HttpServletRequest request, HttpServletResponse response) {

		_mbRequestHelper = new MBRequestHelper(request);
	}

	@Override
	public String getTitle() {
		String title = "add-category[message-board]";

		MBCategory category = _mbRequestHelper.getCategory();

		long parentCategoryId = _mbRequestHelper.getParentCategoryId();

		if (category != null) {
			title = LanguageUtil.format(
				_mbRequestHelper.getRequest(), "edit-x", category.getName(),
				false);
		}
		else if (parentCategoryId !=
					MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID) {

			title = "add-subcategory[message-board]";
		}

		return title;
	}

	@Override
	public UUID getUuid() {
		return _UUID;
	}

	private static final UUID _UUID = UUID.fromString(
		"478C53D5-EB19-4387-A95F-4475746D3E17");

	private final MBRequestHelper _mbRequestHelper;

}