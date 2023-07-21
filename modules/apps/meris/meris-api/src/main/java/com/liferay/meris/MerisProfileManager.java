/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.meris;

import aQute.bnd.annotation.ProviderType;

import java.util.Comparator;
import java.util.List;

/**
 * Represents a profile manager.
 *
 * @author Eduardo García
 * @review
 */
@ProviderType
public interface MerisProfileManager<P extends MerisProfile> {

	/**
	 * Returns a {@code MerisProfile}
	 *
	 * @param  merisProfileId the ID of the {@code MerisProfile}
	 * @return the {@code MerisProfile}
	 * @review
	 */
	public P getMerisProfile(String merisProfileId);

	/**
	 * Returns a range of {@code MerisProfile}
	 *
	 * @param  start the lower bound of the range of model instances
	 * @param  end the upper bound of the range of model instances (not
	 *         inclusive)
	 * @param  comparator the comparator to order the results by (optionally
	 *         {@code null})
	 * @return the range of {@code MerisProfile}
	 * @review
	 */
	public List<P> getMerisProfiles(
		int start, int end, Comparator<P> comparator);

}