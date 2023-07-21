/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.taglib.aui;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.taglib.aui.base.BaseOptionTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

/**
 * @author Julio Camarero
 * @author Jorge Ferrer
 * @author Brian Wing Shun Chan
 */
public class OptionTag extends BaseOptionTag {

	@Override
	protected boolean isCleanUpSetAttributes() {
		return _CLEAN_UP_SET_ATTRIBUTES;
	}

	@Override
	protected int processEndTag() throws Exception {
		JspWriter jspWriter = pageContext.getOut();

		jspWriter.write("</option>");

		return EVAL_PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		super.setAttributes(request);

		Object value = getValue();

		if (value == null) {
			value = getLabel();
		}

		boolean selected = getSelected();

		if (getUseModelValue()) {
			String selectValue = GetterUtil.getString(
				(String)request.getAttribute("aui:select:value"));

			if (Validator.isNotNull(selectValue)) {
				selected = selectValue.equals(String.valueOf(value));
			}
		}

		setNamespacedAttribute(request, "selected", String.valueOf(selected));
		setNamespacedAttribute(request, "value", value);
	}

	private static final boolean _CLEAN_UP_SET_ATTRIBUTES = true;

}