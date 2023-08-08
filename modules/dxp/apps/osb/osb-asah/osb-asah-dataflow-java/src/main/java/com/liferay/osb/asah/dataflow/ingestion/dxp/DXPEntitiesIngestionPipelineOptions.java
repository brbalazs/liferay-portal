/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;

/**
 * @author Rachael Koestartyo
 */
public interface DXPEntitiesIngestionPipelineOptions
	extends DataflowPipelineOptions {

	@Description("Return the GCS output bucket")
	@Validation.Required
	public String getGCSBucket();

	@Description(
		"Return the Pubsub subscription name. The name should be in the format of projects/<project-id>/subscriptions/<subscription-name>."
	)
	@Validation.Required
	public String getPubsubSubscription();

	@Default.Integer(1)
	@Description("Return the shard count")
	public int getShardCount();

	@Default.Integer(200)
	@Description("Return the trigger minimum element count")
	public int getTriggerElementCount();

	@Default.Long(60)
	@Description("Return the trigger interval duration in seconds")
	public long getTriggerIntervalDuration();

	public void setGCSBucket(String gcsBucket);

	public void setPubsubSubscription(String pubsubSubscription);

	public void setShardCount(int shardCount);

	public void setTriggerElementCount(int triggerElementCount);

	public void setTriggerIntervalDuration(long triggerIntervalDuration);

}