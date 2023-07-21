/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template;

import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateResource;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Miroslav Ligas
 */
public abstract class BaseMultiTemplateManager extends BaseTemplateManager {

	@Override
	public Template getTemplate(
		List<TemplateResource> templateResources, boolean restricted) {

		return getTemplate(templateResources, null, restricted);
	}

	@Override
	public Template getTemplate(
		List<TemplateResource> templateResources,
		TemplateResource errorTemplateResource, boolean restricted) {

		return doGetTemplate(
			templateResources, errorTemplateResource, restricted,
			getHelperUtilities(restricted));
	}

	@Override
	public Template getTemplate(
		TemplateResource templateResource, boolean restricted) {

		return getTemplate(
			Collections.singletonList(templateResource), null, restricted);
	}

	@Override
	public Template getTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource, boolean restricted) {

		return getTemplate(
			Collections.singletonList(templateResource), errorTemplateResource,
			restricted);
	}

	protected abstract Template doGetTemplate(
		List<TemplateResource> templateResources,
		TemplateResource errorTemplateResource, boolean restricted,
		Map<String, Object> helperUtilities);

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	protected Template doGetTemplate(
		List<TemplateResource> templateResources,
		TemplateResource errorTemplateResource, boolean restricted,
		Map<String, Object> helperUtilities, boolean privileged) {

		return doGetTemplate(
			templateResources, errorTemplateResource, restricted,
			helperUtilities);
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	protected class DoGetMultiTemplatePrivilegedAction
		extends DoGetAbstractTemplatePrivilegedAction {

		public DoGetMultiTemplatePrivilegedAction(
			List<TemplateResource> templateResources,
			TemplateResource errorTemplateResource, boolean restricted,
			Map<String, Object> helperUtilities) {

			super(errorTemplateResource, restricted, helperUtilities);

			_templateResources = templateResources;
		}

		@Override
		public Template run() {
			return doGetTemplate(
				_templateResources, errorTemplateResource, restricted,
				helperUtilities);
		}

		private final List<TemplateResource> _templateResources;

	}

}