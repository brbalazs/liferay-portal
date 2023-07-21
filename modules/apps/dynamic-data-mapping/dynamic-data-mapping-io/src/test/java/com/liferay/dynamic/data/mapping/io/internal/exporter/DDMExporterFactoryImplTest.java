/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.io.internal.exporter;

import com.liferay.dynamic.data.mapping.io.exporter.DDMFormExporter;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Pedro Queiroz
 */
@PrepareForTest(LocaleUtil.class)
@RunWith(PowerMockRunner.class)
public class DDMExporterFactoryImplTest extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		setupDDMFormExporters();
		setUpLocaleUtil();
	}

	@After
	public void tearDown() {
		tearDownDDMFormExporters();
	}

	@Test
	public void testGetAvailableFormats() {
		Set<String> availableFormats =
			_ddmExporterFactory.getAvailableFormats();

		Assert.assertEquals(
			availableFormats.toString(), 3, availableFormats.size());

		Assert.assertTrue(availableFormats.contains("csv"));
		Assert.assertTrue(availableFormats.contains("xls"));
		Assert.assertTrue(availableFormats.contains("xml"));
	}

	@Test
	public void testGetAvailableFormatsMap() {
		Map<String, String> availableFormatsMap =
			_ddmExporterFactory.getAvailableFormatsMap();

		Assert.assertEquals(
			availableFormatsMap.toString(), 3, availableFormatsMap.size());

		Assert.assertEquals(
			availableFormatsMap.toString(), "csv",
			availableFormatsMap.get("CSV"));
		Assert.assertEquals(
			availableFormatsMap.toString(), "xls",
			availableFormatsMap.get("XLS"));
		Assert.assertEquals(
			availableFormatsMap.toString(), "xml",
			availableFormatsMap.get("XML"));
	}

	@Test
	public void testGetDDMFormCSVExporterFormat() {
		DDMFormExporter ddmFormCSVExporter =
			_ddmExporterFactory.getDDMFormExporter("csv");

		Assert.assertEquals("csv", ddmFormCSVExporter.getFormat());
	}

	@Test
	public void testGetDDMFormXLSExporterLabel() {
		DDMFormExporter ddmFormCSVExporter =
			_ddmExporterFactory.getDDMFormExporter("xls");

		Assert.assertEquals("XLS", ddmFormCSVExporter.getLabel());
	}

	@Test
	public void testGetDDMFormXMLExporterLocale() {
		DDMFormExporter ddmFormCSVExporter =
			_ddmExporterFactory.getDDMFormExporter("xml");

		Assert.assertEquals(LocaleUtil.US, ddmFormCSVExporter.getLocale());
	}

	protected void setupDDMFormExporters() {
		_ddmExporterFactory.addDDMFormExporter(new DDMFormCSVExporter());
		_ddmExporterFactory.addDDMFormExporter(new DDMFormXLSExporter());
		_ddmExporterFactory.addDDMFormExporter(new DDMFormXMLExporter());
	}

	protected void setUpLocaleUtil() {
		mockStatic(LocaleUtil.class);

		when(
			LocaleUtil.getSiteDefault()
		).thenReturn(
			LocaleUtil.US
		);
	}

	protected void tearDownDDMFormExporters() {
		_ddmExporterFactory.removeDDMFormExporter(new DDMFormCSVExporter());
		_ddmExporterFactory.removeDDMFormExporter(new DDMFormXLSExporter());
		_ddmExporterFactory.removeDDMFormExporter(new DDMFormXMLExporter());
	}

	private final DDMExporterFactoryImpl _ddmExporterFactory =
		new DDMExporterFactoryImpl();

}