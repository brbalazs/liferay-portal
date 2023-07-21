/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.meris;

import aQute.bnd.annotation.ProviderType;

import java.util.Map;

/**
 * Represents a profile.
 *
 * @author Eduardo García
 * @review
 */
@ProviderType
public interface MerisProfile {

	/**
	 * Returns the value of a profile attribute
	 *
	 * @param  key the key of the profile attribute
	 * @return the value of a profile attribute
	 * @review
	 */
	public Object getAttribute(String key);

	/**
	 * Returns the profile attributes
	 *
	 * @return the profile attributes
	 * @review
	 */
	public Map<String, Object> getAttributes();

	/**
	 * Returns the unique ID of the profile
	 *
	 * @review
	 */
	public String getMerisProfileId();

}