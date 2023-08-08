/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.windowing.AfterPane;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Repeatedly;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.PCollection;

import org.joda.time.Duration;

/**
 * @author Riccardo Ferrari
 */
public class FixedDurationOrCountWindowPTransform<T>
	extends PTransform<PCollection<T>, PCollection<T>> {

	public FixedDurationOrCountWindowPTransform(
		int elementCount, Duration intervalDuration) {

		_elementCount = elementCount;
		_intervalDuration = intervalDuration;
	}

	@Override
	public PCollection<T> expand(PCollection<T> input) {
		return input.apply(
			Window.<T>into(
				FixedWindows.of(_intervalDuration)
			).triggering(
				Repeatedly.forever(AfterPane.elementCountAtLeast(_elementCount))
			).discardingFiredPanes(
			).withAllowedLateness(
				Duration.ZERO
			));
	}

	private final int _elementCount;
	private final Duration _intervalDuration;

}