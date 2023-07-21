/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.elasticsearch6.internal.search.engine.adapter.document;

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
		BulkRequestBuilder bulkRequestBuilder) {

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
		BulkRequestBuilder bulkRequestBuilder) {

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
		BulkRequestBuilder bulkRequestBuilder) {

		return translate(updateDocumentRequest);
	}

}