/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.meris;

import aQute.bnd.annotation.ProviderType;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Represents a profile manager.
 *
 * @author Eduardo García
 * @review
 */
@ProviderType
public interface MerisSegmentManager
	<S extends MerisSegment, P extends MerisProfile> {

	/**
	 * Returns a range of {@code MerisProfile} that matches a segment
	 *
	 * @param  merisSegmentId the ID of the segment
	 * @param  context the context with which segment matching is evaluated
	 * @param  start the lower bound of the range of model instances
	 * @param  end the upper bound of the range of model instances (not
	 *         inclusive)
	 * @param  comparator the comparator to order the results by (optionally
	 *         {@code null})
	 * @return the range of {@code MerisProfile}
	 * @review
	 */
	public List<P> getMerisProfiles(
		String merisSegmentId, Map<String, Object> context, int start, int end,
		Comparator<P> comparator);

	/**
	 * Returns a {@code MerisSegment}
	 *
	 * @param  merisSegmentId the ID of the {@code MerisSegent}
	 * @return the {@code MerisProfile}
	 * @review
	 */
	public S getMerisSegment(String merisSegmentId);

	/**
	 * Returns a range of {@code MerisSegment} matching the scope
	 *
	 * @param  scopeId the ID of the scope to delimit the search of segments
	 * @param  start the lower bound of the range of model instances
	 * @param  end the upper bound of the range of model instances (not
	 *         inclusive)
	 * @param  comparator the comparator to order the results by (optionally
	 *         {@code null})
	 * @return the range of {@code MerisSegment}
	 * @review
	 */
	public List<S> getMerisSegments(
		String scopeId, int start, int end, Comparator<S> comparator);

	/**
	 * Returns a range of {@code MerisSegment} matching the profile and the
	 * scope
	 *
	 * @param  scopeId the ID of the scope to delimit the search of segments
	 * @param  merisProfileId the ID of the profile matching the segments
	 * @param  start the lower bound of the range of model instances
	 * @param  end the upper bound of the range of model instances (not
	 *         inclusive)
	 * @param  comparator the comparator to order the results by (optionally
	 *         {@code null})
	 * @return the range of {@code MerisSegment}
	 * @review
	 */
	public List<S> getMerisSegments(
		String scopeId, String merisProfileId, Map<String, Object> context,
		int start, int end, Comparator<S> comparator);

	/**
	 * Returns {@code true} if the profile matches the segment given a context,
	 * or {@code false} otherwise
	 *
	 * @param  merisProfileId the ID of the profile
	 * @param  merisSegmentId the ID of the segment
	 * @param  context the context with which segment matching is evaluated
	 * @return {@code true} if the profile matches the segment, or {@code false}
	 *         otherwise
	 * @review
	 */
	public boolean matches(
		String merisProfileId, String merisSegmentId,
		Map<String, Object> context);

}