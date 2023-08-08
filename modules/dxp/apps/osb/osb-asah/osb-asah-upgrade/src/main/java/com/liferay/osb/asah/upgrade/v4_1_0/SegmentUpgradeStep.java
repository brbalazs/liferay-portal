/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_1_0;

import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.upgrade.UpgradeStep;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 */
@Component
public class SegmentUpgradeStep implements UpgradeStep {

	public SegmentUpgradeStep(SegmentRepository segmentRepository) {
		_segmentRepository = segmentRepository;
	}

	@Override
	public void upgrade(String version) {
		for (Segment segment : _segmentRepository.findAll()) {
			String filterString = segment.getFilter();

			if (StringUtils.isBlank(filterString)) {
				continue;
			}

			if (StringUtils.containsAnyIgnoreCase(
					filterString, "dataSourceAccountPKs/accountPKs",
					"demographics/address", "demographics/city",
					"demographics/country", "demographics/department",
					"demographics/division", "demographics/employmentStatus",
					"demographics/fullName", "demographics/gender",
					"demographics/portraitId", "demographics/region",
					"demographics/role", "demographics/state")) {

				segment.setState("DISABLED");

				_segmentRepository.save(segment);

				if (_log.isInfoEnabled()) {
					_log.info(
						String.format(
							"Disabled segment ID %s with filter %s",
							segment.getId(), filterString));
				}
			}
		}
	}

	private static final Log _log = LogFactory.getLog(SegmentUpgradeStep.class);

	private final SegmentRepository _segmentRepository;

}