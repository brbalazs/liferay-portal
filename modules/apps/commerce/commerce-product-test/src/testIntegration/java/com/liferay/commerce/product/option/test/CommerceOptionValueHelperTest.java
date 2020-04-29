/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.product.option.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.option.CommerceOptionValue;
import com.liferay.commerce.product.option.CommerceOptionValueHelper;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.service.CommerceCatalogLocalServiceUtil;
import com.liferay.commerce.product.test.util.CPTestUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.List;

import org.frutilla.FrutillaRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Igor Beslic
 */
@RunWith(Arquillian.class)
public class CommerceOptionValueHelperTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_company = CompanyTestUtil.addCompany();

		_commerceCatalog = CommerceCatalogLocalServiceUtil.addCommerceCatalog(
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			LocaleUtil.US.getDisplayLanguage(), null,
			ServiceContextTestUtil.getServiceContext(_company.getGroupId()));
	}

	@After
	public void tearDown() throws Exception {
		List<CPDefinition> cpDefinitions =
			_cpDefinitionLocalService.getCPDefinitions(
				_commerceCatalog.getGroupId(), WorkflowConstants.STATUS_ANY,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (CPDefinition cpDefinition : cpDefinitions) {
			_cpDefinitionLocalService.deleteCPDefinition(cpDefinition);
		}

		_commerceCatalogLocalService.deleteCommerceCatalog(_commerceCatalog);
	}

	@Test
	public void testGetCPDefinitionCommerceOptionValues() throws Exception {
		frutillaRule.scenario(
			"Get CP instance commerce option values"
		).given(
			StringBundler.concat(
				"I have a product bundle definition with SKU contributor ",
				"option Option_1 and two option values assigned to it so ",
				"there are Option_1_Value_1, Option_1_Value_2 values.")
		).and(
			"Option_1_Value_1 points to product A"
		).and(
			"Option_1_Value_2 points to product B"
		).when(
			"There are two CP instances that represents product bundle " +
				"values combination Option_1_Value_1, Option_1_Value_2"
		).and(
			"commerce option value is requested for SKU's cpIstanceId"
		).then(
			"commerce option value is returned"
		);

		_setupPermissionChecker();

		CPDefinition bundleCPDefinition =
			CPTestUtil.addCPDefinitionWithChildCPDefinitions(
				_commerceCatalog.getGroupId());

		List<CommerceOptionValue> cpInstanceCommerceOptionValues =
			_commerceOptionValueHelper.getCPDefinitionCommerceOptionValues(
				bundleCPDefinition.getCPDefinitionId(), StringPool.BLANK);

		Assert.assertTrue(
			"No option values present if user did not select option",
			cpInstanceCommerceOptionValues.isEmpty());
	}

	@Rule
	public final FrutillaRule frutillaRule = new FrutillaRule();

	private void _setupPermissionChecker() throws Exception {
		User user = UserLocalServiceUtil.getUser(_commerceCatalog.getUserId());

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(user));
	}

	private CommerceCatalog _commerceCatalog;

	@Inject
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	@Inject
	private CommerceOptionValueHelper _commerceOptionValueHelper;

	@DeleteAfterTestRun
	private Company _company;

	@Inject
	private CPDefinitionLocalService _cpDefinitionLocalService;

}