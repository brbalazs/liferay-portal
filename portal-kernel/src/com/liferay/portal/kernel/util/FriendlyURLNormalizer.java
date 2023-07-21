/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import aQute.bnd.annotation.ProviderType;

import java.util.regex.Pattern;

/**
 * @author Julio Camarero
 */
@ProviderType
public interface FriendlyURLNormalizer {

	public String normalize(String friendlyURL);

	/**
	 * @deprecated As of Wilberforce (7.0.x), with no direct replacement
	 */
	@Deprecated
	public String normalize(String friendlyURL, Pattern friendlyURLPattern);

	public String normalizeWithEncoding(String friendlyURL);

	public String normalizeWithPeriodsAndSlashes(String friendlyURL);

}