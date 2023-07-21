/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.adaptive.media.image.internal.scaler;

import com.liferay.adaptive.media.image.configuration.AMImageConfigurationEntry;
import com.liferay.adaptive.media.image.internal.configuration.AMImageConfigurationEntryImpl;
import com.liferay.portal.kernel.test.ReflectionTestUtil;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sergio González
 */
public class AMGIFImageScalerTest {

	@Test
	public void testGetResizeFitArgumentWithBlankMaxHeight() {
		AMGIFImageScaler amGIFImageScaler = new AMGIFImageScaler();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "");
		properties.put("max-width", "100");

		AMImageConfigurationEntry amImageConfigurationEntry =
			new AMImageConfigurationEntryImpl(
				"test", "desc", "12345", properties, true);

		Assert.assertEquals(
			"100x_",
			_getResizeFitValues(amGIFImageScaler, amImageConfigurationEntry));
	}

	@Test
	public void testGetResizeFitArgumentWithBlankMaxWidth() {
		AMGIFImageScaler amGIFImageScaler = new AMGIFImageScaler();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "100");
		properties.put("max-width", "");

		AMImageConfigurationEntry amImageConfigurationEntry =
			new AMImageConfigurationEntryImpl(
				"test", "desc", "12345", properties, true);

		Assert.assertEquals(
			"_x100",
			_getResizeFitValues(amGIFImageScaler, amImageConfigurationEntry));
	}

	@Test
	public void testGetResizeFitArgumentWithMaxWidthAndMaxHeight() {
		AMGIFImageScaler amGIFImageScaler = new AMGIFImageScaler();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "100");
		properties.put("max-width", "200");

		AMImageConfigurationEntry amImageConfigurationEntry =
			new AMImageConfigurationEntryImpl(
				"test", "desc", "12345", properties, true);

		Assert.assertEquals(
			"200x100",
			_getResizeFitValues(amGIFImageScaler, amImageConfigurationEntry));
	}

	@Test
	public void testGetResizeFitArgumentWithOnlyMaxHeight() {
		AMGIFImageScaler amGIFImageScaler = new AMGIFImageScaler();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "100");

		AMImageConfigurationEntry amImageConfigurationEntry =
			new AMImageConfigurationEntryImpl(
				"test", "desc", "12345", properties, true);

		Assert.assertEquals(
			"_x100",
			_getResizeFitValues(amGIFImageScaler, amImageConfigurationEntry));
	}

	@Test
	public void testGetResizeFitArgumentWithZeroMaxHeight() {
		AMGIFImageScaler amGIFImageScaler = new AMGIFImageScaler();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "0");
		properties.put("max-width", "100");

		AMImageConfigurationEntry amImageConfigurationEntry =
			new AMImageConfigurationEntryImpl(
				"test", "desc", "12345", properties, true);

		Assert.assertEquals(
			"100x_",
			_getResizeFitValues(amGIFImageScaler, amImageConfigurationEntry));
	}

	@Test
	public void testGetResizeFitArgumentWithZeroMaxWidth() {
		AMGIFImageScaler amGIFImageScaler = new AMGIFImageScaler();

		Map<String, String> properties = new HashMap<>();

		properties.put("max-height", "100");
		properties.put("max-width", "0");

		AMImageConfigurationEntry amImageConfigurationEntry =
			new AMImageConfigurationEntryImpl(
				"test", "desc", "12345", properties, true);

		Assert.assertEquals(
			"_x100",
			_getResizeFitValues(amGIFImageScaler, amImageConfigurationEntry));
	}

	private String _getResizeFitValues(
		AMGIFImageScaler amGIFImageScaler,
		AMImageConfigurationEntry amImageConfigurationEntry) {

		return ReflectionTestUtil.invoke(
			amGIFImageScaler, "_getResizeFitValues",
			new Class<?>[] {AMImageConfigurationEntry.class},
			amImageConfigurationEntry);
	}

}