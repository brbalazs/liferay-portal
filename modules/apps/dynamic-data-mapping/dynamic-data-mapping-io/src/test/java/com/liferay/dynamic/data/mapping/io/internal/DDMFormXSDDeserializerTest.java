/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.io.internal;

import com.liferay.dynamic.data.mapping.io.DDMFormXSDDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.UnsecureSAXReaderUtil;
import com.liferay.portal.util.HtmlImpl;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.xml.SAXReaderImpl;

import org.junit.Before;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Pablo Carvalho
 */
@PrepareForTest(PropsValues.class)
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor(
	{
		"com.liferay.portal.kernel.xml.SAXReaderUtil",
		"com.liferay.portal.util.PropsValues"
	}
)
public class DDMFormXSDDeserializerTest
	extends BaseDDMFormDeserializerTestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		setUpHtmlUtil();
		setUpPropsValues();
		setUpSAXReaderUtil();
		setUpDDMFormXSDDeserializer();
	}

	@Override
	protected DDMForm deserialize(String serializedDDMForm)
		throws PortalException {

		return _ddmFormXSDDeserializer.deserialize(serializedDDMForm);
	}

	@Override
	protected String getDeserializerType() {
		return "xsd";
	}

	@Override
	protected String getTestFileExtension() {
		return ".xml";
	}

	protected void setUpDDMFormXSDDeserializer() throws Exception {
		field(
			DDMFormXSDDeserializerImpl.class, "_saxReader"
		).set(
			_ddmFormXSDDeserializer, new SAXReaderImpl()
		);
	}

	protected void setUpHtmlUtil() {
		HtmlUtil htmlUtil = new HtmlUtil();

		htmlUtil.setHtml(new HtmlImpl());
	}

	protected void setUpPropsValues() {
		mockStatic(PropsValues.class);
	}

	protected void setUpSAXReaderUtil() {
		SAXReaderUtil saxReaderUtil = new SAXReaderUtil();

		SAXReaderImpl secureSAXReader = new SAXReaderImpl();

		secureSAXReader.setSecure(true);

		saxReaderUtil.setSAXReader(secureSAXReader);

		UnsecureSAXReaderUtil unsecureSAXReaderUtil =
			new UnsecureSAXReaderUtil();

		SAXReaderImpl unsecureSAXReader = new SAXReaderImpl();

		unsecureSAXReaderUtil.setSAXReader(unsecureSAXReader);
	}

	private final DDMFormXSDDeserializer _ddmFormXSDDeserializer =
		new DDMFormXSDDeserializerImpl();

}