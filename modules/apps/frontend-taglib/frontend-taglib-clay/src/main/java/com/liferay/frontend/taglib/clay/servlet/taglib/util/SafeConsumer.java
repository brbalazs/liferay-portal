/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.taglib.clay.servlet.taglib.util;

import java.util.function.Consumer;

/**
 * Defines a {@code Consumer} that can throw an exception.
 *
 * <p>
 * This interface can be implemented with a lambda function.
 * </p>
 *
 * @author Carlos Lancha
 * @param  <A> the type of the first argument of the consumer
 */
@FunctionalInterface
public interface SafeConsumer<A> {

	public static <T> Consumer<T> ignore(SafeConsumer<T> safeConsumer) {
		return t -> {
			try {
				safeConsumer.accept(t);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	/**
	 * Operates with one parameter and returns {@code void}. This function can
	 * be implemented explicitly or with a lambda.
	 *
	 * @param a the first function argument
	 */
	public void accept(A a) throws Exception;

}