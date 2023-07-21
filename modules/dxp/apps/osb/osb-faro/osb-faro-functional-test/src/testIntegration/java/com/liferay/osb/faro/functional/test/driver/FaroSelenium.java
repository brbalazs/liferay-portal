/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.functional.test.driver;

import com.liferay.poshi.runner.selenium.LiferaySelenium;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Cheryl Tang
 */
public interface FaroSelenium extends LiferaySelenium, WebDriver {

	public void assertWebElementHasAnyText(String xpath);

	@Override
	public void click(String locator);

	public void click(WebElement webElement);

	public void clickIcon(String className);

	public void dragAndDropChrome(String fromElement, String toElement)
		throws Exception;

	public WebElement findElement(String xpath);

	public WebElement findElement(String xpath, WebElement webElement);

	public List<WebElement> findElements(String xpath);

	public void forceWindowFocus();

	public String getIdFromURL() throws Exception;

	public String getPropertiesIdFromURL() throws Exception;

	public String getWorkspaceIdFromURL() throws Exception;

	public void refreshUntilElementNotPresent(
			int timeout, int pollInterval, String xpath)
		throws Exception;

	public void refreshUntilElementPresent(
			int timeout, int pollInterval, String xpath)
		throws Exception;

	public void refreshUntilTextAsserted(
			int timeout, int pollInterval, String xpath, String expectedValue)
		throws Exception;

	@Override
	public void sendKeys(String locator, String value) throws Exception;

	public void setMainWindowHandle();

	public void switchToMainWindow();

	public void switchToPopupWindow() throws Exception;

	public void waitForElementNotPresent(String locator) throws Exception;

	public void waitForElementPresent(String locator) throws Exception;

	public void waitForLoadingComplete() throws Exception;

	public void waitForPageLoadingComplete();

	public boolean webElementContainsText(WebElement webElement, String pattern)
		throws Exception;

}