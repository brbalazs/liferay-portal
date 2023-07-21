/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.petra.lang;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Maps servlet context names to/from the servlet context's class loader.
 *
 * @author Shuyang Zhou
 */
public class ClassLoaderPool {

	/**
	 * Returns the class loader associated with the context name.
	 *
	 * <p>
	 * If no class loader is found for the context name, the thread's context
	 * class loader is returned as a fallback.
	 * </p>
	 *
	 * @param  contextName the servlet context's name
	 * @return the class loader associated with the context name
	 */
	public static ClassLoader getClassLoader(String contextName) {
		ClassLoader classLoader = null;

		if ((contextName != null) && !contextName.equals("null")) {
			classLoader = _classLoaders.get(contextName);
		}

		if (classLoader == null) {
			Thread currentThread = Thread.currentThread();

			classLoader = currentThread.getContextClassLoader();
		}

		return classLoader;
	}

	/**
	 * Returns the context name associated with the class loader.
	 *
	 * <p>
	 * If the class loader is <code>null</code> or if no context name is
	 * associated with the class loader, {@link <code>"null"</code>} is
	 * returned.
	 * </p>
	 *
	 * @param  classLoader the class loader
	 * @return the context name associated with the class loader
	 */
	public static String getContextName(ClassLoader classLoader) {
		if (classLoader == null) {
			return "null";
		}

		String contextName = _contextNames.get(classLoader);

		if (contextName == null) {
			contextName = "null";
		}

		return contextName;
	}

	public static void register(String contextName, ClassLoader classLoader) {
		_classLoaders.put(contextName, classLoader);
		_contextNames.put(classLoader, contextName);
	}

	public static void unregister(ClassLoader classLoader) {
		String contextName = _contextNames.remove(classLoader);

		if (contextName != null) {
			_classLoaders.remove(contextName);
		}
	}

	public static void unregister(String contextName) {
		ClassLoader classLoader = _classLoaders.remove(contextName);

		if (classLoader != null) {
			_contextNames.remove(classLoader);
		}
	}

	private static final Map<String, ClassLoader> _classLoaders =
		new ConcurrentHashMap<>();
	private static final Map<ClassLoader, String> _contextNames =
		new ConcurrentHashMap<>();

	static {
		register("GlobalClassLoader", ClassLoaderPool.class.getClassLoader());
	}

}