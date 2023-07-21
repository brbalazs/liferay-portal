/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author     Brian Wing Shun Chan
 * @author     Shuyang Zhou
 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
 */
@Deprecated
public class UniqueList<E> extends ArrayList<E> {

	public UniqueList() {
	}

	public UniqueList(Collection<E> c) {
		super(c.size());

		addAll(c);
	}

	public UniqueList(int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	public boolean add(E e) {
		if (!contains(e)) {
			return super.add(e);
		}

		return false;
	}

	@Override
	public void add(int index, E e) {
		if (!contains(e)) {
			super.add(index, e);
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean modified = false;

		for (E e : c) {
			if (!contains(e)) {
				super.add(e);

				modified = true;
			}
		}

		return modified;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean modified = false;

		for (E e : c) {
			if (!contains(e)) {
				super.add(index++, e);

				modified = true;
			}
		}

		return modified;
	}

	@Override
	public E set(int index, E e) {
		Thread currentThread = Thread.currentThread();

		StackTraceElement[] stackTraceElements = currentThread.getStackTrace();

		if (stackTraceElements.length >= 4) {
			String stackTraceElementString = String.valueOf(
				stackTraceElements[3]);

			if (stackTraceElementString.contains(_STACK_TRACE_COLLECTIONS)) {
				return super.set(index, e);
			}
		}

		if (!contains(e)) {
			return super.set(index, e);
		}

		return e;
	}

	private static final String _STACK_TRACE_COLLECTIONS =
		"java.util.Collections.sort(Collections.java";

}