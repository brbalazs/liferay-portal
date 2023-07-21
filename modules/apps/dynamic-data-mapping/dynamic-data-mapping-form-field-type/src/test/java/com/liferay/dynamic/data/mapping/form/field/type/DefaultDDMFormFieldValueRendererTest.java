/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.field.type;

import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Marcellus Tavares
 */
public class DefaultDDMFormFieldValueRendererTest {

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		setUpHtmlUtil();
	}

	@Test
	public void testRender() {
		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setName("Text");
		ddmFormFieldValue.setValue(
			new UnlocalizedValue(StringUtil.randomString()));

		_defaultDDMFormFieldValueRenderer.render(
			ddmFormFieldValue, LocaleUtil.US);

		Mockito.verify(
			_html
		).escape(
			Matchers.anyString()
		);
	}

	protected void setUpHtmlUtil() {
		HtmlUtil htmlUtil = new HtmlUtil();

		htmlUtil.setHtml(_html);
	}

	private final DefaultDDMFormFieldValueRenderer
		_defaultDDMFormFieldValueRenderer =
			new DefaultDDMFormFieldValueRenderer();

	@Mock
	private Html _html;

}