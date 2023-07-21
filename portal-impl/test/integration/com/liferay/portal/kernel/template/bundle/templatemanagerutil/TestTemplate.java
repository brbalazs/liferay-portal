/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.template.bundle.templatemanagerutil;

import com.liferay.portal.kernel.template.Template;

import java.io.Writer;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Philip Jones
 */
public class TestTemplate implements Template {

	@Override
	public void clear() {
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public void doProcessTemplate(Writer writer) throws Exception {
	}

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		return null;
	}

	@Override
	public Object get(Object key) {
		return null;
	}

	@Override
	public Object get(String key) {
		return null;
	}

	@Override
	public String[] getKeys() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Set<String> keySet() {
		return null;
	}

	@Override
	public void prepare(HttpServletRequest request) {
	}

	@Override
	public void prepareTaglib(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {
	}

	@Override
	public void processTemplate(Writer writer) {
	}

	@Override
	public Object put(String key, Object value) {
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> map) {
	}

	@Override
	public Object remove(Object key) {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Collection<Object> values() {
		return null;
	}

}