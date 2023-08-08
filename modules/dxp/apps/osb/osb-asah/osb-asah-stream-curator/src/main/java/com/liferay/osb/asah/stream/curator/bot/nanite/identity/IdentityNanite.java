/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.stream.curator.bot.nanite.identity;

import com.google.api.core.ApiFuture;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.storage.v1.AppendRowsResponse;
import com.google.cloud.bigquery.storage.v1.BigQueryWriteClient;
import com.google.cloud.bigquery.storage.v1.CreateWriteStreamRequest;
import com.google.cloud.bigquery.storage.v1.FinalizeWriteStreamRequest;
import com.google.cloud.bigquery.storage.v1.JsonStreamWriter;
import com.google.cloud.bigquery.storage.v1.TableName;
import com.google.cloud.bigquery.storage.v1.WriteStream;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.messaging.Channel;
import com.liferay.osb.asah.common.messaging.MessageSubscriber;
import com.liferay.osb.asah.common.messaging.model.Message;
import com.liferay.osb.asah.common.spring.annotation.ConditionalOnGoogleApplicationCredentials;
import com.liferay.osb.asah.stream.curator.bot.nanite.Nanite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author Robson Pastor
 */
@Component
@ConditionalOnGoogleApplicationCredentials
@ConditionalOnProperty(
	havingValue = "empty", matchIfMissing = true, value = "OSB_ASAH_PROJECT_ID"
)
public class IdentityNanite implements Nanite {

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
	}

	private void _acknowledgeMessages(List<Message<JSONObject>> messages) {
		List<String> ids = new ArrayList<>();

		for (Message<JSONObject> message : messages) {
			ids.add(message.getAckId());
		}

		_messageSubscriber.sendAckIds(ids);
	}

	private CreateWriteStreamRequest _buildCreateWriteStreamRequest(
		String datasetName, String tableName) {

		CreateWriteStreamRequest.Builder builder =
			CreateWriteStreamRequest.newBuilder();

		return builder.setParent(
			String.valueOf(
				TableName.of(_googleProjectId, datasetName, tableName))
		).setWriteStream(
			WriteStream.newBuilder(
			).setType(
				WriteStream.Type.COMMITTED
			).build()
		).build();
	}

	private String _getIndividualId(JSONObject jsonObject) {
		String individualId = jsonObject.optString("individualId", null);

		if (individualId == null) {
			individualId = jsonObject.getString("emailAddressHashed");
		}

		if (StringUtils.isBlank(individualId) ||
			Objects.equals(individualId, _EMPTY_EMAIL_ADDRESS_HASHED)) {

			individualId = null;
		}

		return individualId;
	}

	@PostConstruct
	private void _init() {
		BigQueryOptions bigQueryOptions = BigQueryOptions.getDefaultInstance();

		_googleProjectId = bigQueryOptions.getProjectId();
	}

	private void _insertIdentity(
			String datasetName, List<Message<JSONObject>> messages)
		throws Exception {

		try (BigQueryWriteClient bigQueryWriteClient =
				BigQueryWriteClient.create()) {

			WriteStream clientWriteStream =
				bigQueryWriteClient.createWriteStream(
					_buildCreateWriteStreamRequest(
						datasetName, _IDENTITY_TABLE_NAME));

			try (JsonStreamWriter jsonStreamWriter =
					JsonStreamWriter.newBuilder(
						clientWriteStream.getName(),
						clientWriteStream.getTableSchema()
					).build()) {

				ApiFuture<AppendRowsResponse> apiFuture =
					jsonStreamWriter.append(_toIdentityJSONArray(messages));

				apiFuture.get();

				if (_log.isDebugEnabled()) {
					_log.debug(
						String.format(
							"%s: %d rows inserted into identity table",
							datasetName, messages.size()));
				}
			}
			catch (ExecutionException executionException) {
				_log.error(
					"Unable to append records to identity table",
					executionException);
			}

			bigQueryWriteClient.finalizeWriteStream(
				FinalizeWriteStreamRequest.newBuilder(
				).setName(
					clientWriteStream.getName()
				).build());
		}
	}

	private void _run() throws Exception {
		while (true) {
			List<Message<JSONObject>> messages =
				_messageSubscriber.pullMessages(
					_identityNanitePullMessagesSize, JSONObject::new);

			if (_log.isDebugEnabled()) {
				_log.debug(
					String.format(
						"%d identity messages received", messages.size()));
			}

			if (messages.isEmpty()) {
				break;
			}

			Stream<Message<JSONObject>> stream = messages.stream();

			Map<String, List<Message<JSONObject>>> messagesMap = stream.collect(
				Collectors.groupingBy(
					message -> {
						JSONObject jsonObject = message.getObject();

						return jsonObject.getString("projectId");
					}));

			for (Map.Entry<String, List<Message<JSONObject>>> entry :
					messagesMap.entrySet()) {

				_insertIdentity(entry.getKey(), entry.getValue());

				_acknowledgeMessages(entry.getValue());
			}
		}
	}

	private JSONArray _toIdentityJSONArray(List<Message<JSONObject>> messages) {
		JSONArray jsonArray = new JSONArray();

		for (Message<JSONObject> message : messages) {
			JSONObject messageJSONObject = message.getObject();

			jsonArray.put(
				JSONUtil.put(
					"createDate",
					messageJSONObject.optString(
						"createDate", DateUtil.toString(new Date()))
				).put(
					"id", messageJSONObject.getString("userId")
				).put(
					"individualId", _getIndividualId(messageJSONObject)
				).put(
					"projectId", messageJSONObject.getString("projectId")
				));
		}

		return jsonArray;
	}

	private static final String _EMPTY_EMAIL_ADDRESS_HASHED =
		"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

	private static final String _IDENTITY_TABLE_NAME = "identity_raw";

	private static final Log _log = LogFactory.getLog(IdentityNanite.class);

	private String _googleProjectId;

	@Value("${osb.asah.identity.nanite.pull.messages.size:1000}")
	private int _identityNanitePullMessagesSize;

	@MessageSubscriber.Autowired(channel = Channel.IDENTITY_MESSAGE)
	private MessageSubscriber _messageSubscriber;

}