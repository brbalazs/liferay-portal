/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import com.liferay.osb.asah.common.concurrent.BoundedExecutor;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.DXPEntityDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.entity.AsahMarker;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.messaging.Channel;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.util.GetterUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
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

		String projectId = ProjectIdThreadLocal.getProjectId();

		Date currentDate = DateUtil.newDate();

		for (DataSource dataSource :
				_dataSourceDog.getDataSources("LIFERAY", "ACTIVE")) {

			_run(
				currentDate, dataSource.getId(), lastSuccessfulDate, projectId,
				DXPEntity.Type.ANALYTICS_DELETE_MESSAGE, uploadType);
			_run(
				currentDate, dataSource.getId(), lastSuccessfulDate, projectId,
				DXPEntity.Type.EXPANDO_COLUMN, uploadType);
			_run(
				currentDate, dataSource.getId(), lastSuccessfulDate, projectId,
				DXPEntity.Type.GROUP, uploadType);
			_run(
				currentDate, dataSource.getId(), lastSuccessfulDate, projectId,
				DXPEntity.Type.ORGANIZATION, uploadType);
			_run(
				currentDate, dataSource.getId(), lastSuccessfulDate, projectId,
				DXPEntity.Type.ROLE, uploadType);
			_run(
				currentDate, dataSource.getId(), lastSuccessfulDate, projectId,
				DXPEntity.Type.TEAM, uploadType);
			_run(
				currentDate, dataSource.getId(), lastSuccessfulDate, projectId,
				DXPEntity.Type.USER, uploadType);
			_run(
				currentDate, dataSource.getId(), lastSuccessfulDate, projectId,
				DXPEntity.Type.USER_GROUP, uploadType);
		}

		_boundedExecutor.awaitPendingTasks();

		asahMarkerContextJSONObject.put(
			"lastSuccessfulDate", DateUtil.toString(currentDate));

		asahMarkerDog.updateAsahMarker(asahMarker);
	}

	@Override
	protected Log getLog() {
		return _log;
	}

	@PreDestroy
	private void _destroy() {
		_boundedExecutor.shutdown();
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

	private void _run(
		Date currentDate, Long dataSourceId, Date lastSuccessfulDate,
		String projectId, DXPEntity.Type type, String uploadType) {

		Map<String, String> messageAttributes = new HashMap<>();

		messageAttributes.put("projectId", ProjectIdThreadLocal.getProjectId());
		messageAttributes.put(
			"resourceName",
			"com.liferay.analytics.dxp.entity.rest.dto.v1_0.DXPEntity");
		messageAttributes.put("uploadTime", DateUtil.toUTCString(new Date()));
		messageAttributes.put("uploadType", uploadType);

		_boundedExecutor.runAsync(
			() -> {
				ProjectIdThreadLocal.setProjectId(projectId);

				int page = 0;

				while (true) {
					Page<DXPEntity> dxpEntitiesPage =
						_dxpEntityDog.getDXPEntityPage(
							dataSourceId, lastSuccessfulDate, currentDate, type,
							PageRequest.of(
								page++, 500,
								Sort.by(Sort.Direction.ASC, "id")));

					if (dxpEntitiesPage.isEmpty()) {
						break;
					}

					for (DXPEntity dxpEntity : dxpEntitiesPage.getContent()) {
						if (StringUtils.isEmpty(dxpEntity.getIdFieldValue())) {
							continue;
						}

						messageAttributes.put(
							"dataSourceId",
							String.valueOf(dxpEntity.getDataSourceId()));

						_messageBus.sendMessage(
							Channel.DXP_ENTITIES_DEFAULT,
							_toJSON(dxpEntity), messageAttributes);
					}
				}
			});
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

	private static final Log _log = LogFactory.getLog(DXPEntitiesNanite.class);

	private final BoundedExecutor _boundedExecutor =
		BoundedExecutor.newBoundedExecutor(7, 7);

	@Autowired
	private DataSourceDog _dataSourceDog;

	@Autowired
	private DXPEntityDog _dxpEntityDog;

	@Autowired
	private MessageBus _messageBus;

}