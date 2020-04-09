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
import com.liferay.commerce.product.constants.CPConstants;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CPDefinitionOptionValueRel;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.option.CommerceOptionValue;
import com.liferay.commerce.product.option.CommerceOptionValueHelper;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CPDefinitionOptionRelLocalService;
import com.liferay.commerce.product.service.CPDefinitionOptionRelLocalServiceUtil;
import com.liferay.commerce.product.service.CPDefinitionOptionValueRelLocalServiceUtil;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.service.CommerceCatalogLocalServiceUtil;
import com.liferay.commerce.product.test.util.CPTestUtil;
import com.liferay.commerce.product.type.simple.constants.SimpleCPTypeConstants;
import com.liferay.commerce.product.util.CPInstanceHelper;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.ServiceContext;
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

import java.math.BigDecimal;

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
	public void test1() {
		Assert.assertNotNull("I am here to fail");
	}

	@Test
	public void testFetchCPInstanceIgnoreSkuCombinationsFalse()
		throws Exception {

		frutillaRule.scenario(
			"Fetch CP instance with specified SKU contributor options"
		).given(
			StringBundler.concat(
				"I have a product definition with SKU contributor options ",
				"Option_1 and Option_2 with two values assigned to each of ",
				"them so there are Option_1_Value_1, Option_1_Value_2, ",
				"Option_2_Value_1, Option_2_Value_2.")
		).when(
			"There is only CP instance A that represents SKU value " +
				"combination Option_1_Value_2, Option_2_Value_1"
		).and(
			"serialized DDM form values contains combination " +
				"Option_1_Value_2, Option_2_Value_1"
		).then(
			"CP instance A must be fetched"
		).but(
			StringBundler.concat(
				"If serialized DDM form values contains combination other ",
				"than Option_1_Value_2, Option_2_Value_1 nothing should be ",
				"fetched")
		);

		CPDefinition optionACPDefinition =
			CPTestUtil.addCPDefinitionFromCatalog(
				_commerceCatalog.getGroupId(), SimpleCPTypeConstants.NAME, true,
				true);

		CPInstance optionACPInstance = _cpInstanceHelper.getDefaultCPInstance(
			optionACPDefinition.getCPDefinitionId());

		CPDefinition optionBCPDefinition =
			CPTestUtil.addCPDefinitionFromCatalog(
				_commerceCatalog.getGroupId(), SimpleCPTypeConstants.NAME, true,
				true);

		CPInstance optionBCPInstance = _cpInstanceHelper.getDefaultCPInstance(
			optionBCPDefinition.getCPDefinitionId());

		CPDefinition bundleCPDefinition = CPTestUtil.addCPDefinitionFromCatalog(
			_commerceCatalog.getGroupId(), SimpleCPTypeConstants.NAME, true,
			true);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_commerceCatalog.getGroupId());

		CPDefinitionOptionRel cpDefinitionOptionRel =
			CPDefinitionOptionRelLocalServiceUtil.addCPDefinitionOptionRel(
				bundleCPDefinition.getCPDefinitionId(), 0L,
				RandomTestUtil.randomLocaleStringMap(),
				RandomTestUtil.randomLocaleStringMap(),
				CPTestUtil.getDefaultDDMFormFieldType(true), 0.2, false, false,
				false, false, CPConstants.PRODUCT_OPTION_PRICE_TYPE_STATIC,
				serviceContext);

		CPDefinitionOptionValueRel optionACPDefinitionOptionValueRel =
			CPDefinitionOptionValueRelLocalServiceUtil.
				addCPDefinitionOptionValueRel(
					cpDefinitionOptionRel.getCPDefinitionOptionRelId(),
					RandomTestUtil.randomLocaleStringMap(), 0.4, "product-a",
					serviceContext);

		CPDefinitionOptionValueRelLocalServiceUtil.
			updateCPDefinitionOptionValueRel(
				optionACPDefinitionOptionValueRel.
					getCPDefinitionOptionValueRelId(),
				RandomTestUtil.randomLocaleStringMap(), 0.4, "product-a",
				optionACPInstance.getCPInstanceId(), 2,
				new BigDecimal("100.20"), serviceContext);

		CPDefinitionOptionValueRel optionBCPDefinitionOptionValueRel =
			CPDefinitionOptionValueRelLocalServiceUtil.
				addCPDefinitionOptionValueRel(
					cpDefinitionOptionRel.getCPDefinitionOptionRelId(),
					RandomTestUtil.randomLocaleStringMap(), 0.2, "product-b",
					serviceContext);

		CPDefinitionOptionValueRelLocalServiceUtil.
			updateCPDefinitionOptionValueRel(
				optionBCPDefinitionOptionValueRel.
					getCPDefinitionOptionValueRelId(),
				RandomTestUtil.randomLocaleStringMap(), 0.2, "product-b",
				optionBCPInstance.getCPInstanceId(), 2,
				new BigDecimal("200.20"), serviceContext);

		_cpInstanceLocalService.buildCPInstances(
			bundleCPDefinition.getCPDefinitionId(), serviceContext);

		List<CPInstance> cpDefinitionApprovedCPInstances =
			_cpInstanceLocalService.getCPDefinitionApprovedCPInstances(
				bundleCPDefinition.getCPDefinitionId());

		for (CPInstance cpInstance : cpDefinitionApprovedCPInstances) {
			List<CommerceOptionValue> cpInstanceCommerceOptionValues =
				_commerceOptionValueHelper.getCPInstanceCommerceOptionValues(
					cpInstance.getCPInstanceId());

			for (CommerceOptionValue cpInstanceCommerceOptionValue :
					cpInstanceCommerceOptionValues) {

				Assert.assertNotNull(
					cpInstanceCommerceOptionValue.getCPInstanceId());
				Assert.assertNotNull(cpInstanceCommerceOptionValue.getPrice());
			}
		}
	}

	@Rule
	public final FrutillaRule frutillaRule = new FrutillaRule();

	private CommerceCatalog _commerceCatalog;

	@Inject
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	@Inject
	private CommerceOptionValueHelper _commerceOptionValueHelper;

	@DeleteAfterTestRun
	private Company _company;

	@Inject
	private CPDefinitionLocalService _cpDefinitionLocalService;

	@Inject
	private CPDefinitionOptionRelLocalService
		_cpDefinitionOptionRelLocalService;

	@Inject
	private CPInstanceHelper _cpInstanceHelper;

	@Inject
	private CPInstanceLocalService _cpInstanceLocalService;

}