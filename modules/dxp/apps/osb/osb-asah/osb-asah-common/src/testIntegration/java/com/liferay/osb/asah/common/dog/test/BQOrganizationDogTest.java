/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.dog.BQOrganizationDog;
import com.liferay.osb.asah.common.entity.BQOrganization;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Matthew Kong
 */
public class BQOrganizationDogTest extends BaseBQDXPEntityDogTestCase {

	@BeforeEach
	@Override
	public void setUp() {
		super.setUp();
	}

	@BQSQLResource(resourcePath = "test_bq_organization_dog.sql")
	@Test
	public void testFindByDataSourceIdAndOrganizationPKIn() {
		List<BQOrganization> bqOrganizations =
			_bqOrganizationDog.findByDataSourceIdAndOrganizationIdIn(
				123L, Arrays.asList(1L, 2L));

		Assertions.assertEquals(
			2, bqOrganizations.size(), bqOrganizations.toString());

		BQOrganization bqOrganization = bqOrganizations.get(0);

		Assertions.assertEquals(
			"Liferay Brazil", bqOrganization.getDataSourceName());
	}

	@BQSQLResource(
		resourcePath = "test_get_bq_organization_field_value_page_custom.sql"
	)
	@Test
	public void testGetBQOrganizationFieldValuePageCustom() {
		Page<String> fieldValuePage =
			_bqOrganizationDog.getBQOrganizationFieldValuePage(
				null, null, "custom/Organization_Type/value", 0, 5);

		List<String> fieldValues = fieldValuePage.getContent();

		Assertions.assertEquals(5, fieldValues.size());

		List<String> expectedFieldValues = new ArrayList<String>() {
			{
				add("Type One");
				add("Type Two");
				add("Type Three");
				add("Type Four");
				add("Type Five");
			}
		};

		for (String fieldValue : fieldValues) {
			Assertions.assertTrue(expectedFieldValues.remove(fieldValue));
		}

		Assertions.assertEquals(5, fieldValuePage.getTotalElements());
	}

	@BQSQLResource(
		resourcePath = "test_get_bq_organization_field_value_page_custom.sql"
	)
	@Test
	public void testGetBQOrganizationFieldValuePageCustomCheckbox() {
		Page<String> fieldValuePage =
			_bqOrganizationDog.getBQOrganizationFieldValuePage(
				null, null, "custom/Location/value", 0, 10);

		List<String> fieldValues = fieldValuePage.getContent();

		Assertions.assertEquals(5, fieldValues.size());

		List<String> expectedFieldValues = new ArrayList<String>() {
			{
				add("Belgium");
				add("England");
				add("France");
				add("Germany");
				add("Italy");
			}
		};

		for (String fieldValue : fieldValues) {
			Assertions.assertTrue(expectedFieldValues.remove(fieldValue));
		}

		Assertions.assertEquals(5, fieldValuePage.getTotalElements());
	}

	@BQSQLResource(
		resourcePath = "test_get_bq_organization_field_value_page_custom.sql"
	)
	@Test
	public void testGetBQOrganizationFieldValuePageCustomWithFilter() {
		Page<String> fieldValuePage =
			_bqOrganizationDog.getBQOrganizationFieldValuePage(
				null, "contains(custom/Organization_Type/value, 'F')",
				"custom/Organization_Type/value", 0, 5);

		List<String> fieldValues = fieldValuePage.getContent();

		Assertions.assertEquals(2, fieldValues.size());

		List<String> expectedFieldValues = new ArrayList<String>() {
			{
				add("Type Four");
				add("Type Five");
			}
		};

		for (String fieldValue : fieldValues) {
			Assertions.assertTrue(expectedFieldValues.remove(fieldValue));
		}

		Assertions.assertEquals(2, fieldValuePage.getTotalElements());
	}

	@BQSQLResource(resourcePath = "test_bq_organization_dog.sql")
	@Test
	public void testGetOrganizationPage() {
		Page<BQOrganization> bqOrganizationPage =
			_bqOrganizationDog.getBQOrganizationPage(
				11L, null, 10, Sort.asc("name"), 0);

		Assertions.assertEquals(2, bqOrganizationPage.getTotalElements());

		List<BQOrganization> bqOrganizations = bqOrganizationPage.getContent();

		Assertions.assertEquals(
			2, bqOrganizations.size(), bqOrganizations.toString());

		bqOrganizationPage = _bqOrganizationDog.getBQOrganizationPage(
			11L, "Liferay", 10, new Sort("name", "asc"), 0);

		Assertions.assertEquals(bqOrganizationPage.getTotalElements(), 1);

		bqOrganizations = bqOrganizationPage.getContent();

		BQOrganization bqOrganization = bqOrganizations.get(0);

		Assertions.assertEquals(
			"Liferay Brazil", bqOrganization.getDataSourceName());
	}

	@Autowired
	private BQOrganizationDog _bqOrganizationDog;

}