/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityPubsubMessage;

import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;

/**
 * @author Riccardo Ferrari
 */
public class PubsubReaderPTransform
	extends PTransform<PBegin, PCollection<DXPEntityPubsubMessage>> {

	public PubsubReaderPTransform(String pubsubSubscription) {
		_pubsubSubscription = pubsubSubscription;
	}

	@Override
	public PCollection<DXPEntityPubsubMessage> expand(PBegin pBegin) {
		return pBegin.apply(
			PubsubIO.readMessagesWithAttributes(
			).fromSubscription(
				_pubsubSubscription
			)
		).apply(
			MapElements.via(
				new SimpleFunction<PubsubMessage, DXPEntityPubsubMessage>() {

					@Override
					public DXPEntityPubsubMessage apply(
						PubsubMessage pubsubMessage) {

						return new DXPEntityPubsubMessage(pubsubMessage);
					}

				})
		);
	}

	private final String _pubsubSubscription;

}