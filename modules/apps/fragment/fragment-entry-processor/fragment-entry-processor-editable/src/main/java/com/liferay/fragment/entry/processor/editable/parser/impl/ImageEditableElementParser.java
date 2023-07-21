/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.entry.processor.editable.parser.impl;

import com.liferay.fragment.entry.processor.editable.EditableFragmentEntryProcessor;
import com.liferay.fragment.entry.processor.editable.parser.EditableElementParser;
import com.liferay.fragment.exception.FragmentEntryContentException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.List;
import java.util.ResourceBundle;

import org.jsoup.nodes.Element;

import org.osgi.service.component.annotations.Component;

/**
 * @author Pavel Savinov
 */
@Component(
	immediate = true, property = "type=image",
	service = EditableElementParser.class
)
public class ImageEditableElementParser implements EditableElementParser {

	@Override
	public String getFieldTemplate() {
		return _TMPL_IMAGE_FIELD_TEMPLATE;
	}

	@Override
	public String getValue(Element element) {
		List<Element> elements = element.getElementsByTag("img");

		Element replaceableElement = elements.get(0);

		return replaceableElement.attr("src");
	}

	@Override
	public void replace(Element element, String value) {
		List<Element> elements = element.getElementsByTag("img");

		Element replaceableElement = elements.get(0);

		replaceableElement.attr("src", value);
	}

	@Override
	public void validate(Element element) throws FragmentEntryContentException {
		List<Element> elements = element.getElementsByTag("img");

		if (elements.size() != 1) {
			ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
				"content.Language", getClass());

			throw new FragmentEntryContentException(
				LanguageUtil.format(
					resourceBundle,
					"each-editable-image-element-must-contain-an-img-tag",
					new Object[] {"<em>", "</em>"}, false));
		}
	}

	private static final String _TMPL_IMAGE_FIELD_TEMPLATE = StringUtil.read(
		EditableFragmentEntryProcessor.class,
		"/META-INF/resources/fragment/entry/processor/editable" +
			"/image_field_template.tmpl");

}