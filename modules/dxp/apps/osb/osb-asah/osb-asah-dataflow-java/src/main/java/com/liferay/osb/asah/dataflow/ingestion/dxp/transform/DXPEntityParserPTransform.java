/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.transform;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntity;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.DXPEntityPubsubMessage;
import com.liferay.osb.asah.dataflow.ingestion.dxp.entity.Field;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marcos Martins
 * @author Rachael Koestartyo
 */
public class DXPEntityParserPTransform extends BaseParserPTransform<DXPEntity> {

	@Override
	protected DXPEntity doParse(DXPEntityPubsubMessage dxpEntityPubsubMessage)
		throws Exception {

		DXPEntity dxpEntity = ObjectMapperUtil.readValue(
			DXPEntity.class, dxpEntityPubsubMessage.getPayload());

		DXPEntityPubsubMessage.Attributes attributes =
			dxpEntityPubsubMessage.getAttributes();

		dxpEntity.classPK = dxpEntity.id;
		dxpEntity.dataSourceId = attributes.getDataSourceId();
		dxpEntity.projectId = attributes.getProjectId();
		dxpEntity.uploadDate = attributes.getUploadTime();
		dxpEntity.uploadType = attributes.getUploadType();

		if (StringUtils.equals(dxpEntity.type, _DXP_ENTITY_USER_TYPE)) {
			Set<String> suppressedEmailAddresses =
				attributes.getSuppressedEmailAddresses();

			String emailAddress = _getEmailAddress(dxpEntity.fields);

			if (suppressedEmailAddresses.contains(
					StringUtils.lowerCase(emailAddress))) {

				if (_logger.isInfoEnabled()) {
					_logger.info(
						"Skipping ingestion of suppressed user " +
							emailAddress);
				}

				return null;
			}
		}

		return dxpEntity;
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
		DXPEntityParserPTransform.class);

}