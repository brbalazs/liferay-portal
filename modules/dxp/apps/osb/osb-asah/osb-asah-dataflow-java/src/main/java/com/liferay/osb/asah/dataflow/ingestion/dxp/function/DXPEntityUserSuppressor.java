/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.function;

import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntity;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.Field;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.PCollectionView;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marcellus Tavares
 */
public class DXPEntityUserSuppressor extends DoFn<DXPEntity, DXPEntity> {

	public DXPEntityUserSuppressor(
		PCollectionView<List<String>> suppressedEmailAddressesPCollectionView) {

		_suppressedEmailAddressesPCollectionView =
			suppressedEmailAddressesPCollectionView;
	}

	@ProcessElement
	public void processElement(ProcessContext processContext) {
		Set<String> suppressedEmailAddresses = new HashSet<>(
			processContext.sideInput(_suppressedEmailAddressesPCollectionView));

		DXPEntity dxpEntity = processContext.element();

		if (StringUtils.equals(dxpEntity.type, _DXP_ENTITY_USER_TYPE)) {
			String emailAddress = _getEmailAddress(dxpEntity.fields);

			if (emailAddress == null) {
				if (_logger.isWarnEnabled()) {
					_logger.warn(
						"Discarding DXP user entity with null email address");
				}

				return;
			}

			if (suppressedEmailAddresses.contains(
					StringUtils.lowerCase(emailAddress))) {

				if (_logger.isInfoEnabled()) {
					_logger.info(
						"Skipping ingestion of suppressed user " +
							emailAddress);
				}

				return;
			}
		}

		processContext.output(dxpEntity);
	}

	private String _getEmailAddress(List<Field> fields) {
		for (Field field : fields) {
			if (StringUtils.equals(field.name, "emailAddress")) {
				return field.value;
			}
		}

		return null;
	}

	private static final String _DXP_ENTITY_USER_TYPE =
		"com.liferay.portal.kernel.model.User";

	private static final Logger _logger = LoggerFactory.getLogger(
		DXPEntityUserSuppressor.class);

	private final PCollectionView<List<String>>
		_suppressedEmailAddressesPCollectionView;

}