/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.bot.nanite.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.entity.BQIndividual;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.BQIndividualRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.dataflow.emulator.bot.nanite.DXPEntitiesIngestionNanite;
import com.liferay.osb.asah.dataflow.emulator.bot.nanite.IndividualNanite;
import com.liferay.osb.asah.dataflow.emulator.bot.nanite.OSBAsahDataflowEmulatorSpringTestContext;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.codec.digest.DigestUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcellus Tavares
 */
public class IndividualNaniteTest
	implements OSBAsahDataflowEmulatorSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(
		resourcePath = "test_merge_custom_field_name_different_type.sql"
	)
	@Test
	public void testMergeCustomFieldNameDifferentType() throws Exception {
		JSONArray jsonArray = ResourceUtil.readResourceToJSONArray(
			"dependencies/dxp_entities4.json", this);

		for (int i = 0; i < jsonArray.length(); i++) {
			_dxpEntitiesIngestionNanite.process(
				new HashMap<String, String>() {
					{
						put("dataSourceId", "1");
						put("projectId", "test");
					}
				},
				String.valueOf(jsonArray.getJSONObject(i)));
		}

		_individualNanite.run();

		Optional<BQIndividual> bqIndividualOptional =
			_bqIndividualRepository.findByEmailAddress("joe@liferay.com");

		Assertions.assertTrue(bqIndividualOptional.isPresent());

		BQIndividual bqIndividual = bqIndividualOptional.get();

		Assertions.assertEquals(
			"joe@liferay.com", bqIndividual.getEmailAddress());

		Map<String, JSONObject> jsonObjectMap = JSONUtil.toJSONObjectMap(
			_objectMapper.convertValue(
				bqIndividual.getFields(), JSONArray.class),
			"name");

		JSONObject jsonObject = jsonObjectMap.get("age");

		Assertions.assertEquals("[18]", jsonObject.getString("value"));

		jsonObject = jsonObjectMap.get("age_1");

		Assertions.assertEquals("20", jsonObject.getString("value"));
	}

	@BQSQLResource(resourcePath = "test_merge_field.sql")
	@Test
	public void testMergeCustomFieldNameSameType() throws Exception {
		JSONArray jsonArray = ResourceUtil.readResourceToJSONArray(
			"dependencies/dxp_entities5.json", this);

		for (int i = 0; i < jsonArray.length(); i++) {
			_dxpEntitiesIngestionNanite.process(
				new HashMap<String, String>() {
					{
						put("dataSourceId", "1");
						put("projectId", "test");
					}
				},
				String.valueOf(jsonArray.getJSONObject(i)));
		}

		_individualNanite.run();

		Optional<BQIndividual> bqIndividualOptional =
			_bqIndividualRepository.findByEmailAddress("joe@liferay.com");

		BQIndividual bqIndividual = bqIndividualOptional.get();

		Assertions.assertEquals(
			"joe@liferay.com", bqIndividual.getEmailAddress());

		Map<String, JSONObject> jsonObjectMap = JSONUtil.toJSONObjectMap(
			_objectMapper.convertValue(
				bqIndividual.getFields(), JSONArray.class),
			"name");

		JSONObject jsonObject = jsonObjectMap.get("age");

		Assertions.assertEquals("20", jsonObject.getString("value"));
	}

	@BQSQLResource(resourcePath = "test_merge_field.sql")
	@Test
	public void testMergeDefaultField() throws Exception {
		JSONArray jsonArray = ResourceUtil.readResourceToJSONArray(
			"dependencies/dxp_entities6.json", this);

		for (int i = 0; i < jsonArray.length(); i++) {
			_dxpEntitiesIngestionNanite.process(
				new HashMap<String, String>() {
					{
						put("dataSourceId", "1");
						put("projectId", "test");
					}
				},
				String.valueOf(jsonArray.getJSONObject(i)));
		}

		_individualNanite.run();

		Optional<BQIndividual> bqIndividualOptional =
			_bqIndividualRepository.findByEmailAddress("joe@liferay.com");

		BQIndividual bqIndividual = bqIndividualOptional.get();

		Assertions.assertEquals(
			"joe@liferay.com", bqIndividual.getEmailAddress());
		Assertions.assertEquals("Joseph", bqIndividual.getFirstName());

		Map<String, JSONObject> jsonObjectMap = JSONUtil.toJSONObjectMap(
			_objectMapper.convertValue(
				bqIndividual.getFields(), JSONArray.class),
			"name");

		JSONObject jsonObject = jsonObjectMap.get("country");

		Assertions.assertEquals("Brazil", jsonObject.getString("value"));

		jsonObject = jsonObjectMap.get("company");

		Assertions.assertEquals("Liferay", jsonObject.getString("value"));
	}

	@BQSQLResource(resourcePath = "test_merge_field.sql")
	@Test
	public void testMergeMemberships() throws Exception {
		JSONArray jsonArray = ResourceUtil.readResourceToJSONArray(
			"dependencies/dxp_entities6.json", this);

		for (int i = 0; i < jsonArray.length(); i++) {
			_dxpEntitiesIngestionNanite.process(
				new HashMap<String, String>() {
					{
						put("dataSourceId", "1");
						put("projectId", "test");
					}
				},
				String.valueOf(jsonArray.getJSONObject(i)));
		}

		_individualNanite.run();

		Optional<BQIndividual> bqIndividualOptional =
			_bqIndividualRepository.findByEmailAddress("joe@liferay.com");

		BQIndividual bqIndividual = bqIndividualOptional.get();

		Map<String, List<String>> memberships = Stream.of(
			bqIndividual.getMemberships()
		).flatMap(
			List::stream
		).collect(
			HashMap::new,
			(map, membership) -> map.put(
				membership.getName(), membership.getIds()),
			Map::putAll
		);

		List<String> groupIds = memberships.get("groupIds");

		Assertions.assertEquals(2, groupIds.size());
		Assertions.assertTrue(
			groupIds.contains(DigestUtils.sha256Hex("test#1#12")));
		Assertions.assertTrue(
			groupIds.contains(DigestUtils.sha256Hex("test#1#23")));
	}

	@Autowired
	private BQIndividualRepository _bqIndividualRepository;

	@Autowired
	private DXPEntitiesIngestionNanite _dxpEntitiesIngestionNanite;

	@Autowired
	private IndividualNanite _individualNanite;

	@Autowired
	private ObjectMapper _objectMapper;

}