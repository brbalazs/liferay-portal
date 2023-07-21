/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.item.selector.web.internal.util;

import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.web.internal.FlickrItemSelectorCriterion;
import com.liferay.item.selector.web.internal.TestFileEntryItemSelectorReturnType;
import com.liferay.item.selector.web.internal.TestStringItemSelectorReturnType;
import com.liferay.item.selector.web.internal.TestURLItemSelectorReturnType;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactoryUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Iván Zaera
 * @author Roberto Díaz
 */
public class ItemSelectorCriterionSerializerImplTest {

	@Before
	public void setUp() {
		_flickrItemSelectorCriterion = new FlickrItemSelectorCriterion();

		List<ItemSelectorReturnType> desiredItemSelectorReturnTypes =
			new ArrayList<>();

		desiredItemSelectorReturnTypes.add(_testStringItemSelectorReturnType);
		desiredItemSelectorReturnTypes.add(_testURLItemSelectorReturnType);

		_flickrItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			desiredItemSelectorReturnTypes);

		_stubItemSelectorCriterionSerializerImpl.addItemSelectorReturnType(
			_testFileEntryItemSelectorReturnType);
		_stubItemSelectorCriterionSerializerImpl.addItemSelectorReturnType(
			_testStringItemSelectorReturnType);
		_stubItemSelectorCriterionSerializerImpl.addItemSelectorReturnType(
			_testURLItemSelectorReturnType);

		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());
	}

	@Test
	public void testGetProperties() {
		List<ItemSelectorReturnType> desiredItemSelectorReturnTypes =
			new ArrayList<>();

		desiredItemSelectorReturnTypes.add(_testURLItemSelectorReturnType);

		_flickrItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			desiredItemSelectorReturnTypes);

		String json = _stubItemSelectorCriterionSerializerImpl.serialize(
			_flickrItemSelectorCriterion);

		Class<? extends ItemSelectorReturnType>
			testURLItemSelectorReturnTypeClass =
				_testURLItemSelectorReturnType.getClass();

		json = _assert(
			"\"desiredItemSelectorReturnTypes\":\"" +
				testURLItemSelectorReturnTypeClass.getName() + "\"",
			json);

		json = _assert("\"tags\":[\"me\",\"photo\",\"picture\"]", json);
		json = _assert("\"user\":\"anonymous\"", json);

		Assert.assertEquals("{,,}", json);
	}

	@Test
	public void testSetProperties() {
		Class<? extends ItemSelectorReturnType>
			testURLItemSelectorReturnTypeClass =
				_testURLItemSelectorReturnType.getClass();

		String json =
			"{\"desiredItemSelectorReturnTypes\":\"" +
				testURLItemSelectorReturnTypeClass.getName() + "\",\"tags\":" +
					"[\"tag1\",\"tag2\",\"tag3\"],\"user\":\"Joe Bloggs\"}";

		_flickrItemSelectorCriterion =
			_stubItemSelectorCriterionSerializerImpl.deserialize(
				_flickrItemSelectorCriterion.getClass(), json);

		Assert.assertEquals(
			"Joe Bloggs", _flickrItemSelectorCriterion.getUser());
		Assert.assertArrayEquals(
			new String[] {"tag1", "tag2", "tag3"},
			_flickrItemSelectorCriterion.getTags());

		List<ItemSelectorReturnType> expectedDesiredItemSelectorReturnTypes =
			new ArrayList<>();

		expectedDesiredItemSelectorReturnTypes.add(
			_testURLItemSelectorReturnType);

		Assert.assertEquals(
			expectedDesiredItemSelectorReturnTypes,
			_flickrItemSelectorCriterion.getDesiredItemSelectorReturnTypes());
	}

	private String _assert(String expected, String json) {
		Assert.assertTrue(json, json.contains(expected));

		return json.replaceAll(Pattern.quote(expected), "");
	}

	private FlickrItemSelectorCriterion _flickrItemSelectorCriterion;
	private final StubItemSelectorCriterionSerializerImpl
		_stubItemSelectorCriterionSerializerImpl =
			new StubItemSelectorCriterionSerializerImpl();
	private final ItemSelectorReturnType _testFileEntryItemSelectorReturnType =
		new TestFileEntryItemSelectorReturnType();
	private final ItemSelectorReturnType _testStringItemSelectorReturnType =
		new TestStringItemSelectorReturnType();
	private final ItemSelectorReturnType _testURLItemSelectorReturnType =
		new TestURLItemSelectorReturnType();

	private class StubItemSelectorCriterionSerializerImpl
		extends ItemSelectorCriterionSerializerImpl {

		@Override
		public void addItemSelectorReturnType(
			ItemSelectorReturnType itemSelectorReturnType) {

			super.addItemSelectorReturnType(itemSelectorReturnType);
		}

	}

}