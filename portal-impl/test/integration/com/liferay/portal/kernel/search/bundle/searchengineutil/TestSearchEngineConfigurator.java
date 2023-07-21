/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.search.bundle.searchengineutil;

import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.kernel.search.SearchEngineConfigurator;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = SearchEngineConfigurator.class
)
public class TestSearchEngineConfigurator implements SearchEngineConfigurator {

	@Override
	public void afterPropertiesSet() {
		_atomicBoolean.set(Boolean.TRUE);
	}

	@Override
	public void destroy() {
		_atomicBoolean.set(Boolean.TRUE);
	}

	@Override
	public void setSearchEngines(Map<String, SearchEngine> searchEngines) {
		_atomicBoolean.set(Boolean.TRUE);
	}

	@Reference(target = "(test=AtomicState)")
	protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
		_atomicBoolean = atomicBoolean;
	}

	private AtomicBoolean _atomicBoolean;

}