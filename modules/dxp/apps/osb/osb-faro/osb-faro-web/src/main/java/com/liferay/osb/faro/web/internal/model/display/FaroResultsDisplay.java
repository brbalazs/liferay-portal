/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.web.internal.model.display;

import com.liferay.osb.faro.engine.client.model.Results;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Shinn Lok
 */
public class FaroResultsDisplay<T> {

	public FaroResultsDisplay() {
	}

	public FaroResultsDisplay(List<?> items, int total) {
		_items = items;
		_total = total;
	}

	public FaroResultsDisplay(Results<?> results) {
		_items = results.getItems();
		_total = results.getTotal();
	}

	public FaroResultsDisplay(Results<T> results, Function<T, ?> function) {
		this(results, function, false);
	}

	public FaroResultsDisplay(
		Results<T> results, Function<T, ?> function, boolean disableSearch) {

		_disableSearch = disableSearch;

		List<T> items = results.getItems();

		Stream<T> stream = items.stream();

		_items = stream.map(
			function
		).collect(
			Collectors.toList()
		);

		_total = results.getTotal();
	}

	public List<?> getItems() {
		return _items;
	}

	public int getTotal() {
		return _total;
	}

	private boolean _disableSearch;
	private List<?> _items = Collections.emptyList();
	private int _total;

}