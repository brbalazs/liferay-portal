/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.QueryJobConfiguration;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.entity.BlockedKeyword;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.CustomAssetDashboard;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.entity.Preference;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.http.NanitesHttp;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.repository.BQMembershipChangeRepository;
import com.liferay.osb.asah.common.repository.BQMembershipRepository;
import com.liferay.osb.asah.common.repository.BlockedKeywordRepository;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.CustomAssetDashboardRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.repository.ExperimentRepository;
import com.liferay.osb.asah.common.repository.PreferenceRepository;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.common.spring.annotation.CacheEvict;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.common.util.ResourceTemplateUtil;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vishal Reddy
 * @author Matthew Kong
 */
@Profile("!prod")
@RequestMapping("/admin")
@RestController(
	"com.liferay.osb.asah.backend.rest.controller.AdminRestController"
)
public class AdminRestController extends BaseRestController {

	@PostMapping("/data")
	public ResponseEntity<String> addData(
		@RequestBody String sql,
		@RequestHeader(value = HeaderConstants.PROJECT_ID) String projectId) {

		QueryJobConfiguration queryJobConfiguration =
			QueryJobConfiguration.newBuilder(
				ResourceTemplateUtil.replaceSQLVariables(sql)
			).setDefaultDataset(
				projectId
			).build();

		try {
			_bigQuery.query(queryJobConfiguration);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			return new ResponseEntity<>(
				ExceptionUtils.getStackTrace(exception),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@CacheEvict(evictAll = true)
	@DeleteMapping("/cache")
	public void clearCache() {
	}

	@DeleteMapping("/data/{weDeployDataServiceName}/{collectionName}")
	public void deleteData(
		@PathVariable String collectionName,
		@PathVariable String weDeployDataServiceName) {

		if (collectionName.equals("blocked-keywords")) {
			_blockedKeywordRepository.deleteAll();
		}
		else if (collectionName.equals("channels")) {
			_channelRepository.deleteAll();
		}
		else if (collectionName.equals("custom-asset-dashboards")) {
			_customAssetDashboardRepository.deleteAll();
		}
		else if (collectionName.equals("data-sources")) {
			_dataSourceRepository.deleteAll();
		}
		else if (collectionName.equals("experiments")) {
			_experimentRepository.deleteAll();
		}
		else if (collectionName.equals("field-mappings")) {

			// TODO Add BQFieldMappings

		}
		else if (collectionName.equals("individual-segments")) {
			_segmentRepository.deleteAll();
		}
		else if (collectionName.equals("membership-changes")) {
		}
		else if (collectionName.equals("memberships")) {
		}
		else if (collectionName.equals("preferences")) {
			_preferenceRepository.deleteAll();
		}
		else {

			// TODO

		}
	}

	@GetMapping("/snapshot")
	public ResponseEntity downloadSnapshot() {
		ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(
			HttpStatus.NOT_IMPLEMENTED);

		return bodyBuilder.build();
	}

	@PostConstruct
	public void init() {

		// TODO

		Class<?> clazz = getClass();

		try (InputStream inputStream = clazz.getResourceAsStream(
				"nanite_list_schema.json")) {

			_naniteListSchema = SchemaLoader.load(
				new JSONObject(new JSONTokener(inputStream)));
		}
		catch (IOException ioException) {
			_log.error(ioException, ioException);

			throw new IllegalStateException(
				"Unable to read nanite list schema", ioException);
		}
	}

	@PostMapping("/data/{weDeployDataServiceName}/{collectionName}")
	public void postData(
			@PathVariable String collectionName,
			@PathVariable String weDeployDataServiceName,
			@RequestBody String json)
		throws Exception {

		if (collectionName.equals("blocked-keywords")) {
			_addEntities(_blockedKeywordRepository, json, BlockedKeyword.class);
		}
		else if (collectionName.equals("channels")) {
			_addEntities(_channelRepository, json, Channel.class);
		}
		else if (collectionName.equals("custom-asset-dashboards")) {
			_addEntities(
				_customAssetDashboardRepository, json,
				CustomAssetDashboard.class);
		}
		else if (collectionName.equals("data-sources")) {
			_addEntities(_dataSourceRepository, json, DataSource.class);
		}
		else if (collectionName.equals("experiments")) {
			_addEntities(_experimentRepository, json, Experiment.class);
		}
		else if (collectionName.equals("field-mappings")) {

			// TODO Add BQFieldMappings

		}
		else if (collectionName.equals("individual-segments")) {
			_addEntities(_segmentRepository, json, Segment.class);
		}
		else if (collectionName.equals("membership-changes")) {
		}
		else if (collectionName.equals("memberships")) {
		}
		else if (collectionName.equals("preferences")) {
			_addPreferences(new JSONArray(json));
		}
		else {

			// TODO

		}
	}

	@PostMapping("/nanites/{className}")
	public void postTask(
		@PathVariable String className,
		@RequestBody(required = false) String json) {

		JSONObject jsonObject = null;

		if (json != null) {
			jsonObject = new JSONObject(json);
		}

		_asahTaskDog.scheduleAsahTask(className, jsonObject);
	}

	@PostMapping("/nanites")
	public void run(@RequestBody String json) {
		JSONArray jsonArray = new JSONArray(json);

		_naniteListSchema.validate(jsonArray);

		asahMarkerDog.deleteAsahMarkers(JSONUtil.toStringList(jsonArray));

		_nanitesHttp.run(jsonArray);
	}

	@PostMapping("/dag/{dagId}/run")
	public void runDag(@PathVariable String dagId) {
		_messageBus.sendMessage(
			com.liferay.osb.asah.common.messaging.Channel.COMPOSER,
			JSONUtil.put(
				"dagId", dagId
			).put(
				"projectId", ProjectIdThreadLocal.getProjectId()
			).toString());
	}

	private <T, ID> void _addEntities(
			CrudRepository<T, ID> crudRepository, String json,
			Class<T> modelClass)
		throws Exception {

		JSONArray jsonArray = new JSONArray(json);

		for (int i = 0; i < jsonArray.length(); i++) {
			T t = _objectMapper.convertValue(
				jsonArray.getJSONObject(i), modelClass);

			BeanUtils.setProperty(t, "isNew", true);

			crudRepository.save(t);
		}
	}

	private void _addPreferences(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String id = jsonObject.getString("id");

			Preference preference = new Preference(
				id, jsonObject.getString("value"));

			if (!_preferenceRepository.existsById(id)) {
				preference.setIsNew(Boolean.TRUE);
			}

			_preferenceRepository.save(preference);
		}
	}

	private static final Log _log = LogFactory.getLog(
		AdminRestController.class);

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private BigQuery _bigQuery;

	@Autowired
	private BlockedKeywordRepository _blockedKeywordRepository;

	@Autowired
	private BQMembershipChangeRepository _bqMembershipChangeRepository;

	@Autowired
	private BQMembershipRepository _bqMembershipRepository;

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private CustomAssetDashboardRepository _customAssetDashboardRepository;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

	@Autowired
	private ExperimentRepository _experimentRepository;

	@Autowired
	private MessageBus _messageBus;

	private Schema _naniteListSchema;

	@Autowired
	private NanitesHttp _nanitesHttp;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private PreferenceRepository _preferenceRepository;

	@Autowired
	private SegmentRepository _segmentRepository;

}