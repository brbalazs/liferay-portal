/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityPubsubMessage;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.Order;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Riccardo Ferrari
 */
public class OrderParserPTransform extends BaseParserPTransform<Order> {

	@Override
	protected Order doParse(DXPEntityPubsubMessage dxpEntityPubsubMessage)
		throws Exception {

		Order order = ObjectMapperUtil.readValue(
			Order.class, dxpEntityPubsubMessage.getPayload());

		DXPEntityPubsubMessage.Attributes attributes =
			dxpEntityPubsubMessage.getAttributes();

		Map<Long, Long> channelIds =
			attributes.getCommerceChannelIdChannelIds();

		if (_logger.isDebugEnabled()) {
			_logger.debug(
				String.format(
					"Analytics Cloud channel ID %s and commerce channel ID %s",
					channelIds.get(order.commerceChannelId),
					order.commerceChannelId));
		}

		order.channelId = channelIds.get(order.commerceChannelId);
		order.dataSourceId = attributes.getDataSourceId();
		order.projectId = attributes.getProjectId();
		order.uploadDate = attributes.getUploadTime();
		order.uploadType = attributes.getUploadType();

		return order;
	}

	private static final Logger _logger = LoggerFactory.getLogger(
		OrderParserPTransform.class);

}