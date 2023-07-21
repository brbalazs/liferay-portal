/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.journal.web.internal.asset.display.contributor;

import com.liferay.asset.display.contributor.AssetDisplayContributorField;
import com.liferay.journal.model.JournalArticle;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jürgen Kappler
 */
@Component(
	property = "model.class.name=com.liferay.journal.model.JournalArticle",
	service = AssetDisplayContributorField.class
)
public class JournalArticleLastEditorNameAssetDisplayContributorField
	implements AssetDisplayContributorField<JournalArticle> {

	@Override
	public String getKey() {
		return "lastEditorName";
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			locale, "com.liferay.journal.lang");

		return LanguageUtil.get(resourceBundle, "last-editor-name");
	}

	@Override
	public String getType() {
		return "text";
	}

	@Override
	public String getValue(JournalArticle article, Locale locale) {
		User user = _userLocalService.fetchUser(article.getUserId());

		if (user != null) {
			return user.getFullName();
		}

		return StringPool.BLANK;
	}

	@Reference
	private UserLocalService _userLocalService;

}