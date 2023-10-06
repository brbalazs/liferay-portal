/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.DXPEntityDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.entity.AsahMarker;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.util.GetterUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.io.File;
import java.io.FileOutputStream;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * @author Marcos Martins
 */
@Component
public class DXPEntitiesNanite extends BaseNanite {

	@Override
	public void run(JSONObject contextJSONObject) throws Exception {
		AsahMarker asahMarker = getAsahMarker();

		JSONObject asahMarkerContextJSONObject =
			asahMarker.getContextJSONObject();

		Date lastSuccessfulDate = null;

		String lastSuccessfulDateString = asahMarkerContextJSONObject.optString(
			"lastSuccessfulDate", null);

		String uploadType = "FULL";

		if (lastSuccessfulDateString != null) {
			lastSuccessfulDate = DateUtil.toUTCDate(lastSuccessfulDateString);

			uploadType = "INCREMENTAL";
		}

		Date currentDate = DateUtil.newDate();

		Map<Long, File> files = new HashMap<>();

		for (DataSource dataSource :
				_dataSourceDog.getDataSources("LIFERAY", "ACTIVE")) {

			Long dataSourceId = dataSource.getId();

			String fileName = _getValidatedFileName(
				String.join(
					"-", String.valueOf(dataSourceId),
					ProjectIdThreadLocal.getProjectId(),
					DateUtil.toUTCString(currentDate)));

			File file = File.createTempFile(fileName, ".zip");

			ZipOutputStream zipOutputStream = _getZipOutputStream(file);

			_run(
				currentDate, dataSourceId, lastSuccessfulDate,
				DXPEntity.Type.ANALYTICS_DELETE_MESSAGE, zipOutputStream);
			_run(
				currentDate, dataSourceId, lastSuccessfulDate,
				DXPEntity.Type.EXPANDO_COLUMN, zipOutputStream);
			_run(
				currentDate, dataSourceId, lastSuccessfulDate,
				DXPEntity.Type.GROUP, zipOutputStream);
			_run(
				currentDate, dataSourceId, lastSuccessfulDate,
				DXPEntity.Type.ORGANIZATION, zipOutputStream);
			_run(
				currentDate, dataSourceId, lastSuccessfulDate,
				DXPEntity.Type.ROLE, zipOutputStream);
			_run(
				currentDate, dataSourceId, lastSuccessfulDate,
				DXPEntity.Type.TEAM, zipOutputStream);
			_run(
				currentDate, dataSourceId, lastSuccessfulDate,
				DXPEntity.Type.USER, zipOutputStream);
			_run(
				currentDate, dataSourceId, lastSuccessfulDate,
				DXPEntity.Type.USER_GROUP, zipOutputStream);

			zipOutputStream.close();

			if (file.length() == _EMPTY_ZIP_FILE_LENGTH) {
				file.delete();
			}
			else {
				files.put(dataSourceId, file);
			}
		}

		// Move files

		for (Map.Entry<Long, File> entry : files.entrySet()) {
			File file = entry.getValue();

			String targetPath = _getValidatedPath(
				String.format(
					"%s/%s/%s/%s/%s/%s.zip", _dxpBatchEntitiesStoragePath,
					ProjectIdThreadLocal.getProjectId(), entry.getKey(),
					"com.liferay.analytics.dxp.entity.rest.dto.v1_0.DXPEntity",
					uploadType, DateUtil.toUTCString(currentDate)));

			Path path = Paths.get(targetPath);

			FileUtils.createParentDirectories(path.toFile());

			Files.move(file.toPath(), path);
		}

		asahMarkerContextJSONObject.put(
			"lastSuccessfulDate", DateUtil.toString(currentDate));

		asahMarkerDog.updateAsahMarker(asahMarker);
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	private JSONArray _getExpandoFieldsJSONArray(DXPEntity dxpEntity) {
		JSONArray jsonArray = new JSONArray();

		JSONObject fieldsJSONObject = dxpEntity.getFieldsJSONObject();

		JSONObject expandoJSONObject = fieldsJSONObject.optJSONObject(
			"expando");

		if (expandoJSONObject != null) {
			Map<String, Object> map = expandoJSONObject.toMap();

			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String dataType = StringUtils.substringAfterLast(
					entry.getKey(), "-");
				String value = GetterUtil.getString(entry.getValue());

				if (dataType.equalsIgnoreCase("Text") &&
					value.startsWith("[") && value.endsWith("]")) {

					value = value.substring(1, value.length() - 1);

					JSONArray valuesJSONArray = new JSONArray();

					for (String curValue : value.split(",")) {
						valuesJSONArray.put(StringUtils.trim(curValue));
					}

					value = valuesJSONArray.toString();
				}

				jsonArray.put(
					JSONUtil.put(
						"columnId", entry.getKey()
					).put(
						"name",
						StringUtils.substringBeforeLast(entry.getKey(), "-")
					).put(
						"value", value
					));
			}
		}

		return jsonArray;
	}

	private JSONArray _getFieldsJSONArray(DXPEntity dxpEntity) {
		JSONArray jsonArray = new JSONArray();

		JSONObject fieldsJSONObject = dxpEntity.getFieldsJSONObject();

		DXPEntity.Type type = dxpEntity.getType();

		if (type == DXPEntity.Type.EXPANDO_COLUMN) {
			fieldsJSONObject.put("columnId", fieldsJSONObject.get("name"));
		}
		else if (type == DXPEntity.Type.ORGANIZATION) {
			if (fieldsJSONObject.has("nameTreePath")) {
				fieldsJSONObject.put(
					"treePath", fieldsJSONObject.get("nameTreePath"));
			}
		}
		else if (type == DXPEntity.Type.USER) {
			if (fieldsJSONObject.has("contact")) {
				JSONObject contactJSONObject = fieldsJSONObject.getJSONObject(
					"contact");

				for (String key : contactJSONObject.keySet()) {
					fieldsJSONObject.put(key, contactJSONObject.get(key));
				}

				fieldsJSONObject.remove("contact");
			}

			if (!fieldsJSONObject.has("createDate")) {
				fieldsJSONObject.put("createDate", DateUtil.newDateString());
			}

			if (fieldsJSONObject.has("memberships")) {
				JSONObject membershipJSONObject =
					fieldsJSONObject.getJSONObject("memberships");

				for (String key : membershipJSONObject.keySet()) {
					DXPEntity.Type membershipType = DXPEntity.Type.of(key);

					if (membershipType == null) {
						continue;
					}

					fieldsJSONObject.put(
						membershipType.getIndividualFieldName(),
						membershipJSONObject.get(key));
				}

				fieldsJSONObject.remove("memberships");
			}
		}

		if (fieldsJSONObject.has("expando")) {
			fieldsJSONObject.remove("expando");
		}

		Map<String, Object> map = fieldsJSONObject.toMap();

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			jsonArray.put(
				JSONUtil.put(
					"name", entry.getKey()
				).put(
					"value", GetterUtil.getString(entry.getValue())
				));
		}

		return jsonArray;
	}

	private String _getValidatedFileName(String fileName) {
		if (!Objects.equals(fileName, FilenameUtils.getName(fileName))) {
			throw new IllegalArgumentException("Invalid file name");
		}

		return fileName;
	}

	private String _getValidatedPath(String path) {
		if (!Objects.equals(path, FilenameUtils.normalize(path))) {
			throw new IllegalArgumentException("Invalid path");
		}

		return path;
	}

	private ZipOutputStream _getZipOutputStream(File file) throws Exception {
		FileOutputStream fileOutputStream = new FileOutputStream(file, true);

		ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

		ZipEntry zipEntry = new ZipEntry("export.jsonl");

		zipOutputStream.putNextEntry(zipEntry);

		return zipOutputStream;
	}

	private void _run(
			Date currentDate, Long dataSourceId, Date lastSuccessfulDate,
			DXPEntity.Type type, ZipOutputStream zipOutputStream)
		throws Exception {

		int page = 0;

		while (true) {
			Page<DXPEntity> dxpEntitiesPage = _dxpEntityDog.getDXPEntityPage(
				dataSourceId, lastSuccessfulDate, currentDate, type,
				PageRequest.of(page++, 500, Sort.by(Sort.Direction.ASC, "id")));

			if (dxpEntitiesPage.isEmpty()) {
				break;
			}

			for (DXPEntity dxpEntity : dxpEntitiesPage.getContent()) {
				if (StringUtils.isEmpty(dxpEntity.getIdFieldValue())) {
					continue;
				}

				_write(_toJSONString(dxpEntity), zipOutputStream);
			}
		}
	}

	private String _toJSON(DXPEntity dxpEntity) {
		DXPEntity.Type type = dxpEntity.getType();

		return JSONUtil.put(
			"expandoFields", _getExpandoFieldsJSONArray(dxpEntity)
		).put(
			"fields", _getFieldsJSONArray(dxpEntity)
		).put(
			"id", dxpEntity.getIdFieldValue()
		).put(
			"modifiedDate", DateUtil.toUTCString(dxpEntity.getModifiedDate())
		).put(
			"type", type.getClassName()
		).toString();
	}

	private void _write(String data, ZipOutputStream zipOutputStream)
		throws Exception {

		String newLineAppendedData = data + System.lineSeparator();

		zipOutputStream.write(
			newLineAppendedData.getBytes(StandardCharsets.UTF_8));
	}

	private static final long _EMPTY_ZIP_FILE_LENGTH = 140;

	private static final Log _log = LogFactory.getLog(DXPEntitiesNanite.class);

	@Autowired
	private DataSourceDog _dataSourceDog;

	@Value("${osb.asah.dxp.batch.entities.storage.path:/storage}")
	private String _dxpBatchEntitiesStoragePath;

	@Autowired
	private DXPEntityDog _dxpEntityDog;

}