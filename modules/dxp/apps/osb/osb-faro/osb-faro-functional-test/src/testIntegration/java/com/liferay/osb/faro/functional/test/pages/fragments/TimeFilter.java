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

package com.liferay.osb.faro.functional.test.pages.fragments;

import com.liferay.osb.faro.functional.test.driver.FaroSelenium;
import com.liferay.osb.faro.functional.test.util.FaroSeleniumUtil;

import cucumber.api.java.en.Then;

/**
 * @author Cheryl Tang
 */
public class TimeFilter {

	/**
	 * Asserts that the Custom Range date picker dropdown shows an error
	 * message when a date range exceeding 365 days is selected.
	 *
	 * @throws Exception
	 */
	@Then("^I should see an error saying the range exceeds the maximum range$")
	public void assertMaximumCustomRange() throws Exception {
		_faroSelenium.assertText(
			"//div[@class='range-warning']",
			"This exceeds the maximum range of 365 days.");
	}

	/**
	 * Asserts tht the time filter buttons are disabled.
	 *
	 * @throws Exception
	 */
	@Then("^I should see (?:that )?the time filter is disabled$")
	public void assertTimeFilterIsDisabled() throws Exception {
		_faroSelenium.assertElementPresent("//button[text()='D'][@disabled]");
		_faroSelenium.assertElementPresent("//button[text()='W'][@disabled]");
		_faroSelenium.assertElementPresent("//button[text()='M'][@disabled]");
		_faroSelenium.assertElementPresent(
			"//button[contains(text(),'Last ')][@disabled]");
	}

	private static final FaroSelenium _faroSelenium =
		FaroSeleniumUtil.getFaroSelenium();

}