/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.function;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntity;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;

import org.apache.beam.sdk.transforms.DoFn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marcellus Tavares
 */
public class DXPEntityParserDoFn
	extends DoFn<DXPEntityMessageWrapper, DXPEntity> {

	@ProcessElement
	public void processElement(ProcessContext processContext) {
		DXPEntityMessageWrapper dxpEntityMessageWrapper =
			processContext.element();

		try {
			DXPEntity dxpEntity = ObjectMapperUtil.readValue(
				DXPEntity.class, dxpEntityMessageWrapper.payload);

			dxpEntity.classPK = dxpEntity.id;
			dxpEntity.dataSourceId = dxpEntityMessageWrapper.dataSourceId;
			dxpEntity.projectId = dxpEntityMessageWrapper.projectId;
			dxpEntity.uploadDate = dxpEntityMessageWrapper.uploadTime;
			dxpEntity.uploadType = dxpEntityMessageWrapper.uploadType;

			processContext.output(dxpEntity);
		}
		catch (JsonProcessingException jsonProcessingException) {
			_logger.error(
				"Unable to parse DXP entity message {}",
				dxpEntityMessageWrapper.payload);
		}
	}

	private static final Logger _logger = LoggerFactory.getLogger(
		DXPEntityParserDoFn.class);

}