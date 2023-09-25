/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.Product;

/**
 * @author Riccardo Ferrari
 */
public class ProductParserPTransform
	extends BaseParserPTransform<DXPEntityMessageWrapper, Product> {

	@Override
	protected Product doParse(DXPEntityMessageWrapper dxpEntityMessageWrapper)
		throws Exception {

		Product product = ObjectMapperUtil.readValue(
			Product.class, dxpEntityMessageWrapper.payload);

		product.channelId = product.catalogId;
		product.dataSourceId = dxpEntityMessageWrapper.dataSourceId;
		product.projectId = dxpEntityMessageWrapper.projectId;
		product.uploadDate = dxpEntityMessageWrapper.uploadTime;
		product.uploadType = dxpEntityMessageWrapper.uploadType;

		return product;
	}

}