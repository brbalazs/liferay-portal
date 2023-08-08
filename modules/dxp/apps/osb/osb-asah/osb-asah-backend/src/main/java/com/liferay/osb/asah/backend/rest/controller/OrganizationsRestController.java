/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.backend.dto.TransformationDTO;
import com.liferay.osb.asah.common.dog.BQOrganizationDog;
import com.liferay.osb.asah.common.model.Transformation;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.MatcherUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Matthew Kong
 */
@RequestMapping("/organizations")
@RestController
public class OrganizationsRestController extends BaseRestController {

	@GetMapping(params = "apply")
	public PageDTO<TransformationDTO> getTransformationDTOPageDTO(
		@RequestParam String apply,
		@RequestParam(required = false) Long channelId,
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size) {

		Matcher matcher = MatcherUtil.getMatcher(apply);

		if (!matcher.matches()) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST, "Invalid apply string " + apply);
		}

		String groupByField = matcher.group("groupByField");

		Page<String> bqOrganizationFieldValuePage =
			_bqOrganizationDog.getBQOrganizationFieldValuePage(
				channelId, filterString, groupByField, page, size);

		List<Transformation> transformations = new ArrayList<>();

		for (String fieldValue : bqOrganizationFieldValuePage.getContent()) {
			Transformation transformation = new Transformation();

			transformation.setTerm(
				new Transformation.Term(
					Collections.singletonMap(groupByField, fieldValue)));
			transformation.setTotalElements(0);

			transformations.add(transformation);
		}

		return new PageDTO<>(
			"_embedded",
			new TransformationDTO(
				"organization-transformations", transformations),
			bqOrganizationFieldValuePage.getNumber(),
			bqOrganizationFieldValuePage.getSize(),
			bqOrganizationFieldValuePage.getTotalElements(),
			bqOrganizationFieldValuePage.getTotalPages());
	}

	@Autowired
	private BQOrganizationDog _bqOrganizationDog;

}