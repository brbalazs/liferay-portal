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

import com.liferay.commerce.initializer.util.CPAttachmentFileEntryCreator;
import com.liferay.commerce.initializer.util.CommerceAccountsImporter;
import com.liferay.commerce.initializer.util.CommercePriceEntriesImporter;
import com.liferay.commerce.initializer.util.CommercePriceListsImporter;
import com.liferay.commerce.initializer.util.CommerceUserSegmentsImporter;
import com.liferay.commerce.initializer.util.CommerceUsersImporter;
import com.liferay.commerce.initializer.util.OrganizationImporter;
import com.liferay.commerce.media.CommerceCatalogDefaultImage;
import com.liferay.commerce.model.CommerceShippingEngine;
import com.liferay.commerce.model.CommerceShippingMethod;
import com.liferay.commerce.product.model.CPAttachmentFileEntry;
import com.liferay.commerce.product.model.CPAttachmentFileEntryConstants;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CPInstance;
import com.liferay.commerce.product.service.CPAttachmentFileEntryLocalService;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.service.CommerceShippingMethodLocalService;
import com.liferay.commerce.shipping.engine.fixed.service.CommerceShippingFixedOptionLocalService;
import com.liferay.commerce.util.CommerceShippingEngineRegistry;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;

import java.io.IOException;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

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
		return _siteInitializer.getDescription(locale);
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
		return _servletContext.getContextPath() + "/images/thumbnail.png";
	}

	@Override
	public void initialize(long groupId) throws InitializationException {
		try {
			ServiceContext serviceContext = getServiceContext(groupId);

			_siteInitializer.initialize(groupId);

			_importCommercePriceLists(serviceContext);

			_importCommercePriceEntries(serviceContext);

			_importCommerceOrganizations(serviceContext);

			_importCommerceAccounts(serviceContext);

			_importCommerceUsers(serviceContext);

			_importCommerceUserSegments(serviceContext);

			switchImagesToDemo(serviceContext);

			setCommerceShippingMethod("fixed", serviceContext);
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
		return _siteInitializer.isActive(companyId);
	}

	@Activate
	protected void activate() {
	}

	@Deactivate
	protected void deactivate() {
		_siteInitializer = null;
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

	protected void setCommerceShippingMethod(
			String shippingMethod, ServiceContext serviceContext)
		throws PortalException {

		Locale locale = serviceContext.getLocale();

		CommerceShippingEngine commerceShippingEngine =
			_commerceShippingEngineRegistry.getCommerceShippingEngine(
				shippingMethod);

		Map<Locale, String> nameMap = new HashMap<>();
		Map<Locale, String> descriptionMap = new HashMap<>();

		nameMap.put(locale, commerceShippingEngine.getName(locale));
		descriptionMap.put(
			locale, commerceShippingEngine.getDescription(locale));

		CommerceShippingMethod commerceShippingMethod =
			_commerceShippingMethodLocalService.addCommerceShippingMethod(
				nameMap, descriptionMap, null, shippingMethod, 0, true,
				serviceContext);

		setCommerceShippingOption(
			commerceShippingMethod.getCommerceShippingMethodId(),
			"Standard Delivery", StringPool.BLANK, BigDecimal.valueOf(15),
			serviceContext);

		setCommerceShippingOption(
			commerceShippingMethod.getCommerceShippingMethodId(),
			"Expedited Delivery", StringPool.BLANK, BigDecimal.valueOf(25),
			serviceContext);

	}

	protected void setCommerceShippingOption(
			long commerceShippingMethodId, String name, String description,
			BigDecimal price, ServiceContext serviceContext)
		throws PortalException {

		Map<Locale, String> nameMap = new HashMap<>();
		Map<Locale, String> descriptionMap = new HashMap<>();

		nameMap.put(serviceContext.getLocale(), name);
		descriptionMap.put(serviceContext.getLocale(), description);

		_commerceShippingFixedOptionLocalService.addCommerceShippingFixedOption(
			commerceShippingMethodId, nameMap, descriptionMap, price, 0,
			serviceContext);

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

			CPInstance cpInstance =
				_cpInstanceLocalService.fetchByExternalReferenceCode(
					serviceContext.getCompanyId(), StringBundler.concat(
						String.valueOf(serviceContext.getScopeGroupId()), "_",
						sku));

			CPDefinition cpDefinition =
				_cpDefinitionLocalService.getCPDefinition(
					cpInstance.getCPDefinitionId());

			long classNameId = _portal.getClassNameId(CPDefinition.class);

			List<CPAttachmentFileEntry> images =
				_cpAttachmentFileEntryLocalService.getCPAttachmentFileEntries(
					classNameId, cpDefinition.getCPDefinitionId(),
					CPAttachmentFileEntryConstants.TYPE_IMAGE,
					WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS);

			for (CPAttachmentFileEntry image : images) {
				_cpAttachmentFileEntryLocalService.deleteCPAttachmentFileEntry(
					image);
			}

			JSONArray imagesJSONArray = jsonObject.getJSONArray("Images");

			if (imagesJSONArray != null) {
				for (int j = 0; j < imagesJSONArray.length(); j++) {
					String image = imagesJSONArray.getString(j);

					image = image.replace("Minium_", "Minium_Demo_");

					_cpAttachmentFileEntryCreator.addCPAttachmentFileEntry(
						cpDefinition, classLoader,
						DEPENDENCIES_PATH + "images/", image, j,
						CPAttachmentFileEntryConstants.TYPE_IMAGE,
						serviceContext.getScopeGroupId(),
						serviceContext.getUserId());
				}
			}
		}
	}

	protected static final String MINIUM_DEPENDENCIES_PATH =
		"com/liferay/commerce/theme/minium/site/initializer/internal" +
			"/dependencies/";

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
		Class<?> clazz = _siteInitializer.getClass();

		return StringUtil.read(
			clazz.getClassLoader(), MINIUM_DEPENDENCIES_PATH + name);
	}

	private void _importCommerceAccounts(ServiceContext serviceContext)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info("Importing Commerce Accounts...");
		}

		JSONArray jsonArray = _getJSONArray("accounts.json");

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		_commerceAccountsImporter.importCommerceAccounts(
			jsonArray, classLoader, DEPENDENCIES_PATH,
			serviceContext.getScopeGroupId(), serviceContext.getUserId());

		if (_log.isInfoEnabled()) {
			_log.info("Commerce Accounts successfully imported");
		}
	}

	private void _importCommerceOrganizations(ServiceContext serviceContext)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info("Importing organizations...");
		}

		JSONArray jsonArray = _getJSONArray("organizations.json");

		_organizationImporter.importOrganizations(
			jsonArray, serviceContext.getScopeGroupId(),
			serviceContext.getUserId());

		if (_log.isInfoEnabled()) {
			_log.info("Organizations successfully imported");
		}
	}

	private void _importCommercePriceEntries(ServiceContext serviceContext)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info("Importing commerce price entries...");
		}

		JSONArray jsonArray = _getJSONArray("price-entries.json");

		_commercePriceEntriesImporter.importCommercePriceEntries(
			jsonArray, serviceContext.getScopeGroupId(),
			serviceContext.getUserId());

		if (_log.isInfoEnabled()) {
			_log.info("Commerce price entries successfully imported");
		}
	}

	private void _importCommercePriceLists(ServiceContext serviceContext)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info("Importing commerce price lists...");
		}

		JSONArray jsonArray = _getJSONArray("price-lists.json");

		_commercePriceListsImporter.importCommercePriceLists(
			jsonArray, serviceContext.getScopeGroupId(),
			serviceContext.getUserId());

		if (_log.isInfoEnabled()) {
			_log.info("Commerce price lists successfully imported");
		}
	}

	private void _importCommerceUsers(ServiceContext serviceContext)
		throws Exception {

		if (_log.isInfoEnabled()) {
			_log.info("Importing Commerce Users...");
		}

		JSONArray jsonArray = _getJSONArray("users.json");

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		_commerceUsersImporter.importCommerceUsers(
			jsonArray, classLoader, DEPENDENCIES_PATH,
			serviceContext.getScopeGroupId(), serviceContext.getUserId());

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
			jsonArray, serviceContext.getScopeGroupId(),
			serviceContext.getUserId());

		if (_log.isInfoEnabled()) {
			_log.info("Commerce User Segments successfully imported");
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MiniumDemoSiteInitializer.class);

	@Reference
	private CommerceAccountsImporter _commerceAccountsImporter;

	@Reference
	private CommerceCatalogDefaultImage _commerceCatalogDefaultImage;

	@Reference
	private CommercePriceEntriesImporter _commercePriceEntriesImporter;

	@Reference
	private CommercePriceListsImporter _commercePriceListsImporter;

	@Reference
	private CommerceShippingEngineRegistry _commerceShippingEngineRegistry;

	@Reference
	private CommerceShippingFixedOptionLocalService
		_commerceShippingFixedOptionLocalService;

	@Reference
	private CommerceShippingMethodLocalService
		_commerceShippingMethodLocalService;

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

	@Reference
	private Portal _portal;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.theme.minium.demo.site.initializer)"
	)
	private ServletContext _servletContext;

	@Reference(target = "(site.initializer.key=minium-initializer)")
	private SiteInitializer _siteInitializer;

	@Reference
	private UserLocalService _userLocalService;

}