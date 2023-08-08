/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.browscap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Inácio Nery
 */
@ExtendWith(SpringExtension.class)
public class BrowscapEngineTest {

	@Test
	public void testGetBrowser1() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_MAC_OS_CHROME_DESKTOP);

		Assertions.assertEquals("Chrome", browscapDevice.getBrowserName());
	}

	@Test
	public void testGetBrowser2() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_MAC_OS_FIREFOX_DESKTOP);

		Assertions.assertEquals("Firefox", browscapDevice.getBrowserName());
	}

	@Test
	public void testGetCrawler1() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_CRAWLER_AGENT_YANDEX);

		Assertions.assertEquals("True", browscapDevice.getCrawler());
	}

	@Test
	public void testGetCrawler2() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_CRAWLER_AGENT_GOOGLEBOT);

		Assertions.assertEquals("True", browscapDevice.getCrawler());
	}

	@Test
	public void testGetCrawler3() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_ANDROID_CHROME_MOBILE);

		Assertions.assertEquals("False", browscapDevice.getCrawler());
	}

	@Test
	public void testGetDeviceType1() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_ANDROID_CHROME_MOBILE);

		Assertions.assertEquals("SmartPhone", browscapDevice.getDeviceType());
	}

	@Test
	public void testGetDeviceType2() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_MAC_OS_CHROME_DESKTOP);

		Assertions.assertEquals("Desktop", browscapDevice.getDeviceType());
	}

	@Test
	public void testGetDeviceType3() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_IOS_WEBKIT_IPOD);

		Assertions.assertEquals("Mobile", browscapDevice.getDeviceType());
	}

	@Test
	public void testGetDeviceType4() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_ANDROID_WEBKIT_TABLET);

		Assertions.assertEquals("Tablet", browscapDevice.getDeviceType());
	}

	@Test
	public void testGetDeviceType5() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_IOS_WEBKIT_IPAD);

		Assertions.assertEquals("Tablet", browscapDevice.getDeviceType());
	}

	@Test
	public void testGetDeviceType6() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice("");

		Assertions.assertEquals("Unknown", browscapDevice.getDeviceType());
	}

	@Test
	public void testGetDeviceType7() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice("   ");

		Assertions.assertEquals("Unknown", browscapDevice.getDeviceType());
	}

	@Test
	public void testGetDeviceType8() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(null);

		Assertions.assertEquals("Unknown", browscapDevice.getDeviceType());
	}

	@Test
	public void testGetEdgeBrowser() {
		BrowscapDevice linuxBrowscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_LINUX_OS_EDGE_DESKTOP);

		Assertions.assertEquals("Edge", linuxBrowscapDevice.getBrowserName());

		BrowscapDevice macBrowscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_MAC_OS_EDGE_DESKTOP);

		Assertions.assertEquals("Edge", macBrowscapDevice.getBrowserName());

		BrowscapDevice windowsBrowscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_WINDOWS_OS_EDGE_DESKTOP);

		Assertions.assertEquals("Edge", windowsBrowscapDevice.getBrowserName());
	}

	@Test
	public void testGetNotChromiumBasedEdgeBrowser() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_NOT_CHROMIUM_BASED_EDGE_DESKTOP);

		Assertions.assertEquals("Edge", browscapDevice.getBrowserName());
	}

	@Test
	public void testGetPlatform1() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_MAC_OS_CHROME_DESKTOP);

		Assertions.assertEquals("macOS", browscapDevice.getPlatformName());
	}

	@Test
	public void testGetPlatform2() {
		BrowscapDevice browscapDevice = _browscapEngine.getDevice(
			_USER_AGENT_ANDROID_CHROME_MOBILE);

		Assertions.assertEquals("Android", browscapDevice.getPlatformName());
	}

	@Configuration
	@Import(BrowscapEngine.class)
	public static class TestConfiguration {
	}

	private static final String _CRAWLER_AGENT_GOOGLEBOT =
		"Mozilla/5.0 (compatible; Googlebot/2.1; " +
			"+http://www.google.com/bot.html)";

	private static final String _CRAWLER_AGENT_YANDEX =
		"Mozilla/5.0 (compatible; YandexBot/3.0; +http://yandex.com/bots)";

	private static final String _USER_AGENT_ANDROID_CHROME_MOBILE =
		"Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) " +
			"AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 " +
				"Mobile Safari/535.19";

	private static final String _USER_AGENT_ANDROID_WEBKIT_TABLET =
		"Mozilla/5.0 (Linux; Android 4.4.3; KFTHWI Build/KTU84M) " +
			"AppleWebKit/537.36 (KHTML, like Gecko) Silk/47.1.79 like " +
				"Chrome/47.0.2526.80 Safari/537.36";

	private static final String _USER_AGENT_IOS_WEBKIT_IPAD =
		"Mozilla/5.0 (iPad; CPU OS 5_1 like Mac OS X; en-us) " +
			"AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9B176 " +
				"Safari/7534.48.3";

	private static final String _USER_AGENT_IOS_WEBKIT_IPOD =
		"Mozilla/5.0 (iPod touch; CPU iPhone OS 10_2 like Mac OS X)" +
			"AppleWebKit/602.3.12 (KHTML, like Gecko) Version/10.0 " +
				"Mobile/14C92 Safari/602.1";

	private static final String _USER_AGENT_LINUX_OS_EDGE_DESKTOP =
		"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like " +
			"Gecko) Chrome/102.0.5005.124 Safari/537.36 Edg/102.0.1245.44";

	private static final String _USER_AGENT_MAC_OS_CHROME_DESKTOP =
		"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 " +
			"(KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";

	private static final String _USER_AGENT_MAC_OS_EDGE_DESKTOP =
		"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 " +
			"(KHTML, like Gecko) Chrome/102.0.5005.124 Safari/537.36 " +
				"Edg/102.0.1245.44";

	private static final String _USER_AGENT_MAC_OS_FIREFOX_DESKTOP =
		"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:45.0) " +
			"Gecko/20100101 Firefox/45.0";

	private static final String _USER_AGENT_NOT_CHROMIUM_BASED_EDGE_DESKTOP =
		"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
			"(KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 " +
				"Edge/18.18362";

	private static final String _USER_AGENT_WINDOWS_OS_EDGE_DESKTOP =
		"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
			"(KHTML, like Gecko) Chrome/102.0.5005.124 Safari/537.36 " +
				"Edg/102.0.1245.44";

	@Autowired
	private BrowscapEngine _browscapEngine;

}