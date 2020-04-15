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

package com.liferay.commerce.product.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CPDefinitionOptionValueRel;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CPDefinitionOptionValueRelLocalService;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.service.CPOptionLocalService;
import com.liferay.commerce.product.service.CommerceCatalogLocalService;
import com.liferay.commerce.product.test.util.CPTestUtil;
import com.liferay.commerce.product.type.simple.constants.SimpleCPTypeConstants;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
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
 * @author Matija Petanjek
 */
@RunWith(Arquillian.class)
public class CPDefinitionOptionValueRelLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_serviceContext = ServiceContextTestUtil.getServiceContext();

		_commerceCatalog = _commerceCatalogLocalService.addCommerceCatalog(
			RandomTestUtil.randomString(), null,
			LocaleUtil.US.getDisplayLanguage(), null, _serviceContext);
	}

	@After
	public void tearDown() throws Exception {
		_serviceContext = null;

		List<CPDefinition> cpDefinitions =
			_cpDefinitionLocalService.getCPDefinitions(
				_commerceCatalog.getGroupId(), WorkflowConstants.STATUS_ANY,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (CPDefinition cpDefinition : cpDefinitions) {
			_cpDefinitionLocalService.deleteCPDefinition(cpDefinition);
		}

		_cpOptionLocalService.deleteCPOptions(_commerceCatalog.getCompanyId());

		_commerceCatalogLocalService.deleteCommerceCatalog(_commerceCatalog);
	}

	@Test
	public void testResetCPDefinitionOptionValueRelOnDelete() throws Exception {
		frutillaRule.scenario(
			"Delete a product instance which is referenced as an option " +
				"value of another product (product bundle)"
		).given(
			"A product bundle and a product instance"
		).and(
			"Product instance is referenced as an option value of the " +
				"product bundle"
		).when(
			"The referenced product instance is deleted"
		).then(
			"Product bundle's option value attributes should be reset to " +
				"default values"
		);

		CPDefinitionOptionValueRel cpDefinitionOptionValueRel =
			_addCPDefinitionWithOptionValue();

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			_commerceCatalog.getGroupId());

		cpDefinitionOptionValueRel =
			_cpDefinitionOptionValueRelLocalService.
				updateCPDefinitionOptionValueRel(
					cpDefinitionOptionValueRel.
						getCPDefinitionOptionValueRelId(),
					null, 0, "cpInstance-option-value",
					cpInstance.getCPInstanceId(), 1, BigDecimal.TEN,
					_serviceContext);

		Assert.assertEquals(
			cpInstance.getCPInstanceUuid(),
			cpDefinitionOptionValueRel.getCPInstanceUuid());

		CPDefinition cpInstanceCPDefinition = cpInstance.getCPDefinition();

		Assert.assertEquals(
			cpInstanceCPDefinition.getCProductId(),
			cpDefinitionOptionValueRel.getCProductId());

		Assert.assertEquals(
			BigDecimal.TEN, cpDefinitionOptionValueRel.getPrice());
		Assert.assertEquals(1, cpDefinitionOptionValueRel.getQuantity());

		_cpInstanceLocalService.deleteCPInstance(cpInstance);

		cpDefinitionOptionValueRel =
			_cpDefinitionOptionValueRelLocalService.
				getCPDefinitionOptionValueRel(
					cpDefinitionOptionValueRel.
						getCPDefinitionOptionValueRelId());

		Assert.assertEquals(
			StringPool.BLANK, cpDefinitionOptionValueRel.getCPInstanceUuid());
		Assert.assertEquals(0, cpDefinitionOptionValueRel.getCProductId());

		BigDecimal price = cpDefinitionOptionValueRel.getPrice();

		Assert.assertEquals(
			_stripTrailingZeros(BigDecimal.ZERO), _stripTrailingZeros(price));

		Assert.assertEquals(0, cpDefinitionOptionValueRel.getQuantity());
	}

	@Test
	public void testResetCPDefinitionOptionValueRelOnUpdateStatusToApproved()
		throws Exception {

		frutillaRule.scenario(
			"Update a status of APPROVED product instance which is " +
				"referenced as an option value of another product (product " +
					"bundle)"
		).given(
			"A product bundle and a product instance"
		).and(
			"Product instance is referenced as an option value of the " +
				"product bundle"
		).when(
			"The referenced product instance status stays in APPROVED state"
		).then(
			"Product bundle's option value attributes should not be changed"
		);

		CPDefinitionOptionValueRel cpDefinitionOptionValueRel =
			_addCPDefinitionWithOptionValue();

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			_commerceCatalog.getGroupId());

		Assert.assertEquals(
			WorkflowConstants.STATUS_APPROVED, cpInstance.getStatus());

		cpDefinitionOptionValueRel =
			_cpDefinitionOptionValueRelLocalService.
				updateCPDefinitionOptionValueRel(
					cpDefinitionOptionValueRel.
						getCPDefinitionOptionValueRelId(),
					null, 0, "cpInstance-option-value",
					cpInstance.getCPInstanceId(), 1, BigDecimal.TEN,
					_serviceContext);

		Assert.assertEquals(
			cpInstance.getCPInstanceUuid(),
			cpDefinitionOptionValueRel.getCPInstanceUuid());

		CPDefinition cpInstanceCPDefinition = cpInstance.getCPDefinition();

		Assert.assertEquals(
			cpInstanceCPDefinition.getCProductId(),
			cpDefinitionOptionValueRel.getCProductId());

		Assert.assertEquals(
			BigDecimal.TEN, cpDefinitionOptionValueRel.getPrice());
		Assert.assertEquals(1, cpDefinitionOptionValueRel.getQuantity());

		_cpInstanceLocalService.updateStatus(
			_serviceContext.getUserId(), cpInstance.getCPInstanceId(),
			WorkflowConstants.STATUS_APPROVED);

		cpDefinitionOptionValueRel =
			_cpDefinitionOptionValueRelLocalService.
				getCPDefinitionOptionValueRel(
					cpDefinitionOptionValueRel.
						getCPDefinitionOptionValueRelId());

		Assert.assertEquals(
			cpInstance.getCPInstanceUuid(),
			cpDefinitionOptionValueRel.getCPInstanceUuid());

		cpInstanceCPDefinition = cpInstance.getCPDefinition();

		Assert.assertEquals(
			cpInstanceCPDefinition.getCProductId(),
			cpDefinitionOptionValueRel.getCProductId());

		Assert.assertEquals(
			_stripTrailingZeros(BigDecimal.TEN),
			_stripTrailingZeros(cpDefinitionOptionValueRel.getPrice()));
		Assert.assertEquals(1, cpDefinitionOptionValueRel.getQuantity());
	}

	@Test
	public void testResetCPDefinitionOptionValueRelOnUpdateStatusToExpired()
		throws Exception {

		frutillaRule.scenario(
			"Update a status of APPROVED product instance which is " +
				"referenced as an option value of another product (product " +
					"bundle)"
		).given(
			"A product bundle and a product instance"
		).and(
			"Product instance is referenced as an option value of the " +
				"product bundle"
		).when(
			"The referenced product instance status is changed from APPROVED " +
				"to EXPIRED"
		).then(
			"Product bundle's option value attributes should be reset to " +
				"default values"
		);

		testResetCPDefinitionOptionValueRelOnStatusChange(
			WorkflowConstants.STATUS_EXPIRED);
	}

	@Test
	public void testResetCPDefinitionOptionValueRelOnUpdateStatusToInactive()
		throws Exception {

		frutillaRule.scenario(
			"Update a status of APPROVED product instance which is " +
				"referenced as an option value of another product (product " +
					"bundle)"
		).given(
			"A product bundle and a product instance"
		).and(
			"Product instance is referenced as an option value of the " +
				"product bundle"
		).when(
			"The referenced product instance status is changed from APPROVED " +
				"to INACTIVE"
		).then(
			"Product bundle's option value attributes should be reset to " +
				"default values"
		);

		testResetCPDefinitionOptionValueRelOnStatusChange(
			WorkflowConstants.STATUS_INACTIVE);
	}

	@Test
	public void testResetCPDefinitionOptionValueRelOnUpdateStatusToScheduled()
		throws Exception {

		frutillaRule.scenario(
			"Update a status of APPROVED product instance which is " +
				"referenced as an option value of another product (product " +
					"bundle)"
		).given(
			"A product bundle and a product instance"
		).and(
			"Product instance is referenced as an option value of the " +
				"product bundle"
		).when(
			"The referenced product instance status is changed from APPROVED " +
				"to SCHEDULED"
		).then(
			"Product bundle's option value attributes should be reset to " +
				"default values"
		);

		testResetCPDefinitionOptionValueRelOnStatusChange(
			WorkflowConstants.STATUS_SCHEDULED);
	}

	@Rule
	public FrutillaRule frutillaRule = new FrutillaRule();

	protected void testResetCPDefinitionOptionValueRelOnStatusChange(
			int newStatus)
		throws Exception {

		CPDefinitionOptionValueRel cpDefinitionOptionValueRel =
			_addCPDefinitionWithOptionValue();

		CPInstance cpInstance = CPTestUtil.addCPInstanceFromCatalog(
			_commerceCatalog.getGroupId());

		Assert.assertEquals(
			WorkflowConstants.STATUS_APPROVED, cpInstance.getStatus());

		cpDefinitionOptionValueRel =
			_cpDefinitionOptionValueRelLocalService.
				updateCPDefinitionOptionValueRel(
					cpDefinitionOptionValueRel.
						getCPDefinitionOptionValueRelId(),
					null, 0, "cpInstance-option-value",
					cpInstance.getCPInstanceId(), 1, BigDecimal.TEN,
					_serviceContext);

		Assert.assertEquals(
			cpInstance.getCPInstanceUuid(),
			cpDefinitionOptionValueRel.getCPInstanceUuid());

		CPDefinition cpInstanceCPDefinition = cpInstance.getCPDefinition();

		Assert.assertEquals(
			cpInstanceCPDefinition.getCProductId(),
			cpDefinitionOptionValueRel.getCProductId());

		Assert.assertEquals(
			BigDecimal.TEN, cpDefinitionOptionValueRel.getPrice());
		Assert.assertEquals(1, cpDefinitionOptionValueRel.getQuantity());

		_cpInstanceLocalService.updateStatus(
			_serviceContext.getUserId(), cpInstance.getCPInstanceId(),
			newStatus);

		cpDefinitionOptionValueRel =
			_cpDefinitionOptionValueRelLocalService.
				getCPDefinitionOptionValueRel(
					cpDefinitionOptionValueRel.
						getCPDefinitionOptionValueRelId());

		Assert.assertEquals(
			StringPool.BLANK, cpDefinitionOptionValueRel.getCPInstanceUuid());
		Assert.assertEquals(0, cpDefinitionOptionValueRel.getCProductId());

		BigDecimal price = cpDefinitionOptionValueRel.getPrice();

		Assert.assertEquals(
			_stripTrailingZeros(BigDecimal.ZERO), _stripTrailingZeros(price));

		Assert.assertEquals(0, cpDefinitionOptionValueRel.getQuantity());
	}

	private CPDefinitionOptionValueRel _addCPDefinitionWithOptionValue()
		throws Exception {

		CPDefinition cpDefinition = CPTestUtil.addCPDefinitionFromCatalog(
			_commerceCatalog.getGroupId(), SimpleCPTypeConstants.NAME, true,
			true);

		List<CPDefinitionOptionRel> cpDefinitionOptionRels =
			CPTestUtil.addCPOption(
				_commerceCatalog.getGroupId(), cpDefinition.getCPDefinitionId(),
				1, 1);

		CPDefinitionOptionRel cpDefinitionOptionRel =
			cpDefinitionOptionRels.get(0);

		return _cpDefinitionOptionValueRelLocalService.
			addCPDefinitionOptionValueRel(
				cpDefinitionOptionRel.getCPDefinitionOptionRelId(), null, 0,
				"cpInstance-option-value", _serviceContext);
	}

	private BigDecimal _stripTrailingZeros(BigDecimal bigDecimal) {
		if (bigDecimal == null) {
			return bigDecimal;
		}

		return bigDecimal.stripTrailingZeros();
	}

	private CommerceCatalog _commerceCatalog;

	@Inject
	private CommerceCatalogLocalService _commerceCatalogLocalService;

	@Inject
	private CPDefinitionLocalService _cpDefinitionLocalService;

	@Inject
	private CPDefinitionOptionValueRelLocalService
		_cpDefinitionOptionValueRelLocalService;

	@Inject
	private CPInstanceLocalService _cpInstanceLocalService;

	@Inject
	private CPOptionLocalService _cpOptionLocalService;

	private ServiceContext _serviceContext;

}