/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.MetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetMetric;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.BlogMetric;
import com.liferay.osb.asah.backend.model.BlogMetricType;
import com.liferay.osb.asah.backend.model.DocumentLibraryMetric;
import com.liferay.osb.asah.backend.model.DocumentLibraryMetricType;
import com.liferay.osb.asah.backend.model.JournalMetricType;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.model.PageMetric;
import com.liferay.osb.asah.backend.model.SiteMetric;
import com.liferay.osb.asah.backend.model.SiteMetricType;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQBlogRepository;
import com.liferay.osb.asah.test.util.repository.CrudBQDocumentLibraryRepository;
import com.liferay.osb.asah.test.util.repository.CrudBQPageRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;

/**
 * @author Lino Alves
 */
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class MetricDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQBlogRepository.class,
		resourcePath = "osbasahcerebroinfo/asset_metric_blog_info_1.json"
	)
	@Test
	public void testBlogMetricShouldContainURL() {
		AssetMetric assetMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				"1", AssetType.BLOG, null, TimeRange.LAST_7_DAYS, null));

		Assertions.assertNotNull(assetMetric);
		Assertions.assertEquals("1", assetMetric.getAssetId());

		List<String> urls = assetMetric.getURLs();

		Assertions.assertEquals(1, urls.size(), urls.toString());
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQBlogRepository.class,
		resourcePath = "osbasahcerebroinfo/asset_metric_blog_info_1.json"
	)
	@Test
	public void testBlogMetricShouldReturnEmptyListIfNoURLsAreFetched() {
		AssetMetric assetMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				"1", AssetType.BLOG, null, TimeRange.LAST_24_HOURS, null));

		Assertions.assertNotNull(assetMetric);
		Assertions.assertEquals("1", assetMetric.getAssetId());

		List<String> urls = assetMetric.getURLs();

		Assertions.assertTrue(urls.isEmpty());
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQBlogRepository.class,
		resourcePath = "osbasahcerebroinfo/asset_metric_blog_average_info.json"
	)
	@Test
	public void testBlogRatingAverage() {
		BlogMetric blogMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				"1", AssetType.BLOG, null, TimeRange.LAST_24_HOURS, null),
			new HashSet<String>() {
				{
					add(BlogMetricType.RATINGS.getName());
				}
			});

		Assertions.assertNotNull(blogMetric);

		Metric ratingsMetric = blogMetric.getRatingsMetric();

		Assertions.assertEquals(0.8, ratingsMetric.getValue(), 0.01);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQBlogRepository.class,
		resourcePath = "osbasahcerebroinfo/asset_metric_blog_info_2.json"
	)
	@Test
	public void testBlogRatingMetric() {
		BlogMetric blogMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				"1", AssetType.BLOG, null, TimeRange.LAST_7_DAYS, null),
			new HashSet<String>() {
				{
					add(BlogMetricType.RATINGS.getName());
				}
			});

		Assertions.assertNotNull(blogMetric);

		Metric ratingsMetric = blogMetric.getRatingsMetric();

		Assertions.assertEquals(0.6, ratingsMetric.getValue(), 0.1);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQBlogRepository.class,
		resourcePath = "osbasahcerebroinfo/asset_metric_blog_info_1.json"
	)
	@Test
	public void testBlogRatingMetricShouldNotBeNegative() {
		BlogMetric blogMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				"2", AssetType.BLOG, null, TimeRange.LAST_24_HOURS, null),
			new HashSet<String>() {
				{
					add(BlogMetricType.RATINGS.getName());
				}
			});

		Assertions.assertNotNull(blogMetric);

		Metric ratingsMetric = blogMetric.getRatingsMetric();

		Assertions.assertEquals(0, ratingsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testCTAClicksMetrics() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, null, TimeRange.LAST_7_DAYS, null),
			new HashSet<String>() {
				{
					add(PageMetricType.CTA_CLICKS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric ctrMetric = pageMetric.getCTAClicksMetric();

		Assertions.assertEquals(3, ctrMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQDocumentLibraryRepository.class,
		resourcePath = "osbasahcerebroinfo/document_library_info.json"
	)
	@Test
	public void testDocumentLibraryMetrics() {
		DocumentLibraryMetric documentLibraryMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				"1", AssetType.DOCUMENT, null, TimeRange.LAST_7_DAYS, null),
			new HashSet<String>() {
				{
					add(DocumentLibraryMetricType.RATINGS.getName());
				}
			});

		Assertions.assertNotNull(documentLibraryMetric);

		Metric ratingsMetric = documentLibraryMetric.getRatingsMetric();

		Assertions.assertEquals(0.5, ratingsMetric.getValue(), 0.1);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testGetAssetMetricLast7Days() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, null, TimeRange.LAST_7_DAYS, null),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(9, viewsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testGetAssetMetricLast24Hours() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, null, TimeRange.LAST_24_HOURS, null),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(0, viewsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testGetAssetMetricLast28Days() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, null, TimeRange.LAST_28_DAYS, null),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(11, viewsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testGetAssetMetricLast30Days() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, null, TimeRange.LAST_30_DAYS, null),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(11, viewsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testGetAssetMetricLast90Days() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, null, TimeRange.LAST_90_DAYS, null),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(14, viewsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testGetAssetMetricLast180Days() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, null, TimeRange.LAST_180_DAYS, null),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(15, viewsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testGetAssetMetricLastCustomRange() {
		LocalDateTime localDateTime = LocalDateTime.now();

		LocalDateTime endLocalDateTime = localDateTime.minusDays(20);

		LocalDateTime startLocalDateTime = endLocalDateTime.minusDays(180);

		TimeRange timeRange = TimeRange.of(
			endLocalDateTime, startLocalDateTime);

		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(null, AssetType.PAGE, null, timeRange, null),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(6, viewsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testGetAssetMetricLastYear() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, null, TimeRange.LAST_YEAR, null),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(17, viewsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_3.json"
	)
	@Test
	public void testGetAssetMetricMissingSessionId() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, null, TimeRange.LAST_7_DAYS, null),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(6, viewsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_4.json"
	)
	@Test
	public void testgetAssetMetricsCount() {
		SearchQueryContext searchQueryContext = _createSearchQuery(
			null, AssetType.PAGE, null, TimeRange.LAST_7_DAYS, null);

		searchQueryContext.setKeywords("Beryl");

		Assertions.assertEquals(
			3, _metricDog.getAssetMetricsCount(searchQueryContext));

		searchQueryContext.setKeywords("Beryl Pro");

		Assertions.assertEquals(
			2, _metricDog.getAssetMetricsCount(searchQueryContext));

		searchQueryContext.setKeywords("Beryl Processing Newsletter");

		Assertions.assertEquals(
			1, _metricDog.getAssetMetricsCount(searchQueryContext));

		searchQueryContext.setKeywords("Irrigation");

		Assertions.assertEquals(
			1, _metricDog.getAssetMetricsCount(searchQueryContext));
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testGetAssetMetricYesterday() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, null, TimeRange.YESTERDAY, null),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(6, viewsMetric.getValue(), 0);
	}

	@BQSQLResource(
		resourcePath = "journal_asset_metric_views_histogram_last_14_days.sql"
	)
	@Test
	public void testJournalDefaultMetric() {
		AssetMetric assetMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				"e131fabc", "1", AssetType.JOURNAL, TimeRange.LAST_7_DAYS),
			SetUtil.of(JournalMetricType.VIEWS.getName()));

		Assertions.assertNotNull(assetMetric);

		Assertions.assertEquals("e131fabc", assetMetric.getAssetId());

		Metric metric = assetMetric.getDefaultMetric();

		Assertions.assertEquals(5, metric.getPreviousValue(), 0);
		Assertions.assertEquals(3, metric.getValue(), 0);
	}

	@BQSQLResource(
		resourcePath = "journal_asset_metric_views_histogram_last_14_days.sql"
	)
	@Test
	public void testJournalMetrics() {
		List<AssetMetric> assetMetrics = _metricDog.getAssetMetrics(
			0,
			_createSearchQuery(
				null, "1", AssetType.JOURNAL, TimeRange.LAST_7_DAYS),
			new HashSet<String>() {
				{
					add(JournalMetricType.VIEWS.getName());
				}
			},
			10, Sort.desc(JournalMetricType.VIEWS.getName()));

		Assertions.assertEquals(
			1, assetMetrics.size(), assetMetrics.toString());

		AssetMetric assetMetric = assetMetrics.get(0);

		Assertions.assertEquals("e131fabc", assetMetric.getAssetId());

		Metric metric = assetMetric.getDefaultMetric();

		Assertions.assertEquals(3, metric.getValue(), 0);
		Assertions.assertNull(metric.getPreviousValue());
	}

	@BQSQLResource(
		resourcePath = "journal_asset_metric_views_histogram_last_14_days.sql"
	)
	@Test
	public void testJournalMetricsCount() {
		Assertions.assertEquals(
			1,
			_metricDog.getAssetMetricsCount(
				_createSearchQuery(
					null, "1", AssetType.JOURNAL, TimeRange.LAST_7_DAYS)));
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testPageAssetIdsFilter() {
		Set<String> assetIds = Collections.singleton(
			"http://192.168.108.90:8080/");

		List<PageMetric> pageMetrics = _metricDog.getAssetMetrics(
			0, new SearchQueryContext(),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			},
			10, null);

		Assertions.assertNotNull(pageMetrics);

		Assertions.assertEquals(1, pageMetrics.size(), 0);

		PageMetric pageMetric = pageMetrics.get(0);

		Assertions.assertTrue(assetIds.contains(pageMetric.getAssetId()));

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(6, viewsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testPagesExperimentFilter() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, 10L, TimeRange.LAST_7_DAYS, null),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(3, viewsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testPagesVariantFilter() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, null, TimeRange.LAST_7_DAYS, "2"),
			new HashSet<String>() {
				{
					add(PageMetricType.VIEWS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(2, viewsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testPagesVisitorMetrics() {
		PageMetric pageMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				null, AssetType.PAGE, null, TimeRange.LAST_7_DAYS, null),
			new HashSet<String>() {
				{
					add(PageMetricType.VISITORS.getName());
				}
			});

		Assertions.assertNotNull(pageMetric);

		Metric visitorsMetric = pageMetric.getVisitorsMetric();

		Assertions.assertEquals(4, visitorsMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testSessionsMetrics() {
		SiteMetric siteMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				"1", AssetType.SITE, null, TimeRange.LAST_7_DAYS, null),
			new HashSet<String>() {
				{
					add(SiteMetricType.SESSION_DURATION.getName());
					add(SiteMetricType.SESSIONS.getName());
					add(SiteMetricType.SESSIONS_PER_VISITOR.getName());
				}
			});

		Assertions.assertNotNull(siteMetric);

		Metric sessionDurationMetric = siteMetric.getSessionDurationMetric();

		Assertions.assertEquals(445229, sessionDurationMetric.getValue(), 0);

		Metric sessionsMetric = siteMetric.getSessionsMetric();

		Assertions.assertEquals(5, sessionsMetric.getValue(), 0);

		Metric sessionsPerVisitorMetric =
			siteMetric.getSessionsPerVisitorMetric();

		Assertions.assertEquals(
			1.6666666666666667, sessionsPerVisitorMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testSiteBounceRateMetric() {
		SiteMetric siteMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				"1", AssetType.SITE, null, TimeRange.LAST_7_DAYS, null),
			new HashSet<String>() {
				{
					add(SiteMetricType.BOUNCE_RATE.getName());
				}
			});

		Assertions.assertNotNull(siteMetric);

		Metric bounceRateMetric = siteMetric.getBounceRateMetric();

		Assertions.assertEquals(
			0.16666666666666666, bounceRateMetric.getValue(), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/pages_info_1.json"
	)
	@Test
	public void testSiteVisitorsMetrics() {
		SiteMetric siteMetric = _metricDog.getAssetMetric(
			_createSearchQuery(
				"1", AssetType.SITE, null, TimeRange.LAST_7_DAYS, null),
			new HashSet<String>() {
				{
					add(SiteMetricType.ANONYMOUS_VISITORS.getName());
					add(SiteMetricType.KNOWN_VISITORS.getName());
					add(SiteMetricType.VISITORS.getName());
				}
			});

		Assertions.assertNotNull(siteMetric);

		Metric anonymousVisitorsMetric =
			siteMetric.getAnonymousVisitorsMetric();

		Assertions.assertEquals(2, anonymousVisitorsMetric.getValue(), 0);

		Metric knownVisitorsMetric = siteMetric.getKnownVisitorsMetric();

		Assertions.assertEquals(1, knownVisitorsMetric.getValue(), 0);

		Metric visitorsMetric = siteMetric.getVisitorsMetric();

		Assertions.assertEquals(3, visitorsMetric.getValue(), 0);
	}

	private SearchQueryContext _createSearchQuery(
		String assetId, AssetType assetType, Long experimentId,
		TimeRange timeRange, String variantId) {

		return new SearchQueryContext(assetId, assetType) {
			{
				setExperimentId(experimentId);
				setTimeRange(timeRange);
				setVariantId(variantId);
			}
		};
	}

	private SearchQueryContext _createSearchQuery(
		String assetId, String channelId, AssetType assetType,
		TimeRange timeRange) {

		return new SearchQueryContext(assetId, assetType) {
			{
				setChannelId(channelId);
				setTimeRange(timeRange);
			}
		};
	}

	@Autowired
	private MetricDog _metricDog;

}