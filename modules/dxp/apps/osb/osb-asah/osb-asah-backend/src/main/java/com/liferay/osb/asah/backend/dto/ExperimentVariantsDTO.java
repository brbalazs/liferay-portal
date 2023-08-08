/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.entity.ExperimentVariant;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

/**
 * @author Marcos Martins
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperimentVariantsDTO {

	public ExperimentVariantsDTO() {
	}

	public ExperimentVariantsDTO(Set<ExperimentVariant> experimentVariants) {
		SetUtil.map(experimentVariants, ExperimentVariantDTO::new);
	}

	@JsonProperty("dxpVariants")
	@Valid
	public Set<ExperimentVariantDTO> getExperimentVariantDTOs() {
		return _experimentVariantDTOs;
	}

	public void setExperimentVariantDTOs(
		Set<ExperimentVariantDTO> experimentVariantDTOs) {

		_experimentVariantDTOs = experimentVariantDTOs;
	}

	private Set<ExperimentVariantDTO> _experimentVariantDTOs = new HashSet<>();

}