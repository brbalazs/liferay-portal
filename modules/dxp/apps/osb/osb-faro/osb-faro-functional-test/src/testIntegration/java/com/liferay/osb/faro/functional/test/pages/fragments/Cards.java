/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.functional.test.pages.fragments;

import com.liferay.osb.faro.functional.test.driver.FaroSelenium;
import com.liferay.osb.faro.functional.test.util.FaroSeleniumUtil;
import com.liferay.portal.kernel.util.StringBundler;

import cucumber.api.java.en.Then;

/**
 * @author Cheryl Tang
 */
public class Cards {

	/**
	 * Asserts the presence of a card by title.
	 *
	 * @param  cardName the name of the card
	 * @throws Exception if an exception occurred
	 */
	@Then("^I should see a (.*) card$")
	public void assertCard(String cardName) throws Exception {
		StringBundler sb = new StringBundler(4);

		sb.append("//div[contains(@class,'card ')]");
		sb.append("/descendant::div[@title and text()='");
		sb.append(cardName);
		sb.append("']");

		_faroSelenium.assertElementPresent(sb.toString());
	}

	private static final FaroSelenium _faroSelenium =
		FaroSeleniumUtil.getFaroSelenium();

}