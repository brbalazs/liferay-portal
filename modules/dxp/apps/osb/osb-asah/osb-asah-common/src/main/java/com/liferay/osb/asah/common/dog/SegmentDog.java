/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.util.SortUtil;
import com.liferay.osb.asah.common.entity.BQAsset;
import com.liferay.osb.asah.common.entity.BQGroup;
import com.liferay.osb.asah.common.entity.BQOrganization;
import com.liferay.osb.asah.common.entity.BQRole;
import com.liferay.osb.asah.common.entity.BQTeam;
import com.liferay.osb.asah.common.entity.BQUser;
import com.liferay.osb.asah.common.entity.BQUserGroup;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.filter.expression.FilterExpression;
import com.liferay.osb.asah.common.filter.expression.FilterExpressionReferencedObjectsVisitor;
import com.liferay.osb.asah.common.filter.expression.FilterExpressionValidatorVisitor;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.model.MembershipCountSnapshot;
import com.liferay.osb.asah.common.model.Transformation;
import com.liferay.osb.asah.common.postgresql.converter.helper.SegmentFilterStringConverterHelper;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahDuplicateNameException;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahNameException;
import com.liferay.osb.asah.common.util.BeanUtils;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.util.TimeOrderedUuidGenerator;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Michael Bowerman
 */
@Component
public class SegmentDog {

	public Segment addSegment(Segment segment) {
		_validateFilterString(segment.getFilter());
		_validateSegmentName(segment.getChannelId(), null, segment.getName());

		setReferencedFields(segment);

		segment.setId(_timeOrderedUuidGenerator.generateIdAsLong());

		segment.setIsNew(Boolean.TRUE);

		if ((segment.getType() == null) ||
			(segment.getType() == Segment.Type.DYNAMIC)) {

			segment.setState("IN_PROGRESS");
		}
		else {
			segment.setState("READY");
		}

		segment = _segmentRepository.save(segment);

		_bqMembershipChangeDog.initializeBQMembershipChanges(segment.getId());

		_addAsahTask(segment);

		return segment;
	}

	public void assignChannel(Long channelId, Long segmentId) throws Exception {
		Segment segment = getSegment(segmentId);

		if (segment.getChannelId() != null) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"Individual segment already assigned to property: " +
					segment.getChannelId());
		}

		segment.setChannelId(channelId);

		segment = _segmentRepository.save(segment);

		_updateMemberships(segment);
	}

	public void deleteSegment(Long segmentId) {
		_bqMembershipDog.deleteBQMemberships(
			Collections.singletonList(segmentId));

		_segmentRepository.deleteById(segmentId);
	}

	public void disableDynamicSegments(
		Long dataSourceId, List<String> fieldMappingFieldNames) {

		if ((dataSourceId == null) && fieldMappingFieldNames.isEmpty()) {
			return;
		}

		List<Segment> segments;

		if ((dataSourceId != null) && !fieldMappingFieldNames.isEmpty()) {
			segments =
				_segmentRepository.
					findByReferencedDataSourceIdsOrReferencedFieldMappingFieldNameInAndStateNotAndType(
						dataSourceId, fieldMappingFieldNames, "DISABLED",
						Segment.Type.DYNAMIC);
		}
		else if (dataSourceId != null) {
			segments =
				_segmentRepository.
					findByReferencedDataSourceIdsAndStateNotAndType(
						dataSourceId, "DISABLED", Segment.Type.DYNAMIC);
		}
		else if (!fieldMappingFieldNames.isEmpty()) {
			segments =
				_segmentRepository.
					findByReferencedFieldMappingFieldNameInAndStateNotAndType(
						fieldMappingFieldNames, "DISABLED",
						Segment.Type.DYNAMIC);
		}
		else {
			segments = _segmentRepository.findByStateNotAndType(
				"DISABLED", Segment.Type.DYNAMIC);
		}

		for (Segment segment : segments) {
			segment.setState("DISABLED");
		}

		if (!segments.isEmpty()) {
			_segmentRepository.saveAll(segments);
		}
	}

	public Segment fetchSegment(Long segmentId) {
		Optional<Segment> segmentOptional = _segmentRepository.findById(
			segmentId);

		return segmentOptional.orElse(null);
	}

	public List<Segment> getBQIndividualSegments(String individualId) {
		List<Long> segmentIds = _bqMembershipDog.getIndividualSegmentIds(
			individualId);

		return IterableUtils.toList(_segmentRepository.findAllById(segmentIds));
	}

	public Date getLastActivityDate(Segment segment) {

		// TODO Implement operation

		return null;
	}

	public Segment getSegment(Long segmentId) {
		Optional<Segment> segmentOptional = _segmentRepository.findById(
			segmentId);

		return segmentOptional.orElseThrow(
			() -> new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no Segment with ID " + segmentId));
	}

	public Page<Segment> getSegmentPage(
		Date fromCreateDate, Long segmentId, int size, Sort sort,
		Date toCreateDate) {

		return PageableExecutionUtils.getPage(
			_segmentRepository.findByCreateDateBetweenAndIdAfter(
				fromCreateDate, toCreateDate, segmentId,
				PageRequest.of(0, size, sort)),
			PageRequest.of(0, size, sort),
			() -> _segmentRepository.countByCreateDateBetweenAndIdAfter(
				fromCreateDate, toCreateDate, segmentId));
	}

	public Page<Segment> getSegmentPage(
		@Nullable Long channelId, int page, int size) {

		PageRequest pageRequest = PageRequest.of(page, size);

		if (channelId == null) {
			return _segmentRepository.findAll(PageRequest.of(page, size));
		}

		return PageableExecutionUtils.getPage(
			_segmentRepository.findByChannelId(channelId, pageRequest),
			pageRequest, () -> _segmentRepository.countByChannelId(channelId));
	}

	public List<Segment> getSegments(int page, int size, Segment.Type type) {
		return _segmentRepository.findByType(PageRequest.of(page, size), type);
	}

	public List<Segment> getSegments(Iterable<Long> segmentIds) {
		return IterableUtils.toList(_segmentRepository.findAllById(segmentIds));
	}

	public Map<String, JSONObject> getSegmentsJSONObjects(
			List<Individual> individuals)
		throws Exception {

		Map<String, JSONObject> segmentsJSONObjects = new HashMap<>();

		for (Individual individual : individuals) {
			segmentsJSONObjects.put(
				individual.getId(),
				JSONUtil.put(
					"individual-segments",
					JSONUtil.toJSONArray(
						getSegments(
							_bqMembershipDog.getSegmentIds(
								String.valueOf(individual.getId()))),
						segment -> _objectMapper.convertValue(
							segment, JSONObject.class))));
		}

		return segmentsJSONObjects;
	}

	public Page<Transformation> getTransformationPage(
		String apply, @Nullable String filterString, int page, int size) {

		PageRequest pageRequest = PageRequest.of(
			page, size,
			SortUtil.getSort(
				Sort.by(Sort.Order.desc("totalElements")),
				new String[] {"totalElements", "desc", "terms", "asc"}));

		List<Transformation> transformations =
			_segmentRepository.getSegmentTransformations(
				apply, new FilterHelper(filterString), pageRequest, null);

		return PageableExecutionUtils.getPage(
			transformations, pageRequest, transformations::size);
	}

	public boolean isIncludeAnonymousUsers(Long segmentId) {
		Segment segment = getSegment(segmentId);

		return BooleanUtils.toBoolean(segment.getIncludeAnonymousUsers());
	}

	public Page<Segment> searchPreviewDisabledSegmentPage(
		Long dataSourceId, String filterString, int page, int size,
		String[] sorts) {

		List<Long> fieldMappingFieldNames = Collections.emptyList();

		FilterHelper filterHelper = new FilterHelper(filterString);

		PageRequest pageRequest = PageRequest.of(
			page, size, SortUtil.getSort(sorts));

		return PageableExecutionUtils.getPage(
			_segmentRepository.searchPreviewDisabledSegments(
				fieldMappingFieldNames, dataSourceId, filterHelper,
				pageRequest),
			pageRequest,
			() -> _segmentRepository.countPreviewDisabledSegments(
				fieldMappingFieldNames, dataSourceId, filterHelper));
	}

	public Page<Segment> searchSegmentPage(
		Long dataSourceId, String filterString, int page, int size,
		String[] sorts) {

		List<Long> channelIds = ListUtil.map(
			_channelRepository.findByDataSourceId(dataSourceId),
			Channel::getId);

		FilterHelper filterHelper = new FilterHelper(
			null, filterString,
			new SegmentFilterStringConverterHelper(_bqMembershipChangeDog));

		PageRequest pageRequest = PageRequest.of(
			page, size, SortUtil.getSort(sorts));

		return PageableExecutionUtils.getPage(
			_segmentRepository.searchSegments(
				channelIds, filterHelper, pageRequest),
			pageRequest,
			() -> _segmentRepository.countSegments(channelIds, filterHelper));
	}

	public Page<Segment> searchSegmentPage(
		String filterString, String individualId, int page, int size,
		String[] sorts) {

		List<Map<String, Long>> segmentIdIdentityCounts =
			_bqMembershipDog.getActiveSegmentIds(individualId);

		PageRequest pageRequest = PageRequest.of(
			page, size, SortUtil.getSort(sorts));

		if (segmentIdIdentityCounts.isEmpty()) {
			return PageableExecutionUtils.getPage(
				Collections.emptyList(), pageRequest, () -> 0);
		}

		FilterHelper filterHelper = new FilterHelper(filterString);

		return PageableExecutionUtils.getPage(
			_segmentRepository.searchSegments(
				filterHelper, segmentIdIdentityCounts, pageRequest),
			pageRequest,
			() -> _segmentRepository.countSegments(
				filterHelper, segmentIdIdentityCounts));
	}

	public void setReferencedFields(Segment segment) {
		if (StringUtils.isBlank(segment.getFilter())) {
			return;
		}

		FilterExpressionReferencedObjectsVisitor
			filterExpressionReferencedObjectsVisitor =
				new FilterExpressionReferencedObjectsVisitor();

		new FilterExpression(
			segment.getFilter(), filterExpressionReferencedObjectsVisitor);

		Map<String, Set<String>> referencedObjectIds =
			filterExpressionReferencedObjectsVisitor.getReferencedObjectIds();

		if (referencedObjectIds.isEmpty()) {
			return;
		}

		segment.setReferencedAssetIds(
			SetUtil.map(
				referencedObjectIds.get("referencedAssetIds"),
				String::valueOf));
		segment.setReferencedFieldMappingFieldNames(
			SetUtil.map(
				referencedObjectIds.get("referencedFieldMappingFieldNames"),
				String::valueOf));
		segment.setReferencedGroupIds(
			SetUtil.map(
				referencedObjectIds.get("referencedGroupIds"),
				String::valueOf));
		segment.setReferencedOrganizationIds(
			SetUtil.map(
				referencedObjectIds.get("referencedOrganizationIds"),
				String::valueOf));
		segment.setReferencedRoleIds(
			SetUtil.map(
				referencedObjectIds.get("referencedRoleIds"), String::valueOf));
		segment.setReferencedTeamIds(
			SetUtil.map(
				referencedObjectIds.get("referencedTeamIds"), String::valueOf));
		segment.setReferencedUserGroupIds(
			SetUtil.map(
				referencedObjectIds.get("referencedUserGroupIds"),
				String::valueOf));
		segment.setReferencedUserIds(
			SetUtil.map(
				referencedObjectIds.get("referencedUserIds"), String::valueOf));

		_setReferencedDataSourceIds(segment);
	}

	public Segment updateSegment(Segment partialSegment, Long segmentId) {
		_validateSegmentName(
			partialSegment.getChannelId(), segmentId, partialSegment.getName());

		return _updateSegment(getSegment(segmentId), partialSegment);
	}

	public Segment updateSegmentMembershipCount(
		MembershipCountSnapshot membershipCountSnapshot, Segment segment) {

		segment.setIdentitiesCount(
			membershipCountSnapshot.getIdentitiesCount());
		segment.setIndividualsCount(
			membershipCountSnapshot.getIndividualsCount());

		return _segmentRepository.save(segment);
	}

	public Segment updateSegmentState(Segment segment, String state) {
		segment.setState(state);

		return _segmentRepository.save(segment);
	}

	private void _addAsahTask(Segment segment) {
		if (Objects.equals(segment.getType(), Segment.Type.DYNAMIC)) {
			segment.setState("IN_PROGRESS");

			_asahTaskDog.scheduleAsahTask(
				"UpdateMembershipsNanite",
				JSONUtil.put(
					"dateModified",
					DateUtil.toUTCString(segment.getModifiedDate())
				).put(
					"individualSegmentJSONObject",
					_objectMapper.convertValue(segment, JSONObject.class)
				));
		}
	}

	private void _setReferencedDataSourceIds(Segment segment) {
		Set<Long> referencedDataSourceIds =
			segment.getReferencedDataSourceIds();

		List<BQAsset> bqAssets = _bqAssetDog.getBQAssets(
			segment.getReferencedAssetIds());

		if (!bqAssets.isEmpty()) {
			Stream<BQAsset> bqAssetsStream = bqAssets.stream();

			referencedDataSourceIds.addAll(
				bqAssetsStream.map(
					BQAsset::getDataSourceId
				).collect(
					Collectors.toSet()
				));
		}

		List<BQGroup> bqGroups = _bqGroupDog.getBQGroups(
			segment.getReferencedGroupIds());

		if (!bqGroups.isEmpty()) {
			Stream<BQGroup> bqGroupsStream = bqGroups.stream();

			referencedDataSourceIds.addAll(
				bqGroupsStream.map(
					BQGroup::getDataSourceId
				).collect(
					Collectors.toSet()
				));
		}

		List<BQOrganization> bqOrganizations =
			_bqOrganizationDog.getBQOrganizations(
				segment.getReferencedOrganizationIds());

		if (!bqOrganizations.isEmpty()) {
			Stream<BQOrganization> bqOrganizationsStream =
				bqOrganizations.stream();

			referencedDataSourceIds.addAll(
				bqOrganizationsStream.map(
					BQOrganization::getDataSourceId
				).collect(
					Collectors.toSet()
				));
		}

		List<BQRole> bqRoles = _bqRoleDog.getBQRoles(
			segment.getReferencedRoleIds());

		if (!bqRoles.isEmpty()) {
			Stream<BQRole> bqRolesStream = bqRoles.stream();

			referencedDataSourceIds.addAll(
				bqRolesStream.map(
					BQRole::getDataSourceId
				).collect(
					Collectors.toSet()
				));
		}

		List<BQTeam> bqTeams = _bqTeamDog.getBQTeams(
			segment.getReferencedTeamIds());

		if (!bqTeams.isEmpty()) {
			Stream<BQTeam> bqTeamsStream = bqTeams.stream();

			referencedDataSourceIds.addAll(
				bqTeamsStream.map(
					BQTeam::getDataSourceId
				).collect(
					Collectors.toSet()
				));
		}

		List<BQUserGroup> bqUserGroups = _bqUserGroupDog.getBQUserGroups(
			segment.getReferencedUserGroupIds());

		if (!bqUserGroups.isEmpty()) {
			Stream<BQUserGroup> bqUserGroupsStream = bqUserGroups.stream();

			referencedDataSourceIds.addAll(
				bqUserGroupsStream.map(
					BQUserGroup::getDataSourceId
				).collect(
					Collectors.toSet()
				));
		}

		List<BQUser> bqUsers = _bqUserDog.getBQUsers(
			segment.getReferencedUserIds());

		if (!bqUsers.isEmpty()) {
			Stream<BQUser> bqUsersStream = bqUsers.stream();

			referencedDataSourceIds.addAll(
				bqUsersStream.map(
					BQUser::getDataSourceId
				).collect(
					Collectors.toSet()
				));
		}

		segment.setReferencedDataSourceIds(referencedDataSourceIds);
	}

	private void _setState(Segment segment) {
		if ((segment.getType() == null) ||
			Objects.equals(segment.getType(), Segment.Type.DYNAMIC)) {

			segment.setState("IN_PROGRESS");
		}
		else {
			segment.setState("READY");
		}
	}

	private void _updateMemberships(Segment segment) {
		if (Objects.equals(segment.getType(), Segment.Type.DYNAMIC)) {
			_addAsahTask(segment);
		}
	}

	private Segment _updateSegment(
		Segment existingSegment, Segment partialSegment) {

		if (StringUtils.isNotEmpty(partialSegment.getFilter()) &&
			Objects.equals(
				existingSegment.getFilter(), partialSegment.getFilter()) &&
			!Objects.isNull(partialSegment.getIncludeAnonymousUsers()) &&
			Objects.equals(
				existingSegment.getIncludeAnonymousUsers(),
				partialSegment.getIncludeAnonymousUsers())) {

			existingSegment.setModifiedDate(partialSegment.getModifiedDate());
			existingSegment.setName(partialSegment.getName());
			existingSegment.setState(partialSegment.getState());
			existingSegment.setStatus(partialSegment.getStatus());

			existingSegment = _segmentRepository.save(existingSegment);
		}
		else {
			setReferencedFields(partialSegment);

			_setState(partialSegment);

			BeanUtils.copyProperties(partialSegment, existingSegment);

			_validateFilterString(existingSegment.getFilter());

			existingSegment = _segmentRepository.save(existingSegment);

			_addAsahTask(existingSegment);
		}

		return existingSegment;
	}

	private void _validateFilterString(String filterString) {
		if (StringUtils.isBlank(filterString)) {
			return;
		}

		new FilterExpression(
			filterString, new FilterExpressionValidatorVisitor());
	}

	private void _validateSegmentName(
		Long channelId, @Nullable Long segmentId, String name) {

		if (StringUtils.isBlank(name)) {
			throw new OSBAsahNameException();
		}

		Optional<Segment> segmentOptional =
			_segmentRepository.findByChannelIdAndNameIgnoreCase(
				channelId, name);

		if (!segmentOptional.isPresent()) {
			return;
		}

		Segment existingSegment = segmentOptional.get();

		if (!Objects.equals(existingSegment.getId(), segmentId)) {
			throw new OSBAsahDuplicateNameException();
		}
	}

	@Autowired
	private AsahTaskDog _asahTaskDog;

	@Autowired
	private BQAssetDog _bqAssetDog;

	@Autowired
	private BQGroupDog _bqGroupDog;

	@Autowired
	private BQMembershipChangeDog _bqMembershipChangeDog;

	@Autowired
	private BQMembershipDog _bqMembershipDog;

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

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private SegmentRepository _segmentRepository;

	private final TimeOrderedUuidGenerator _timeOrderedUuidGenerator =
		new TimeOrderedUuidGenerator();

}