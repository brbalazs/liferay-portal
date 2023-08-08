/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.model.Field;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rachael Koestartyo
 */
@GraphQLType("individual")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("individuals")
public class ReportIndividualDTO {

	public ReportIndividualDTO() {
	}

	public ReportIndividualDTO(Individual individual) {
		_custom = _getIndividualProperties(individual.getCustomDemographics());
		_demographics = _getIndividualProperties(individual.getDemographics());
		_id = StringUtil.get(individual.getId());
		_individualSegmentIds = ListUtil.map(
			individual.getSegmentIds(), String::valueOf);
	}

	public Map<String, String> getCustom() {
		return _custom;
	}

	public Map<String, String> getDemographics() {
		return _demographics;
	}

	@GraphQLProperty("email")
	@JsonIgnore
	public String getEmailAddress() {
		return _demographics.get("email");
	}

	public String getId() {
		return _id;
	}

	@JsonIgnore
	public List<String> getIndividualSegmentIds() {
		return _individualSegmentIds;
	}

	@JsonIgnore
	public String getName() {
		return _demographics.get("name");
	}

	public void setCustom(Map<String, String> custom) {
		_custom = custom;
	}

	public void setDemographics(Map<String, String> demographics) {
		_demographics = demographics;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setIndividualSegmentIds(List<String> individualSegmentIds) {
		_individualSegmentIds = individualSegmentIds;
	}

	private Map<String, String> _getIndividualProperties(
		Individual.Demographics demographics) {

		if (demographics == null) {
			return Collections.emptyMap();
		}

		Map<String, String> individualProperties = new HashMap<>();

		for (Field field : demographics.getFields()) {
			individualProperties.put(
				field.getName(), String.valueOf(field.getValue()));
		}

		return individualProperties;
	}

	private Map<String, String> _custom = new HashMap<>();
	private Map<String, String> _demographics = new HashMap<>();
	private String _id;
	private List<String> _individualSegmentIds;

}