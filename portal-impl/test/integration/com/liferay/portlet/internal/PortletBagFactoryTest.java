/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.internal;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.model.impl.PortletAppImpl;
import com.liferay.portal.model.impl.PortletImpl;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portlet.PortletBagFactory;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.mock.web.MockServletContext;

/**
 * @author Raymond Augé
 */
public class PortletBagFactoryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void test1() throws Exception {
		try {
			PortletBagFactory portletBagFactory = new PortletBagFactory();

			portletBagFactory.create(new PortletImpl());

			Assert.fail();
		}
		catch (IllegalStateException ise) {
		}
	}

	@Test
	public void test2() throws Exception {
		try {
			PortletBagFactory portletBagFactory = new PortletBagFactory();

			Class<?> clazz = getClass();

			portletBagFactory.setClassLoader(clazz.getClassLoader());

			portletBagFactory.create(new PortletImpl());

			Assert.fail();
		}
		catch (IllegalStateException ise) {
		}
	}

	@Test
	public void test3() throws Exception {
		try {
			PortletBagFactory portletBagFactory = new PortletBagFactory();

			Class<?> clazz = getClass();

			portletBagFactory.setClassLoader(clazz.getClassLoader());

			portletBagFactory.setServletContext(new MockServletContext());

			portletBagFactory.create(new PortletImpl());

			Assert.fail();
		}
		catch (IllegalStateException ise) {
		}
	}

	@Test
	public void test4_initializedInstance() throws Exception {
		PortletImpl portletImpl = new PortletImpl();

		portletImpl.setPortletApp(new PortletAppImpl(StringPool.BLANK));
		portletImpl.setPortletClass(MVCPortlet.class.getName());

		PortletBagFactory portletBagFactory = new PortletBagFactory();

		Class<?> clazz = getClass();

		portletBagFactory.setClassLoader(clazz.getClassLoader());

		portletBagFactory.setServletContext(new MockServletContext());
		portletBagFactory.setWARFile(false);

		portletBagFactory.create(portletImpl);
	}

	@Test
	public void test5_concreteInstance() throws Exception {
		PortletImpl portletImpl = new PortletImpl();

		portletImpl.setPortletApp(new PortletAppImpl(StringPool.BLANK));

		PortletBagFactory portletBagFactory = new PortletBagFactory();

		Class<?> clazz = getClass();

		portletBagFactory.setClassLoader(clazz.getClassLoader());

		portletBagFactory.setServletContext(new MockServletContext());
		portletBagFactory.setWARFile(false);

		portletBagFactory.create(portletImpl, new MVCPortlet(), false);
	}

}