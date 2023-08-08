/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.backend.dto.BQFieldMappingDTO;
import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.common.dog.BQFieldMappingDog;
import com.liferay.osb.asah.common.entity.BQFieldMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vishal Reddy
 * @author David Bhasme
 */
@RequestMapping("/field-mappings")
@RestController
public class FieldMappingsRestController extends BaseRestController {

	@GetMapping("/{id}")
	public BQFieldMappingDTO getBQFieldMappingDTO(
		@PathVariable(name = "id") String fieldName) {

		return new BQFieldMappingDTO(
			_bqFieldMappingDog.getBQFieldMapping(fieldName));
	}

	@GetMapping(params = "!apply")
	public PageDTO<BQFieldMappingDTO> getBQFieldMappingDTOPageDTO(
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts) {

		return _toPageDTO(
			_bqFieldMappingDog.searchBQFieldMappingPage(
				filterString, page, size, sorts));
	}

	private PageDTO<BQFieldMappingDTO> _toPageDTO(
		Page<BQFieldMapping> bqFieldMappingPage) {

		return new PageDTO<>(
			"_embedded", new BQFieldMappingDTO(bqFieldMappingPage.getContent()),
			bqFieldMappingPage.getNumber(), bqFieldMappingPage.getSize(),
			bqFieldMappingPage.getTotalElements(),
			bqFieldMappingPage.getTotalPages());
	}

	@Autowired
	private BQFieldMappingDog _bqFieldMappingDog;

}