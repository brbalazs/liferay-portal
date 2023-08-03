/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.model.DataControlTaskStatus;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.DataControlTaskRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.TimeOrderedUuidGenerator;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.File;

import java.nio.file.Path;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Matthew Kong
 */
@Component
public class DataControlTaskDog {

	@Transactional
	public boolean addDataControlTasks(
		List<String> emailAddresses, Path path, String ownerId,
		List<String> types, String userId, String userName) {

		if (path != null) {
			File file = path.toFile();

			if (!file.exists()) {
				return false;
			}

			emailAddresses = _readFile(file);

			if (!file.delete() && _log.isWarnEnabled()) {
				_log.warn("Unable to delete file " + file.getName());
			}
		}

		List<DataControlTask> dataControlTasks = new ArrayList<>();

		Long batchId = _timeOrderedUuidGenerator.generateIdAsLong();
		Date date = new Date();

		for (String emailAddress : emailAddresses) {
			for (String type : types) {
				DataControlTask.Type dataControlTaskType =
					DataControlTask.Type.valueOf(type);

				if (dataControlTaskType == DataControlTask.Type.UNSUPPRESS) {
					_suppressionDog.deleteByEmailAddress(emailAddress);
				}

				DataControlTask dataControlTask = new DataControlTask();

				dataControlTask.setBatchId(batchId);
				dataControlTask.setCreateDate(date);
				dataControlTask.setEmailAddress(emailAddress);
				dataControlTask.setId(
					_timeOrderedUuidGenerator.generateIdAsLong());
				dataControlTask.setIsNew(Boolean.TRUE);
				dataControlTask.setOwnerId(ownerId);
				dataControlTask.setStatus(
					DataControlTaskStatus.PENDING.toString());
				dataControlTask.setType(dataControlTaskType);
				dataControlTask.setUserId(userId);
				dataControlTask.setUserName(userName);

				dataControlTasks.add(dataControlTask);
			}
		}

		_dataControlTaskRepository.saveAll(dataControlTasks);

		return true;
	}

	public Boolean existsCompletedDataControlTask(
		@Nullable String emailAddress,
		DataControlTask.Type dataControlTaskType) {

		if (StringUtils.isBlank(emailAddress)) {
			return false;
		}

		return _dataControlTaskRepository.existsByEmailAddressAndStatusAndType(
			emailAddress, DataControlTaskStatus.COMPLETED.toString(),
			dataControlTaskType.toString());
	}

	public Boolean existsDataControlTask(Long batchId, List<String> status) {
		return _dataControlTaskRepository.existsByBatchIdAndStatusIn(
			batchId, status);
	}

	public DataControlTask fetchDataControlTask(Long id, String status) {
		return _dataControlTaskRepository.findByIdAndStatus(id, status);
	}

	public Page<DataControlTask> getDataControlTaskPage(
		Long batchId, String keywords, Integer rangeKey, int page, int size,
		Sort sort, List<String> statuses, List<String> types) {

		Date startCreateDate = _getStartCreateDate(rangeKey);

		if (StringUtils.contains(sort.getColumn(), "Date")) {
			sort = new Sort("id", sort.getType());
		}

		PageRequest pageRequest = PageRequest.of(page, size, sort);

		return PageableExecutionUtils.getPage(
			_dataControlTaskRepository.searchDataControlTasks(
				batchId, keywords, startCreateDate, statuses, types,
				pageRequest),
			pageRequest,
			() -> _dataControlTaskRepository.countDataControlTasks(
				batchId, keywords, startCreateDate, statuses, types));
	}

	public List<DataControlTask> getDataControlTasks(
		Date endCompleteDate, List<String> statuses, List<String> types) {

		return _dataControlTaskRepository.searchDataControlTasks(
			endCompleteDate, statuses, types);
	}

	public List<DataControlTask> getDataControlTasks(
		String filterString, String status) {

		FilterHelper filterHelper = new FilterHelper(filterString);

		return _dataControlTaskRepository.searchDataControlTasks(
			filterHelper, status);
	}

	public DataControlTask updateDataControlTask(
		DataControlTask dataControlTask) {

		if (dataControlTask.isNew()) {
			throw new IllegalArgumentException(
				"Unable to update data control task");
		}

		return _dataControlTaskRepository.save(dataControlTask);
	}

	private Date _getStartCreateDate(Integer rangeKey) {
		if (rangeKey == null) {
			return null;
		}

		LocalDateTime localDateTime = LocalDateTime.now(
			_timeZoneDog.getZoneId());

		localDateTime = localDateTime.minusDays(rangeKey);
		localDateTime = localDateTime.with(LocalTime.MIDNIGHT);

		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

	private List<String> _readFile(File file) {
		CsvParserSettings csvParserSettings = new CsvParserSettings();

		csvParserSettings.setHeaderExtractionEnabled(false);

		CsvParser csvParser = new CsvParser(csvParserSettings);

		return ListUtil.map(csvParser.parseAll(file), row -> row[0]);
	}

	private static final Log _log = LogFactory.getLog(DataControlTaskDog.class);

	@Autowired
	private AuditEventDog _auditEventDog;

	@Autowired
	private DataControlTaskRepository _dataControlTaskRepository;

	@Autowired
	private SuppressionDog _suppressionDog;

	private final TimeOrderedUuidGenerator _timeOrderedUuidGenerator =
		new TimeOrderedUuidGenerator();

	@Autowired
	private TimeZoneDog _timeZoneDog;

}