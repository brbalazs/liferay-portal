/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.internal.increment;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.increment.Increment;
import com.liferay.portal.kernel.util.StringBundler;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Zsolt Berentey
 */
public class BufferedIncreasableEntry<K, T>
	extends IncreasableEntry<K, Increment<T>> {

	public BufferedIncreasableEntry(
		MethodInvocation methodInvocation, K key, Increment<T> value) {

		super(key, value);

		_methodInvocation = methodInvocation;
	}

	@Override
	public BufferedIncreasableEntry<K, T> increase(Increment<T> deltaValue) {
		return new BufferedIncreasableEntry<>(
			_methodInvocation, key,
			value.increaseForNew(deltaValue.getValue()));
	}

	public void proceed() throws Throwable {
		Object[] arguments = _methodInvocation.getArguments();

		arguments[arguments.length - 1] = getValue().getValue();

		_methodInvocation.proceed();
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(4);

		sb.append(_methodInvocation.toString());
		sb.append(StringPool.OPEN_PARENTHESIS);
		sb.append(Arrays.toString(_methodInvocation.getArguments()));
		sb.append(StringPool.CLOSE_PARENTHESIS);

		return sb.toString();
	}

	private final MethodInvocation _methodInvocation;

}