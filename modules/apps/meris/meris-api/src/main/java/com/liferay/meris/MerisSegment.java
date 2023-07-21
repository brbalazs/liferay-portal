/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.meris;

import aQute.bnd.annotation.ProviderType;

import java.util.Locale;

/**
 * Represents a segment.
 *
 * @author Eduardo García
 * @review
 */
@ProviderType
public interface MerisSegment {

	/**
	 * Returns the segment description
	 *
	 * @param  locale the {@code Locale} of the language
	 * @return the segment description
	 * @review
	 */
	public String getDescription(Locale locale);

	/**
	 * Returns the unique ID of the segment
	 *
	 * @return the unique ID of the segment
	 * @review
	 */
	public String getMerisSegmentId();

	/**
	 * Returns the segment name
	 *
	 * @param  locale the {@code Locale} of the language
	 * @return the segment name
	 * @review
	 */
	public String getName(Locale locale);

	/**
	 * Returns the ID of the segment scope
	 *
	 * @return the ID of the segment scope
	 * @review
	 */
	public String getScopeId();

}