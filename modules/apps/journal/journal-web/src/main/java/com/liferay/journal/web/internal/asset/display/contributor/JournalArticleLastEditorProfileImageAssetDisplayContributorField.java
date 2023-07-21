/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.journal.web.internal.asset.display.contributor;

import com.liferay.asset.display.contributor.AssetDisplayContributorField;
import com.liferay.journal.model.JournalArticle;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
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
public class JournalArticleLastEditorProfileImageAssetDisplayContributorField
	implements AssetDisplayContributorField<JournalArticle> {

	@Override
	public String getKey() {
		return "lastEditorProfileImage";
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			locale, "com.liferay.journal.lang");

		return LanguageUtil.get(resourceBundle, "last-editor-profile-image");
	}

	@Override
	public String getType() {
		return "image";
	}

	@Override
	public String getValue(JournalArticle article, Locale locale) {
		User user = _userLocalService.fetchUser(article.getUserId());

		if (user != null) {
			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			try {
				return user.getPortraitURL(serviceContext.getThemeDisplay());
			}
			catch (PortalException pe) {
				if (_log.isDebugEnabled()) {
					_log.debug(pe, pe);
				}
			}
		}

		return StringPool.BLANK;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JournalArticleLastEditorProfileImageAssetDisplayContributorField.class);

	@Reference
	private UserLocalService _userLocalService;

}