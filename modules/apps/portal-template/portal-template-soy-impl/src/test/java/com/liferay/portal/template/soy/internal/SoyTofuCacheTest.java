/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template.soy.internal;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.tofu.SoyTofu;

import com.liferay.portal.kernel.template.TemplateResource;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Bruno Basto
 */
public class SoyTofuCacheTest {

	@Before
	public void setUp() throws Exception {
		_soyTestHelper.setUp();

		_soyTofuCacheHandler = new SoyTofuCacheHandler(
			_soyTestHelper.mockPortalCache());
	}

	@After
	public void tearDown() {
		_soyTestHelper.tearDown();
	}

	@Test
	public void testCacheHit() throws Exception {
		List<TemplateResource> templateResources =
			_soyTestHelper.getTemplateResources(
				Arrays.asList("simple.soy", "context.soy"));

		SoyFileSet soyFileSet = _soyTestHelper.getSoyFileSet(templateResources);

		SoyTofu soyTofu = Mockito.mock(SoyTofu.class);

		_soyTofuCacheHandler.add(templateResources, soyFileSet, soyTofu);

		Assert.assertNotNull(_soyTofuCacheHandler.get(templateResources));
	}

	@Test
	public void testCacheMiss() throws Exception {
		List<TemplateResource> templateResources =
			_soyTestHelper.getTemplateResources(
				Arrays.asList("simple.soy", "context.soy"));

		SoyFileSet soyFileSet = _soyTestHelper.getSoyFileSet(templateResources);

		SoyTofu soyTofu = Mockito.mock(SoyTofu.class);

		_soyTofuCacheHandler.add(templateResources, soyFileSet, soyTofu);

		List<TemplateResource> templateResourcesA =
			_soyTestHelper.getTemplateResources(Arrays.asList("context.soy"));

		Assert.assertNull(_soyTofuCacheHandler.get(templateResourcesA));
	}

	@Test
	public void testRemoveAny() throws Exception {
		List<TemplateResource> cachedTemplateResources =
			_soyTestHelper.getTemplateResources(
				Arrays.asList(
					"simple.soy", "context.soy", "multi-context.soy"));

		SoyFileSet soyFileSet = _soyTestHelper.getSoyFileSet(
			cachedTemplateResources);

		SoyTofu soyTofu = Mockito.mock(SoyTofu.class);

		_soyTofuCacheHandler.add(cachedTemplateResources, soyFileSet, soyTofu);

		List<TemplateResource> templateResources =
			_soyTestHelper.getTemplateResources(Arrays.asList("context.soy"));

		_soyTofuCacheHandler.removeIfAny(templateResources);

		Assert.assertNull(_soyTofuCacheHandler.get(templateResources));
	}

	@Test
	public void testRemoveAnyWithMultipleEntries() throws Exception {
		List<TemplateResource> cachedTemplateResourcesA =
			_soyTestHelper.getTemplateResources(Arrays.asList("simple.soy"));

		SoyFileSet soyFileSetA = _soyTestHelper.getSoyFileSet(
			cachedTemplateResourcesA);

		SoyTofu soyTofuA = Mockito.mock(SoyTofu.class);

		_soyTofuCacheHandler.add(
			cachedTemplateResourcesA, soyFileSetA, soyTofuA);

		Assert.assertNotNull(
			_soyTofuCacheHandler.get(cachedTemplateResourcesA));

		List<TemplateResource> cachedTemplateResourcesB =
			_soyTestHelper.getTemplateResources(
				Arrays.asList("context.soy", "multi-context.soy"));

		SoyFileSet soyFileSetB = _soyTestHelper.getSoyFileSet(
			cachedTemplateResourcesA);

		SoyTofu soyTofuB = Mockito.mock(SoyTofu.class);

		_soyTofuCacheHandler.add(
			cachedTemplateResourcesB, soyFileSetB, soyTofuB);

		List<TemplateResource> templateResources =
			_soyTestHelper.getTemplateResources(Arrays.asList("context.soy"));

		_soyTofuCacheHandler.removeIfAny(templateResources);

		Assert.assertNull(_soyTofuCacheHandler.get(cachedTemplateResourcesB));
		Assert.assertNotNull(
			_soyTofuCacheHandler.get(cachedTemplateResourcesA));
	}

	private final SoyTestHelper _soyTestHelper = new SoyTestHelper();
	private SoyTofuCacheHandler _soyTofuCacheHandler;

}