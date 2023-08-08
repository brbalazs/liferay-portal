/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller;

import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.backend.dto.SegmentDTO;
import com.liferay.osb.asah.common.findbugs.SuppressFBWarnings;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vishal Reddy
 * @author David Bhasme
 * @author Rachael Koestartyo
 */
@RequestMapping("/individual-segments")
@RestController(
	"com.liferay.osb.asah.backend.rest.controller.IndividualSegmentsRestController"
)
@SuppressFBWarnings("NM_SAME_SIMPLE_NAME_AS_SUPERCLASS")
public class IndividualSegmentsRestController
	extends com.liferay.osb.asah.backend.rest.controller.api.data.source.v1.
				IndividualSegmentsRestController {

	@PutMapping("/{id}/channel/{channelId}")
	public void assignChannel(
			@PathVariable Long channelId, @PathVariable Long id)
		throws Exception {

		segmentDog.assignChannel(channelId, id);
	}

	@DeleteMapping("/{id}/memberships/{individualId}")
	public void deleteIndividual(
		@PathVariable Long id, @PathVariable String individualId) {

		bqMembershipDog.deleteBQMembership(
			individualId, segmentDog.getSegment(id));
	}

	@DeleteMapping("/{id}")
	public void deleteIndividualSegment(@PathVariable Long id) {
		segmentDog.deleteSegment(id);
	}

	@GetMapping("/preview-disabled-segments")
	public PageDTO<SegmentDTO> getPreviewDisabledSegmentDTOPageDTO(
		@RequestParam Long dataSourceId,
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts) {

		return toSegmentDTOPageDTO(
			segmentDog.searchPreviewDisabledSegmentPage(
				dataSourceId, filterString, page, Math.max(1, size), sorts));
	}

}