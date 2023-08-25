/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.stream.curator.bot.nanite.dxpentity;

import com.liferay.osb.asah.common.concurrent.BoundedExecutor;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.DXPEntityDog;
import com.liferay.osb.asah.common.dog.DataControlTaskDog;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.DataControlTask;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.lock.KeyReentrantLock;
import com.liferay.osb.asah.common.messaging.Channel;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.messaging.MessageSubscriber;
import com.liferay.osb.asah.common.messaging.model.Message;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.stream.curator.bot.nanite.Nanite;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 */
@Component
public class DXPEntitiesNanite implements Nanite {

	@Override
	public long getInterval() {
		return DateUtil.MINUTE;
	}

	@Override
	public void run() {
		try {
			_run();
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		_boundedExecutor.awaitPendingTasks();
	}

	@PreDestroy
	private void _destroy() {
		_boundedExecutor.shutdown();
	}

	private void _processAssociationObject(
		String action, JSONObject objectJSONObject, DXPEntity.Type type) {

		Long dataSourceId = Long.valueOf(
			objectJSONObject.getString("osbAsahDataSourceId"));

		DXPEntity dxpEntity = _dxpEntityDog.fetchByFieldsAndType(
			new HashMap() {
				{
					put("dataSourceId", dataSourceId);
					put(
						"fields." + DXPEntity.Type.USER.getIdFieldName(),
						objectJSONObject.getLong("userId"));
				}
			},
			DXPEntity.Type.USER);

		if (dxpEntity == null) {
			return;
		}

		JSONObject fieldsJSONObject = dxpEntity.getFieldsJSONObject();

		JSONObject membershipsJSONObject = fieldsJSONObject.optJSONObject(
			"memberships");

		if (membershipsJSONObject == null) {
			membershipsJSONObject = new JSONObject();

			fieldsJSONObject.put("memberships", membershipsJSONObject);
		}

		JSONArray membershipJSONArray = membershipsJSONObject.optJSONArray(
			type.getClassName());

		if (membershipJSONArray == null) {
			membershipJSONArray = new JSONArray();

			membershipsJSONObject.put(type.getClassName(), membershipJSONArray);
		}

		if (StringUtils.isBlank(objectJSONObject.optString("emailAddress"))) {
			return;
		}

		long classPK = objectJSONObject.getLong("classPK");

		if (action.equals("addAssociation")) {
			membershipJSONArray.put(classPK);
		}
		else if (action.equals("deleteAssociation")) {
			JSONUtil.removeValue(membershipJSONArray, classPK);
		}

		_dxpEntityDog.updateDXPEntity(dxpEntity);
	}

	private void _processMessageJSONObject(JSONObject messageJSONObject) {
		JSONObject contextJSONObject = messageJSONObject.getJSONObject(
			"context");
		JSONObject objectJSONObject = messageJSONObject.getJSONObject("object");

		DXPEntity.Type dxpEntityType = DXPEntity.Type.of(
			contextJSONObject.getString("type"));

		if ((dxpEntityType == null) || dxpEntityType.isUser()) {
			DataControlTask dataControlTask =
				_dataControlTaskDog.fetchLatestActiveSuppressionDataControlTask(
					objectJSONObject.optString("emailAddress"));

			if (!Objects.isNull(dataControlTask)) {
				return;
			}
		}

		_processObject(
			contextJSONObject.getString("action"), objectJSONObject,
			dxpEntityType);
	}

	private void _processMessages(
		String projectId, List<Message<JSONArray>> messages) {

		_boundedExecutor.runAsync(
			() -> {
				try {
					ProjectIdThreadLocal.setProjectId(projectId);

					long start = System.currentTimeMillis();

					for (Message<JSONArray> message : messages) {
						JSONArray jsonArray = message.getObject();

						for (int i = 0; i < jsonArray.length(); i++) {
							_processMessageJSONObject(
								jsonArray.getJSONObject(i));
						}

						_messageSubscriber.sendAckIds(
							Collections.singletonList(message.getAckId()));
					}

					if (_log.isDebugEnabled()) {
						Class<?> clazz = getClass();

						_log.debug(
							String.format(
								"%s processed %d DXP entities in %d ms",
								clazz.getSimpleName(), messages.size(),
								System.currentTimeMillis() - start));
					}
				}
				catch (Exception exception) {
					messages.forEach(
						message -> _messageSubscriber.registerException(
							exception, message));

					_log.error(
						"Unable to process DXP entities message " +
							ListUtil.map(messages, Message::getObject),
						exception);
				}
			},
			KeyReentrantLock.getReentrantLock(getClass(), projectId));
	}

	private void _processObject(
		String action, JSONObject objectJSONObject, DXPEntity.Type type) {

		if (type == null) {
			return;
		}

		if (action.equalsIgnoreCase("addAssociation") ||
			action.equalsIgnoreCase("deleteAssociation")) {

			_processAssociationObject(action, objectJSONObject, type);

			return;
		}

		if (type.isUserField()) {
			return;
		}

		DXPEntity dxpEntity = _dxpEntityDog.fetchByFieldsAndType(
			new HashMap<String, Object>() {
				{
					put(
						"dataSourceId",
						objectJSONObject.getString("osbAsahDataSourceId"));

					if (type.isExpandoColumn()) {
						put("fields.name", objectJSONObject.optString("name"));
					}
					else {
						put(
							"fields." + type.getIdFieldName(),
							objectJSONObject.optInt(type.getIdFieldName()));
					}
				}
			},
			type);

		if (action.equalsIgnoreCase("delete") && (dxpEntity != null)) {
			_sendDeleteMessage(dxpEntity);

			_dxpEntityDog.delete(dxpEntity);
		}
		else if (!action.equalsIgnoreCase("delete")) {
			DXPEntity newDXPEntity = new DXPEntity(
				objectJSONObject.getLong("osbAsahDataSourceId"),
				objectJSONObject);

			if (dxpEntity != null) {
				newDXPEntity.setFieldsJSONObject(
					JSONUtil.merge(
						dxpEntity.getFieldsJSONObject(), objectJSONObject));
				newDXPEntity.setId(dxpEntity.getId());
				newDXPEntity.setType(type);

				_dxpEntityDog.updateDXPEntity(newDXPEntity);
			}
			else {
				_dxpEntityDog.addDXPEntity(newDXPEntity, type);
			}
		}
	}

	private void _run() throws Exception {
		while (true) {
			long start = System.currentTimeMillis();

			List<Message<JSONArray>> messages = _messageSubscriber.pullMessages(
				_dxpEntitiesNanitePullMessagesSize, JSONArray::new);

			if (_log.isDebugEnabled()) {
				_log.debug(
					String.format(
						"%d DXP entities messages received", messages.size()));
			}

			if (messages.isEmpty()) {
				break;
			}

			Stream<Message<JSONArray>> stream = messages.stream();

			stream.collect(
				Collectors.groupingBy(
					message -> {
						Map<String, String> attributesMap =
							message.getAttributes();

						return attributesMap.get("projectId");
					},
					LinkedHashMap::new, Collectors.toList())
			).forEach(
				this::_processMessages
			);

			if (_log.isDebugEnabled()) {
				Class<?> clazz = getClass();

				_log.debug(
					String.format(
						"%s dispatched %d DXP entities messages in %d ms",
						clazz.getSimpleName(), messages.size(),
						System.currentTimeMillis() - start));
			}
		}
	}

	private void _sendDeleteMessage(DXPEntity dxpEntity) {
		Map<String, String> messageAttributes = new HashMap<>();

		messageAttributes.put(
			"dataSourceId", String.valueOf(dxpEntity.getDataSourceId()));
		messageAttributes.put("projectId", ProjectIdThreadLocal.getProjectId());
		messageAttributes.put(
			"resourceName",
			"com.liferay.analytics.dxp.entity.rest.dto.v1_0.DXPEntity");
		messageAttributes.put("uploadTime", DateUtil.toUTCString(new Date()));
		messageAttributes.put("uploadType", "FULL");

		DXPEntity.Type type = dxpEntity.getType();

		_messageBus.sendMessage(
			Channel.DXP_ENTITIES_DEFAULT,
			JSONUtil.put(
				"fields",
				JSONUtil.putAll(
					JSONUtil.put(
						"name", "className"
					).put(
						"value", type.getClassName()
					),
					JSONUtil.put(
						"name", "classPK"
					).put(
						"value", dxpEntity.getIdFieldValue()
					))
			).put(
				"id", dxpEntity.getIdFieldValue()
			).put(
				"modifiedDate",
				DateUtil.toUTCString(dxpEntity.getModifiedDate())
			).put(
				"type",
				"com.liferay.analytics.message.storage.model." +
					"AnalyticsDeleteMessage"
			).toString(),
			messageAttributes);
	}

	private static final Log _log = LogFactory.getLog(DXPEntitiesNanite.class);

	private final BoundedExecutor _boundedExecutor =
		BoundedExecutor.newBoundedExecutor(15, 10);

	@Autowired
	private DataControlTaskDog _dataControlTaskDog;

	@Value("${osb.asah.dxp.entities.nanite.pull.messages.size:50}")
	private int _dxpEntitiesNanitePullMessagesSize;

	@Autowired
	private DXPEntityDog _dxpEntityDog;

	@Autowired
	private MessageBus _messageBus;

	@MessageSubscriber.Autowired(channel = Channel.DXP_ENTITIES_MESSAGE)
	private MessageSubscriber _messageSubscriber;

}