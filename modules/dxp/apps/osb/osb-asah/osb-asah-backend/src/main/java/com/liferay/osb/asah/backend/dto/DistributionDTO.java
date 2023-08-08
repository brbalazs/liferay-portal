/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.model.Distribution;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Rachael Koestartyo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistributionDTO {

	public DistributionDTO(Distribution distribution) {
		_count = distribution.getCount();
		_values = distribution.getValues();
	}

	public DistributionDTO(
		List<Distribution> distributions, String transformationKey) {

		_distributionDTOsMap = Collections.singletonMap(
			transformationKey,
			SetUtil.map(distributions, DistributionDTO::new));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DistributionDTO)) {
			return false;
		}

		DistributionDTO distributionDTO = (DistributionDTO)obj;

		if (Objects.equals(_count, distributionDTO._count) &&
			Objects.equals(_values, distributionDTO._values)) {

			return true;
		}

		return false;
	}

	@JsonProperty("count")
	public Integer getCount() {
		return _count;
	}

	@JsonAnyGetter
	public Map<String, Set<DistributionDTO>> getDistributionDTOsMap() {
		return _distributionDTOsMap;
	}

	@JsonProperty("values")
	public List<Object> getValues() {
		return _values;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_count, _values);
	}

	public void setCount(Integer count) {
		_count = count;
	}

	public void setValues(List<Object> values) {
		_values = values;
	}

	private Integer _count;
	private Map<String, Set<DistributionDTO>> _distributionDTOsMap =
		new HashMap<>();
	private List<Object> _values;

}