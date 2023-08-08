/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.api.data.source.v1;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.backend.dto.BQAssetDTO;
import com.liferay.osb.asah.backend.dto.BQMembershipChangeDTO;
import com.liferay.osb.asah.backend.dto.BQMembershipDTO;
import com.liferay.osb.asah.backend.dto.IndividualDTO;
import com.liferay.osb.asah.backend.dto.PageDTO;
import com.liferay.osb.asah.backend.dto.SegmentDTO;
import com.liferay.osb.asah.backend.dto.TransformationDTO;
import com.liferay.osb.asah.backend.rest.controller.BaseRestController;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.BQAssetDog;
import com.liferay.osb.asah.common.dog.BQFieldMappingDog;
import com.liferay.osb.asah.common.dog.BQGroupDog;
import com.liferay.osb.asah.common.dog.BQIdentityDog;
import com.liferay.osb.asah.common.dog.BQIndividualDog;
import com.liferay.osb.asah.common.dog.BQMembershipChangeDog;
import com.liferay.osb.asah.common.dog.BQMembershipDog;
import com.liferay.osb.asah.common.dog.BQMembershipIndividualDog;
import com.liferay.osb.asah.common.dog.BQOrganizationDog;
import com.liferay.osb.asah.common.dog.BQRoleDog;
import com.liferay.osb.asah.common.dog.BQTeamDog;
import com.liferay.osb.asah.common.dog.BQUserDog;
import com.liferay.osb.asah.common.dog.BQUserGroupDog;
import com.liferay.osb.asah.common.dog.SegmentDog;
import com.liferay.osb.asah.common.entity.BQDataSourceUser;
import com.liferay.osb.asah.common.entity.BQMembership;
import com.liferay.osb.asah.common.entity.BQMembershipChange;
import com.liferay.osb.asah.common.entity.BQMembershipIndividual;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.model.Transformation;
import com.liferay.osb.asah.common.spring.annotation.Cacheable;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vishal Reddy
 * @author David Bhasme
 * @author Rachael Koestartyo
 */
@RequestMapping(
	produces = "application/json", value = "/api/1.0/individual-segments"
)
@RestController(
	"com.liferay.osb.asah.backend.rest.controller.api.data.source.v1.IndividualSegmentsRestController"
)
public class IndividualSegmentsRestController extends BaseRestController {

	@GetMapping(params = "!apply", value = "/{id}/membership-changes")
	public PageDTO<BQMembershipChangeDTO> getBQMembershipChangeDTOPageDTO(
		@PathVariable Long id, @RequestParam(required = false) String expand,
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts) {

		Page<BQMembershipChange> bqMembershipChangePage =
			_bqMembershipChangeDog.searchBQMembershipChangePage(
				filterString, id, page, size, sorts);

		if (StringUtils.isEmpty(expand)) {
			return _toBQMembershipChangeDTOPageDTO(bqMembershipChangePage);
		}

		List<BQMembershipChange> bqMembershipChanges =
			bqMembershipChangePage.getContent();

		Set<BQMembershipChangeDTO> bqMembershipChangeDTOs =
			new LinkedHashSet<>();

		Stream<BQMembershipChange> stream = bqMembershipChanges.stream();

		stream.forEachOrdered(
			bqMembershipChange -> bqMembershipChangeDTOs.add(
				new BQMembershipChangeDTO(bqMembershipChange)));

		return _toBQMembershipChangeDTOPageDTO(
			new BQMembershipChangeDTO(bqMembershipChangeDTOs),
			bqMembershipChangePage);
	}

	@GetMapping("/{id}/memberships")
	public PageDTO<BQMembershipDTO> getBQMembershipDTOPageDTO(
		@PathVariable Long id,
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts) {

		if (!segmentDog.isIncludeAnonymousUsers(id)) {

			// TODO Change getBQMembershipPage to use filterString instead of
			//  individualIds

			return _toBQMembershipDTOPageDTO(
				bqMembershipDog.getBQMembershipPage(
					Collections.emptyList(), id, "ACTIVE", page, size, sorts));
		}

		return _toBQMembershipDTOPageDTO(
			bqMembershipDog.getBQMembershipPage(
				id, "ACTIVE", page, size, sorts));
	}

	@GetMapping(params = "!apply", value = "/{id}/individuals")
	public PageDTO<IndividualDTO> getIndividualDTOPageDTO(
		@PathVariable Long id,
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(required = false) Boolean includeAnonymousUsers,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts) {

		if (size >= 100) {
			return _toIndividualDTOPageDTO(
				_toIndividualPage(
					_bqMembershipIndividualDog.getMembershipIndividualPage(
						page, id, size, sorts)));
		}

		return _toIndividualDTOPageDTO(
			_bqIndividualDog.searchBQIndividualPage(
				null, null, null, filterString, includeAnonymousUsers, null,
				null, page, null, id, size, sorts));
	}

	@Cacheable
	@GetMapping(params = "apply", value = "/{id}/membership-changes")
	public String getMembershipChangeTransformations(
			@PathVariable Long id, @RequestParam String apply,
			@RequestParam(name = "filter", required = false) String
				filterString,
			@RequestParam(defaultValue = "true") boolean includeToday,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size)
		throws Exception {

		List<Transformation> transformations =
			_bqMembershipChangeDog.getMembershipChangeTransformations(
				includeToday, id, page, size);

		return JSONUtil.put(
			"_embedded",
			JSONUtil.put(
				"membership-change-transformations",
				JSONUtil.toJSONArray(
					transformations,
					transformation -> transformation.getTerm(
					).getTermsMap()))
		).put(
			"page", getPageJSONObject(page, transformations.size(), size)
		).toString();
	}

	@GetMapping("/{id}")
	public SegmentDTO getSegmentDTO(
			@PathVariable Long id,
			@RequestParam(required = false) String expand)
		throws Exception {

		Segment segment = segmentDog.getSegment(id);

		SegmentDTO segmentDTO = new SegmentDTO(
			_bqMembershipDog.getActiveBQMembershipsCount(
				segment.getIncludeAnonymousUsers(), id),
			_bqMembershipChangeDog.getLastBQMembershipChangeBySegmentId(id),
			segmentDog.getLastActivityDate(segment), segment);

		if (StringUtils.isNotEmpty(expand)) {
			String[] expandParts = expand.split(",");

			for (String expandPart : expandParts) {
				if (expandPart.equals("referenced-objects")) {
					segmentDTO.setEmbedded(
						Collections.singletonMap(
							"referenced-objects",
							_getReferencedObjectsJSONObject(segment)));
				}
				else if (_log.isWarnEnabled()) {
					_log.warn("Invalid expand: " + expandPart);
				}
			}
		}

		return segmentDTO;
	}

	@GetMapping(params = "!apply")
	public PageDTO<SegmentDTO> getSegmentDTOPageDTO(
		@RequestParam(required = false) Long dataSourceId,
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestParam(name = "sort", required = false) String[] sorts) {

		if (StringUtils.contains(filterString, "individualCount ge '1'")) {
			filterString = StringUtils.replace(
				filterString, "individualCount ge '1'", "individualCount ge 1");
		}

		Page<Segment> segmentsPage = segmentDog.searchSegmentPage(
			dataSourceId, filterString, page, Math.max(1, size), sorts);

		return toSegmentDTOPageDTO(segmentsPage);
	}

	@GetMapping(params = "apply", value = "/{id}/individuals")
	public PageDTO<TransformationDTO> getTransformationDTOPageDTO(
		@PathVariable Long id, @RequestParam String apply,
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(required = false) Boolean includeAnonymousUsers,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size) {

		// TODO Implement operation

		return _toTransformationDTOPageDTO(
			"individual-transformations", Page.empty());
	}

	@GetMapping(params = "apply")
	public PageDTO<TransformationDTO> getTransformationDTOPageDTO(
		@RequestParam String apply,
		@RequestParam(name = "filter", required = false) String filterString,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size) {

		return _toTransformationDTOPageDTO(
			"individual-segment-transformations",
			segmentDog.getTransformationPage(apply, filterString, page, size));
	}

	@PostMapping("/{id}/memberships")
	public String postMembership(
			@PathVariable Long id, @RequestBody String json)
		throws Exception {

		Segment segment = segmentDog.getSegment(id);

		if (!Objects.equals(segment.getType(), Segment.Type.STATIC) ||
			!Objects.equals(segment.getStatus(), "ACTIVE")) {

			throw new Exception(
				"Membership POST requests can only be made to an active " +
					"static individual segment");
		}

		Date date = new Date();

		if (json.startsWith("{")) {
			BQMembership bqMembership = objectMapper.convertValue(
				new JSONObject(json), BQMembership.class);

			bqMembership.setSegmentId(id);

			if (_isMember(
					bqMembership.getIdentityId(),
					bqMembership.getSegmentId())) {

				return null;
			}

			bqMembership.setChannelId(segment.getChannelId());
			bqMembership.setCreateDate(date);
			bqMembership.setModifiedDate(date);

			bqMembership = bqMembershipDog.addBQMembership(bqMembership);

			bqMembership.setId(null);

			JSONObject bqMembershipJSONObject = objectMapper.convertValue(
				bqMembership, JSONObject.class);

			return bqMembershipJSONObject.toString();
		}

		List<BQMembership> bqMemberships = new ArrayList<>();

		JSONArray jsonArray = new JSONArray(json);

		for (int i = 0; i < jsonArray.length(); i++) {
			BQMembershipDTO bqMembershipDTO = objectMapper.convertValue(
				jsonArray.getJSONObject(i), BQMembershipDTO.class);

			for (String identityId :
					_bqIdentityDog.getBQIdentityIds(
						bqMembershipDTO.getIndividualId())) {

				BQMembership bqMembership = new BQMembership();

				bqMembership.setChannelId(segment.getChannelId());
				bqMembership.setCreateDate(date);
				bqMembership.setModifiedDate(date);
				bqMembership.setSegmentId(id);
				bqMembership.setIndividualId(bqMembershipDTO.getIndividualId());
				bqMembership.setIdentityId(identityId);
				bqMembership.setStatus(bqMembershipDTO.getStatus());

				bqMemberships.add(bqMembership);
			}
		}

		if (bqMemberships.isEmpty()) {
			return null;
		}

		bqMemberships = bqMembershipDog.addBQMemberships(bqMemberships);

		bqMemberships.forEach(bqMembership -> bqMembership.setId(null));

		JSONArray bqMembershipJSONArray = JSONUtil.toJSONArray(
			bqMemberships,
			bqMembership -> objectMapper.convertValue(
				bqMembership, JSONObject.class));

		return bqMembershipJSONArray.toString();
	}

	@PostMapping
	public SegmentDTO postSegment(@RequestBody SegmentDTO segmentDTO) {
		segmentDTO.setActivitiesCount(0L);

		Date date = DateUtil.newDate();

		segmentDTO.setCreateDate(date);

		segmentDTO.setId(null);

		segmentDTO.setModifiedDate(date);

		return objectMapper.convertValue(
			segmentDog.addSegment(
				objectMapper.convertValue(segmentDTO, Segment.class)),
			SegmentDTO.class);
	}

	@PutMapping("/{id}")
	public SegmentDTO putSegment(
		@PathVariable Long id, @RequestBody SegmentDTO segmentDTO) {

		segmentDTO.setActiveIdentitiesCount(null);
		segmentDTO.setActivitiesCount(null);
		segmentDTO.setAnonymousIdentitiesCount(null);
		segmentDTO.setCreateDate(null);
		segmentDTO.setIdentitiesCount(null);
		segmentDTO.setKnownIdentitiesCount(null);
		segmentDTO.setModifiedDate(new Date());

		return objectMapper.convertValue(
			segmentDog.updateSegment(
				objectMapper.convertValue(segmentDTO, Segment.class), id),
			SegmentDTO.class);
	}

	protected PageDTO<SegmentDTO> toSegmentDTOPageDTO(
		Page<Segment> segmentsPage) {

		List<Segment> segments = segmentsPage.getContent();

		return new PageDTO<>(
			"_embedded",
			new SegmentDTO(
				_bqMembershipChangeDog.getLastBQMembershipChanges(segments),
				segments),
			segmentsPage.getNumber(), segmentsPage.getSize(),
			segmentsPage.getTotalElements(), segmentsPage.getTotalPages());
	}

	@Autowired
	protected BQMembershipDog bqMembershipDog;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	protected SegmentDog segmentDog;

	private JSONObject _getReferencedObjectsJSONObject(Segment segment)
		throws Exception {

		return JSONUtil.put(
			"assets",
			JSONUtil.toJSONArray(
				ListUtil.map(
					_bqAssetDog.getBQAssets(segment.getReferencedAssetIds()),
					BQAssetDTO::new),
				this::_toJSONObject)
		).put(
			"field-mappings",
			JSONUtil.toJSONArray(
				_bqFieldMappingDog.getBQFieldMappings(
					segment.getReferencedFieldMappingFieldNames()),
				this::_toJSONObject)
		).put(
			"groups",
			JSONUtil.toJSONArray(
				_bqGroupDog.getBQGroups(segment.getReferencedGroupIds()),
				this::_toJSONObject)
		).put(
			"organizations",
			JSONUtil.toJSONArray(
				_bqOrganizationDog.getBQOrganizations(
					segment.getReferencedOrganizationIds()),
				this::_toJSONObject)
		).put(
			"roles",
			JSONUtil.toJSONArray(
				_bqRoleDog.getBQRoles(segment.getReferencedRoleIds()),
				this::_toJSONObject)
		).put(
			"teams",
			JSONUtil.toJSONArray(
				_bqTeamDog.getBQTeams(segment.getReferencedTeamIds()),
				this::_toJSONObject)
		).put(
			"user-groups",
			JSONUtil.toJSONArray(
				_bqUserGroupDog.getBQUserGroups(
					segment.getReferencedUserGroupIds()),
				this::_toJSONObject)
		).put(
			"users",
			JSONUtil.toJSONArray(
				_bqUserDog.getBQUsers(segment.getReferencedUserIds()),
				this::_toJSONObject)
		);
	}

	private boolean _isMember(String identityId, Long segmentId) {
		if (bqMembershipDog.isMember(identityId, segmentId)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Not adding membership because identity " + identityId +
						" is already a member of individual segment " +
							segmentId);
			}

			return true;
		}

		return false;
	}

	private PageDTO<BQMembershipChangeDTO> _toBQMembershipChangeDTOPageDTO(
		BQMembershipChangeDTO bqMembershipChangeDTO,
		Page<BQMembershipChange> bqMembershipChangesPage) {

		return new PageDTO<>(
			"_embedded", bqMembershipChangeDTO,
			bqMembershipChangesPage.getNumber(),
			bqMembershipChangesPage.getSize(),
			bqMembershipChangesPage.getTotalElements(),
			bqMembershipChangesPage.getTotalPages());
	}

	private PageDTO<BQMembershipChangeDTO> _toBQMembershipChangeDTOPageDTO(
		Page<BQMembershipChange> bqMembershipChangesPage) {

		return new PageDTO<>(
			"_embedded",
			new BQMembershipChangeDTO(bqMembershipChangesPage.getContent()),
			bqMembershipChangesPage.getNumber(),
			bqMembershipChangesPage.getSize(),
			bqMembershipChangesPage.getTotalElements(),
			bqMembershipChangesPage.getTotalPages());
	}

	private PageDTO<BQMembershipDTO> _toBQMembershipDTOPageDTO(
		Page<BQMembership> bqMembershipsPage) {

		return new PageDTO<>(
			"_embedded", new BQMembershipDTO(bqMembershipsPage.getContent()),
			bqMembershipsPage.getNumber(), bqMembershipsPage.getSize(),
			bqMembershipsPage.getTotalElements(),
			bqMembershipsPage.getTotalPages());
	}

	private PageDTO<IndividualDTO> _toIndividualDTOPageDTO(
		Page<Individual> individualsPage) {

		return new PageDTO<>(
			"_embedded", new IndividualDTO(individualsPage.getContent()),
			individualsPage.getNumber(), individualsPage.getSize(),
			individualsPage.getTotalElements(),
			individualsPage.getTotalPages());
	}

	private Page<Individual> _toIndividualPage(
		Page<BQMembershipIndividual> bqMembershipIndividualPage) {

		List<Individual> individuals = new ArrayList<>();

		for (BQMembershipIndividual bqMembershipIndividual :
				bqMembershipIndividualPage.getContent()) {

			Individual individual = new Individual();

			individual.setId(bqMembershipIndividual.getIndividualId());

			individual.setBQDataSourceUsers(
				SetUtil.map(
					bqMembershipIndividual.getDataSourceUUIDs(),
					dataSourceUUID -> new BQDataSourceUser(
						Collections.emptySet(),
						dataSourceUUID.getDataSourceId(), null,
						SetUtil.of(dataSourceUUID.getUuid()))));

			individuals.add(individual);
		}

		return new PageImpl<>(
			individuals, bqMembershipIndividualPage.getPageable(),
			bqMembershipIndividualPage.getTotalElements());
	}

	private <T> JSONObject _toJSONObject(T value) throws Exception {
		return objectMapper.convertValue(value, JSONObject.class);
	}

	private PageDTO<TransformationDTO> _toTransformationDTOPageDTO(
		String transformationKey, Page<Transformation> transformations) {

		return _toTransformationDTOPageDTO(
			new TransformationDTO(
				transformationKey, transformations.getContent()),
			transformations);
	}

	private PageDTO<TransformationDTO> _toTransformationDTOPageDTO(
		TransformationDTO transformationDTO,
		Page<Transformation> transformations) {

		return new PageDTO<>(
			"_embedded", transformationDTO, transformations.getNumber(),
			transformations.getSize(), transformations.getTotalElements(),
			transformations.getTotalPages());
	}

	private static final Log _log = LogFactory.getLog(
		IndividualSegmentsRestController.class);

	@Autowired
	private BQAssetDog _bqAssetDog;

	@Autowired
	private BQFieldMappingDog _bqFieldMappingDog;

	@Autowired
	private BQGroupDog _bqGroupDog;

	@Autowired
	private BQIdentityDog _bqIdentityDog;

	@Autowired
	private BQIndividualDog _bqIndividualDog;

	@Autowired
	private BQMembershipChangeDog _bqMembershipChangeDog;

	@Autowired
	private BQMembershipDog _bqMembershipDog;

	@Autowired
	private BQMembershipIndividualDog _bqMembershipIndividualDog;

	@Autowired
	private BQOrganizationDog _bqOrganizationDog;

	@Autowired
	private BQRoleDog _bqRoleDog;

	@Autowired
	private BQTeamDog _bqTeamDog;

	@Autowired
	private BQUserDog _bqUserDog;

	@Autowired
	private BQUserGroupDog _bqUserGroupDog;

}