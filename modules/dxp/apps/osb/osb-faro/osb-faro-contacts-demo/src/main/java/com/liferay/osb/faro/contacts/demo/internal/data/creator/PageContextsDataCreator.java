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

package com.liferay.osb.faro.contacts.demo.internal.data.creator;

import java.util.HashMap;
import java.util.Map;

import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.DeviceType;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;

/**
 * @author Matthew Kong
 */
public class PageContextsDataCreator extends DataCreator {

	@Override
	protected Map<String, Object> doCreate(Object[] params) {
		Map<String, Object> pageContext = new HashMap<>();

		pageContext.put("browserName", "Chrome");

		String url = "https://" + internet.url();

		pageContext.put("canonicalUrl", url);

		String userAgentString = internet.userAgentAny();

		UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

		Browser browser = userAgent.getBrowser();

		pageContext.put("browserName", browser.getName());

		pageContext.put("contentLanguageId", "en-US");
		pageContext.put("crawler", "False");

		OperatingSystem operatingSystem = userAgent.getOperatingSystem();

		String deviceName = null;

		DeviceType deviceType = operatingSystem.getDeviceType();

		if (deviceType == DeviceType.COMPUTER) {
			deviceName = "Desktop";
		}
		else {
			deviceName = deviceType.getName();
		}

		pageContext.put("deviceType", deviceName);

		pageContext.put("languageId", "en-US");

		pageContext.put("platformName", operatingSystem.getName());

		pageContext.put("referrer", url);
		pageContext.put("title", company.bs());
		pageContext.put("url", url);
		pageContext.put("userAgent", userAgentString);

		return pageContext;
	}

}