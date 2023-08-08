/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;

/**
 * @author Riccardo Ferrari
 */
public interface DXPCommerceEntitiesIngestionPipelineOptions
	extends DataflowPipelineOptions {

	@Description("Return the GCS output bucket")
	@Validation.Required
	public String getGCSBucket();

	@Description("Return the order BigQuery table name.")
	@Validation.Required
	public String getOrderBigQueryTable();

	@Description(
		"Return the order queue pubsub subscription name. The name should be in the format of projects/<project-id>/subscriptions/<subscription-name>."
	)
	@Validation.Required
	public String getOrderPubsubSubscription();

	@Description("Return the product BigQuery table name.")
	@Validation.Required
	public String getProductBigQueryTable();

	@Description(
		"Return the product queue pubsub subscription name. The name should be in the format of projects/<project-id>/subscriptions/<subscription-name>."
	)
	@Validation.Required
	public String getProductPubsubSubscription();

	@Description("Return the shard count")
	@Validation.Required
	public int getShardCount();

	@Description("Return the trigger minimum element count")
	@Validation.Required
	public int getTriggerElementCount();

	@Description("Return the trigger interval duration in seconds")
	@Validation.Required
	public long getTriggerIntervalDuration();

	public void setGCSBucket(String gcsBucket);

	public void setOrderBigQueryTable(String orderBigQueryTable);

	public void setOrderPubsubSubscription(String pubsubSubscription);

	public void setProductBigQueryTable(String productBigQueryTable);

	public void setProductPubsubSubscription(String pubsubSubscription);

	public void setShardCount(int shardCount);

	public void setTriggerElementCount(int triggerElementCount);

	public void setTriggerIntervalDuration(long triggerIntervalDuration);

}