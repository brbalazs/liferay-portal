/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.sso;

import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;
import com.liferay.portal.util.PrefsPropsUtil;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class SSOUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.ssoutil"));

	@Before
	public void setUp() throws Exception {
		_setLoginDialogDisable(1, true);
		_setLoginDialogDisable(2, false);
	}

	@After
	public void tearDown() throws Exception {
		for (Map.Entry<Long, String> entry :
				_oldLoginDialogDisableds.entrySet()) {

			PortletPreferences portletPreferences =
				PrefsPropsUtil.getPreferences(entry.getKey());

			String disabled = entry.getValue();

			if (disabled == null) {
				portletPreferences.reset(PropsKeys.LOGIN_DIALOG_DISABLED);
			}
			else {
				portletPreferences.setValue(
					PropsKeys.LOGIN_DIALOG_DISABLED, disabled);
			}

			portletPreferences.store();
		}
	}

	@Test
	public void testGetSessionExpirationRedirectURL() {
		Assert.assertEquals(
			"getSessionExpirationRedirectUrl:1",
			SSOUtil.getSessionExpirationRedirectURL(
				1, "sessionExpirationRedirectURL"));
	}

	@Test
	public void testGetSignInURL() {
		Assert.assertEquals(
			"signInURL:1", SSOUtil.getSignInURL(1, "signInURL"));
	}

	@Test
	public void testIsLoginRedirectRequired() {
		Assert.assertTrue(SSOUtil.isLoginRedirectRequired(1));
		Assert.assertFalse(SSOUtil.isLoginRedirectRequired(2));
	}

	@Test
	public void testIsRedirectRequired() {
		Assert.assertTrue(SSOUtil.isRedirectRequired(1));
		Assert.assertFalse(SSOUtil.isRedirectRequired(2));
	}

	@Test
	public void testIsSessionRedirectOnExpire() {
		Assert.assertTrue(SSOUtil.isSessionRedirectOnExpire(1));
		Assert.assertFalse(SSOUtil.isSessionRedirectOnExpire(2));
	}

	private void _setLoginDialogDisable(long companyId, boolean disabled)
		throws Exception {

		PortletPreferences portletPreferences = PrefsPropsUtil.getPreferences(
			companyId);

		_oldLoginDialogDisableds.put(
			companyId,
			portletPreferences.getValue(PropsKeys.LOGIN_DIALOG_DISABLED, null));

		portletPreferences.setValue(
			PropsKeys.LOGIN_DIALOG_DISABLED, String.valueOf(disabled));

		portletPreferences.store();
	}

	private final Map<Long, String> _oldLoginDialogDisableds = new HashMap<>();

}