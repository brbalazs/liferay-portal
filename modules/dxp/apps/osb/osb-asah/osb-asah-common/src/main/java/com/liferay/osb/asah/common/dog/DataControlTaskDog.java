/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.google.cloud.bigquery.BigQuery;

import com.liferay.osb.asah.common.data.exporter.BigQueryDataExporter;
import com.liferay.osb.asah.common.data.exporter.DataExporter;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.entity.Suppression;
import com.liferay.osb.asah.common.model.DataControlTaskStatus;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.DataControlTaskRepository;
import com.liferay.osb.asah.common.repository.executor.BigQueryQueryExecutor;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.TimeOrderedUuidGenerator;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.File;
import java.io.Serializable;

import java.nio.file.Path;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jooq.DSLContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
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

		for (String emailAddress : new HashSet<>(emailAddresses)) {
			emailAddress = StringUtils.lowerCase(emailAddress);

			for (String type : types) {
				DataControlTask.Type dataControlTaskType =
					DataControlTask.Type.valueOf(type);

				if (dataControlTaskType == DataControlTask.Type.UNSUPPRESS) {
					_suppressionDog.hideSuppressionByEmailAddress(emailAddress);
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

	public Boolean existsDataControlTask(Long batchId, List<String> status) {
		return _dataControlTaskRepository.existsByBatchIdAndStatusIn(
			batchId, status);
	}

	public DataControlTask fetchDataControlTask(Long id, String status) {
		return _dataControlTaskRepository.findByIdAndStatus(id, status);
	}

	public DataControlTask fetchLatestActiveSuppressionDataControlTask(
		@Nullable String emailAddress) {

		if (Objects.isNull(emailAddress)) {
			return null;
		}

		Optional<DataControlTask> dataControlTaskOptional =
			_dataControlTaskRepository.
				findLatestActiveSuppressionDataControlTask(emailAddress);

		return dataControlTaskOptional.orElse(null);
	}

	public Page<DataControlTask> getDataControlTaskPage(
		Long batchId, String keywords, Integer rangeKey, int page, int size,
		Sort sort, List<String> statuses, List<String> types) {

		List<DataControlTask.Type> dataControlTaskTypes;

		if ((types != null) && !types.isEmpty()) {
			Stream<String> typesStream = types.stream();

			dataControlTaskTypes = typesStream.map(
				DataControlTask.Type::valueOf
			).collect(
				Collectors.toList()
			);
		}
		else {
			dataControlTaskTypes = Collections.emptyList();
		}

		Date startCreateDate = _getStartCreateDate(rangeKey);

		if (StringUtils.contains(sort.getColumn(), "Date")) {
			sort = new Sort("id", sort.getType());
		}

		PageRequest pageRequest = PageRequest.of(page, size, sort);

		return PageableExecutionUtils.getPage(
			_dataControlTaskRepository.searchDataControlTasks(
				batchId, keywords, startCreateDate, statuses,
				dataControlTaskTypes, pageRequest),
			pageRequest,
			() -> _dataControlTaskRepository.countDataControlTasks(
				batchId, keywords, startCreateDate, statuses,
				dataControlTaskTypes));
	}

	public List<DataControlTask> getPrioritizedDataControlTasks(
		@Nullable Date endCompleteDate, List<String> statuses,
		List<DataControlTask.Type> types) {

		List<DataControlTask> dataControlTasks =
			_dataControlTaskRepository.searchDataControlTasks(
				null, endCompleteDate, statuses, types);

		dataControlTasks.sort(new DataControlTaskComparator());

		return dataControlTasks;
	}

	public List<DataControlTask> getPrioritizedDataControlTasks(
		@Nullable Long batchId, @Nullable Date fromDate, @Nullable Long[] ids,
		@Nullable String status, @Nullable Date toDate) {

		return _dataControlTaskRepository.searchDataControlTasks(
			batchId, fromDate, ids, status, toDate);
	}

	public List<DataControlTask> getPrioritizedPendingDataControlTasks() {
		List<DataControlTask> dataControlTasks = new ArrayList<>();

		dataControlTasks.addAll(
			_dataControlTaskRepository.searchPendingAccessDataControlTasks());
		dataControlTasks.addAll(
			_dataControlTaskRepository.searchPendingDeleteDataControlTasks());
		dataControlTasks.addAll(
			_dataControlTaskRepository.searchPendingSuppressDataControlTasks());
		dataControlTasks.addAll(
			_dataControlTaskRepository.
				searchPendingUnsuppressDataControlTasks());

		dataControlTasks.sort(new DataControlTaskComparator());

		return dataControlTasks;
	}

	public Set<String> getSuppressedEmailAddresses() {
		return _dataControlTaskRepository.findSuppressedEmailAddresses();
	}

	public boolean isSuppressedEmailAddress(
		@Nullable String emailAddressHashed) {

		if (StringUtils.isBlank(emailAddressHashed)) {
			return false;
		}

		Optional<DataControlTask> dataControlTaskOptional =
			_dataControlTaskRepository.findLatestByEmailAddressHashedAndTypesIn(
				emailAddressHashed,
				Arrays.asList(
					DataControlTask.Type.SUPPRESS,
					DataControlTask.Type.UNSUPPRESS));

		return dataControlTaskOptional.map(
			dataControlTask -> Objects.equals(
				dataControlTask.getType(), DataControlTask.Type.SUPPRESS)
		).orElse(
			false
		);
	}

	@Transactional
	public void processDataControlTask(DataControlTask dataControlTask) {
		DataControlTask.Type type = dataControlTask.getType();

		try {
			boolean complete = true;

			if (type == DataControlTask.Type.ACCESS) {
				complete = _access(dataControlTask);
			}
			else if (type == DataControlTask.Type.DELETE) {
				complete = _delete(dataControlTask);
			}
			else if (type == DataControlTask.Type.SUPPRESS) {
				complete = _suppress(dataControlTask);
			}
			else if (type == DataControlTask.Type.UNSUPPRESS) {
				complete = _unsuppress(dataControlTask);
			}

			if (complete) {
				_updateDataControlTaskStatus(
					dataControlTask, DataControlTaskStatus.COMPLETED);
			}
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			if (type == DataControlTask.Type.UNSUPPRESS) {
				_suppressionDog.unhideSuppressionByEmailAddress(
					dataControlTask.getEmailAddress());
			}

			_updateDataControlTaskStatus(
				dataControlTask, DataControlTaskStatus.ERROR);
		}
	}

	public void run(DataControlTask dataControlTask) {
		String status = dataControlTask.getStatus();

		if (!status.equals(DataControlTaskStatus.RUNNING.toString())) {
			_updateDataControlTaskStatus(
				dataControlTask, DataControlTaskStatus.RUNNING);
		}

		processDataControlTask(dataControlTask);
	}

	public DataControlTask updateDataControlTask(
		DataControlTask dataControlTask) {

		if (dataControlTask.isNew()) {
			throw new IllegalArgumentException(
				"Unable to update data control task");
		}

		return _dataControlTaskRepository.save(dataControlTask);
	}

	private boolean _access(DataControlTask dataControlTask) throws Exception {
		DataExporter dataExporter = new BigQueryDataExporter(
			_bigQueryQueryExecutor, dataControlTask, _dslContext, _exportPath,
			Arrays.asList("BQEvent", "BQExpandoValue", "BQUser"));

		dataExporter.export();

		return true;
	}

	private boolean _delete(DataControlTask dataControlTask) throws Exception {
		DataControlTask suppressDataControlTask =
			_dataControlTaskRepository.
				findByBatchIdAndEmailAddressAndStatusInAndType(
					dataControlTask.getBatchId(),
					dataControlTask.getEmailAddress(),
					Arrays.asList(
						DataControlTaskStatus.COMPLETED.toString(),
						DataControlTaskStatus.ERROR.toString()),
					DataControlTask.Type.SUPPRESS.toString());

		if (suppressDataControlTask == null) {
			throw new RuntimeException(
				"Unable to find the corresponding suppression task for " +
					"deletion");
		}

		String status = suppressDataControlTask.getStatus();

		if (status.equals(DataControlTaskStatus.ERROR.toString())) {
			throw new RuntimeException(
				"Unable to process deletion due to failure of the " +
					"corresponding suppression task");
		}

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
			StringUtils.replaceEach(
				ResourceUtil.readResourceToString(
					"dependencies/delete_individual_data_statement.sql",
					getClass()),
				new String[] {
					"${individual_id}", "${new_identity_id}",
					"${range_end_date}"
				},
				new String[] {
					DigestUtils.sha256Hex(dataControlTask.getEmailAddress()),
					String.valueOf(UUID.randomUUID()),
					DateUtil.toUTCString(suppressDataControlTask.getStartDate())
				}));

		if (_log.isInfoEnabled()) {
			_log.info(
				String.format(
					"Individual data associated with email %s deleted " +
						"successfully",
					dataControlTask.getEmailAddress()));
		}

		return true;
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

	private boolean _skipSuppression(String emailAddress) {
		DataControlTask dataControlTask =
			fetchLatestActiveSuppressionDataControlTask(emailAddress);

		if (Objects.isNull(dataControlTask)) {
			return false;
		}

		String status = dataControlTask.getStatus();

		if ((dataControlTask.getType() == DataControlTask.Type.SUPPRESS) &&
			StringUtils.isNotBlank(status) &&
			status.equals(DataControlTaskStatus.RUNNING.toString())) {

			return false;
		}

		return true;
	}

	private boolean _suppress(DataControlTask dataControlTask)
		throws Exception {

		String emailAddress = dataControlTask.getEmailAddress();

		if (_skipSuppression(emailAddress)) {
			return true;
		}

		if (Objects.isNull(dataControlTask.getContinueDate())) {
			dataControlTask.setContinueDate(
				DateUtil.addMinutes(
					dataControlTask.getStartDate(),
					_dataControlTaskDelayMinutes));

			updateDataControlTask(dataControlTask);

			return false;
		}

		String individualId = DigestUtils.sha256Hex(emailAddress);

		List<Segment> segments = _segmentDog.getBQIndividualSegments(
			individualId);

		Stream<Segment> stream = segments.stream();

		List<Segment> updateSegments = stream.filter(
			segment ->
				(segment.getType() == Segment.Type.STATIC) ||
				!segment.getIncludeAnonymousUsers()
		).collect(
			Collectors.toList()
		);

		String deleteMembershipStatement = "";

		if (!updateSegments.isEmpty()) {
			deleteMembershipStatement = String.format(
				"DELETE FROM BQMembership WHERE individualId = '%s' AND " +
					"segmentId IN (%s);",
				individualId,
				StringUtils.join(
					ListUtil.map(updateSegments, Segment::getId), ", "));
		}

		_bigQueryQueryExecutor.queryExecute(
			StringUtils.replaceEach(
				ResourceUtil.readResourceToString(
					"dependencies/suppress_individual_statement.sql",
					getClass()),
				new String[] {
					"${data_control_task_batch_id}",
					"${data_control_task_create_date}",
					"${delete_membership_statement}", "${email_address}",
					"${individual_id}", "${new_identity_id}",
					"${range_end_date}"
				},
				new String[] {
					String.valueOf(dataControlTask.getBatchId()),
					DateUtil.toUTCString(dataControlTask.getCreateDate()),
					deleteMembershipStatement, emailAddress, individualId,
					String.valueOf(UUID.randomUUID()),
					DateUtil.toUTCString(dataControlTask.getStartDate())
				}));

		_updateSegments(updateSegments);

		if (_log.isInfoEnabled()) {
			_log.info(
				String.format(
					"Individual associated with email address %s suppressed " +
						"successfully",
					emailAddress));
		}

		return true;
	}

	private boolean _unsuppress(DataControlTask dataControlTask)
		throws Exception {

		String emailAddress = dataControlTask.getEmailAddress();

		Suppression suppression = _suppressionDog.fetchSuppression(
			emailAddress);

		if (suppression == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to find suppression for " + emailAddress);
			}

			return true;
		}

		if (Objects.isNull(dataControlTask.getContinueDate())) {
			dataControlTask.setContinueDate(
				DateUtil.addMinutes(
					dataControlTask.getStartDate(),
					_dataControlTaskDelayMinutes));

			updateDataControlTask(dataControlTask);

			return false;
		}

		DataControlTask suppressionDataControlTask =
			_dataControlTaskRepository.
				findByBatchIdAndEmailAddressAndStatusInAndType(
					suppression.getDataControlTaskBatchId(),
					suppression.getEmailAddress(),
					Collections.singletonList(
						DataControlTaskStatus.COMPLETED.toString()),
					DataControlTask.Type.SUPPRESS.toString());

		if (suppressionDataControlTask == null) {
			throw new RuntimeException(
				"Unable to find the corresponding suppression task for " +
					"unsuppression");
		}

		String individualId = DigestUtils.sha256Hex(
			dataControlTask.getEmailAddress());

		if (_environment.acceptsProfiles(Profiles.of("prod"))) {
			_bigQueryQueryExecutor.queryExecute(
				StringUtils.replaceEach(
					ResourceUtil.readResourceToString(
						"dependencies/unsuppress_individual_statement.sql",
						getClass()),
					new String[] {
						"${email_address}", "${individual_id}",
						"${range_end_date}", "${range_start_date}"
					},
					new String[] {
						emailAddress, individualId,
						DateUtil.toUTCString(dataControlTask.getStartDate()),
						DateUtil.toUTCString(
							suppressionDataControlTask.getStartDate())
					}));
		}
		else {
			StringBuilder sb = new StringBuilder();

			List<String> identityIds =
				_bqIdentityDog.getBQIdentityIdsIgnoreSuppresion(individualId);

			for (String identityId : identityIds) {
				sb.append(
					StringUtils.replaceEach(
						ResourceUtil.readResourceToString(
							"dependencies" +
								"/anonymize_activities_statement_emulator.sql",
							getClass()),
						new String[] {
							"${new_identity_id}", "${old_identity_id}",
							"${range_end_date}", "${range_start_date}"
						},
						new String[] {
							String.valueOf(UUID.randomUUID()), identityId,
							DateUtil.toUTCString(
								dataControlTask.getStartDate()),
							DateUtil.toUTCString(
								suppressionDataControlTask.getStartDate())
						}));
				sb.append("\n");
			}

			_bigQueryQueryExecutor.queryExecute(
				StringUtils.replaceEach(
					ResourceUtil.readResourceToString(
						"dependencies" +
							"/unsuppress_individual_statement_emulator.sql",
						getClass()),
					new String[] {
						"${anonymize_activities_statement}", "${email_address}",
						"${individual_id}", "${range_end_date}",
						"${range_start_date}"
					},
					new String[] {
						sb.toString(), emailAddress, individualId,
						DateUtil.toUTCString(dataControlTask.getStartDate()),
						DateUtil.toUTCString(
							suppressionDataControlTask.getStartDate())
					}));
		}

		return true;
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

		return updateDataControlTask(dataControlTask);
	}

	private void _updateSegments(List<Segment> segments) {
		for (Segment segment : segments) {
			long membershipsCount = _bqMembershipDog.getBQMembershipsCount(
				segment.getId());

			if ((membershipsCount == 0) &&
				(segment.getType() == Segment.Type.STATIC)) {

				_segmentDog.disableSegment(segment);
			}
		}
	}

	private static final Log _log = LogFactory.getLog(DataControlTaskDog.class);

	@Autowired
	private BigQuery _bigQuery;

	@Autowired
	private BigQueryQueryExecutor _bigQueryQueryExecutor;

	@Autowired
	private BQIdentityDog _bqIdentityDog;

	@Autowired
	private BQIndividualDog _bqIndividualDog;

	@Autowired
	private BQMembershipDog _bqMembershipDog;

	@Autowired
	private BQMembershipIndividualDog _bqMembershipIndividualDog;

	@Value("${data.control.task.delay.minutes:90}")
	private int _dataControlTaskDelayMinutes;

	@Autowired
	private DataControlTaskRepository _dataControlTaskRepository;

	@Autowired
	private DSLContext _dslContext;

	@Autowired
	private DXPEntityDog _dxpEntityDog;

	@Autowired
	private Environment _environment;

	@Value("${osb.asah.batch.curator.data.export.path:/export}")
	private String _exportPath;

	@Autowired
	private SegmentDog _segmentDog;

	@Autowired
	private SuppressionDog _suppressionDog;

	private final TimeOrderedUuidGenerator _timeOrderedUuidGenerator =
		new TimeOrderedUuidGenerator();

	@Autowired
	private TimeZoneDog _timeZoneDog;

	private static class DataControlTaskComparator
		implements Comparator<DataControlTask>, Serializable {

		@Override
		public int compare(
			DataControlTask dataControlTask1,
			DataControlTask dataControlTask2) {

			Date createDate1 = dataControlTask1.getCreateDate();
			Date createDate2 = dataControlTask2.getCreateDate();

			if ((createDate1 != null) && (createDate2 != null) &&
				(createDate1.getTime() != createDate2.getTime())) {

				return createDate1.compareTo(createDate2);
			}

			DataControlTask.Type type1 = dataControlTask1.getType();
			DataControlTask.Type type2 = dataControlTask2.getType();

			return Integer.compare(type1.getPriority(), type2.getPriority());
		}

	}

}