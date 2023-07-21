/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.journal.web.internal.servlet.taglib.ui;

import com.liferay.item.selector.ItemSelectorView;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;
import com.liferay.portal.kernel.theme.ThemeDisplay;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Eudaldo Alonso
 */
@Component(
	property = "form.navigator.entry.order:Integer=50",
	service = FormNavigatorEntry.class
)
public class JournalDisplayPageFormNavigatorEntry
	extends BaseJournalFormNavigatorEntry {

	@Override
	public String getKey() {
		return "display-page";
	}

	@Override
	public boolean isVisible(User user, JournalArticle article) {
		Group group = null;

		if ((article != null) && (article.getId() > 0)) {
			group = _groupLocalService.fetchGroup(article.getGroupId());
		}
		else {
			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			ThemeDisplay themeDisplay = serviceContext.getThemeDisplay();

			group = themeDisplay.getScopeGroup();
		}

		if ((group != null) && group.isCompany()) {
			return false;
		}

		return true;
	}

	@Reference(target = "(view=private)", unbind = "-")
	public void setPrivateLayoutsItemSelectorView(
		ItemSelectorView itemSelectorView) {
	}

	@Reference(target = "(view=public)", unbind = "-")
	public void setPublicLayoutsItemSelectorView(
		ItemSelectorView itemSelectorView) {
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.journal.web)", unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

	@Override
	protected String getJspPath() {
		return "/article/display_page.jsp";
	}

	@Reference
	private GroupLocalService _groupLocalService;

}