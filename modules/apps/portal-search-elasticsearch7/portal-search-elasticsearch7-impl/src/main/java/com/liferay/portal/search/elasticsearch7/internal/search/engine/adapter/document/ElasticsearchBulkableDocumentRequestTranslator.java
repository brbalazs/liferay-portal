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

package com.liferay.portal.search.elasticsearch7.internal.search.engine.adapter.document;

import com.liferay.portal.search.engine.adapter.document.BulkableDocumentRequestTranslator;
import com.liferay.portal.search.engine.adapter.document.DeleteDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.GetDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.IndexDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateDocumentRequest;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;

/**
 * @author Adam Brandizzi
 */
public interface ElasticsearchBulkableDocumentRequestTranslator
	extends BulkableDocumentRequestTranslator
		<DeleteRequestBuilder, IndexRequestBuilder, UpdateRequestBuilder,
		 BulkRequestBuilder> {

	public DeleteRequestBuilder translate(
		DeleteDocumentRequest deleteDocumentRequest);

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 *             #translate(UpdateDocumentRequest)}
	 */
	@Deprecated
	@Override
	public default DeleteRequestBuilder translate(
		DeleteDocumentRequest deleteDocumentRequest,
		BulkRequestBuilder searchEngineAdapterRequest) {

		return translate(deleteDocumentRequest);
	}

	public GetRequestBuilder translate(GetDocumentRequest getDocumentRequest);

	public IndexRequestBuilder translate(
		IndexDocumentRequest indexDocumentRequest);

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 *             #translate(UpdateDocumentRequest)}
	 */
	@Deprecated
	@Override
	public default IndexRequestBuilder translate(
		IndexDocumentRequest indexDocumentRequest,
		BulkRequestBuilder searchEngineAdapterRequest) {

		return translate(indexDocumentRequest);
	}

	public UpdateRequestBuilder translate(
		UpdateDocumentRequest updateDocumentRequest);

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 *             #translate(UpdateDocumentRequest)}
	 */
	@Deprecated
	@Override
	public default UpdateRequestBuilder translate(
		UpdateDocumentRequest updateDocumentRequest,
		BulkRequestBuilder searchEngineAdapterRequest) {

		return translate(updateDocumentRequest);
	}

}