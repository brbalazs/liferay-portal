/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.BQIndividualDog;
import com.liferay.osb.asah.common.dog.BQMembershipDog;
import com.liferay.osb.asah.common.dog.DXPEntityDog;
import com.liferay.osb.asah.common.dog.DataControlTaskDog;
import com.liferay.osb.asah.common.dog.SegmentDog;
import com.liferay.osb.asah.common.dog.SuppressionDog;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.model.DataControlTaskStatus;
import com.liferay.osb.asah.common.repository.executor.BigQueryQueryExecutor;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Marcellus Tavares
 */
@Component
public class DataControlTaskRunner {

	@Transactional
	public void run(DataControlTask dataControlTask) {
		DataControlTask.Type type = dataControlTask.getType();

		_updateDataControlTaskStatus(
			dataControlTask, DataControlTaskStatus.RUNNING);

		try {
			if (type == DataControlTask.Type.ACCESS) {
				_access(dataControlTask);
			}
			else if (type == DataControlTask.Type.DELETE) {
				_delete(dataControlTask);
			}
			else if (type == DataControlTask.Type.SUPPRESS) {
				_suppress(dataControlTask);
			}
			else if (type == DataControlTask.Type.UNSUPPRESS) {
				_unsuppress(dataControlTask);
			}

			_updateDataControlTaskStatus(
				dataControlTask, DataControlTaskStatus.COMPLETED);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			_updateDataControlTaskStatus(
				dataControlTask, DataControlTaskStatus.ERROR);
		}
	}

	private void _access(DataControlTask dataControlTask) {
	}

	private void _delete(DataControlTask dataControlTask) throws Exception {

		// DXP User

		List<DXPEntity> dxpEntities = _dxpEntityDog.fetchAllByFieldsAndType(
			Collections.singletonMap(
				"fields.emailAddress", dataControlTask.getEmailAddress()),
			DXPEntity.Type.USER);

		if (!dxpEntities.isEmpty()) {
			_dxpEntityDog.delete(dxpEntities);

			if (_log.isInfoEnabled()) {
				_log.info(
					String.format(
						"%s DXP user(s) with email %s deleted successfully",
						dxpEntities.size(), dataControlTask.getEmailAddress()));
			}
		}

		// BigQuery

		_bigQueryQueryExecutor.queryExecute(
			StringUtils.replace(
				ResourceUtil.readResourceToString(
					"dependencies/delete_individual_data_statement.sql",
					getClass()),
				"${individual_id}",
				DigestUtils.sha256Hex(dataControlTask.getEmailAddress())));

		if (_log.isInfoEnabled()) {
			_log.info(
				String.format(
					"Individual data associated with email %s deleted " +
						"successfully",
					dataControlTask.getEmailAddress()));
		}
	}

	private void _suppress(DataControlTask dataControlTask) {
		DataControlTask.Type type =
			_dataControlTaskDog.fetchLatestCompletedDataControlTaskType(
				dataControlTask.getEmailAddress(),
				Arrays.asList(
					DataControlTask.Type.SUPPRESS,
					DataControlTask.Type.UNSUPPRESS));

		if (type == DataControlTask.Type.SUPPRESS) {
			return;
		}

		_updateMemberships(dataControlTask.getEmailAddress());

		_bqIndividualDog.suppress(
			DigestUtils.sha256Hex(dataControlTask.getEmailAddress()));

		_suppressionDog.addSuppression(
			dataControlTask.getBatchId(), dataControlTask.getCreateDate(),
			dataControlTask.getEmailAddress());
	}

	private void _unsuppress(DataControlTask dataControlTask) {
		_bqIndividualDog.unsuppress(
			DigestUtils.sha256Hex(dataControlTask.getEmailAddress()));
	}

	private DataControlTask _updateDataControlTaskStatus(
		DataControlTask dataControlTask,
		DataControlTaskStatus dataControlTaskStatus) {

		if (dataControlTaskStatus == DataControlTaskStatus.COMPLETED) {
			dataControlTask.setCompleteDate(DateUtil.newDate());
		}
		else if (dataControlTaskStatus == DataControlTaskStatus.RUNNING) {
			dataControlTask.setStartDate(DateUtil.newDate());
		}

		dataControlTask.setStatus(dataControlTaskStatus.toString());

		return _dataControlTaskDog.updateDataControlTask(dataControlTask);
	}

	private void _updateMemberships(String emailAddress) {
		String individualId = DigestUtils.sha256Hex(emailAddress);

		List<Segment> segments = _segmentDog.getBQIndividualSegments(
			individualId);

		for (Segment segment : segments) {
			if (segment.getType() != Segment.Type.STATIC) {
				continue;
			}

			_bqMembershipDog.deleteBQMembership(individualId, segment);

			long membershipsCount = _bqMembershipDog.getBQMembershipsCount(
				segment.getId());

			if (membershipsCount == 0) {
				_segmentDog.disableSegment(segment);
			}
		}
	}

	private static final Log _log = LogFactory.getLog(
		DataControlTaskRunner.class);

	@Autowired
	private BigQueryQueryExecutor _bigQueryQueryExecutor;

	@Autowired
	private BQIndividualDog _bqIndividualDog;

	@Autowired
	private BQMembershipDog _bqMembershipDog;

	@Autowired
	private DataControlTaskDog _dataControlTaskDog;

	@Autowired
	private DXPEntityDog _dxpEntityDog;

	@Autowired
	private SegmentDog _segmentDog;

	@Autowired
	private SuppressionDog _suppressionDog;

}