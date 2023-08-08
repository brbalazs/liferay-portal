/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

/**
 * @author Marcellus Tavares
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("interest-terms")
public class InterestTopicDTO {

	public InterestTopicDTO() {
	}

	public InterestTopicDTO(List<String> interestTerms) {
		_interestTerms = interestTerms;
	}

	public InterestTopicDTO(String interestTerm) {
		_interestTerm = interestTerm;
	}

	@JsonProperty("term")
	public String getInterestTerm() {
		return _interestTerm;
	}

	@JsonProperty("interest-terms")
	public List<String> getInterestTerms() {
		return _interestTerms;
	}

	public void setInterestTerm(String interestTerm) {
		_interestTerm = interestTerm;
	}

	private String _interestTerm;
	private List<String> _interestTerms;

}