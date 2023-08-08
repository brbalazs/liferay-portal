/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.ext.seo.rest.controller.api.data.source.v1.test;

import com.liferay.osb.asah.backend.ext.seo.model.CountrySearchKeywords;
import com.liferay.osb.asah.backend.ext.seo.model.SearchKeyword;
import com.liferay.osb.asah.backend.ext.seo.model.TrafficSource;
import com.liferay.osb.asah.backend.ext.seo.rest.controller.api.data.source.v1.RootRestController;
import com.liferay.osb.asah.backend.ext.seo.spring.OSBAsahBackendExtSEOSpringBootApplication;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.spring.http.Http;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.test.util.spring.OSBAsahRepositoryTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSQLTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSpringExtension;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author David Arques
 */
@ExtendWith(OSBAsahSpringExtension.class)
@SpringBootTest(classes = OSBAsahBackendExtSEOSpringBootApplication.class)
@TestExecutionListeners(
	mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
	value = {
		OSBAsahRepositoryTestExecutionListener.class,
		OSBAsahSQLTestExecutionListener.class
	}
)
@TestPropertySource(properties = "osb.asah.seo.semrush.api.key=4d5e9886")
public class RootRestControllerTest {

	@Test
	public void testGetTrafficSources() {
		Mockito.doAnswer(
			invocationOnMock -> JSONUtil.putAll(
				JSONUtil.put("valueKey", "United States")
			).toString()
		).when(
			_http
		).exchange(
			ArgumentMatchers.any(), ArgumentMatchers.contains("geolocations"),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> ResponseEntity.ok("Traffic\n3400\n")
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.contains("domain_organic_unique"),
			ArgumentMatchers.any(), ArgumentMatchers.any(HttpMethod.class),
			ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> ResponseEntity.ok(
				ResourceUtil.readResourceToString(
					"dependencies/semrush_url_organic.csv", this))
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.contains("url_organic"), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> ResponseEntity.ok(
				ResourceUtil.readResourceToString(
					"dependencies/semrush_url_adwords.csv", this))
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.contains("url_adwords"), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		List<TrafficSource> trafficSources =
			_rootRestController.getTrafficSources(RandomTestUtil.randomURL());

		Assertions.assertEquals(
			2, trafficSources.size(), trafficSources.toString());
		Assertions.assertEquals(
			new TrafficSource(
				Collections.singletonList(
					new CountrySearchKeywords(
						"us",
						Arrays.asList(
							new SearchKeyword("liferay", 1, 3600, 2880),
							new SearchKeyword("liferay portal", 1, 390, 312),
							new SearchKeyword("liferay inc", 1, 260, 208)))),
				"organic", 3400, 85.43D),
			trafficSources.get(0));
		Assertions.assertEquals(
			new TrafficSource(
				Collections.singletonList(
					new CountrySearchKeywords(
						"us",
						Arrays.asList(
							new SearchKeyword("dxp enterprises", 1, 4400, 206),
							new SearchKeyword(
								"intranet definition", 1, 4400, 205),
							new SearchKeyword("dxp", 1, 3600, 169)))),
				"paid", 580, 14.57D),
			trafficSources.get(1));
	}

	@Test
	public void testGetTrafficSourcesWithDomainOrganicUniqueSEMrushAPIError() {
		Mockito.doAnswer(
			invocationOnMock -> JSONUtil.putAll(
				JSONUtil.put("valueKey", "United States")
			).toString()
		).when(
			_http
		).exchange(
			ArgumentMatchers.any(), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> ResponseEntity.ok("")
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.any(), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> {
				ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(
					HttpStatus.INTERNAL_SERVER_ERROR);

				return bodyBuilder.build();
			}
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.contains("domain_organic_unique"),
			ArgumentMatchers.any(), ArgumentMatchers.any(HttpMethod.class),
			ArgumentMatchers.any()
		);

		Assertions.assertThrows(
			HttpClientErrorException.class,
			() -> _rootRestController.getTrafficSources(
				RandomTestUtil.randomURL()));
	}

	@Test
	public void testGetTrafficSourcesWithEmptyDatabases() {
		Mockito.doAnswer(
			invocationOnMock -> String.valueOf(new JSONArray())
		).when(
			_http
		).exchange(
			ArgumentMatchers.any(), ArgumentMatchers.contains("geolocations"),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		List<TrafficSource> trafficSources =
			_rootRestController.getTrafficSources(RandomTestUtil.randomURL());

		Assertions.assertEquals(
			2, trafficSources.size(), trafficSources.toString());
		Assertions.assertEquals(
			new TrafficSource(Collections.emptyList(), "organic", 0, 0D),
			trafficSources.get(0));
		Assertions.assertEquals(
			new TrafficSource(Collections.emptyList(), "paid", 0, 0D),
			trafficSources.get(1));
	}

	@Test
	public void testGetTrafficSourcesWithEmptyURL() {
		Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _rootRestController.getTrafficSources(""));
	}

	@Test
	public void testGetTrafficSourcesWithInvalidURL() {
		Mockito.doAnswer(
			invocationOnMock -> "[]"
		).when(
			_http
		).exchange(
			ArgumentMatchers.any(), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> ResponseEntity.ok()
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.any(), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _rootRestController.getTrafficSources(
				RandomTestUtil.randomString()));
	}

	@Test
	public void testGetTrafficSourcesWithMultipleDatabases() {
		Mockito.doAnswer(
			invocationOnMock -> JSONUtil.putAll(
				JSONUtil.put("valueKey", "Spain"),
				JSONUtil.put("valueKey", "United States")
			).toString()
		).when(
			_http
		).exchange(
			ArgumentMatchers.any(), ArgumentMatchers.contains("geolocations"),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> ResponseEntity.ok("Traffic\n3400\n")
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.contains("domain_organic_unique"),
			ArgumentMatchers.any(), ArgumentMatchers.any(HttpMethod.class),
			ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> ResponseEntity.ok(
				ResourceUtil.readResourceToString(
					"dependencies/semrush_url_organic.csv", this))
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.contains("url_organic"), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> ResponseEntity.ok(
				ResourceUtil.readResourceToString(
					"dependencies/semrush_url_adwords.csv", this))
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.contains("url_adwords"), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		List<TrafficSource> trafficSources =
			_rootRestController.getTrafficSources(RandomTestUtil.randomURL());

		Assertions.assertEquals(
			2, trafficSources.size(), trafficSources.toString());
		Assertions.assertEquals(
			new TrafficSource(
				Arrays.asList(
					new CountrySearchKeywords(
						"es",
						Arrays.asList(
							new SearchKeyword("liferay", 1, 3600, 2880),
							new SearchKeyword("liferay portal", 1, 390, 312),
							new SearchKeyword("liferay inc", 1, 260, 208))),
					new CountrySearchKeywords(
						"us",
						Arrays.asList(
							new SearchKeyword("liferay", 1, 3600, 2880),
							new SearchKeyword("liferay portal", 1, 390, 312),
							new SearchKeyword("liferay inc", 1, 260, 208)))),
				"organic", 6800, 85.43D),
			trafficSources.get(0));
		Assertions.assertEquals(
			new TrafficSource(
				Arrays.asList(
					new CountrySearchKeywords(
						"es",
						Arrays.asList(
							new SearchKeyword("dxp enterprises", 1, 4400, 206),
							new SearchKeyword(
								"intranet definition", 1, 4400, 205),
							new SearchKeyword("dxp", 1, 3600, 169))),
					new CountrySearchKeywords(
						"us",
						Arrays.asList(
							new SearchKeyword("dxp enterprises", 1, 4400, 206),
							new SearchKeyword(
								"intranet definition", 1, 4400, 205),
							new SearchKeyword("dxp", 1, 3600, 169)))),
				"paid", 1160, 14.57D),
			trafficSources.get(1));
	}

	@Test
	public void testGetTrafficSourcesWithNothingFound() {
		Mockito.doAnswer(
			invocationOnMock -> JSONUtil.putAll(
				JSONUtil.put("valueKey", "United States")
			).toString()
		).when(
			_http
		).exchange(
			ArgumentMatchers.any(), ArgumentMatchers.contains("geolocations"),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> ResponseEntity.ok("ERROR 50 :: NOTHING FOUND")
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.any(), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		List<TrafficSource> trafficSources =
			_rootRestController.getTrafficSources(RandomTestUtil.randomURL());

		Assertions.assertEquals(
			2, trafficSources.size(), trafficSources.toString());
		Assertions.assertEquals(
			new TrafficSource(
				Collections.singletonList(
					new CountrySearchKeywords("us", Collections.emptyList())),
				"organic", 0, 0D),
			trafficSources.get(0));
		Assertions.assertEquals(
			new TrafficSource(
				Collections.singletonList(
					new CountrySearchKeywords("us", Collections.emptyList())),
				"paid", 0, 0D),
			trafficSources.get(1));
	}

	@Test
	public void testGetTrafficSourcesWithNullURL() {
		Assertions.assertThrows(
			IllegalArgumentException.class,
			() -> _rootRestController.getTrafficSources(null));
	}

	@Test
	public void testGetTrafficSourcesWithSEMrushAPIError() {
		Mockito.doAnswer(
			invocationOnMock -> JSONUtil.putAll(
				JSONUtil.put("valueKey", "United States")
			).toString()
		).when(
			_http
		).exchange(
			ArgumentMatchers.any(), ArgumentMatchers.contains("geolocations"),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> {
				ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(
					HttpStatus.INTERNAL_SERVER_ERROR);

				return bodyBuilder.build();
			}
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.any(), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Assertions.assertThrows(
			HttpClientErrorException.class,
			() -> _rootRestController.getTrafficSources(
				RandomTestUtil.randomURL()));
	}

	@Test
	public void testGetTrafficSourcesWithURLAdwordsSEMrushAPIError() {
		Mockito.doAnswer(
			invocationOnMock -> JSONUtil.putAll(
				JSONUtil.put("valueKey", "United States")
			).toString()
		).when(
			_http
		).exchange(
			ArgumentMatchers.any(), ArgumentMatchers.contains("geolocations"),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> ResponseEntity.ok("Traffic\n3400\n")
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.contains("domain_organic_unique"),
			ArgumentMatchers.any(), ArgumentMatchers.any(HttpMethod.class),
			ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> ResponseEntity.ok(
				ResourceUtil.readResourceToString(
					"dependencies/semrush_url_organic.csv", this))
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.contains("url_organic"), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> {
				ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(
					HttpStatus.INTERNAL_SERVER_ERROR);

				return bodyBuilder.build();
			}
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.contains("url_adwords"), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Assertions.assertThrows(
			HttpClientErrorException.class,
			() -> _rootRestController.getTrafficSources(
				RandomTestUtil.randomURL()));
	}

	@Test
	public void testGetTrafficSourcesWithURLOrganicSEMrushAPIError() {
		Mockito.doAnswer(
			invocationOnMock -> JSONUtil.putAll(
				JSONUtil.put("valueKey", "United States")
			).toString()
		).when(
			_http
		).exchange(
			ArgumentMatchers.any(), ArgumentMatchers.contains("geolocations"),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> {
				ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(
					HttpStatus.INTERNAL_SERVER_ERROR);

				return bodyBuilder.build();
			}
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.contains("url_organic"), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Mockito.doAnswer(
			invocationOnMock -> ResponseEntity.ok(
				ResourceUtil.readResourceToString(
					"dependencies/semrush_url_adwords.csv", this))
		).when(
			_http
		).exchangeResponseEntity(
			ArgumentMatchers.contains("url_adwords"), ArgumentMatchers.any(),
			ArgumentMatchers.any(HttpMethod.class), ArgumentMatchers.any()
		);

		Assertions.assertThrows(
			HttpClientErrorException.class,
			() -> _rootRestController.getTrafficSources(
				RandomTestUtil.randomURL()));
	}

	@MockBean
	private Http _http;

	@Autowired
	private RootRestController _rootRestController;

}