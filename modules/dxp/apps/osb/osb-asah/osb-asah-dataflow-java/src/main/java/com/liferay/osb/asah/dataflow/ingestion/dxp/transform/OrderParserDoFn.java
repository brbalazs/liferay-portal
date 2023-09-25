/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityMessageWrapper;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.Order;

import java.util.Map;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.PCollectionView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Riccardo Ferrari
 */
public class OrderParserDoFn extends DoFn<DXPEntityMessageWrapper, Order> {

	public OrderParserDoFn(
		PCollectionView<Map<Long, Long>> commerceChannelIdMapPCollectionView) {

		_commerceChannelIdMapPCollectionView =
			commerceChannelIdMapPCollectionView;
	}

	@ProcessElement
	public void processElement(ProcessContext processContext) {
		DXPEntityMessageWrapper dxpEntityMessageWrapper =
			processContext.element();

		try {
			Order order = ObjectMapperUtil.readValue(
				Order.class, dxpEntityMessageWrapper.payload);

			Map<Long, Long> channelIds = processContext.sideInput(
				_commerceChannelIdMapPCollectionView);

			if (_logger.isDebugEnabled()) {
				_logger.debug(
					"Analytics Cloud channel ID {} and commerce channel ID {}",
					channelIds.get(order.commerceChannelId),
					order.commerceChannelId);
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

	private final PCollectionView<Map<Long, Long>>
		_commerceChannelIdMapPCollectionView;

}