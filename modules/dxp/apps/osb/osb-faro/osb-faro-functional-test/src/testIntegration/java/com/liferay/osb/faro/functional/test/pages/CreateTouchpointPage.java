/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.functional.test.pages;

import com.liferay.osb.faro.functional.test.steps.ClickSteps;
import com.liferay.osb.faro.functional.test.steps.InputSteps;
import com.liferay.osb.faro.functional.test.util.FaroTestConstants;
import com.liferay.osb.faro.functional.test.util.FaroTransformer;

import cucumber.api.Transform;
import cucumber.api.java.en.When;

/**
 * @author Cheryl Tang
 */
public class CreateTouchpointPage {

	/**
	 * Creates a touchpoint.
	 *
	 * @param name the name of the touchpoint
	 * @param url the url of the touchpoint
	 */
	@When("I create a Touchpoint named (.*) for the url: (.*)")
	public static void createTouchpoint(
			@Transform(FaroTransformer.class) String name,
			@Transform(FaroTransformer.class) String url)
		throws Exception {

		InputSteps.inputText(name, "name", FaroTestConstants.INPUT_TYPE_INPUT);
		InputSteps.inputText(url, "url", FaroTestConstants.INPUT_TYPE_INPUT);

		ClickSteps.clickButton("Next Step");
		ClickSteps.clickButton("Create Touchpoint");
	}

}