/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.common;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leslie Wong
 */
public class URLUtilTest {

	@Test
	public void testCreateURL() throws Exception {
		URI uri = URLUtil.createURI(
			"https://test.liferay.com/web/guest/home?searchString=" +
				"testing 1 2 3");

		Assert.assertEquals("https", uri.getScheme());
		Assert.assertEquals("test.liferay.com", uri.getAuthority());
		Assert.assertEquals("/web/guest/home", uri.getPath());
		Assert.assertEquals("searchString=testing 1 2 3", uri.getQuery());
		Assert.assertEquals(null, uri.getFragment());
	}

	@Test
	public void testCreateURLDoubleHashMark() throws Exception {
		URI uri = URLUtil.createURI(
			"https://test.liferay.com/web/guest/home#/#abc123");

		Assert.assertEquals("https", uri.getScheme());
		Assert.assertEquals("test.liferay.com", uri.getAuthority());
		Assert.assertEquals("/web/guest/home", uri.getPath());
		Assert.assertEquals(null, uri.getQuery());
		Assert.assertEquals("/#abc123", uri.getFragment());
	}

	@Test(expected = NullPointerException.class)
	public void testCreateUrlEmptyString() throws Exception {
		URLUtil.createURI("");
	}

	@Test
	public void testCreateURLNoAuthority() throws Exception {
		URI uri = URLUtil.createURI("https://?searchString=testing 1 2 3");

		Assert.assertEquals("https", uri.getScheme());
		Assert.assertEquals(null, uri.getAuthority());
		Assert.assertEquals("", uri.getPath());
		Assert.assertEquals("searchString=testing 1 2 3", uri.getQuery());
		Assert.assertEquals(null, uri.getFragment());
	}

	@Test
	public void testCreateURLNoFragment() throws Exception {
		URI uri = URLUtil.createURI("https://test.liferay.com/web/guest/home#");

		Assert.assertEquals("https", uri.getScheme());
		Assert.assertEquals("test.liferay.com", uri.getAuthority());
		Assert.assertEquals("/web/guest/home", uri.getPath());
		Assert.assertEquals(null, uri.getQuery());
		Assert.assertEquals(null, uri.getFragment());
	}

	@Test
	public void testCreateURLNoPath1() throws Exception {
		URI uri = URLUtil.createURI(
			"https://test.liferay.com?searchString=testing 1 2 3");

		Assert.assertEquals("https", uri.getScheme());
		Assert.assertEquals("test.liferay.com", uri.getAuthority());
		Assert.assertEquals("", uri.getPath());
		Assert.assertEquals("searchString=testing 1 2 3", uri.getQuery());
		Assert.assertEquals(null, uri.getFragment());
	}

	@Test
	public void testCreateURLNoPath2() throws Exception {
		URI uri = URLUtil.createURI(
			"https://test.liferay.com/?searchString=testing 1 2 3");

		Assert.assertEquals("https", uri.getScheme());
		Assert.assertEquals("test.liferay.com", uri.getAuthority());
		Assert.assertEquals("", uri.getPath());
		Assert.assertEquals("searchString=testing 1 2 3", uri.getQuery());
		Assert.assertEquals(null, uri.getFragment());
	}

	@Test
	public void testCreateURLNoQuery1() throws Exception {
		URI uri = URLUtil.createURI("https://test.liferay.com/web/guest/home");

		Assert.assertEquals("https", uri.getScheme());
		Assert.assertEquals("test.liferay.com", uri.getAuthority());
		Assert.assertEquals("/web/guest/home", uri.getPath());
		Assert.assertEquals(null, uri.getQuery());
		Assert.assertEquals(null, uri.getFragment());
	}

	@Test
	public void testCreateURLNoQuery2() throws Exception {
		URI uri = URLUtil.createURI("https://test.liferay.com/web/guest/home/");

		Assert.assertEquals("https", uri.getScheme());
		Assert.assertEquals("test.liferay.com", uri.getAuthority());
		Assert.assertEquals("/web/guest/home/", uri.getPath());
		Assert.assertEquals(null, uri.getQuery());
		Assert.assertEquals(null, uri.getFragment());
	}

	@Test(expected = URISyntaxException.class)
	public void testCreateURLNoScheme() throws Exception {
		URLUtil.createURI("://test.liferay.com/web/guest/home");
	}

	@Test(expected = NullPointerException.class)
	public void testCreateUrlNullString() throws Exception {
		URLUtil.createURI(null);
	}

}