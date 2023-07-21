/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.reports.engine.console.web.admin.util;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;

/**
 * @author Gavin Wan
 * @author Brian Wing Shun Chan
 */
public class EmailConfigurationUtil {

	public static Map<String, String> getEmailDefinitionTerms(
		PortletRequest portletRequest, String emailFromAddress,
		String emailFromName) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Locale locale = themeDisplay.getLocale();

		String fromAddress = HtmlUtil.escape(emailFromAddress);
		String fromName = HtmlUtil.escape(emailFromName);

		String toAddress = LanguageUtil.get(
			locale, "the-address-of-the-email-recipient");
		String toName = LanguageUtil.get(
			locale, "the-name-of-the-email-recipient");

		ResourceBundle resourceBundle = getResourceBundle(locale);

		String pageURL = LanguageUtil.get(resourceBundle, "the-report-url");

		String reportName = LanguageUtil.get(
			resourceBundle, "the-name-of-the-report");

		Map<String, String> definitionTerms = new LinkedHashMap<>();

		definitionTerms.put("[$FROM_ADDRESS$]", fromAddress);
		definitionTerms.put("[$FROM_NAME$]", fromName);

		definitionTerms.put("[$TO_ADDRESS$]", toAddress);
		definitionTerms.put("[$TO_NAME$]", toName);

		definitionTerms.put("[$PAGE_URL$]", pageURL);
		definitionTerms.put("[$REPORT_NAME$]", reportName);

		Company company = themeDisplay.getCompany();

		definitionTerms.put("[$PORTAL_URL$]", company.getVirtualHostname());

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		definitionTerms.put(
			"[$PORTLET_NAME$]", HtmlUtil.escape(portletDisplay.getTitle()));

		return definitionTerms;
	}

	public static ResourceBundle getResourceBundle(Locale locale) {
		return ResourceBundleUtil.getBundle(
			locale, EmailConfigurationUtil.class);
	}

}