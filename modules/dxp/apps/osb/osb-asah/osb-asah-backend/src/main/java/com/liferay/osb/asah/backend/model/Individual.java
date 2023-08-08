/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author André Miranda
 */
@GraphQLType
public class Individual {

	public Individual() {
	}

	public Individual(String emailAddress, String id, String name) {
		_emailAddress = emailAddress;
		_id = id;
		_name = name;
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
		return _emailAddress;
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
		return _name;
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

	private Map<String, String> _custom = new HashMap<>();
	private Map<String, String> _demographics = new HashMap<>();
	private String _emailAddress;
	private String _id;
	private List<String> _individualSegmentIds;
	private String _name;

}