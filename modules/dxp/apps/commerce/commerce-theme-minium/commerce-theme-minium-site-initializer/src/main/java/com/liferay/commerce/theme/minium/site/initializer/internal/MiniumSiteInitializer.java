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

package com.liferay.commerce.theme.minium.site.initializer.internal;

import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.initializer.util.CPDefinitionsImporter;
import com.liferay.commerce.initializer.util.CommerceWarehousesImporter;
import com.liferay.commerce.initializer.util.PortletSettingsImporter;
import com.liferay.commerce.model.CommerceWarehouse;
import com.liferay.commerce.organization.constants.CommerceOrganizationConstants;
import com.liferay.commerce.organization.service.CommerceOrganizationLocalService;
import com.liferay.commerce.product.constants.CPRuleConstants;
import com.liferay.commerce.product.importer.CPFileImporter;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPRule;
import com.liferay.commerce.product.service.CPMeasurementUnitLocalService;
import com.liferay.commerce.product.service.CPRuleLocalService;
import com.liferay.commerce.product.service.CPRuleUserSegmentRelLocalService;
import com.liferay.commerce.product.service.CPSpecificationOptionLocalService;
import com.liferay.commerce.service.CommerceCountryLocalService;
import com.liferay.commerce.user.segment.model.CommerceUserSegmentEntry;
import com.liferay.commerce.user.segment.model.CommerceUserSegmentEntryConstants;
import com.liferay.commerce.user.segment.service.CommerceUserSegmentEntryLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.ListTypeConstants;
import com.liferay.portal.kernel.model.OrganizationConstants;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.model.ThemeSetting;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ThemeLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = "site.initializer.key=" + MiniumSiteInitializer.KEY,
	service = SiteInitializer.class
)
public class MiniumSiteInitializer implements SiteInitializer {

	public static final int ACCOUNT_ORGANIZATIONS_COUNT = 3;

	public static final String DEPENDENCIES_PATH =
		"com/liferay/commerce/theme/minium/site/initializer/internal" +
			"/dependencies/";

	public static final String KEY = "minium-initializer";

	@Override
	public String getDescription(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.get(resourceBundle, "minium-description");
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getName(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.get(resourceBundle, "minium");
	}

	@Override
	public String getThumbnailSrc() {
		return _servletContext.getContextPath() + "/images/thumbnail.png";
	}

	@Override
	public void initialize(long groupId) throws InitializationException {
		try {
			ServiceContext serviceContext = getServiceContext(groupId);

			configureB2BSite(groupId, serviceContext);

			_cpFileImporter.updateLookAndFeel(
				_MINIUM_THEME_ID, true, serviceContext);

			updateLogo(serviceContext);

			createRoles(serviceContext);

			_miniumLayoutsInitializer.initialize(serviceContext);

			List<CommerceWarehouse> commerceWarehouses =
				_importCommerceWarehouses(serviceContext);

			_importCPDefinitions(commerceWarehouses, serviceContext);

			createCPRule(serviceContext);

			_importThemePortletSettings(serviceContext);

			_importPortletSettings(serviceContext);

			setThemeSettings(serviceContext);
		}
		catch (InitializationException ie) {
			throw ie;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new InitializationException(e);
		}
	}

	@Override
	public boolean isActive(long companyId) {
		Theme theme = _themeLocalService.fetchTheme(
			companyId, _MINIUM_THEME_ID);

		if (theme == null) {
			if (_log.isInfoEnabled()) {
				_log.info(_MINIUM_THEME_ID + " is not registered");
			}

			return false;
		}

		return true;
	}

	protected void configureB2BSite(long groupId, ServiceContext serviceContext)
		throws Exception {

		updateOrganizationTypes();

		_commerceOrganizationLocalService.configureB2BSite(
			groupId, serviceContext);

		Group group = _groupLocalService.getGroup(groupId);

		_addDemoAccountOrganizations(
			group.getOrganizationId(), group.getNameCurrentValue(),
			ACCOUNT_ORGANIZATIONS_COUNT, serviceContext);

		_commerceCountryLocalService.importDefaultCountries(serviceContext);
		_commerceCurrencyLocalService.importDefaultValues(serviceContext);
		_commerceUserSegmentEntryLocalService.
			importSystemCommerceUserSegmentEntries(serviceContext);
		_commerceWarehousesImporter.importDefaultCommerceWarehouse(
			serviceContext);
		_cpMeasurementUnitLocalService.importDefaultValues(serviceContext);
	}

	protected void createCommerceRoles(JSONArray jsonArray) throws Exception {
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String name = jsonObject.getString("name");

			_createCommerceRole(name);
		}
	}

	protected void createCPRule(ServiceContext serviceContext)
		throws PortalException {

		CPRule cpRule = _cpRuleLocalService.addCPRule(
			"all", true, CPRuleConstants.TYPE_ALL_PRODUCTS, serviceContext);

		CommerceUserSegmentEntry commerceUserSegmentEntry =
			_commerceUserSegmentEntryLocalService.getCommerceUserSegmentEntry(
				serviceContext.getScopeGroupId(),
				CommerceUserSegmentEntryConstants.KEY_USER);

		_cpRuleUserSegmentRelLocalService.addCPRuleUserSegmentRel(
			cpRule.getCPRuleId(),
			commerceUserSegmentEntry.getCommerceUserSegmentEntryId(),
			serviceContext);
	}

	protected void createRoles(ServiceContext serviceContext) throws Exception {
		JSONArray jsonArray = _getJSONArray("roles.json");

		_cpFileImporter.createRoles(jsonArray, serviceContext);

		createCommerceRoles(jsonArray);

		updateUserRole(serviceContext);
	}

	protected ServiceContext getServiceContext(long groupId)
		throws PortalException {

		User user = _userLocalService.getUser(PrincipalThreadLocal.getUserId());
		Group group = _groupLocalService.getGroup(groupId);

		Locale locale = LocaleUtil.getSiteDefault();

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setCompanyId(group.getCompanyId());
		serviceContext.setLanguageId(LanguageUtil.getLanguageId(locale));
		serviceContext.setScopeGroupId(groupId);
		serviceContext.setTimeZone(user.getTimeZone());
		serviceContext.setUserId(user.getUserId());

		return serviceContext;
	}

	protected void setThemeSettings(ServiceContext serviceContext)
		throws Exception {

		JSONObject themeSettingsJSONObject = _getJSONObject(
			"theme-settings.json");

		Iterator<String> iterator = themeSettingsJSONObject.keys();

		while (iterator.hasNext()) {
			String key = iterator.next();

			String value = themeSettingsJSONObject.getString(key);

			updateThemeSetting(key, value, serviceContext);
		}
	}

	protected void updateLogo(ServiceContext serviceContext) throws Exception {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(
			DEPENDENCIES_PATH + "images/minium-logo.png");

		File file = FileUtil.createTempFile(inputStream);

		_cpFileImporter.updateLogo(file, true, true, serviceContext);
	}

	protected void updateOrganizationTypes() throws Exception {
		Configuration[] configurations = _configurationAdmin.listConfigurations(
			_getConfigurationFilter(_ORGANIZATION_TYPE_CONFIGURATION_PID));

		for (String organizationType : _ORGANIZATION_TYPES) {
			_updateOrganizationType(configurations, organizationType);
		}
	}

	protected void updateThemeSetting(
		String key, String value, ServiceContext serviceContext) {

		Theme theme = _themeLocalService.fetchTheme(
			serviceContext.getCompanyId(), _MINIUM_THEME_ID);

		if (theme == null) {
			return;
		}

		Map<String, ThemeSetting> configurableSettings =
			theme.getConfigurableSettings();

		ThemeSetting themeSetting = configurableSettings.get(key);

		themeSetting.setValue(value);
	}

	protected void updateUserRole(ServiceContext serviceContext)
		throws PortalException {

		Role role = _roleLocalService.fetchRole(
			serviceContext.getCompanyId(), "User");

		_resourcePermissionLocalService.addResourcePermission(
			serviceContext.getCompanyId(), "com.liferay.commerce.product",
			ResourceConstants.SCOPE_GROUP_TEMPLATE,
			String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
			role.getRoleId(), "VIEW_PRICE");
	}

	private void _addDemoAccountOrganizations(
			long organizationId, String groupName, int quantity,
			ServiceContext serviceContext)
		throws Exception {

		int i = 1;

		while (i <= quantity) {
			String name = "Demo Account " + groupName + StringPool.SPACE + i;

			_organizationLocalService.addOrganization(
				serviceContext.getUserId(), organizationId, name,
				CommerceOrganizationConstants.TYPE_ACCOUNT, 0L, 0L,
				ListTypeConstants.ORGANIZATION_STATUS_DEFAULT, StringPool.BLANK,
				false, serviceContext);

			i++;
		}
	}

	private void _createCommerceRole(String name) throws Exception {
		Configuration[] configurations = _configurationAdmin.listConfigurations(
			_getConfigurationFilter(_COMMERCE_ROLE_CONFIGURATION_PID));

		if (configurations != null) {
			for (Configuration configuration : configurations) {
				Dictionary<String, Object> properties =
					configuration.getProperties();

				String roleName = (String)properties.get("roleName");

				if (name.equals(roleName)) {
					return;
				}
			}
		}

		Configuration configuration =
			_configurationAdmin.createFactoryConfiguration(
				_COMMERCE_ROLE_CONFIGURATION_PID, StringPool.QUESTION);

		Dictionary<String, Object> properties = configuration.getProperties();

		if (properties == null) {
			properties = new Hashtable<>();
		}

		properties.put("roleName", name);

		configuration.update(properties);
	}

	private void _createOrganizationType(String organizationType)
		throws Exception {

		Configuration configuration =
			_configurationAdmin.createFactoryConfiguration(
				_ORGANIZATION_TYPE_CONFIGURATION_PID, StringPool.QUESTION);

		configuration.update(
			_getOrganizationTypeProperties(configuration, organizationType));
	}

	private String _getConfigurationFilter(String configurationPid) {
		StringBundler sb = new StringBundler(5);

		sb.append(StringPool.OPEN_PARENTHESIS);
		sb.append(ConfigurationAdmin.SERVICE_FACTORYPID);
		sb.append(StringPool.EQUAL);
		sb.append(configurationPid);
		sb.append(StringPool.CLOSE_PARENTHESIS);

		return sb.toString();
	}

	private String _getJSON(String name) throws IOException {
		return StringUtil.read(
			MiniumSiteInitializer.class.getClassLoader(),
			DEPENDENCIES_PATH + name);
	}

	private JSONArray _getJSONArray(String name) throws Exception {
		String json = _getJSON(name);

		return _jsonFactory.createJSONArray(json);
	}

	private JSONObject _getJSONObject(String name) throws Exception {
		String json = _getJSON(name);

		return _jsonFactory.createJSONObject(json);
	}

	private String[] _getOrganizationTypeChildrenTypes(
		String organizationType) {

		if (organizationType.equals(OrganizationConstants.TYPE_ORGANIZATION)) {
			return new String[] {
				OrganizationConstants.TYPE_ORGANIZATION,
				CommerceOrganizationConstants.TYPE_ACCOUNT
			};
		}
		else if (organizationType.equals(
					CommerceOrganizationConstants.TYPE_ACCOUNT)) {

			return new String[] {
				OrganizationConstants.TYPE_ORGANIZATION,
				CommerceOrganizationConstants.TYPE_BRANCH
			};
		}

		return new String[0];
	}

	private Dictionary<String, Object> _getOrganizationTypeProperties(
		Configuration configuration, String organizationType) {

		Dictionary<String, Object> properties = configuration.getProperties();

		if (properties == null) {
			properties = new Hashtable<>();
		}

		boolean rootable = false;

		if (organizationType.equals(OrganizationConstants.TYPE_ORGANIZATION)) {
			rootable = true;
		}

		properties.put(
			"childrenTypes",
			_getOrganizationTypeChildrenTypes(organizationType));
		properties.put("countryEnabled", false);
		properties.put("countryRequired", false);
		properties.put("name", organizationType);
		properties.put("rootable", rootable);

		return properties;
	}

	private List<CommerceWarehouse> _importCommerceWarehouses(
			ServiceContext serviceContext)
		throws Exception {

		JSONArray jsonArray = _getJSONArray("warehouses.json");

		return _commerceWarehousesImporter.importCommerceWarehouses(
			jsonArray, serviceContext);
	}

	private List<CPDefinition> _importCPDefinitions(
			List<CommerceWarehouse> commerceWarehouses,
			ServiceContext serviceContext)
		throws Exception {

		JSONArray jsonArray = _getJSONArray("products.json");

		long[] commerceWarehouseIds = ListUtil.toLongArray(
			commerceWarehouses,
			CommerceWarehouse.COMMERCE_WAREHOUSE_ID_ACCESSOR);

		return _cpDefinitionsImporter.importCPDefinitions(
			jsonArray, _COMMERCE_VOCABULARY, commerceWarehouseIds,
			MiniumSiteInitializer.class.getClassLoader(),
			DEPENDENCIES_PATH + "images/", serviceContext);
	}

	private void _importPortletSettings(ServiceContext serviceContext)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info("Importing portlet settings...");
		}

		JSONArray jsonArray = _getJSONArray("portlet-settings.json");

		_portletSettingsImporter.importPortletSettings(
			jsonArray, MiniumSiteInitializer.class.getClassLoader(),
			DEPENDENCIES_PATH + "display_templates/", serviceContext);

		if (_log.isInfoEnabled()) {
			_log.info("Portlet settings successfully imported");
		}
	}

	private void _importThemePortletSettings(ServiceContext serviceContext)
		throws Exception {

		JSONArray jsonArray = _getJSONArray("theme-portlet-settings.json");

		_portletSettingsImporter.importPortletSettings(
			jsonArray, MiniumSiteInitializer.class.getClassLoader(),
			DEPENDENCIES_PATH + "display_templates/", serviceContext);
	}

	private void _updateOrganizationType(
			Configuration[] configurations, String organizationType)
		throws Exception {

		if (configurations != null) {
			for (Configuration configuration : configurations) {
				Dictionary<String, Object> properties =
					configuration.getProperties();

				String name = (String)properties.get("name");

				if (organizationType.equals(name)) {
					configuration.update(
						_getOrganizationTypeProperties(
							configuration, organizationType));

					return;
				}
			}
		}

		_createOrganizationType(organizationType);
	}

	private static final String _COMMERCE_ROLE_CONFIGURATION_PID =
		"com.liferay.commerce.user.web.internal.configuration." +
			"CommerceRoleGroupServiceConfiguration";

	private static final String _COMMERCE_VOCABULARY = "Commerce";

	private static final String _MINIUM_THEME_ID = "minium_WAR_miniumtheme";

	private static final String _ORGANIZATION_TYPE_CONFIGURATION_PID =
		"com.liferay.organizations.service.internal.configuration." +
			"OrganizationTypeConfiguration";

	private static final String[] _ORGANIZATION_TYPES = ArrayUtil.append(
		new String[] {OrganizationConstants.TYPE_ORGANIZATION},
		CommerceOrganizationConstants.TYPES);

	private static final Log _log = LogFactoryUtil.getLog(
		MiniumSiteInitializer.class);

	@Reference
	private CommerceCountryLocalService _commerceCountryLocalService;

	@Reference
	private CommerceCurrencyLocalService _commerceCurrencyLocalService;

	@Reference
	private CommerceOrganizationLocalService _commerceOrganizationLocalService;

	@Reference
	private CommerceUserSegmentEntryLocalService
		_commerceUserSegmentEntryLocalService;

	@Reference
	private CommerceWarehousesImporter _commerceWarehousesImporter;

	@Reference
	private ConfigurationAdmin _configurationAdmin;

	@Reference
	private CPDefinitionsImporter _cpDefinitionsImporter;

	@Reference
	private CPFileImporter _cpFileImporter;

	@Reference
	private CPMeasurementUnitLocalService _cpMeasurementUnitLocalService;

	@Reference
	private CPRuleLocalService _cpRuleLocalService;

	@Reference
	private CPRuleUserSegmentRelLocalService _cpRuleUserSegmentRelLocalService;

	@Reference
	private CPSpecificationOptionLocalService
		_cpSpecificationOptionLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private MiniumLayoutsInitializer _miniumLayoutsInitializer;

	@Reference
	private OrganizationLocalService _organizationLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private PortletSettingsImporter _portletSettingsImporter;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.theme.minium.site.initializer)"
	)
	private ServletContext _servletContext;

	@Reference
	private ThemeLocalService _themeLocalService;

	@Reference
	private UserLocalService _userLocalService;

}