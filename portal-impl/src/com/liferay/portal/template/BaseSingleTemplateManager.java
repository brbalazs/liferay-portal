/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template;

import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateResource;

import java.util.List;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public abstract class BaseSingleTemplateManager extends BaseTemplateManager {

	@Override
	public Template getTemplate(
		List<TemplateResource> templateResources, boolean restricted) {

		return getTemplate(templateResources, null, restricted);
	}

	@Override
	public Template getTemplate(
		List<TemplateResource> templateResources,
		TemplateResource errorTemplateResource, boolean restricted) {

		throw new UnsupportedOperationException(
			"Template type does not support multi templates");
	}

	@Override
	public Template getTemplate(
		TemplateResource templateResource, boolean restricted) {

		return getTemplate(templateResource, null, restricted);
	}

	@Override
	public Template getTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource, boolean restricted) {

		return doGetTemplate(
			templateResource, errorTemplateResource, restricted,
			getHelperUtilities(restricted));
	}

	protected abstract Template doGetTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource, boolean restricted,
		Map<String, Object> helperUtilities);

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #doGetTemplate(TemplateResource, TemplateResource, boolean, Map)}
	 */
	@Deprecated
	protected Template doGetTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource, boolean restricted,
		Map<String, Object> helperUtilities, boolean privileged) {

		return doGetTemplate(
			templateResource, errorTemplateResource, restricted,
			helperUtilities);
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	protected class DoGetSingleTemplatePrivilegedAction
		extends DoGetAbstractTemplatePrivilegedAction {

		public DoGetSingleTemplatePrivilegedAction(
			TemplateResource templateResource,
			TemplateResource errorTemplateResource, boolean restricted,
			Map<String, Object> helperUtilities) {

			super(errorTemplateResource, restricted, helperUtilities);

			_templateResource = templateResource;
		}

		@Override
		public Template run() {
			return doGetTemplate(
				_templateResource, errorTemplateResource, restricted,
				helperUtilities);
		}

		private final TemplateResource _templateResource;

	}

}