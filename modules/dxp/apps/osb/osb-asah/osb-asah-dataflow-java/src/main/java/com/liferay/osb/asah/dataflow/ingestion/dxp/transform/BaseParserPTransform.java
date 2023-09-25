/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.BaseDXPEntity;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Riccardo Ferrari
 */
public abstract class BaseParserPTransform<E, T extends BaseDXPEntity>
	extends PTransform<PCollection<E>, PCollectionTuple> {

	@Override
	public PCollectionTuple expand(PCollection<E> input) {
		return input.apply(
			"Parse DXP Entity",
			ParDo.of(
				new DoFn<E, T>() {

					@ProcessElement
					public void processElement(ProcessContext processContext) {
						E element = processContext.element();

						try {
							T t = doParse(element);

							if (t != null) {
								processContext.output(_successTupleTag, t);
							}
						}
						catch (Exception exception) {
							_logger.error(
								"Unable to parse {}: {}", element,
								exception.getMessage());

							processContext.output(
								_failTupleTag,
								KV.of(exception.getMessage(), element));
						}
					}

				}
			).withOutputTags(
				_successTupleTag, TupleTagList.of(_failTupleTag)
			));
	}

	public TupleTag<KV<String, E>> getFailTupleTag() {
		return _failTupleTag;
	}

	public TupleTag<T> getSuccessTupleTag() {
		return _successTupleTag;
	}

	protected abstract T doParse(E e) throws Exception;

	private static final Logger _logger = LoggerFactory.getLogger(
		BaseParserPTransform.class);

	private final TupleTag<KV<String, E>> _failTupleTag =
		new TupleTag<KV<String, E>>() {
		};
	private final TupleTag<T> _successTupleTag = new TupleTag<T>() {
	};

}