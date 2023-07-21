/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.test;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.portal.kernel.comment.Comment;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.IndexerRegistry;
import com.liferay.portal.kernel.search.RelatedSearchResult;
import com.liferay.portal.kernel.search.SearchResult;
import com.liferay.portal.kernel.search.result.SearchResultTranslator;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.test.util.PropsTestUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactory;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;

/**
 * @author André de Oliveira
 */
public abstract class BaseSearchResultUtilTestCase extends PowerMockito {

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		setUpRegistryUtil();

		setUpClassNameLocalService();
		setUpFastDateFormatFactoryUtil();
		setUpIndexerRegistry();
		setUpPropsUtil();
		setUpSearchResultTranslator();
	}

	protected void assertEmptyCommentRelatedSearchResults(
		SearchResult searchResult) {

		List<RelatedSearchResult<Comment>> commentRelatedSearchResults =
			searchResult.getCommentRelatedSearchResults();

		Assert.assertTrue(
			commentRelatedSearchResults.toString(),
			commentRelatedSearchResults.isEmpty());
	}

	protected void assertEmptyFileEntryRelatedSearchResults(
		SearchResult searchResult) {

		List<RelatedSearchResult<FileEntry>> fileEntryRelatedSearchResults =
			searchResult.getFileEntryRelatedSearchResults();

		Assert.assertTrue(
			fileEntryRelatedSearchResults.toString(),
			fileEntryRelatedSearchResults.isEmpty());
	}

	protected void assertEmptyVersions(SearchResult searchResult) {
		List<String> versions = searchResult.getVersions();

		Assert.assertTrue(versions.toString(), versions.isEmpty());
	}

	protected SearchResult assertOneSearchResult(Document document) {
		List<SearchResult> searchResults = SearchTestUtil.getSearchResults(
			searchResultTranslator, document);

		Assert.assertEquals(searchResults.toString(), 1, searchResults.size());

		return searchResults.get(0);
	}

	protected abstract SearchResultTranslator createSearchResultTranslator();

	protected void setUpClassNameLocalService() throws Exception {
		ClassName className = Mockito.mock(ClassName.class);

		when(
			classNameLocalService.getClassName(
				SearchTestUtil.ATTACHMENT_OWNER_CLASS_NAME_ID)
		).thenReturn(
			className
		);

		when(
			className.getClassName()
		).thenReturn(
			SearchTestUtil.ATTACHMENT_OWNER_CLASS_NAME
		);
	}

	protected void setUpFastDateFormatFactoryUtil() {
		FastDateFormatFactoryUtil fastDateFormatFactoryUtil =
			new FastDateFormatFactoryUtil();

		fastDateFormatFactoryUtil.setFastDateFormatFactory(
			mock(FastDateFormatFactory.class));
	}

	protected void setUpIndexerRegistry() {
		Registry registry = RegistryUtil.getRegistry();

		registry.registerService(
			IndexerRegistry.class, new TestIndexerRegistry());
	}

	protected void setUpPropsUtil() {
		PropsTestUtil.setProps(Collections.emptyMap());
	}

	protected void setUpRegistryUtil() {
		RegistryUtil.setRegistry(new BasicRegistryImpl());

		mockStatic(
			AssetRendererFactoryRegistryUtil.class, Mockito.CALLS_REAL_METHODS);
	}

	protected void setUpSearchResultTranslator() {
		searchResultTranslator = createSearchResultTranslator();
	}

	@Mock
	@SuppressWarnings("rawtypes")
	protected AssetRenderer assetRenderer;

	@Mock
	protected AssetRendererFactory<?> assetRendererFactory;

	@Mock
	protected ClassNameLocalService classNameLocalService;

	protected SearchResultTranslator searchResultTranslator;

}