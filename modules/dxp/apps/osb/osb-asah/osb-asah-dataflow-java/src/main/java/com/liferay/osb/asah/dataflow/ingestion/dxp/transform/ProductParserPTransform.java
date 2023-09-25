/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityPubsubMessage;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.Product;

/**
 * @author Riccardo Ferrari
 */
public class ProductParserPTransform
	extends BaseParserPTransform<DXPEntityPubsubMessage, Product> {

	@Override
	protected Product doParse(DXPEntityPubsubMessage dxpEntityPubsubMessage)
		throws Exception {

		Product product = ObjectMapperUtil.readValue(
			Product.class, dxpEntityPubsubMessage.getPayload());

		DXPEntityPubsubMessage.Attributes attributes =
			dxpEntityPubsubMessage.getAttributes();

		product.channelId = product.catalogId;
		product.dataSourceId = attributes.getDataSourceId();
		product.projectId = attributes.getProjectId();
		product.uploadDate = attributes.getUploadTime();
		product.uploadType = attributes.getUploadType();

		return product;
	}

}