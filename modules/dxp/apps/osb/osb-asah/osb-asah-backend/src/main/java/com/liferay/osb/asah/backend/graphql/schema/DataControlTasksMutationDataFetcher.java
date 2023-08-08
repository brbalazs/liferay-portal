/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.graphql.schema;

import com.liferay.osb.asah.common.dog.AuditEventDog;
import com.liferay.osb.asah.common.dog.DataControlTaskDog;
import com.liferay.osb.asah.common.graphql.GraphQLTypeWiring;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
@GraphQLTypeWiring(fieldName = "dataControlTasks", typeName = "MutationType")
public class DataControlTasksMutationDataFetcher
	implements DataFetcher<Boolean> {

	@Override
	public Boolean get(DataFetchingEnvironment dataFetchingEnvironment) {
		return _dataControlTaskDog.addDataControlTasks(
			dataFetchingEnvironment.getArgument("emailAddresses"),
			_getPath(dataFetchingEnvironment.getArgument("fileName")),
			dataFetchingEnvironment.getArgument("ownerId"),
			dataFetchingEnvironment.getArgument("types"),
			dataFetchingEnvironment.getArgument("userId"),
			dataFetchingEnvironment.getArgument("userName"));
	}

	private Path _getPath(String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return null;
		}

		Path path = Paths.get(_tempPathName, FilenameUtils.getName(fileName));

		path = path.normalize();

		if (!path.startsWith(_tempPathName)) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST, "Invalid file name: " + fileName);
		}

		return path;
	}

	@Autowired
	private AuditEventDog _auditEventDog;

	@Autowired
	private DataControlTaskDog _dataControlTaskDog;

	@Value("${osb.asah.backend.temp.path:/temp}")
	private String _tempPathName;

}