/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.user.associated.data.component;

/**
 * The base interface for the UAD framework. Do not implement this interface
 * directly.
 *
 * @author Drew Brokke
 * @param <T> the type of entity to be anonymized, deleted, edited, exported, or
 *        displayed. Also used as an identifier for grouping the various
 *        components
 * @see com.liferay.user.associated.data.anonymizer.UADAnonymizer
 * @see com.liferay.user.associated.data.display.UADDisplay
 * @see com.liferay.user.associated.data.exporter.UADExporter
 * @review
 */
public interface UADComponent<T> {

	/**
	 * Returns a class representing the type of data the extending components
	 * are concerned with.
	 *
	 * @return the identifying class of type {@code T}
	 * @review
	 */
	public Class<T> getTypeClass();

}