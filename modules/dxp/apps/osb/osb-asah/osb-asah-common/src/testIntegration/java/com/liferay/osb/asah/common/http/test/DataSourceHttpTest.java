/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.http.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.ChannelDog;
import com.liferay.osb.asah.common.dog.DXPEntityDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.dog.SegmentDog;
import com.liferay.osb.asah.common.entity.Asset;
import com.liferay.osb.asah.common.entity.Channel;
import com.liferay.osb.asah.common.entity.DXPEntity;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.faro.info.dog.test.BaseFaroInfoDogTestCase;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.Field;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.repository.AssetRepository;
import com.liferay.osb.asah.common.repository.BQCSVUserRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.test.util.faro.FaroInfoTestUtil;
import com.liferay.osb.asah.test.util.spring.OSBAsahRepositoryTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSQLTestExecutionListener;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author Leslie Wong
 */
@TestExecutionListeners(
	mergeMode = TestExecutionListeners.MergeMode.REPLACE_DEFAULTS,
	value = {
		DependencyInjectionTestExecutionListener.class,
		MockitoTestExecutionListener.class,
		OSBAsahRepositoryTestExecutionListener.class,
		OSBAsahSQLTestExecutionListener.class,
		ResetMocksTestExecutionListener.class
	}
)
public class DataSourceHttpTest extends BaseFaroInfoDogTestCase {

	@Test
	public void testAddDataSource() {
		_dataSourceDog.addDataSource(FaroInfoTestUtil.buildLiferayDataSource());
	}

	@Test
	public void testAddDataSourceWithDuplicateName() {
		for (int i = 0; i < 4; i++) {
			_dataSourceDog.addDataSource(
				FaroInfoTestUtil.buildLiferayDataSource(
					"Liferay", RandomTestUtil.randomURL()));
		}

		List<DataSource> dataSources = _dataSourceDog.getDataSources();

		Assertions.assertEquals(4L, dataSources.size());

		Assertions.assertTrue(_dataSourceRepository.existsByName("Liferay"));
		Assertions.assertTrue(
			_dataSourceRepository.existsByName("Liferay (1)"));
		Assertions.assertTrue(
			_dataSourceRepository.existsByName("Liferay (2)"));
		Assertions.assertTrue(
			_dataSourceRepository.existsByName("Liferay (3)"));
	}

	@Disabled
	@Test
	public void testDeleteCSVDataSource() throws Exception {
		DataSource dataSource1 = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildCSVDataSource(1L));

		dataSource1.setName("DataSource1");

		_dataSourceRepository.save(dataSource1);

		DataSource dataSource2 = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildCSVDataSource(2L));

		dataSource2.setName("DataSource2");

		_dataSourceRepository.save(dataSource2);

		_bqCSVUserRepository.insert(
			FaroInfoTestUtil.buildBQCSVUser(
				1L, RandomTestUtil.randomUUID(), dataSource1.getId()));

		_bqCSVUserRepository.insert(
			FaroInfoTestUtil.buildBQCSVUser(
				2L, RandomTestUtil.randomUUID(), dataSource2.getId()));

		dataSource1.setDeletionDate(new Date());

		_dataSourceRepository.delete(dataSource1);

		Assertions.assertFalse(
			_dataSourceRepository.existsByName("DataSource1"));
		Assertions.assertTrue(
			_dataSourceRepository.existsByName("DataSource2"));
	}

	@Disabled
	@Test
	public void testDeleteDataSourceDeletesEmptyFieldMappings()
		throws Exception {

		DataSource dataSource = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource());

		// TODO Add BQFieldMapping "givenName", "Text"

		dataSource.setDeletionDate(new Date());

		_dataSourceDog.deleteDataSource(dataSource);

		Assertions.assertFalse(
			false,
			"Field mapping should have been deleted on data source deletion");
	}

	@Disabled
	@Test
	public void testDeleteDataSourceDeletesIndividual() throws Exception {
		DataSource dataSource = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource());

		// TODO Add Individual related to dataSource

		dataSource.setDeletionDate(new Date());

		_dataSourceDog.deleteDataSource(dataSource);

		// TODO Check if exists Individual with individual's Id

		Assertions.assertFalse(
			true,
			"Individual was not deleted on data source deletion despite only " +
				"containing fields from the deleted data source");
	}

	@Disabled
	@Test
	public void testDeleteDataSourceDeletesReferenceInFieldMapping()
		throws Exception {

		DataSource dataSource1 = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource());

		// TODO Add BQFieldMapping "givenName", "Text"

		dataSource1.setDeletionDate(new Date());

		_dataSourceDog.deleteDataSource(dataSource1);

		Assertions.assertFalse(
			false,
			"Field mapping reference to deleted data source was not removed");
		Assertions.assertTrue(
			false,
			"Field mapping reference to existing data source was removed on " +
				"deletion of another data source");
	}

	@Disabled
	@Test
	public void testDeleteDataSourceDisablesIndividualDynamicSegment1()
		throws Exception {

		DataSource dataSource = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource());

		// TODO Add BQFieldMapping "givenName", "Text"

		Channel channel = _channelDog.addChannel("Liferay");

		Segment segment = _segmentDog.addSegment(
			FaroInfoTestUtil.buildDynamicSegment(
				channel.getId(), "(((demographics/givenName/value ne null)))"));

		dataSource.setDeletionDate(new Date());

		_dataSourceDog.deleteDataSource(dataSource);

		segment = _segmentDog.getSegment(segment.getId());

		Assertions.assertEquals(
			"DISABLED", segment.getState(),
			"Individual dynamic segment with properties was not disabled " +
				"when data source was deleted");
	}

	@Disabled
	@Test
	public void testDeleteDataSourceDisablesIndividualDynamicSegment2()
		throws Exception {

		DataSource dataSource = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource());

		Asset asset = _assetRepository.save(
			_objectMapper.convertValue(
				FaroInfoTestUtil.buildPageAssetJSONObject(dataSource.getId()),
				Asset.class));

		Channel channel = _channelDog.addChannel("Liferay");

		Segment segment = _segmentDog.addSegment(
			FaroInfoTestUtil.buildDynamicSegment(
				channel.getId(),
				"activities/ever eq 'page#pageViewed#" + asset.getId() + "'"));

		dataSource.setDeletionDate(new Date());

		_dataSourceDog.deleteDataSource(dataSource);

		segment = _segmentDog.getSegment(segment.getId());

		Assertions.assertEquals(
			"DISABLED", segment.getState(),
			"Individual dynamic segment with assets was not disabled when " +
				"data source was deleted");
	}

	@Test
	public void testDeleteDataSourceDoesNotDeleteDefaultFieldMappings()
		throws Exception {

		DataSource dataSource = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource());

		// TODO Save BQFieldMapping "givenName", "Text"

		dataSource.setDeletionDate(new Date());

		_dataSourceDog.deleteDataSource(dataSource);

		Assertions.assertTrue(
			true,
			"Field mappings created by the default user should not be " +
				"deleted on data source deletion");
	}

	@Disabled
	@Test
	public void testDeleteDataSourceUpdatesIndividualFields() throws Exception {
		DataSource dataSource1 = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource());
		DataSource dataSource2 = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource());

		Long dataSourceId1 = dataSource1.getId();

		// TODO Add BQFieldMapping "email", "Text"
		// TODO Add BQFieldMapping "givenName", "Text"

		Individual individual = FaroInfoTestUtil.buildIndividual(
			dataSource1, 1L);

		// TODO Add individual

		FaroInfoTestUtil.buildIndividualField(
			dataSource1, "email", "http://schema.org/email",
			RandomTestUtil.randomString(), individual, "email");

		// TODO Add dataSource2 to individual

		Date lastEnrichmentDate = individual.getLastEnrichmentDate();

		dataSource2.setDeletionDate(new Date());

		_dataSourceDog.deleteDataSource(dataSource2);

		// TODO check if individual with individualId was not deleted

		Assertions.assertTrue(
			false,
			"Individual was deleted even though another data source with " +
				"data on the individual exists");

		// TODO check if theres individuals related to deleted dataSourceId2

		Assertions.assertFalse(
			true,
			"Data source individual PK was not deleted on data source " +
				"deletion");

		Set<Field> fields = individual.getFields();

		Stream<Field> stream = fields.stream();

		Assertions.assertFalse(
			stream.anyMatch(
				field -> Objects.equals(field.getName(), "givenName")),
			"Individual field 'givenName' was not deleted");

		stream = fields.stream();

		Field emailField = stream.filter(
			field -> Objects.equals(field.getName(), "email")
		).findFirst(
		).orElse(
			null
		);

		Assertions.assertNotNull(emailField);

		Assertions.assertEquals(dataSourceId1, emailField.getDataSourceId());

		Assertions.assertEquals(
			lastEnrichmentDate, individual.getLastEnrichmentDate(),
			"Data source deletion should not count towards individual " +
				"enrichment");
	}

	@Disabled
	@Test
	public void testDeleteLiferayDataSource() throws Exception {
		DataSource dataSource1 = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource());

		_addActivityAndUserToLiferayDataSource(dataSource1);

		DataSource dataSource2 = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource());

		_addActivityAndUserToLiferayDataSource(dataSource2);

		dataSource1.setDeletionDate(
			DateUtil.toUTCDate(
				DateUtil.addDays(DateUtil.newDayDateString(), -1)));

		_dataSourceDog.deleteDataSource(dataSource1);

		// TODO Assert BQEvent and Assets are not deleted

		// TODO Assert individuals

	}

	@Test
	public void testUpdateDataSourceModifiesDataSourceName() {
		DataSource dataSource = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource(
				"foo", RandomTestUtil.randomURL()));

		DataSource liferayDataSource = FaroInfoTestUtil.buildLiferayDataSource(
			"bar", RandomTestUtil.randomURL());

		liferayDataSource.setId(dataSource.getId());
		liferayDataSource.setIsNew(Boolean.FALSE);

		dataSource = _dataSourceDog.updateDataSourceConfiguration(
			liferayDataSource);

		Assertions.assertEquals("bar", dataSource.getName());
	}

	@Test
	public void testUpdateDataSourceModifyingToDuplicateDataSourceName() {
		_dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource(
				"bar", RandomTestUtil.randomURL()));

		DataSource dataSource = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource(
				"foo", RandomTestUtil.randomURL()));

		DataSource liferayDataSource = FaroInfoTestUtil.buildLiferayDataSource(
			"bar", RandomTestUtil.randomURL());

		liferayDataSource.setId(dataSource.getId());

		Exception exception = Assertions.assertThrows(
			Exception.class,
			() -> _dataSourceDog.updateDataSourceConfiguration(
				liferayDataSource));

		MatcherAssert.assertThat(
			exception.getMessage(),
			CoreMatchers.containsString("Duplicate data source name"));
	}

	@Test
	public void testUpdateDataSourceWithoutModifyingDataSourceName() {
		String dataSourceName = "test";

		DataSource dataSource = _dataSourceDog.addDataSource(
			FaroInfoTestUtil.buildLiferayDataSource(
				dataSourceName, RandomTestUtil.randomURL()));

		String updatedURL = "https://foo.bar";

		DataSource liferayDataSource = FaroInfoTestUtil.buildLiferayDataSource(
			dataSourceName, updatedURL);

		liferayDataSource.setId(dataSource.getId());
		liferayDataSource.setIsNew(Boolean.FALSE);

		dataSource = _dataSourceDog.updateDataSourceConfiguration(
			liferayDataSource);

		Assertions.assertEquals(updatedURL, dataSource.getURL());
	}

	private void _addActivityAndUserToLiferayDataSource(DataSource dataSource) {
		if (dataSource == null) {
			return;
		}

		Long dataSourceId = dataSource.getId();

		if (dataSourceId == null) {
			return;
		}

		// TODO Add Individual related to dataSourceId

		// TODO Add BQEvent

		DXPEntity dxpEntity = new DXPEntity();

		dxpEntity.setDataSourceId(dataSourceId);
		dxpEntity.setFieldsJSONObject(
			JSONUtil.put(
				"contact",
				JSONUtil.put(
					"email", RandomTestUtil.randomEmailAddress()
				).put(
					"firstName", RandomTestUtil.randomString()
				).put(
					"jobTitle", RandomTestUtil.randomString()
				).put(
					"lastName", RandomTestUtil.randomString()
				).put(
					"subscription", RandomTestUtil.randomString()
				)
			).put(
				"modifiedDate", System.currentTimeMillis()
			).put(
				"userId", RandomTestUtil.randomNumber()
			).put(
				"uuid", RandomTestUtil.randomUUID()
			));

		_dxpEntityDog.addDXPEntity(dxpEntity, DXPEntity.Type.USER);
	}

	@Autowired
	private AssetRepository _assetRepository;

	@Autowired
	private BQCSVUserRepository _bqCSVUserRepository;

	@Autowired
	private ChannelDog _channelDog;

	@Autowired
	private DataSourceDog _dataSourceDog;

	@Autowired
	private DataSourceRepository _dataSourceRepository;

	@Autowired
	private DXPEntityDog _dxpEntityDog;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private SegmentDog _segmentDog;

}