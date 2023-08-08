/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityPubsubMessage;

import org.apache.beam.sdk.transforms.DoFn;

/**
 * @author Riccardo Ferrari
 */
public class DXPEntityPubsubMessageParser
	extends DoFn<String, DXPEntityPubsubMessage> {

	@ProcessElement
	public void processElement(ProcessContext processContext) {
		try {
			DXPEntityPubsubMessage dxpEntityPubsubMessage =
				ObjectMapperUtil.readValue(
					DXPEntityPubsubMessage.class, processContext.element());

			processContext.output(dxpEntityPubsubMessage);
		}
		catch (Exception exception) {
			throw new RuntimeException(
				"Unable to parse DXP entity pubsub message", exception);
		}
	}

}