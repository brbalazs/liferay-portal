/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.event;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;

/**
 * @author Marcellus Tavares
 */
public interface EventIngestionPipelineOptions extends DataflowPipelineOptions {

	@Description(
		"Return the pub/sub subscription input name. The name should be in the format of projects/<project-id>/subscriptions/<subscription-name>."
	)
	@Validation.Required
	public String getInputSubscription();

	@Description(
		"Return the output directory for the files. The directory must end with a slash."
	)
	@Validation.Required
	public String getOutputDirectory();

	@Default.String("output")
	@Description("Return the output file name prefix. The default is 'output'.")
	public String getOutputFileNamePrefix();

	@Default.Long(5)
	public long getSessionWindowAllowedLateness();

	@Default.Long(1)
	public long getSessionWindowEarlyFiringsInterval();

	@Default.Long(30)
	public long getSessionWindowGapDuration();

	@Default.Long(5)
	@Description(
		"Return the interval in minutes in which the data will be written. The default is 5 minutes."
	)
	public long getWriteInterval();

	public void setInputSubscription(String inputSubscription);

	public void setOutputDirectory(String outputDirectory);

	public void setOutputFileNamePrefix(String outputFileNamePrefix);

	public void setSessionWindowAllowedLateness(long value);

	public void setSessionWindowEarlyFiringsInterval(long value);

	public void setSessionWindowGapDuration(long value);

	public void setWriteInterval(long value);

}