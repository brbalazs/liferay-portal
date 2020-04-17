/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.product.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * @author Alessio Antonio Rendina
 * @author Igor Beslic
 */
public class DDMFormValuesUtil {

	/**
	 * @param json1
	 * @param json2
	 * @return
	 *
	 * @throws PortalException
	 * @deprecated As of Athanasius (7.3.x)
	 */
	@Deprecated
	public static boolean equals(String json1, String json2)
		throws PortalException {

		throw new UnsupportedOperationException();
	}

	/**
	 * @param keyValues
	 * @return
	 *
	 * @deprecated As of Athanasius (7.3.x)
	 */
	@Deprecated
	public static JSONArray toJSONArray(Map<String, List<String>> keyValues) {
		throw new UnsupportedOperationException();
	}

}