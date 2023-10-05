/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.bot.nanite.test;

import com.liferay.osb.asah.common.entity.BQExpandoValue;
import com.liferay.osb.asah.common.entity.BQOrganization;
import com.liferay.osb.asah.common.entity.BQUserGroup;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.repository.BQAccountEntryRepository;
import com.liferay.osb.asah.common.repository.BQAccountGroupRepository;
import com.liferay.osb.asah.common.repository.BQExpandoColumnRepository;
import com.liferay.osb.asah.common.repository.BQExpandoValueRepository;
import com.liferay.osb.asah.common.repository.BQGroupRepository;
import com.liferay.osb.asah.common.repository.BQOrganizationRepository;
import com.liferay.osb.asah.common.repository.BQRoleRepository;
import com.liferay.osb.asah.common.repository.BQTeamRepository;
import com.liferay.osb.asah.common.repository.BQUserGroupRepository;
import com.liferay.osb.asah.common.repository.BQUserRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.dataflow.emulator.bot.nanite.DXPEntitiesIngestionNanite;
import com.liferay.osb.asah.dataflow.emulator.bot.nanite.OSBAsahDataflowEmulatorSpringTestContext;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.IterableUtils;

import org.json.JSONArray;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcos Martins
 */
public class DXPEntitiesIngestionNaniteTest
	implements OSBAsahDataflowEmulatorSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testRun() throws Exception {
		JSONArray jsonArray = ResourceUtil.readResourceToJSONArray(
			"dependencies/dxp_entities1.json", this);

		for (int i = 0; i < jsonArray.length(); i++) {
			_dxpEntitiesIngestionNanite.processMessage(
				new HashMap<String, String>() {
					{
						put("dataSourceId", "1");
						put("projectId", "test");
					}
				},
				String.valueOf(jsonArray.getJSONObject(i)));
		}

		Assertions.assertEquals(1, _bqAccountEntryRepository.count());
		Assertions.assertEquals(1, _bqAccountGroupRepository.count());
		Assertions.assertEquals(2, _bqExpandoColumnRepository.count());
		Assertions.assertEquals(2, _bqExpandoValueRepository.count());
		Assertions.assertEquals(1, _bqGroupRepository.count());
		Assertions.assertEquals(1, _bqOrganizationRepository.count());
		Assertions.assertEquals(1, _bqRoleRepository.count());
		Assertions.assertEquals(1, _bqTeamRepository.count());
		Assertions.assertEquals(1, _bqUserGroupRepository.count());
		Assertions.assertEquals(1, _bqUserRepository.count());
	}

	@Test
	public void testRunAnalyticsDeleteMessage() throws Exception {
		BQUserGroup bqUserGroup = new BQUserGroup();

		bqUserGroup.setId(
			DigestUtils.sha256Hex(String.join("#", "test", "1", "123")));
		bqUserGroup.setName("test");

		_bqUserGroupRepository.insert(bqUserGroup);

		JSONArray jsonArray = ResourceUtil.readResourceToJSONArray(
			"dependencies/dxp_entities3.json", this);

		for (int i = 0; i < jsonArray.length(); i++) {
			_dxpEntitiesIngestionNanite.processMessage(
				new HashMap<String, String>() {
					{
						put("dataSourceId", "1");
						put("projectId", "test");
					}
				},
				String.valueOf(jsonArray.getJSONObject(i)));
		}

		Optional<BQUserGroup> bqUserGroupOptional =
			_bqUserGroupRepository.findById(
				DigestUtils.sha256Hex(String.join("#", "test", "1", "123")));

		Assertions.assertFalse(bqUserGroupOptional.isPresent());
	}

	@Disabled
	@Test
	public void testRunWithExistingDXPEntity() throws Exception {
		JSONArray jsonArray = ResourceUtil.readResourceToJSONArray(
			"dependencies/dxp_entities2.json", this);

		for (int i = 0; i < jsonArray.length(); i++) {
			_dxpEntitiesIngestionNanite.processMessage(
				new HashMap<String, String>() {
					{
						put("dataSourceId", "1");
						put("projectId", "test");
					}
				},
				String.valueOf(jsonArray.getJSONObject(i)));
		}

		BQOrganization bqOrganization = new BQOrganization();

		bqOrganization.setId(
			DigestUtils.sha256Hex(String.join("#", "test", "1", "123")));

		bqOrganization.setName("Test");

		BQExpandoValue bqExpandoValue = new BQExpandoValue();

		bqExpandoValue.setClassPK("123");
		bqExpandoValue.setColumnId("1");
		bqExpandoValue.setClassType(DXPEntity.Type.CLASS_NAME_ORGANIZATION);
		bqExpandoValue.setFieldName("custom_field_1");

		bqExpandoValue.setId(
			DigestUtils.sha256Hex(String.join("#", "test", "1", "1", "123")));

		bqExpandoValue.setValue("1234");

		_bqExpandoValueRepository.insert(bqExpandoValue);

		_bqOrganizationRepository.insert(bqOrganization);

		_dxpEntitiesIngestionNanite.run();

		Assertions.assertEquals(1, _bqOrganizationRepository.count());

		List<BQOrganization> bqOrganizations = IterableUtils.toList(
			_bqOrganizationRepository.findAll());

		Assertions.assertEquals(
			1, bqOrganizations.size(), bqOrganizations.toString());

		Assertions.assertEquals(1, _bqExpandoValueRepository.count());
	}

	@Autowired
	private BQAccountEntryRepository _bqAccountEntryRepository;

	@Autowired
	private BQAccountGroupRepository _bqAccountGroupRepository;

	@Autowired
	private BQExpandoColumnRepository _bqExpandoColumnRepository;

	@Autowired
	private BQExpandoValueRepository _bqExpandoValueRepository;

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

	@Autowired
	private DXPEntitiesIngestionNanite _dxpEntitiesIngestionNanite;

}