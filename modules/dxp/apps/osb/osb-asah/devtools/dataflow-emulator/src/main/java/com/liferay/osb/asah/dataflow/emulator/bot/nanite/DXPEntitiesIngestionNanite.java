/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.bot.nanite;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.entity.BQAccountEntry;
import com.liferay.osb.asah.common.entity.BQAccountGroup;
import com.liferay.osb.asah.common.entity.BQExpandoColumn;
import com.liferay.osb.asah.common.entity.BQExpandoValue;
import com.liferay.osb.asah.common.entity.BQFieldMapping;
import com.liferay.osb.asah.common.entity.BQGroup;
import com.liferay.osb.asah.common.entity.BQOrganization;
import com.liferay.osb.asah.common.entity.BQRole;
import com.liferay.osb.asah.common.entity.BQTeam;
import com.liferay.osb.asah.common.entity.BQUser;
import com.liferay.osb.asah.common.entity.BQUserGroup;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.Suppression;
import com.liferay.osb.asah.common.repository.BQAccountEntryRepository;
import com.liferay.osb.asah.common.repository.BQAccountGroupRepository;
import com.liferay.osb.asah.common.repository.BQExpandoColumnRepository;
import com.liferay.osb.asah.common.repository.BQExpandoValueRepository;
import com.liferay.osb.asah.common.repository.BQFieldMappingRepository;
import com.liferay.osb.asah.common.repository.BQGroupRepository;
import com.liferay.osb.asah.common.repository.BQOrganizationRepository;
import com.liferay.osb.asah.common.repository.BQRoleRepository;
import com.liferay.osb.asah.common.repository.BQTeamRepository;
import com.liferay.osb.asah.common.repository.BQUserGroupRepository;
import com.liferay.osb.asah.common.repository.BQUserRepository;
import com.liferay.osb.asah.common.repository.SuppressionRepository;
import com.liferay.osb.asah.common.util.MapUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.dataflow.emulator.model.AnalyticsDeleteMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipInputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Marcos Martins
 */
@Component
public class DXPEntitiesIngestionNanite {

	public void process(Map<String, String> attributes, String dxpEntityJSON) {
		Long dataSourceId = MapUtil.getLong(attributes, "dataSourceId");

		String projectId = MapUtil.getString(attributes, "projectId");

		ProjectIdThreadLocal.setProjectId(projectId);

		JSONObject jsonObject = new JSONObject(dxpEntityJSON);

		String type = jsonObject.getString("type");

		Map<String, Object> fields = _parseFields(
			jsonObject.getJSONArray("fields"));

		fields.put("modifiedDate", jsonObject.getString("modifiedDate"));

		if (StringUtils.equals(
				type, "com.liferay.account.model.AccountEntry")) {

			BQAccountEntry accountEntry = _objectMapper.convertValue(
				fields, BQAccountEntry.class);

			accountEntry.setDataSourceId(dataSourceId);
			accountEntry.setId(
				_generateDXPEntityId(
					accountEntry.getAccountEntryId(), dataSourceId, projectId));

			_bqAccountEntryRepository.deleteById(accountEntry.getId());

			_bqAccountEntryRepository.insert(accountEntry);
		}
		else if (StringUtils.equals(
					type, "com.liferay.account.model.AccountGroup")) {

			BQAccountGroup bqAccountGroup = _objectMapper.convertValue(
				fields, BQAccountGroup.class);

			bqAccountGroup.setDataSourceId(dataSourceId);
			bqAccountGroup.setId(
				_generateDXPEntityId(
					bqAccountGroup.getAccountGroupId(), dataSourceId,
					projectId));

			_bqAccountGroupRepository.deleteById(bqAccountGroup.getId());

			_bqAccountGroupRepository.insert(bqAccountGroup);
		}
		else if (StringUtils.equals(
					type,
					"com.liferay.analytics.message.storage.model." +
						"AnalyticsDeleteMessage")) {

			try {
				AnalyticsDeleteMessage analyticsDeleteMessage =
					_objectMapper.convertValue(
						fields, AnalyticsDeleteMessage.class);

				String dxpEntityId = _generateDXPEntityId(
					analyticsDeleteMessage.getClassPK(), dataSourceId,
					projectId);

				_processDeleteMessage(
					analyticsDeleteMessage.getClassName(), dxpEntityId);
			}
			catch (Exception exception) {
				_log.error(exception, exception);
			}
		}
		else if (StringUtils.equals(
					type, "com.liferay.expando.kernel.model.ExpandoColumn")) {

			BQExpandoColumn bqExpandoColumn = _objectMapper.convertValue(
				fields, BQExpandoColumn.class);

			bqExpandoColumn.setDataSourceId(dataSourceId);
			bqExpandoColumn.setId(
				_generateDXPEntityId(
					bqExpandoColumn.getColumnId(), dataSourceId, projectId));
			bqExpandoColumn.setName(
				StringUtils.substringBeforeLast(
					bqExpandoColumn.getName(), "-"));

			_bqExpandoColumnRepository.deleteById(bqExpandoColumn.getId());

			_bqExpandoColumnRepository.insert(bqExpandoColumn);
		}
		else if (StringUtils.equals(
					type, "com.liferay.portal.kernel.model.Group")) {

			BQGroup bqGroup = _objectMapper.convertValue(fields, BQGroup.class);

			bqGroup.setDataSourceId(dataSourceId);
			bqGroup.setId(
				_generateDXPEntityId(
					bqGroup.getGroupId(), dataSourceId, projectId));

			_bqGroupRepository.deleteById(bqGroup.getId());

			_bqGroupRepository.insert(bqGroup);
		}
		else if (StringUtils.equals(
					type, "com.liferay.portal.kernel.model.Organization")) {

			BQOrganization bqOrganization = _objectMapper.convertValue(
				fields, BQOrganization.class);

			bqOrganization.setDataSourceId(dataSourceId);

			JSONArray expandoFieldsJSONArray = jsonObject.optJSONArray(
				"expandoFields");

			if (expandoFieldsJSONArray != null) {
				Set<BQExpandoValue> bqExpandoValues = _getExpandoValues(
					bqOrganization.getOrganizationId(),
					DXPEntity.Type.CLASS_NAME_ORGANIZATION, dataSourceId,
					expandoFieldsJSONArray, bqOrganization.getModifiedDate(),
					projectId);

				bqExpandoValues.forEach(_bqExpandoValueRepository::insert);
			}

			bqOrganization.setId(
				_generateDXPEntityId(
					bqOrganization.getOrganizationId(), dataSourceId,
					projectId));

			_bqOrganizationRepository.deleteById(bqOrganization.getId());

			_bqOrganizationRepository.insert(bqOrganization);
		}
		else if (StringUtils.equals(
					type, "com.liferay.portal.kernel.model.Role")) {

			BQRole bqRole = _objectMapper.convertValue(fields, BQRole.class);

			bqRole.setDataSourceId(dataSourceId);
			bqRole.setId(
				_generateDXPEntityId(
					bqRole.getRoleId(), dataSourceId, projectId));

			_bqRoleRepository.deleteById(bqRole.getId());

			_bqRoleRepository.insert(bqRole);
		}
		else if (StringUtils.equals(
					type, "com.liferay.portal.kernel.model.Team")) {

			BQTeam bqTeam = _objectMapper.convertValue(fields, BQTeam.class);

			bqTeam.setDataSourceId(dataSourceId);
			bqTeam.setId(
				_generateDXPEntityId(
					bqTeam.getTeamId(), dataSourceId, projectId));

			_bqTeamRepository.deleteById(bqTeam.getId());

			_bqTeamRepository.insert(bqTeam);
		}
		else if (StringUtils.equals(
					type, "com.liferay.portal.kernel.model.User")) {

			Set<String> suppressedEmailAddresses =
				_getSuppressedEmailAddresses();

			BQUser bqUser = _objectMapper.convertValue(fields, BQUser.class);

			if (suppressedEmailAddresses.contains(
					StringUtils.lowerCase(bqUser.getEmailAddress()))) {

				if (_log.isInfoEnabled()) {
					_log.info(
						"Skipping ingestion of suppressed user " +
							bqUser.getEmailAddress());
				}

				return;
			}

			bqUser.setDataSourceId(dataSourceId);

			JSONArray expandoFieldsJSONArray = jsonObject.optJSONArray(
				"expandoFields");

			if (expandoFieldsJSONArray != null) {
				Set<BQExpandoValue> bqExpandoValues = _getExpandoValues(
					bqUser.getDXPUserId(), DXPEntity.Type.CLASS_NAME_USER,
					dataSourceId, expandoFieldsJSONArray,
					bqUser.getModifiedDate(), projectId);

				bqExpandoValues.forEach(_bqExpandoValueRepository::insert);
			}

			bqUser.setFields(_toFields(jsonObject.optJSONArray("fields")));

			bqUser.setId(
				_generateDXPEntityId(
					bqUser.getDXPUserId(), dataSourceId, projectId));
			bqUser.setIndividualId(
				DigestUtils.sha256Hex(
					StringUtils.lowerCase(bqUser.getEmailAddress())));

			_bqUserRepository.deleteById(bqUser.getId());

			_bqUserRepository.insert(bqUser);
		}
		else if (StringUtils.equals(
					type, "com.liferay.portal.kernel.model.UserGroup")) {

			BQUserGroup bqUserGroup = _objectMapper.convertValue(
				fields, BQUserGroup.class);

			bqUserGroup.setDataSourceId(dataSourceId);
			bqUserGroup.setId(
				_generateDXPEntityId(
					bqUserGroup.getUserGroupId(), dataSourceId, projectId));

			_bqUserGroupRepository.deleteById(bqUserGroup.getId());

			_bqUserGroupRepository.insert(bqUserGroup);
		}
	}

	public void run() {
		String pathName = _dxpBatchEntitiesStoragePath + "/" +
			ProjectIdThreadLocal.getProjectId();

		File file = new File(pathName);

		file.mkdirs();

		try {
			Files.walkFileTree(
				Paths.get(pathName),
				new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(
							Path path, BasicFileAttributes basicFileAttributes)
						throws IOException {

						File file = path.toFile();

						if (StringUtils.contains(file.getName(), ".zip") &&
							basicFileAttributes.isRegularFile() &&
							(basicFileAttributes.size() > 0)) {

							_processFile(file);
						}

						file.delete();

						return FileVisitResult.CONTINUE;
					}

				});
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}
	}

	private String _generateBQExpandoValueId(
		String columnId, Long classPK, Long dataSourceId, String projectId) {

		return DigestUtils.sha256Hex(
			String.join(
				"#", projectId, String.valueOf(dataSourceId), columnId,
				String.valueOf(classPK)));
	}

	private String _generateDXPEntityId(
		Object classPK, Long dataSourceId, String projectId) {

		return DigestUtils.sha256Hex(
			String.join(
				"#", projectId, String.valueOf(dataSourceId),
				String.valueOf(classPK)));
	}

	private Map<String, String> _getAttributes(String path) {
		Map<String, String> attributes = new HashMap<>();

		String[] split = path.split("/");

		int length = split.length;

		attributes.put("dataSourceId", split[length - 4]);
		attributes.put("projectId", split[length - 5]);

		return attributes;
	}

	private Set<BQExpandoValue> _getExpandoValues(
		Long classPK, String classType, Long dataSourceId,
		JSONArray expandoFieldsJSONArray, Date modifiedDate, String projectId) {

		Set<BQExpandoValue> bqExpandoValues = new HashSet<>();

		expandoFieldsJSONArray.forEach(
			object -> {
				JSONObject jsonObject = (JSONObject)object;

				BQExpandoValue bqExpandoValue = new BQExpandoValue();

				bqExpandoValue.setClassPK(String.valueOf(classPK));
				bqExpandoValue.setClassType(classType);
				bqExpandoValue.setColumnId(
					String.valueOf(jsonObject.get("columnId")));
				bqExpandoValue.setDataSourceId(dataSourceId);
				bqExpandoValue.setFieldName(
					_getFieldName(
						String.valueOf(jsonObject.get("columnId")),
						dataSourceId, jsonObject.optString("name")));
				bqExpandoValue.setId(
					_generateBQExpandoValueId(
						String.valueOf(jsonObject.get("columnId")), classPK,
						dataSourceId, projectId));
				bqExpandoValue.setModifiedDate(modifiedDate);
				bqExpandoValue.setValue(jsonObject.optString("value"));

				bqExpandoValues.add(bqExpandoValue);
			});

		return bqExpandoValues;
	}

	private String _getFieldName(
		String columnId, Long dataSourceId, String fieldName) {

		Optional<BQExpandoColumn> bqExpandoColumnOptional =
			_bqExpandoColumnRepository.findByColumnIdAndDataSourceId(
				columnId, dataSourceId);

		if (bqExpandoColumnOptional.isPresent()) {
			BQExpandoColumn bqExpandoColumn = bqExpandoColumnOptional.get();

			Optional<BQFieldMapping> bqFieldMappingOptional =
				_bqFieldMappingRepository.findByDisplayNameAndFieldType(
					bqExpandoColumn.getName(), bqExpandoColumn.getDataType());

			if (bqFieldMappingOptional.isPresent()) {
				BQFieldMapping bqFieldMapping = bqFieldMappingOptional.get();

				return bqFieldMapping.getFieldName();
			}
		}

		if (NumberUtils.isCreatable(columnId)) {
			return fieldName;
		}

		int index = columnId.lastIndexOf("-");

		if (index > 0) {
			columnId = columnId.substring(0, index);
		}

		return columnId.replaceAll("[\\W]", "_");
	}

	private Set<String> _getSuppressedEmailAddresses() {
		List<Suppression> suppressions = _suppressionRepository.findAll();

		Stream<Suppression> stream = suppressions.stream();

		return stream.map(
			Suppression::getEmailAddress
		).collect(
			Collectors.toSet()
		);
	}

	private Map<String, Object> _parseFields(JSONArray fieldsJSONArray) {
		Map<String, Object> fields = new HashMap<>();

		fieldsJSONArray.forEach(
			object -> {
				JSONObject jsonObject = (JSONObject)object;

				fields.put(
					jsonObject.getString("name"), jsonObject.opt("value"));
			});

		return fields;
	}

	private void _processDeleteMessage(String className, String classPK) {
		String entityName = StringUtils.substringAfterLast(className, ".");

		if (Objects.equals(entityName, "AccountEntry")) {
			_bqAccountEntryRepository.deleteById(classPK);
		}
		else if (Objects.equals(entityName, "AccountGroup")) {
			_bqAccountGroupRepository.deleteById(classPK);
		}
		else if (Objects.equals(entityName, "ExpandoColumn")) {
			_bqExpandoColumnRepository.deleteById(classPK);
		}
		else if (Objects.equals(entityName, "ExpandoValue")) {
			_bqExpandoValueRepository.deleteById(classPK);
		}
		else if (Objects.equals(entityName, "Group")) {
			_bqGroupRepository.deleteById(classPK);
		}
		else if (Objects.equals(entityName, "Organization")) {
			_bqOrganizationRepository.deleteById(classPK);
		}
		else if (Objects.equals(entityName, "Role")) {
			_bqRoleRepository.deleteById(classPK);
		}
		else if (Objects.equals(entityName, "Team")) {
			_bqTeamRepository.deleteById(classPK);
		}
		else if (Objects.equals(entityName, "User")) {
			_bqUserRepository.deleteById(classPK);
		}
		else if (Objects.equals(entityName, "UserGroup")) {
			_bqUserGroupRepository.deleteById(classPK);
		}
		else {
			throw new IllegalStateException("Unsupported entity " + entityName);
		}
	}

	private void _processFile(File file) throws IOException {
		String absolutePath = file.getAbsolutePath();

		if (_log.isInfoEnabled()) {
			_log.info("Processing file " + absolutePath);
		}

		Map<String, String> attributes = _getAttributes(absolutePath);

		ZipInputStream zipInputStream = new ZipInputStream(
			new FileInputStream(file));

		zipInputStream.getNextEntry();

		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(
					zipInputStream, StandardCharsets.UTF_8))) {

			process(attributes, bufferedReader.readLine());
		}
	}

	private List<BQUser.Field> _toFields(JSONArray jsonArray) {
		if (jsonArray == null) {
			return Collections.emptyList();
		}

		List<BQUser.Field> fields = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			fields.add(
				new BQUser.Field(
					jsonObject.getString("name"),
					String.valueOf(jsonObject.get("value"))));
		}

		return fields;
	}

	private static final Log _log = LogFactory.getLog(
		DXPEntitiesIngestionNanite.class);

	@Autowired
	private BQAccountEntryRepository _bqAccountEntryRepository;

	@Autowired
	private BQAccountGroupRepository _bqAccountGroupRepository;

	@Autowired
	private BQExpandoColumnRepository _bqExpandoColumnRepository;

	@Autowired
	private BQExpandoValueRepository _bqExpandoValueRepository;

	@Autowired
	private BQFieldMappingRepository _bqFieldMappingRepository;

	@Autowired
	private BQGroupRepository _bqGroupRepository;

	@Autowired
	private BQOrganizationRepository _bqOrganizationRepository;

	@Autowired
	private BQRoleRepository _bqRoleRepository;

	@Autowired
	private BQTeamRepository _bqTeamRepository;

	@Autowired
	private BQUserGroupRepository _bqUserGroupRepository;

	@Autowired
	private BQUserRepository _bqUserRepository;

	@Value("${osb.asah.dxp.batch.entities.storage.path:/storage}")
	private String _dxpBatchEntitiesStoragePath;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private SuppressionRepository _suppressionRepository;

}