/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.faro;

import com.liferay.osb.asah.common.entity.BQCSVUser;
import com.liferay.osb.asah.common.entity.BQDataSourceUser;
import com.liferay.osb.asah.common.entity.BQMembership;
import com.liferay.osb.asah.common.entity.BQMembershipChange;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.ExperimentStatus;
import com.liferay.osb.asah.common.model.Field;
import com.liferay.osb.asah.common.model.Goal;
import com.liferay.osb.asah.common.model.GoalMetric;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.util.Collections;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Michael Bowerman
 * @author Rachael Koestartyo
 */
public class FaroInfoTestUtil {

	public static JSONObject buildAssetJSONObject(
		String assetType, Long dataSourceId) {

		return buildAssetJSONObject(
			assetType, Long.parseLong(RandomStringUtils.randomNumeric(4)),
			dataSourceId);
	}

	public static JSONObject buildAssetJSONObject(
		String assetType, Long channelId, Long dataSourceId) {

		return JSONUtil.put(
			"assetType", assetType
		).put(
			"channelIds", JSONUtil.put(String.valueOf(channelId))
		).put(
			"dataSourceAssetPK", String.valueOf(RandomTestUtil.randomNumber())
		).put(
			"dataSourceId", String.valueOf(dataSourceId)
		).put(
			"name", RandomTestUtil.randomMultipleWordString(5, 20)
		);
	}

	public static BQCSVUser buildBQCSVUser(
		Long bqCSVUserId, String dataSourceUserPK, Long dataSourceId) {

		BQCSVUser bqCSVUser = new BQCSVUser();

		bqCSVUser.setDataSourceUserPK(dataSourceUserPK);
		bqCSVUser.setDataSourceId(dataSourceId);
		bqCSVUser.setId(bqCSVUserId);

		return bqCSVUser;
	}

	public static BQMembership buildBQMembership(
		String identityId, Long segmentId) {

		Date date = new Date();

		BQMembership bqMembership = new BQMembership();

		bqMembership.setCreateDate(date);
		bqMembership.setIdentityId(identityId);
		bqMembership.setModifiedDate(date);
		bqMembership.setSegmentId(segmentId);
		bqMembership.setStatus("ACTIVE");

		return bqMembership;
	}

	public static BQMembershipChange buildBQMembershipChange(Long segmentId) {
		BQMembershipChange bqMembershipChange = new BQMembershipChange();

		bqMembershipChange.setCreateDate(new Date());
		bqMembershipChange.setIdentitiesCount(RandomUtils.nextLong());
		bqMembershipChange.setSegmentId(segmentId);

		return bqMembershipChange;
	}

	public static JSONObject buildChannelJSONObject(
		String dataSourceId, String channelType) {

		return JSONUtil.put(
			"channelType", channelType
		).put(
			"dataSourceId", dataSourceId
		).put(
			"groups",
			JSONUtil.putAll(
				JSONUtil.put(
					"id", RandomTestUtil.randomId()
				).put(
					"name", RandomTestUtil.randomString()
				),
				JSONUtil.put(
					"id", RandomTestUtil.randomId()
				).put(
					"name", RandomTestUtil.randomString()
				))
		);
	}

	public static DataSource buildCSVDataSource(Long dataSourceId) {
		DataSource dataSource = new DataSource();

		dataSource.setAuthorId(
			Long.valueOf(RandomStringUtils.randomNumeric(5, 7)));
		dataSource.setAuthorName(RandomTestUtil.randomFullName());

		Date date = new Date();

		dataSource.setCreateDate(date);

		dataSource.setId(dataSourceId);
		dataSource.setIsNew(Boolean.TRUE);
		dataSource.setModifiedDate(date);
		dataSource.setName(RandomTestUtil.randomMultipleWordString(5, 20));
		dataSource.setProviderType("CSV");
		dataSource.setStatus("ACTIVE");

		return dataSource;
	}

	public static Segment buildDynamicSegment(
		Long channelId, String filterString) {

		Segment segment = new Segment();

		if (channelId != null) {
			segment.setChannelId(channelId);
		}

		Date date = new Date();

		segment.setCreateDate(date);

		segment.setFilter(filterString);
		segment.setModifiedDate(date);
		segment.setName(RandomTestUtil.randomString());
		segment.setScope("PROJECT");
		segment.setStatus("ACTIVE");
		segment.setType(Segment.Type.DYNAMIC);

		return segment;
	}

	public static Experiment buildExperiment(
		ExperimentStatus experimentStatus, GoalMetric goalMetric, Long id) {

		Experiment experiment = new Experiment();

		experiment.setChannelId(1L);
		experiment.setDataSourceId(1L);
		experiment.setExperimentStatus(experimentStatus);
		experiment.setId(id);

		Goal goal = new Goal();

		goal.setGoalMetric(goalMetric);

		experiment.setGoal(goal);

		return experiment;
	}

	public static Individual buildIndividual(
		DataSource dataSource, Long individualId) {

		return buildIndividual(
			Long.parseLong(RandomStringUtils.randomNumeric(4)), dataSource,
			individualId);
	}

	public static Individual buildIndividual(
		Long channelId, DataSource dataSource, Long individualId) {

		Long dataSourceId = dataSource.getId();
		String dataId = RandomTestUtil.randomUUID();
		Date date = new Date();

		String providerType = dataSource.getProviderType();

		String sourceName = "email";

		if (providerType.equals("LIFERAY")) {
			sourceName = "emailAddress";
		}

		Individual individual = new Individual();

		BQDataSourceUser bqDataSourceUser = new BQDataSourceUser();

		bqDataSourceUser.setAccountPKs(Collections.emptySet());
		bqDataSourceUser.setDataSourceId(dataSourceId);
		bqDataSourceUser.setUserId(individualId);
		bqDataSourceUser.setUserPKs(Collections.singleton(dataId));

		individual.setBQDataSourceUsers(
			Collections.singleton(bqDataSourceUser));

		individual.setChannelIds(Collections.singleton(channelId));
		individual.setCreateDate(date);
		individual.setId(individualId.toString());
		individual.setModifiedDate(date);
		individual.setSegmentIds(Collections.emptySet());

		Field field = new Field();

		field.setContext("demographics");
		field.setDataSourceId(dataSourceId);
		field.setDataSourceName(dataSource.getName());
		field.setFieldType("http://schema.org/email");
		field.setModifiedDate(date);
		field.setName("email");
		field.setOwnerId(individualId);
		field.setOwnerType("individual");
		field.setSourceName(sourceName);
		field.setValue(RandomTestUtil.randomEmailAddress());

		individual.setFields(Collections.singleton(field));

		return individual;
	}

	public static Field buildIndividualField(
		DataSource dataSource, String fieldName, String fieldType,
		String fieldValue, Individual individual, String sourceName) {

		Field field = new Field();

		field.setContext("demographics");
		field.setDataSourceId(dataSource.getId());
		field.setDataSourceName(dataSource.getName());
		field.setFieldType(fieldType);
		field.setId(Long.parseLong(RandomStringUtils.randomNumeric(4)));
		field.setModifiedDate(new Date());
		field.setName(fieldName);
		field.setOwnerId(Long.valueOf(individual.getId()));
		field.setOwnerType("individual");
		field.setSourceName(sourceName);
		field.setValue(fieldValue);

		return field;
	}

	public static DataSource buildLiferayDataSource() {
		return buildLiferayDataSource(
			RandomTestUtil.randomMultipleWordString(5, 20),
			RandomTestUtil.randomURL());
	}

	public static DataSource buildLiferayDataSource(String name, String url) {
		return buildLiferayDataSource("Token Authentication", name, url);
	}

	public static DataSource buildLiferayDataSource(
		String authenticationType, String name, String url) {

		Date date = new Date();

		JSONObject authorJSONObject = _getAuthorJSONObject();
		JSONObject credentialsJSONObject = _getCredentialsJSONObject(
			authenticationType);

		DataSource dataSource = new DataSource();

		dataSource.setAuthorId(authorJSONObject.getLong("id"));
		dataSource.setAuthorName(authorJSONObject.getString("name"));
		dataSource.setCreateDate(date);
		dataSource.setCredentialType(authenticationType);
		dataSource.setIsNew(Boolean.TRUE);
		dataSource.setModifiedDate(date);
		dataSource.setName(name);
		dataSource.setURL(url);
		dataSource.setState("CREDENTIALS_VALID");
		dataSource.setStatus("ACTIVE");

		if (authenticationType.equals("Basic Authentication")) {
			dataSource.setLogin(credentialsJSONObject.getString("login"));
			dataSource.setPassword(credentialsJSONObject.getString("password"));
		}
		else if (authenticationType.equals("OAuth 2 Authentication")) {
			JSONObject ownerJSONObject = credentialsJSONObject.getJSONObject(
				"oAuthOwner");

			dataSource.setOAuthClientId(
				credentialsJSONObject.getString("oAuthClientId"));
			dataSource.setOAuthClientSecret(
				credentialsJSONObject.getString("oAuthClientSecret"));
			dataSource.setOAuthOwnerEmailAddress(
				ownerJSONObject.getString("email"));
			dataSource.setOAuthOwnerName(ownerJSONObject.getString("name"));
			dataSource.setOAuthRefreshToken(
				credentialsJSONObject.getString("oAuthRefreshToken"));
		}
		else if (authenticationType.equals("Token Authentication")) {
			dataSource.setPrivateKey(
				credentialsJSONObject.getString("privateKey"));
			dataSource.setPublicKey(
				credentialsJSONObject.getString("publicKey"));
		}

		DataSource.Provider provider = new DataSource.Provider();

		DataSource.AnalyticsConfiguration analyticsConfiguration =
			new DataSource.AnalyticsConfiguration();

		analyticsConfiguration.setEnableAllSites(Boolean.TRUE);
		analyticsConfiguration.setDataSourceSites(Collections.emptySet());

		provider.setAnalyticsConfiguration(analyticsConfiguration);

		DataSource.ContactsConfiguration contactsConfiguration =
			new DataSource.ContactsConfiguration();

		contactsConfiguration.setEnableAllContacts(Boolean.TRUE);
		contactsConfiguration.setDataSourceOrganizations(
			Collections.emptySet());
		contactsConfiguration.setDataSourceUserGroups(Collections.emptySet());

		provider.setContactsConfiguration(contactsConfiguration);

		provider.setType("LIFERAY");

		dataSource.setProvider(provider);
		dataSource.setProviderType("LIFERAY");

		return dataSource;
	}

	public static JSONObject buildPageAssetJSONObject(Long dataSourceId) {
		return buildPageAssetJSONObject(
			dataSourceId, _randomKeywordsJSONArray());
	}

	public static JSONObject buildPageAssetJSONObject(
		Long dataSourceId, JSONArray keywordsJSONArray) {

		return JSONUtil.put(
			"assetType", "Page"
		).put(
			"canonicalUrl", RandomTestUtil.randomURL()
		).put(
			"dataSourceAssetPK", RandomTestUtil.randomURL()
		).put(
			"dataSourceId", String.valueOf(dataSourceId)
		).put(
			"keywords", keywordsJSONArray
		).put(
			"name", RandomTestUtil.randomMultipleWordString(5, 20)
		);
	}

	public static Segment buildStaticSegment() {
		Segment segment = new Segment();

		Date date = new Date();

		segment.setCreateDate(date);
		segment.setModifiedDate(date);

		segment.setName(RandomTestUtil.randomString());
		segment.setScope("PROJECT");
		segment.setStatus("ACTIVE");
		segment.setType(Segment.Type.STATIC);

		return segment;
	}

	private static JSONObject _getAuthorJSONObject() {
		return JSONUtil.put(
			"id", RandomStringUtils.randomNumeric(5, 7)
		).put(
			"name", RandomTestUtil.randomFullName()
		);
	}

	private static JSONObject _getBasicCredentialsJSONObject(
		String login, String password) {

		return JSONUtil.put(
			"login", login
		).put(
			"password", password
		).put(
			"type", "Basic Authentication"
		);
	}

	private static JSONObject _getCredentialsJSONObject(String type) {
		if (type.equals("Basic Authentication")) {
			return _getBasicCredentialsJSONObject(
				RandomTestUtil.randomEmailAddress(),
				RandomStringUtils.randomAlphanumeric(6, 10));
		}

		if (type.equals("OAuth 2 Authentication")) {
			return _getOAuth2CredentialsJSONObject();
		}

		if (type.equals("Token Authentication")) {
			return _getTokenAuthenticationCredentialsJSONObject();
		}

		throw new IllegalArgumentException(
			"Unsupported authentication type: " + type);
	}

	private static JSONObject _getOAuth2CredentialsJSONObject() {
		return JSONUtil.put(
			"oAuthClientId", "id-" + RandomTestUtil.randomUUID()
		).put(
			"oAuthClientSecret", "secret-" + RandomTestUtil.randomUUID()
		).put(
			"oAuthOwner",
			JSONUtil.put(
				"emailAddress", RandomTestUtil.randomEmailAddress()
			).put(
				"name", RandomTestUtil.randomFullName()
			)
		).put(
			"oAuthRefreshToken",
			RandomTestUtil.randomHexString(RandomUtils.nextInt(59, 65))
		).put(
			"type", "OAuth 2 Authentication"
		);
	}

	private static JSONObject _getTokenAuthenticationCredentialsJSONObject() {
		return JSONUtil.put(
			"privateKey", RandomTestUtil.randomUUID()
		).put(
			"publicKey", RandomTestUtil.randomUUID()
		).put(
			"type", "Token Authentication"
		);
	}

	private static JSONArray _randomKeywordsJSONArray() {
		JSONArray keywordsJSONArray = new JSONArray();
		int length = RandomUtils.nextInt(3, 10);
		String[] types = {"keyword", "title", "description"};

		for (int i = 0; i < length; i++) {
			keywordsJSONArray.put(
				JSONUtil.put(
					"keyword", RandomStringUtils.randomAlphabetic(3, 10)
				).put(
					"type", types[RandomUtils.nextInt(0, types.length)]
				));
		}

		return keywordsJSONArray;
	}

}