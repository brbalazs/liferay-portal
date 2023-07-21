/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template;

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Writer;

import java.util.List;
import java.util.Map;

/**
 * @author Miroslav Ligas
 */
public abstract class AbstractMultiResourceTemplate extends AbstractTemplate {

	public AbstractMultiResourceTemplate(
		List<TemplateResource> templateResources,
		TemplateResource errorTemplateResource, Map<String, Object> context,
		TemplateContextHelper templateContextHelper, boolean restricted) {

		super(
			errorTemplateResource, context, templateContextHelper, restricted);

		if (ListUtil.isEmpty(templateResources)) {
			throw new IllegalArgumentException("Template resource is null");
		}

		this.templateResources = templateResources;
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 *             #AbstractMultiResourceTemplate(List, TemplateResource, Map,
	 *             TemplateContextHelper, String, long, boolean)}}
	 */
	@Deprecated
	public AbstractMultiResourceTemplate(
		List<TemplateResource> templateResources,
		TemplateResource errorTemplateResource, Map<String, Object> context,
		TemplateContextHelper templateContextHelper, String templateManagerName,
		long interval) {

		this(
			templateResources, errorTemplateResource, context,
			templateContextHelper, templateManagerName, interval, false);
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #AbstractMultiResourceTemplate(List, TemplateResource, Map,
	 *             TemplateContextHelper, boolean)}}
	 */
	@Deprecated
	public AbstractMultiResourceTemplate(
		List<TemplateResource> templateResources,
		TemplateResource errorTemplateResource, Map<String, Object> context,
		TemplateContextHelper templateContextHelper, String templateManagerName,
		long interval, boolean restricted) {

		this(
			templateResources, errorTemplateResource, context,
			templateContextHelper, false);
	}

	@Override
	public void doProcessTemplate(Writer writer) throws Exception {
		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		put(TemplateConstants.WRITER, unsyncStringWriter);

		processTemplates(templateResources, unsyncStringWriter);

		StringBundler sb = unsyncStringWriter.getStringBundler();

		sb.writeTo(writer);
	}

	@Override
	public void processTemplate(Writer writer) throws TemplateException {
		if (errorTemplateResource == null) {
			try {
				processTemplates(templateResources, writer);

				return;
			}
			catch (Exception e) {
				StringBuilder sb = new StringBuilder();

				for (TemplateResource templateResource : templateResources) {
					sb.append(templateResource.getTemplateId());
					sb.append(",");
				}

				throw new TemplateException("Unable to process templates", e);
			}
		}

		write(writer);
	}

	protected abstract void processTemplates(
			List<TemplateResource> templateResource, Writer writer)
		throws Exception;

	protected List<TemplateResource> templateResources;

}