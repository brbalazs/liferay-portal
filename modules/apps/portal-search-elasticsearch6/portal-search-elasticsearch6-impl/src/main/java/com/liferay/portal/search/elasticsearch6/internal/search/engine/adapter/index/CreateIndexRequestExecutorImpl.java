/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.index;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchClientResolver;
import com.liferay.portal.search.engine.adapter.index.CreateIndexRequest;
import com.liferay.portal.search.engine.adapter.index.CreateIndexResponse;

import org.elasticsearch.action.admin.indices.create.CreateIndexAction;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.common.xcontent.XContentType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(service = CreateIndexRequestExecutor.class)
public class CreateIndexRequestExecutorImpl
	implements CreateIndexRequestExecutor {

	@Override
	public CreateIndexResponse execute(CreateIndexRequest createIndexRequest) {
		CreateIndexRequestBuilder createIndexRequestBuilder =
			createCreateIndexRequestBuilder(createIndexRequest);

		org.elasticsearch.action.admin.indices.create.CreateIndexResponse
			elasticsearchCreateIndexResponse = createIndexRequestBuilder.get();

		return new CreateIndexResponse(
			elasticsearchCreateIndexResponse.isAcknowledged());
	}

	protected CreateIndexRequestBuilder createCreateIndexRequestBuilder(
		CreateIndexRequest createIndexRequest) {

		CreateIndexRequestBuilder createIndexRequestBuilder =
			CreateIndexAction.INSTANCE.newRequestBuilder(
				_elasticsearchClientResolver.getClient(false));

		createIndexRequestBuilder.setIndex(createIndexRequest.getIndexName());

		try {
			JSONObject jsonObject = _jsonFactory.createJSONObject(
				createIndexRequest.getSource());

			JSONObject settingsJsonObject = _jsonFactory.createJSONObject(
				jsonObject.getString("settings"));

			if (settingsJsonObject == null) {
				settingsJsonObject = _jsonFactory.createJSONObject();
			}

			settingsJsonObject.put("index.soft_deletes.enabled", "true");

			jsonObject.put("settings", settingsJsonObject);

			createIndexRequest.setSource(jsonObject.toString());
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}

		createIndexRequestBuilder.setSource(
			createIndexRequest.getSource(), XContentType.JSON);

		return createIndexRequestBuilder;
	}

	@Reference(unbind = "-")
	protected void setElasticsearchClientResolver(
		ElasticsearchClientResolver elasticsearchClientResolver) {

		_elasticsearchClientResolver = elasticsearchClientResolver;
	}

	@Reference(unbind = "-")
	protected void setJsonFactory(JSONFactory jsonFactory) {
		_jsonFactory = jsonFactory;
	}

	private ElasticsearchClientResolver _elasticsearchClientResolver;
	private JSONFactory _jsonFactory;

}