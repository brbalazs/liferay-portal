/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template;

import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateException;

import java.io.Writer;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tina Tian
 * @author Shuyang Zhou
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class PrivilegedTemplateWrapper implements Template {

	public PrivilegedTemplateWrapper(
		AccessControlContext accessControlContext, Template template) {

		_accessControlContext = accessControlContext;
		_template = template;
	}

	@Override
	public void clear() {
		_template.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return _template.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return _template.containsValue(value);
	}

	@Override
	public void doProcessTemplate(Writer writer) throws Exception {
		AccessController.doPrivileged(
			new ProcessTemplatePrivilegedExceptionAction(_template, writer),
			_accessControlContext);
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return _template.entrySet();
	}

	@Override
	public Object get(Object key) {
		return _template.get(key);
	}

	@Override
	public Object get(String key) {
		return _template.get(key);
	}

	@Override
	public String[] getKeys() {
		return _template.getKeys();
	}

	@Override
	public boolean isEmpty() {
		return _template.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return _template.keySet();
	}

	@Override
	public void prepare(HttpServletRequest request) {
		_template.prepare(request);
	}

	@Override
	public void prepareTaglib(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		_template.prepareTaglib(httpServletRequest, httpServletResponse);
	}

	@Override
	public void processTemplate(Writer writer) throws TemplateException {
		try {
			doProcessTemplate(writer);
		}
		catch (PrivilegedActionException pae) {
			throw (TemplateException)pae.getException();
		}
		catch (Exception e) {
			throw new TemplateException(e);
		}
	}

	@Override
	public Object put(String key, Object value) {
		return _template.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> map) {
		_template.putAll(map);
	}

	@Override
	public Object remove(Object key) {
		return _template.remove(key);
	}

	@Override
	public int size() {
		return _template.size();
	}

	@Override
	public Collection<Object> values() {
		return _template.values();
	}

	private final AccessControlContext _accessControlContext;
	private Template _template;

	private static class ProcessTemplatePrivilegedExceptionAction
		implements PrivilegedExceptionAction<Void> {

		public ProcessTemplatePrivilegedExceptionAction(
			Template template, Writer writer) {

			_template = template;
			_writer = writer;
		}

		@Override
		public Void run() throws Exception {
			_template.processTemplate(_writer);

			return null;
		}

		private Template _template;
		private final Writer _writer;

	}

}