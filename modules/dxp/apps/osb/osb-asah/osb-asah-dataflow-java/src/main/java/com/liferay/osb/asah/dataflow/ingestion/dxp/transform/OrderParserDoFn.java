/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.Order;

import java.util.HashMap;
import java.util.Map;

import org.apache.beam.sdk.transforms.DoFn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Riccardo Ferrari
 */
public class OrderParserDoFn
	extends DoFn<DXPEntityMessageWrapper, Order> {

	@ProcessElement
	public void processElement(ProcessContext processContext) {
		DXPEntityMessageWrapper dxpEntityMessageWrapper =
			processContext.element();

		try {
			Order order = ObjectMapperUtil.readValue(
				Order.class, dxpEntityMessageWrapper.payload);

			// TODO Pass channelIds as a side input

			Map<Long, Long> channelIds = new HashMap<>();

			if (_logger.isDebugEnabled()) {
				_logger.debug(
					String.format(
						"Analytics Cloud channel ID %s and commerce channel ID %s",
						channelIds.get(order.commerceChannelId),
						order.commerceChannelId));
			}

			order.channelId = channelIds.get(order.commerceChannelId);
			order.dataSourceId = dxpEntityMessageWrapper.dataSourceId;
			order.projectId = dxpEntityMessageWrapper.projectId;
			order.uploadDate = dxpEntityMessageWrapper.uploadTime;
			order.uploadType = dxpEntityMessageWrapper.uploadType;

			processContext.output(order);
		}
		catch (JsonProcessingException jsonProcessingException) {
			_logger.error(
				"Unable to parse order message {}",
				dxpEntityMessageWrapper.payload);
		}
	}

	private static final Logger _logger = LoggerFactory.getLogger(
		OrderParserDoFn.class);

}