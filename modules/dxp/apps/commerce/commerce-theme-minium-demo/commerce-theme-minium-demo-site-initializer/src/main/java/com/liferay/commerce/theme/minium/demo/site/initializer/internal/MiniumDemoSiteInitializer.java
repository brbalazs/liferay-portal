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

package com.liferay.commerce.theme.minium.demo.site.initializer.internal;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.initializer.util.CPAttachmentFileEntryCreator;
import com.liferay.commerce.initializer.util.CommerceAccountsImporter;
import com.liferay.commerce.initializer.util.CommerceUserSegmentsImporter;
import com.liferay.commerce.initializer.util.CommerceUsersImporter;
import com.liferay.commerce.initializer.util.OrganizationImporter;
import com.liferay.commerce.product.model.CPAttachmentFileEntryConstants;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPAttachmentFileEntryLocalService;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.settings.ModifiableSettings;
import com.liferay.portal.kernel.settings.Settings;
import com.liferay.portal.kernel.settings.SettingsFactory;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;
import com.liferay.site.initializer.SiteInitializerRegistry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alec Sloan
 */
@Component(
	immediate = true,
	property = "site.initializer.key=" + MiniumDemoSiteInitializer.KEY,
	service = SiteInitializer.class
)
public class MiniumDemoSiteInitializer implements SiteInitializer {

	public static final String DEPENDENCIES_PATH =
		"com/liferay/commerce/theme/minium/demo/site/initializer/internal" +
			"/dependencies/";

	public static final String KEY = "minium-demo-initializer";

	@Override
	public String getDescription(Locale locale) {
		return siteInitializer.getDescription(locale);
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getName(Locale locale) {
		return "Minium Demo";
	}

	@Override
	public String getThumbnailSrc() {
		return siteInitializer.getThumbnailSrc();
	}

	@Override
	public void initialize(long groupId) throws InitializationException {
		try {
			ServiceContext serviceContext = getServiceContext(groupId);

			siteInitializer.initialize(groupId);

			_importCommerceUserSegments(serviceContext);

			List<Organization> organizations = _importCommerceOrganizations(
				serviceContext);

			List<CommerceAccount> commerceAccounts = _importCommerceAccounts(
				organizations, serviceContext);

			_importCommerceUsers(commerceAccounts, serviceContext);

			switchImagesToDemo(serviceContext);

			setDefaultCatalogImage(serviceContext);
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
		return siteInitializer.isActive(companyId);
	}

	@Activate
	protected void activate() {
		init();
	}

	@Deactivate
	protected void deactivate() {
		_commerceAccounts = null;
		siteInitializer = null;
	}

	protected CPInstance getCPInstanceBySku(String sku) {
		DynamicQuery dynamicQuery = _cpInstanceLocalService.dynamicQuery();

		Property nameProperty = PropertyFactoryUtil.forName("sku");

		dynamicQuery.add(nameProperty.eq(sku));

		List<CPInstance> cpInstances = _cpInstanceLocalService.dynamicQuery(
			dynamicQuery);

		return cpInstances.get(0);
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

	protected void init() {
		_commerceAccounts = new HashMap<>();

		_organizations = new HashMap<>();

		siteInitializer = _siteInitializerRegistry.getSiteInitializer(
			"minium-initializer");
	}

	protected void setDefaultCatalogImage(ServiceContext serviceContext)
		throws Exception {

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(
			DEPENDENCIES_PATH + "images/Minium_Demo_ProductImage_Default.png");

		File file = FileUtil.createTempFile(inputStream);

		String mimeType = MimeTypesUtil.getContentType(file);

		byte[] byteArray = FileUtil.getBytes(file);

		FileEntry fileEntry = _dlAppLocalService.addFileEntry(
			serviceContext.getUserId(), serviceContext.getScopeGroupId(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, "DefaultCatalogImage",
			mimeType, "DefaultCatalogImage", null, null, byteArray,
			serviceContext);

		Settings settings = _settingsFactory.getSettings(
			new GroupServiceSettingsLocator(
				serviceContext.getScopeGroupId(),
				"com.liferay.commerce.media.impl.configuration." +
					"CommerceMediaDefaultImageConfiguration"));

		ModifiableSettings modifiableSettings =
			settings.getModifiableSettings();

		modifiableSettings.setValue(
			"defaultFileEntryId", String.valueOf(fileEntry.getFileEntryId()));

		modifiableSettings.store();
	}

	protected void switchImagesToDemo(ServiceContext serviceContext)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info("Switching CPDefinition images to Demo images...");
		}

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		JSONArray jsonArray = _getJSONArrayFromMinium("products.json");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String sku = jsonObject.getString("Sku");

			if (Validator.isBlank(sku)) {
				JSONArray skusJSONArray = jsonObject.getJSONArray("Skus");

				JSONObject firstSkuJSONObject = skusJSONArray.getJSONObject(0);

				sku = firstSkuJSONObject.getString("Sku");
			}

			CPInstance cpInstance = getCPInstanceBySku(sku);

			CPDefinition cpDefinition =
				_cpDefinitionLocalService.getCPDefinition(
					cpInstance.getCPDefinitionId());

			_cpAttachmentFileEntryLocalService.deleteCPAttachmentFileEntries(
				CPDefinition.class.getName(), cpDefinition.getCPDefinitionId());

			JSONArray imagesJSONArray = jsonObject.getJSONArray("Images");

			if (imagesJSONArray != null) {
				for (int j = 0; j < imagesJSONArray.length(); j++) {
					String image = imagesJSONArray.getString(j);

					image = image.replace("Minium_", "Minium_Demo_");

					_cpAttachmentFileEntryCreator.addCPAttachmentFileEntry(
						cpDefinition, classLoader,
						DEPENDENCIES_PATH + "images/", image, j,
						CPAttachmentFileEntryConstants.TYPE_IMAGE,
						serviceContext);
				}
			}
		}
	}

	protected static final String MINIUM_DEPENDENCIES_PATH =
		"com/liferay/commerce/theme/minium/site/initializer/internal" +
			"/dependencies/";

	protected SiteInitializer siteInitializer;

	private String _getJSON(String name) throws IOException {
		return StringUtil.read(
			MiniumDemoSiteInitializer.class.getClassLoader(),
			DEPENDENCIES_PATH + name);
	}

	private JSONArray _getJSONArray(String name) throws Exception {
		String json = _getJSON(name);

		return _jsonFactory.createJSONArray(json);
	}

	private JSONArray _getJSONArrayFromMinium(String name) throws Exception {
		String json = _getJSONFromMinium(name);

		return _jsonFactory.createJSONArray(json);
	}

	private String _getJSONFromMinium(String name) throws IOException {
		Class<?> clazz = siteInitializer.getClass();

		return StringUtil.read(
			clazz.getClassLoader(), MINIUM_DEPENDENCIES_PATH + name);
	}

	private List<CommerceAccount> _importCommerceAccounts(
			List<Organization> organizations, ServiceContext serviceContext)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info("Importing Commerce Accounts...");
		}

		JSONArray jsonArray = _getJSONArray("accounts.json");

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		for (Organization organization : organizations) {
			_organizations.put(organization.getName(), organization);
		}

		List<CommerceAccount> commerceAccounts =
			_commerceAccountsImporter.importCommerceAccounts(
				jsonArray, classLoader, _organizations, DEPENDENCIES_PATH,
				serviceContext);

		if (_log.isInfoEnabled()) {
			_log.info("Commerce Accounts successfully imported");
		}

		return commerceAccounts;
	}

	private List<Organization> _importCommerceOrganizations(
			ServiceContext serviceContext)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info("Importing organizations...");
		}

		JSONArray jsonArray = _getJSONArray("organizations.json");

		List<Organization> organizations =
			_organizationImporter.importOrganizations(
				jsonArray, serviceContext);

		if (_log.isInfoEnabled()) {
			_log.info("Organizations successfully imported");
		}

		return organizations;
	}

	private void _importCommerceUsers(
			List<CommerceAccount> commerceAccounts,
			ServiceContext serviceContext)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info("Importing Commerce Users...");
		}

		for (CommerceAccount commerceAccount : commerceAccounts) {
			_commerceAccounts.put(commerceAccount.getName(), commerceAccount);
		}

		JSONArray jsonArray = _getJSONArray("users.json");

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		_commerceUsersImporter.importCommerceUsers(
			jsonArray, classLoader, _commerceAccounts, DEPENDENCIES_PATH,
			serviceContext);

		if (_log.isInfoEnabled()) {
			_log.info("Commerce Users successfully imported");
		}
	}

	private void _importCommerceUserSegments(ServiceContext serviceContext)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info("Importing Commerce User Segments...");
		}

		JSONArray jsonArray = _getJSONArray("segments.json");

		_commerceUserSegmentsImporter.importCommerceUserSegments(
			jsonArray, serviceContext);

		if (_log.isInfoEnabled()) {
			_log.info("Commerce User Segments successfully imported");
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MiniumDemoSiteInitializer.class);

	private Map<String, CommerceAccount> _commerceAccounts;

	@Reference
	private CommerceAccountsImporter _commerceAccountsImporter;

	@Reference
	private CommerceUserSegmentsImporter _commerceUserSegmentsImporter;

	@Reference
	private CommerceUsersImporter _commerceUsersImporter;

	@Reference
	private CPAttachmentFileEntryCreator _cpAttachmentFileEntryCreator;

	@Reference
	private CPAttachmentFileEntryLocalService
		_cpAttachmentFileEntryLocalService;

	@Reference
	private CPDefinitionLocalService _cpDefinitionLocalService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private OrganizationImporter _organizationImporter;

	private Map<String, Organization> _organizations;

	@Reference
	private SettingsFactory _settingsFactory;

	@Reference
	private SiteInitializerRegistry _siteInitializerRegistry;

	@Reference
	private UserLocalService _userLocalService;

}