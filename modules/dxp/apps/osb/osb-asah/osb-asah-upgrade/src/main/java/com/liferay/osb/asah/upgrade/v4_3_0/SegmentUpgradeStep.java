/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_3_0;

import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.upgrade.UpgradeStep;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class SegmentUpgradeStep implements UpgradeStep {

	@Override
	public void upgrade(String version) {
		for (Segment segment : _segmentRepository.findAll()) {
			Long channelId = segment.getChannelId();

			if (channelId != null) {
				continue;
			}

			segment.setState("DISABLED");

			_segmentRepository.save(segment);

			if (_log.isInfoEnabled()) {
				_log.info(
					String.format(
						"Disabled segment ID %s due to a null channel ID",
						segment.getId()));
			}
		}
	}

	private static final Log _log = LogFactory.getLog(SegmentUpgradeStep.class);

	@Autowired
	private SegmentRepository _segmentRepository;

}