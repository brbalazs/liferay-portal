/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.backend.dto.DataSourceDTO;
import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.AsahTaskDog;
import com.liferay.osb.asah.common.dog.BQCSVUserDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.dog.RunLogDog;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.entity.RunLog;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.wedeploy.data.WeDeployDataService;

import java.util.Date;
import java.util.Objects;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vishal Reddy
 * @author David Bhasme
 * @author Rachael Koestartyo
 */
@RequestMapping("/data-sources")
@RestController(
	"com.liferay.osb.asah.backend.rest.controller.DataSourcesRestController"
)
public class DataSourcesRestController extends BaseRestController {

	@DeleteMapping("/{id}")
	public void deleteDataSource(@PathVariable Long id) {
		DataSource dataSource = _dataSourceDog.getDataSource(id);

		dataSource.setDeletionDate(DateUtil.newDate());
		dataSource.setState("IN_PROGRESS_DELETING");

		_asahTaskDog.scheduleAsahTask(
			"DeleteDataSourcesNanite",
			_objectMapper.convertValue(
				_dataSourceDog.updateDataSource(dataSource), JSONObject.class));
	}

	@PostMapping("/{id}/disconnect")
	public String disconnectDataSource(@PathVariable Long id) throws Exception {
		DataSource dataSource = _dataSourceDog.disconnectDataSource(id);

		_sanitize(dataSource);

		JSONObject dataSourceJSONObject = _objectMapper.convertValue(
			dataSource, JSONObject.class);

		return dataSourceJSONObject.toString();
	}

	@GetMapping("/{id}")
	public DataSourceDTO getDataSourceDTO(@PathVariable Long id) {
		DataSource dataSource = _dataSourceDog.getDataSource(id);

		_sanitize(dataSource);

		return new DataSourceDTO(dataSource);
	}

	@GetMapping
	public PageDTO<DataSourceDTO> getDataSourceDTOPageDTO(
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts) {

		Page<DataSource> dataSourcePage = _dataSourceDog.getDataSourcePage(
			filterString, page, size, sorts);

		return _toPageDTO(
			dataSourcePage.map(
				dataSource -> {
					_sanitize(dataSource);

					return dataSource;
				}));
	}

	@GetMapping("/{id}/progress")
	public String getProgress(@PathVariable Long id) {
		DataSource dataSource = _dataSourceDog.getDataSource(id);

		String providerType = dataSource.getProviderType();

		if (Objects.equals(providerType, "CSV")) {
			return String.valueOf(_getCSVDataSourceProgressJSONObject(id));
		}

		throw new UnsupportedOperationException(
			"Unknown data source type " + providerType);
	}

	@PatchMapping("/{id}")
	public DataSourceDTO patchDataSource(
		@PathVariable String id, @RequestBody DataSourceDTO dataSourceDTO) {

		dataSourceDTO.setId(id);

		DataSource dataSource = _dataSourceDog.patchDataSource(
			_objectMapper.convertValue(dataSourceDTO, DataSource.class));

		return new DataSourceDTO(dataSource);
	}

	@PostMapping
	public String postDataSource(@RequestBody DataSourceDTO dataSourceDTO) {
		_beforeAdd(dataSourceDTO);

		DataSource dataSource = _dataSourceDog.addDataSource(
			_objectMapper.convertValue(dataSourceDTO, DataSource.class));

		JSONObject dataSourceJSONObject = _objectMapper.convertValue(
			dataSource, JSONObject.class);

		return dataSourceJSONObject.toString();
	}

	@PutMapping("/{id}")
	public String putDataSource(
		@PathVariable String id, @RequestBody DataSourceDTO dataSourceDTO) {

		dataSourceDTO.setId(id);

		_beforeUpdate(dataSourceDTO);

		DataSource dataSource = _dataSourceDog.updateDataSourceConfiguration(
			_objectMapper.convertValue(dataSourceDTO, DataSource.class));

		JSONObject dataSourceJSONObject = _objectMapper.convertValue(
			dataSource, JSONObject.class);

		return dataSourceJSONObject.toString();
	}

	private void _beforeAdd(DataSourceDTO dataSourceDTO) {
		Date date = DateUtil.newDate();

		dataSourceDTO.setCreateDate(date);

		dataSourceDTO.setId(null);

		dataSourceDTO.setModifiedDate(date);
	}

	private void _beforeUpdate(DataSourceDTO dataSourceDTO) {
		dataSourceDTO.setModifiedDate(DateUtil.newDate());
	}

	private JSONObject _getCSVDataSourceProgressJSONObject(Long dataSourceId) {
		RunLog runLog = _runLogDog.fetchLatestRunLog(
			dataSourceId, "CSVUsersNanite", null,
			WeDeployDataService.OSB_ASAH_FARO_INFO);

		if (runLog == null) {
			return new JSONObject();
		}

		String status = runLog.getStatus();

		if (status.equals("STARTED")) {
			JSONObject runLogContextJSONObject = runLog.getContextJSONObject();

			return JSONUtil.put(
				"dateRecorded", DateUtil.newDateString()
			).put(
				"processedOperations",
				runLogContextJSONObject.getInt("processedOperations")
			).put(
				"status", "IN_PROGRESS"
			).put(
				"totalOperations",
				_bqCSVUserDog.getBQCSVUsersCount(dataSourceId)
			);
		}

		return JSONUtil.put(
			"dateRecorded", DateUtil.toUTCString(runLog.getDateLogged())
		).put(
			"status", status
		);
	}

	private void _sanitize(DataSource dataSource) {
		dataSource.setFaroBackendSecuritySignature(null);
	}

	private PageDTO<DataSourceDTO> _toPageDTO(
		Page<DataSource> dataSourcesPage) {

		return new PageDTO<>(
			"_embedded", new DataSourceDTO(dataSourcesPage.getContent()),
			dataSourcesPage.getNumber(), dataSourcesPage.getSize(),
			dataSourcesPage.getTotalElements(),
			dataSourcesPage.getTotalPages());
	}

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private BQCSVUserDog _bqCSVUserDog;

	@Autowired
	private DataSourceDog _dataSourceDog;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private RunLogDog _runLogDog;

}