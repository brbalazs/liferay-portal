/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateResource;

import java.io.Writer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tina Tian
 */
public abstract class AbstractTemplate implements Template {

	public AbstractTemplate(
		TemplateResource errorTemplateResource, Map<String, Object> context,
		TemplateContextHelper templateContextHelper, boolean restricted) {

		if (templateContextHelper == null) {
			throw new IllegalArgumentException(
				"Template context helper is null");
		}

		this.errorTemplateResource = errorTemplateResource;

		this.context = new HashMap<>();

		if (context != null) {
			for (Map.Entry<String, Object> entry : context.entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
		}

		_templateContextHelper = templateContextHelper;
		_restricted = restricted;
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 *             #AbstractTemplate(TemplateResource, Map,
	 *             TemplateContextHelper, String, boolean)}}
	 */
	@Deprecated
	public AbstractTemplate(
		TemplateResource errorTemplateResource, Map<String, Object> context,
		TemplateContextHelper templateContextHelper,
		String templateManagerName) {

		this(
			errorTemplateResource, context, templateContextHelper,
			templateManagerName, false);
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #AbstractTemplate(TemplateResource, Map,
	 *             TemplateContextHelper, boolean)}}
	 */
	@Deprecated
	public AbstractTemplate(
		TemplateResource errorTemplateResource, Map<String, Object> context,
		TemplateContextHelper templateContextHelper, String templateManagerName,
		boolean restricted) {

		this(errorTemplateResource, context, templateContextHelper, false);
	}

	@Override
	public void clear() {
		context.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return context.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return context.containsValue(value);
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return context.entrySet();
	}

	@Override
	public Object get(Object key) {
		if (key == null) {
			return null;
		}

		return context.get(key);
	}

	@Override
	public Object get(String key) {
		if (key == null) {
			return null;
		}

		return context.get(key);
	}

	@Override
	public String[] getKeys() {
		Set<String> keys = context.keySet();

		return keys.toArray(new String[0]);
	}

	@Override
	public boolean isEmpty() {
		return context.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return context.keySet();
	}

	@Override
	public void prepare(HttpServletRequest request) {
		_templateContextHelper.prepare(this, request);
	}

	@Override
	public void prepareTaglib(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {
	}

	@Override
	public Object put(String key, Object value) {
		if ((key == null) || (value == null)) {
			return null;
		}

		if (_restricted) {
			Set<String> restrictedVariables =
				_templateContextHelper.getRestrictedVariables();

			if (restrictedVariables.contains(key)) {
				return null;
			}
		}

		if (value instanceof Class) {
			return putClass(key, (Class<?>)value);
		}

		return context.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> map) {
		context.putAll(map);
	}

	@Override
	public Object remove(Object key) {
		return context.remove(key);
	}

	@Override
	public int size() {
		return context.size();
	}

	@Override
	public Collection<Object> values() {
		return context.values();
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link #write(Writer)}
	 */
	@Deprecated
	protected void _write(Writer writer) throws TemplateException {
		write(writer);
	}

	protected String getTemplateResourceUUID(
		TemplateResource templateResource) {

		return TemplateConstants.TEMPLATE_RESOURCE_UUID_PREFIX.concat(
			StringPool.POUND
		).concat(
			templateResource.getTemplateId()
		);
	}

	protected abstract void handleException(Exception exception, Writer writer)
		throws TemplateException;

	protected Object putClass(String key, Class<?> clazz) {
		return context.put(key, clazz);
	}

	protected void write(Writer writer) throws TemplateException {
		Writer oldWriter = (Writer)get(TemplateConstants.WRITER);

		try {
			doProcessTemplate(writer);
		}
		catch (Exception e) {
			put(TemplateConstants.WRITER, writer);

			handleException(e, writer);
		}
		finally {
			put(TemplateConstants.WRITER, oldWriter);
		}
	}

	protected Map<String, Object> context;
	protected TemplateResource errorTemplateResource;

	private final boolean _restricted;
	private final TemplateContextHelper _templateContextHelper;

}