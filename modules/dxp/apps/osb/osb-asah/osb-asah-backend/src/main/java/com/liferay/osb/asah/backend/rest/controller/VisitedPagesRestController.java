/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.common.dog.BQIdentityInterestPageDog;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eddie Olson
 */
@RequestMapping("/pages-visited") // TODO Rename
@RestController
public class VisitedPagesRestController extends BaseRestController {

	@GetMapping
	public String getVisitedPages(
		@RequestParam(required = false) Long channelId,
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam String ownerId, @RequestParam String ownerType,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts,
		@RequestParam(defaultValue = "true") boolean visitedPages) {

		if (!ownerType.equals("individual") &&
			!ownerType.equals("individual-segment")) {

			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST, "Invalid ownerType " + ownerType);
		}

		List<Map<String, Object>> pagesTransformations;
		long totalElements;

		if (visitedPages) {
			pagesTransformations =
				_bqIdentityInterestPageDog.getActivePagesTransformations(
					channelId, filterString, ownerId, ownerType, page, size,
					sorts);
			totalElements = _bqIdentityInterestPageDog.countActivePages(
				channelId, filterString, ownerId, ownerType);
		}
		else {
			pagesTransformations =
				_bqIdentityInterestPageDog.getInactivePagesTransformations(
					channelId, filterString, ownerId, ownerType, page, size,
					sorts);
			totalElements = _bqIdentityInterestPageDog.countInactivePages(
				channelId, filterString, ownerId, ownerType);
		}

		return JSONUtil.put(
			"_embedded",
			JSONUtil.put(
				"visited-pages-transformation",
				new JSONArray(pagesTransformations))
		).put(
			"page", getPageJSONObject(page, size, totalElements)
		).toString();
	}

	@GetMapping("/{id}")
	public String getVisitedPages(@PathVariable String id) {
		throw new OSBAsahException(
			HttpStatus.BAD_REQUEST, "Unable to process request");
	}

	@Autowired
	private BQIdentityInterestPageDog _bqIdentityInterestPageDog;

}