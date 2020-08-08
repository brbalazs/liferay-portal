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
	 * Asserts tht the time filter buttons are disabled.
	 */
	@Then("^I should see (?:that )?the time filter is disabled$")
	public void iShouldSeeTheTimeFilterIsDisabled() throws Exception {
		_faroSelenium.assertElementPresent("//button[text()='D'][@disabled]");
		_faroSelenium.assertElementPresent("//button[text()='W'][@disabled]");
		_faroSelenium.assertElementPresent("//button[text()='M'][@disabled]");
		_faroSelenium.assertElementPresent(
			"//button[contains(text(),'Last ')][@disabled]");
	}

	private static final FaroSelenium _faroSelenium =
		FaroSeleniumUtil.getFaroSelenium();

}