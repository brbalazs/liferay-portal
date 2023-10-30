/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.function;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.AssetEntity;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;

import org.apache.beam.sdk.transforms.DoFn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Riccardo Ferrari
 */
public class AssetEntityParserDoFn
	extends DoFn<DXPEntityMessageWrapper, AssetEntity> {

	@ProcessElement
	public void processElement(ProcessContext processContext) {
		DXPEntityMessageWrapper dxpEntityMessageWrapper =
			processContext.element();

		try {
			AssetEntity assetEntity = ObjectMapperUtil.readValue(
				AssetEntity.class, dxpEntityMessageWrapper.payload);

			assetEntity.dataSourceId = dxpEntityMessageWrapper.dataSourceId;
			assetEntity.projectId = dxpEntityMessageWrapper.projectId;
			assetEntity.uploadDate = dxpEntityMessageWrapper.uploadTime;
			assetEntity.uploadType = dxpEntityMessageWrapper.uploadType;

			processContext.output(assetEntity);
		}
		catch (JsonProcessingException jsonProcessingException) {
			_logger.error(
				"Unable to parse asset entity message {}",
				dxpEntityMessageWrapper.payload);
		}
	}

	private static final Logger _logger = LoggerFactory.getLogger(
		AssetEntityParserDoFn.class);

}