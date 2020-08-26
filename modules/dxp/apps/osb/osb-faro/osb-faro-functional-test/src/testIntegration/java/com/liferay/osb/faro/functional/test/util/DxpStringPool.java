/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.functional.test.util;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * For storing long, ambiguous, or version specific DXP XPath or URL path
 * strings
 *
 * @author Cheryl Tang
 */
public class DxpStringPool {

	public static final String AC_INSTANCE_SETTINGS_URL_BASE =
		new StringBundler().append(
			"/group/control_panel/manage?p_p_id=com_liferay_configuration_admi"
		).append(
			"n_web_portlet_InstanceSettingsPortlet&p_p_lifecycle=0&p_p_state=m"
		).append(
			"aximized&p_p_mode=view&_com_liferay_configuration_admin_web_portl"
		).append(
			"et_InstanceSettingsPortlet_mvcRenderCommandName=%2Fview_configura"
		).append(
			"tion_screen&_com_liferay_configuration_admin_web_portlet_Instance"
		).append(
			"SettingsPortlet_configurationScreenKey="
		).toString();

	public static final String AC_INSTANCE_SETTINGS_URL_PATH =
		AC_INSTANCE_SETTINGS_URL_BASE + "analytics-cloud-connection";

	public static final String ADD_OAUTH_APP_BUTTON_XPATH =
		"//a[@title='Add OAuth 2 Application']";

	public static final String ADD_SITES_URL_PATH = StringBundler.concat(
		"/group/control_panel/manage/-/sites/sites/select_site?_com_liferay_",
		"site_admin_web_portlet_SiteAdminPortlet_redirect=%2Fgroup%",
		"3DR83NOhx7&p_p_auth=R83NOhx7",
		"2Fcontrol_panel%2Fmanage%2F-%2Fsites%2Fsites%3Fp_p_auth%");

	public static final String ANALYTICS_SCOPE_EXPANDED_XPATH =
		"//span[text()='Analytics']/parent::a[@aria-expanded='true']";

	public static final String ANALYTICS_SCOPE_XPATH =
		"//span[text()='Analytics']/parent::a";

	public static final String AUTH_TOKEN_FIELD_XPATH =
		"//input[@id='_com_liferay_configuration_admin_web_portlet_InstanceS" +
			"ettingsPortlet_token']";

	public static final String CONNECT_AUTH_TOKEN_BUTTON_XPATH =
		"//button[@id='_com_liferay_configuration_admin_web_portlet_Instance" +
			"SettingsPortlet_tokenButton']";

	public static final String MASTER_OAUTH_SCOPES_TAB_XPATH =
		"//a[text()='Scopes']";

	public static final String OAUTH_ID_XPATH =
		"//input[@id='_com_liferay_oauth2_provider_web_internal_portlet_OAut" +
			"h2AdminPortlet_clientId']";

	public static final String OAUTH_SCOPES_TAB_XPATH =
		"//span[text()='Scopes']/parent::a";

	public static final String OAUTH_SECRET_XPATH =
		"//input[@id='_com_liferay_oauth2_provider_web_internal_portlet_OAut" +
			"h2AdminPortlet_clientSecret']";

	public static final String OAUTH_URL_PATH =
		"/group/control_panel/manage?p_p_id=com_liferay_oauth2_provider_web_" +
			"internal_portlet_OAuth2AdminPortlet";

	public static final String PAGE_CREATION_NAME_FIELD =
		"//input[@id='_com_liferay_layout_admin_web_portlet" +
			"_GroupPagesPortlet_name']";

	private static String _lintNameForUrl(String name) {
		if (name.equals("Liferay DXP")) {
			name = "guest";
		} else {
			name = StringUtil.replace(name, StringPool.SPACE, StringPool.DASH);
		}
		return name;
	}

	public static String getPageCreationUrlPath(String site) {

		return new StringBundler().append("/group/"
		).append(_lintNameForUrl(site)
		).append("/~/control_panel/manage?p_p_id=com_liferay_layout_adm"
		).append(
				"in_web_portlet_GroupPagesPortlet&p_p_lifecycle=0&p_p_state=maximi"
		).append(
				"zed&p_p_mode=view&_com_liferay_layout_admin_web_portlet_GroupPage"
		).append(
				"sPortlet_mvcPath=%2Fselect_layout_page_template_entry.jsp&_com_li"
		).append(
				"feray_layout_admin_web_portlet_GroupPagesPortlet_groupId=20126&p_"
		).append(
				"r_p_selPlid=0&p_r_p_privateLayout=false&_com_liferay_layout_admin"
		).append(
				"_web_portlet_GroupPagesPortlet_selectedTab=global-templates&p_p_a"
		).append(
				"uth=em8IWLqf"
		).toString();
	}

	public static final String SITES_CONTROL_PANEL_URL_PATH =
		"/group/control_panel/manage/-/sites/sites";

	public static final String USERS_ORGANIZATIONS_CONTROL_PANEL_URL_PATH =
		new StringBundler().append("/group/control_panel/manage?p_p_id=com_lif"
		).append("eray_users_admin_web_portlet_UsersAdminPortlet&p_p_lifecycle"
		).append("=0&p_p_state=maximized&p_v_l_s_g_id=20122"
		).toString();

	public static final String SCOPE_CHECKBOX_XPATH =
		"//a[@aria-expanded='true']//following-sibling::div//input[@type='c" +
			"heckbox']";

	public static final String SYNCED_CONTACTS_PATH =
		AC_INSTANCE_SETTINGS_URL_BASE + "synced-contacts";

	public static final String SYNCED_SITES_PATH =
		AC_INSTANCE_SETTINGS_URL_BASE + "synced-sites";

}