/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.Product;

import org.apache.beam.sdk.transforms.DoFn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Riccardo Ferrari
 */
public class ProductParserDoFn extends DoFn<DXPEntityMessageWrapper, Product> {

	@ProcessElement
	public void processElement(ProcessContext processContext) {
		DXPEntityMessageWrapper dxpEntityMessageWrapper =
			processContext.element();

		try {
			Product product = ObjectMapperUtil.readValue(
				Product.class, dxpEntityMessageWrapper.payload);

			product.channelId = product.catalogId;
			product.dataSourceId = dxpEntityMessageWrapper.dataSourceId;
			product.projectId = dxpEntityMessageWrapper.projectId;
			product.uploadDate = dxpEntityMessageWrapper.uploadTime;
			product.uploadType = dxpEntityMessageWrapper.uploadType;

			processContext.output(product);
		}
		catch (JsonProcessingException jsonProcessingException) {
			_logger.error(
				"Unable to parse product message {}",
				dxpEntityMessageWrapper.payload);
		}
	}

	private static final Logger _logger = LoggerFactory.getLogger(
		ProductParserDoFn.class);

}