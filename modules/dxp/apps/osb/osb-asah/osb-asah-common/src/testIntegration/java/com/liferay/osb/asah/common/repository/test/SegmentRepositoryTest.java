/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.repository.SegmentRepository;
import com.liferay.osb.asah.common.repository.helper.FilterHelper;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Inácio Nery
 */
@Import(JDBCTestConfiguration.class)
public class SegmentRepositoryTest
	extends BaseRepositoryTestCase<Segment, Long> {

	@BeforeEach
	public void setUp() {
		Segment segment1 = new Segment();

		Channel channel1 = _addChannel(1, "Liferay Brazil");

		segment1.setChannelId(channel1.getId());

		segment1.setAuthorName("Test Test");
		segment1.setCreateDate(DateUtil.addDays(new Date(), -5));
		segment1.setFilter("(channelId eq '1')");
		segment1.setIdentitiesCount(20L);
		segment1.setName("Segment 1");
		segment1.setReferencedDataSourceIds(SetUtil.of(5L, 6L));
		segment1.setReferencedFieldMappingFieldNames(SetUtil.of("7", "8"));
		segment1.setState("READY");
		segment1.setStatus("STARTED");
		segment1.setType(Segment.Type.DYNAMIC);

		Segment segment2 = new Segment();

		Channel channel2 = _addChannel(2, "Liferay USA");

		segment2.setChannelId(channel2.getId());

		segment2.setAuthorName("Marcos Martins");
		segment2.setCreateDate(DateUtil.addDays(new Date(), -3));
		segment2.setFilter("(channelId eq '2')");
		segment2.setIdentitiesCount(30L);
		segment2.setName("Segment 2");
		segment2.setReferencedDataSourceIds(SetUtil.of(5L, 6L));
		segment2.setReferencedFieldMappingFieldNames(SetUtil.of("7", "8"));
		segment2.setState("READY");
		segment2.setStatus("STARTED");
		segment2.setType(Segment.Type.DYNAMIC);

		DataSource dataSource = new DataSource("Liferay Brazil");

		dataSource.setCredentialType("Token Authentication");
		dataSource.setFaroBackendSecuritySignature(
			"faroBackendSecuritySignature");
		dataSource.setId(100L);
		dataSource.setIsNew(Boolean.TRUE);
		dataSource.setProviderType("LIFERAY");
		dataSource.setState("READY");
		dataSource.setStatus("STARTED");
		dataSource.setURL("");

		_dataSourceRepository.save(dataSource);

		Segment segment3 = new Segment();

		segment3.setName("Account: 456");

		Segment segment4 = new Segment();

		segment4.setChannelId(channel1.getId());
		segment4.setAuthorName("Riccardo Ferrari");
		segment4.setName("Quote's test");

		setUpRepository(segment1, segment2, segment3, segment4);

		segment1 = entityModels.get(0);

		_segment1Id = segment1.getId();

		segment2 = entityModels.get(1);

		_segment2Id = segment2.getId();
	}

	@AfterEach
	@Override
	public void tearDown() {
		super.tearDown();

		_channelRepository.deleteAll();
		_dataSourceRepository.deleteAll();
	}

	@Test
	public void testCountByCreateDateBetweenAndIdAfter() {
		Date fromDate = DateUtil.addDays(new Date(), -4);
		Date toDate = DateUtil.addDays(new Date(), -2);

		Assertions.assertEquals(
			1,
			_segmentRepository.countByCreateDateBetweenAndIdAfter(
				fromDate, toDate, 0L));
		Assertions.assertEquals(
			0,
			_segmentRepository.countByCreateDateBetweenAndIdAfter(
				fromDate, toDate, _segment2Id));
	}

	@Test
	public void testCountByIdAfter() {
		Assertions.assertEquals(4, _segmentRepository.countByIdAfter(0L));

		Segment segment = entityModels.get(0);

		Assertions.assertEquals(
			3, _segmentRepository.countByIdAfter(segment.getId()));
	}

	@Test
	public void testCountSegments() {
		long segmentsCount = _segmentRepository.countSegments(
			FilterHelper.EMPTY, Collections.emptyList());

		Assertions.assertEquals(0, segmentsCount);

		segmentsCount = _segmentRepository.countSegments(
			FilterHelper.EMPTY,
			Arrays.asList(
				new HashMap<String, Long>() {
					{
						put("identitiesCount", 20L);
						put("segmentId", _segment1Id);
					}
				},
				new HashMap<String, Long>() {
					{
						put("identitiesCount", 30L);
						put("segmentId", _segment2Id);
					}
				}));

		Assertions.assertEquals(2, segmentsCount);
	}

	@Test
	public void testDeleteByChannelIdIn() {
		_segmentRepository.deleteByChannelIdIn(
			SetUtil.map(entityModels, Segment::getChannelId));

		Assertions.assertEquals(1, _segmentRepository.count());
	}

	@Test
	public void testFindByChannelIdIn() {
		Segment segment1 = entityModels.get(0);
		Segment segment2 = entityModels.get(1);

		List<Segment> segments = _segmentRepository.findByChannelIdIn(
			SetUtil.of(segment1.getChannelId(), segment2.getChannelId()),
			PageRequest.of(0, 10));

		Assertions.assertEquals(3, segments.size(), segments.toString());
	}

	@Test
	public void testFindByChannelIdIsNotNullOrNameStartingWith() {
		Assertions.assertEquals(
			entityModels,
			_segmentRepository.findByChannelIdIsNotNullOrNameStartingWith(
				"Account: ", PageRequest.of(0, 10, Sort.by("id"))));
	}

	@Test
	public void testFindByCreateDateBetweenAndIdAfter() {
		List<Segment> segments =
			_segmentRepository.findByCreateDateBetweenAndIdAfter(
				DateUtil.addDays(new Date(), -10), new Date(), _segment1Id - 1L,
				PageRequest.of(0, 1));

		Assertions.assertEquals(1, segments.size(), segments.toString());

		Segment segment = segments.get(0);

		Assertions.assertEquals(_segment1Id, segment.getId());
	}

	@Test
	public void testFindByNameAndStatus() {
		Optional<Segment> segmentOptional =
			_segmentRepository.findByNameAndStatus("Segment 1", "STARTED");

		Assertions.assertTrue(segmentOptional.isPresent());

		segmentOptional = _segmentRepository.findByNameAndStatus(
			"Segment 1", "END");

		Assertions.assertFalse(segmentOptional.isPresent());
	}

	@Test
	public void testFindByReferencedDataSourceIdsAndStateNotAndType() {
		Assertions.assertEquals(
			Arrays.asList(entityModels.get(0), entityModels.get(1)),
			_segmentRepository.findByReferencedDataSourceIdsAndStateNotAndType(
				5L, "INACTIVE", Segment.Type.DYNAMIC));
	}

	@Test
	public void testFindByReferencedDataSourceIdsOrReferencedFieldMappingFieldNameInAndStateNotAndType() {
		Assertions.assertEquals(
			Arrays.asList(entityModels.get(0), entityModels.get(1)),
			_segmentRepository.
				findByReferencedDataSourceIdsOrReferencedFieldMappingFieldNameInAndStateNotAndType(
					5L, Arrays.asList("7"), "INACTIVE", Segment.Type.DYNAMIC));
	}

	@Test
	public void testFindByReferencedFieldMappingFieldNameInAndStateNotAndType() {
		Assertions.assertEquals(
			Arrays.asList(entityModels.get(0), entityModels.get(1)),
			_segmentRepository.
				findByReferencedFieldMappingFieldNameInAndStateNotAndType(
					Arrays.asList("7"), "INACTIVE", Segment.Type.DYNAMIC));
	}

	@Test
	public void testFindByType() {
		List<Segment> segments = _segmentRepository.findByType(
			PageRequest.of(0, 20), Segment.Type.DYNAMIC);

		Assertions.assertEquals(2, segments.size());
	}

	@Test
	public void testFindIdByNameInAndStatus() {
		List<Long> ids = _segmentRepository.findIdByNameInAndStatus(
			Arrays.asList("Segment 1", "Segment 2"), "STARTED");

		Assertions.assertEquals(2, ids.size(), ids.toString());
	}

	@Test
	public void testSearchSegmentsOrderByAuthorName() {
		List<Segment> segments = _segmentRepository.searchSegments(
			Arrays.asList(1L, 2L), FilterHelper.EMPTY,
			PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "author/name")));

		Assertions.assertEquals(3, segments.size(), segments.toString());

		Segment segment = segments.get(0);

		Assertions.assertEquals("Marcos Martins", segment.getAuthorName());

		segments = _segmentRepository.searchSegments(
			Arrays.asList(1L, 2L), FilterHelper.EMPTY,
			PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "author/name")));

		segment = segments.get(2);

		Assertions.assertEquals("Marcos Martins", segment.getAuthorName());
	}

	@Test
	public void testSearchSegmentsOrderByIndividualCountDesc() {
		List<Segment> segments = _segmentRepository.searchSegments(
			FilterHelper.EMPTY, Collections.emptyList(),
			PageRequest.of(
				0, 10, Sort.by(Sort.Direction.DESC, "individualCount")));

		Assertions.assertEquals(0, segments.size(), segments.toString());

		segments = _segmentRepository.searchSegments(
			FilterHelper.EMPTY,
			Arrays.asList(
				new HashMap<String, Long>() {
					{
						put("identitiesCount", 20L);
						put("segmentId", _segment1Id);
					}
				},
				new HashMap<String, Long>() {
					{
						put("identitiesCount", 30L);
						put("segmentId", _segment2Id);
					}
				}),
			PageRequest.of(
				0, 10, Sort.by(Sort.Direction.DESC, "individualCount")));

		Assertions.assertEquals(2, segments.size(), segments.toString());

		Segment segment = segments.get(0);

		Assertions.assertEquals(
			segment.getId(), _segment2Id, segment.toString());
	}

	@Test
	public void testSearchSegmentWithQuotes() {
		FilterHelper filterHelper = new FilterHelper(
			"contains(name,'quote''s')");

		List<Segment> segments = _segmentRepository.searchSegments(
			Arrays.asList(1L, 2L), filterHelper, PageRequest.of(0, 10));

		Assertions.assertEquals(1, segments.size());
	}

	@Override
	protected PagingAndSortingRepository<Segment, Long>
		getPagingAndSortingRepository() {

		return _segmentRepository;
	}

	private Channel _addChannel(long id, String name) {
		Channel channel = new Channel(name);

		channel.setId(id);
		channel.setIsNew(Boolean.TRUE);

		return _channelRepository.save(channel);
	}

	@Autowired
	private ChannelRepository _channelRepository;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

	private Long _segment1Id;
	private Long _segment2Id;

	@Autowired
	private SegmentRepository _segmentRepository;

}