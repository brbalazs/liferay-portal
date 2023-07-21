/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.ldap;

import com.liferay.portal.kernel.security.ldap.LDAPSettingsUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import java.util.Properties;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class LDAPSettingsUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.ldapsettingsutil"));

	@Test
	public void testGetAuthSearchFilter() throws Exception {
		Assert.assertEquals(
			"(companyId=1)",
			LDAPSettingsUtil.getAuthSearchFilter(
				1, 1, "test@liferay-test.com", "test-ip", "test"));
	}

	@Test
	public void testGetContactExpandoMappings() throws Exception {
		Properties properties = LDAPSettingsUtil.getContactExpandoMappings(
			1, 1);

		Assert.assertEquals("1", properties.get("ldapServerId"));
	}

	@Test
	public void testGetContactMappings() throws Exception {
		Properties properties = LDAPSettingsUtil.getContactMappings(1, 1);

		Assert.assertEquals("1", properties.get("ldapServerId"));
	}

	@Test
	public void testGetGroupMappings() throws Exception {
		Properties properties = LDAPSettingsUtil.getGroupMappings(1, 1);

		Assert.assertEquals("1", properties.get("ldapServerId"));
	}

	@Test
	public void testGetPreferredLDAPServerId() {
		long ldapServerId = LDAPSettingsUtil.getPreferredLDAPServerId(
			1, "test");

		Assert.assertEquals(1234567890, ldapServerId);
	}

	@Test
	public void testGetPropertyPostfix() {
		String postfix = LDAPSettingsUtil.getPropertyPostfix(1);

		Assert.assertEquals("liferay.ldap", postfix);
	}

	@Test
	public void testGetUserExpandoMappings() throws Exception {
		Properties properties = LDAPSettingsUtil.getUserExpandoMappings(1, 1);

		Assert.assertEquals("1", properties.get("ldapServerId"));
	}

	@Test
	public void testGetUserMappings() throws Exception {
		Properties properties = LDAPSettingsUtil.getUserMappings(1, 1);

		Assert.assertEquals("1", properties.get("ldapServerId"));
	}

	@Test
	public void testIsExportEnabled() {
		Assert.assertTrue(LDAPSettingsUtil.isExportEnabled(1));
		Assert.assertFalse(LDAPSettingsUtil.isExportEnabled(2));
	}

	@Test
	public void testIsExportGroupEnabled() {
		Assert.assertTrue(LDAPSettingsUtil.isExportGroupEnabled(1));
		Assert.assertFalse(LDAPSettingsUtil.isExportGroupEnabled(2));
	}

	@Test
	public void testIsImportEnabled() {
		Assert.assertTrue(LDAPSettingsUtil.isImportEnabled(1));
		Assert.assertFalse(LDAPSettingsUtil.isImportEnabled(2));
	}

	@Test
	public void testIsImportOnStartup() {
		Assert.assertTrue(LDAPSettingsUtil.isImportOnStartup(1));
		Assert.assertFalse(LDAPSettingsUtil.isImportOnStartup(2));
	}

	@Test
	public void testIsPasswordPolicyEnabled() {
		Assert.assertTrue(LDAPSettingsUtil.isPasswordPolicyEnabled(1));
		Assert.assertFalse(LDAPSettingsUtil.isPasswordPolicyEnabled(2));
	}

}