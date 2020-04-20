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

package com.liferay.commerce.product.ddm.internal;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.util.CommerceAccountHelper;
import com.liferay.commerce.media.CommerceMediaResolver;
import com.liferay.commerce.product.ddm.DDMHelper;
import com.liferay.commerce.product.model.CPDefinitionOptionRel;
import com.liferay.commerce.product.model.CPDefinitionOptionValueRel;
import com.liferay.commerce.product.permission.CommerceProductViewPermission;
import com.liferay.commerce.product.service.CPAttachmentFileEntryLocalService;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.commerce.product.service.CPDefinitionOptionRelLocalService;
import com.liferay.commerce.product.service.CPDefinitionOptionValueRelLocalService;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.product.service.CPInstanceOptionValueRelLocalService;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.commerce.product.util.DDMFormValuesHelper;
import com.liferay.commerce.product.util.JsonHelper;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderer;
import com.liferay.dynamic.data.mapping.form.renderer.DDMFormRenderingContext;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.DDMFormRule;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 * @author Igor Beslic
 */
@Component(immediate = true, service = DDMHelper.class)
public class DDMHelperImpl implements DDMHelper {

	@Override
	public DDMForm getCPAttachmentFileEntryDDMForm(
			long cpDefinitionId, Locale locale,
			Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
				cpDefinitionOptionRelCPDefinitionOptionValueRels)
		throws PortalException {

		return _getDDMForm(
			cpDefinitionId, locale, false, true, true, false,
			cpDefinitionOptionRelCPDefinitionOptionValueRels);
	}

	@Override
	public DDMForm getCPInstanceDDMForm(
			long cpDefinitionId, Locale locale, boolean ignoreSKUCombinations,
			boolean skuContributor,
			Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
				cpDefinitionOptionRelCPDefinitionOptionValueRels)
		throws PortalException {

		return _getDDMForm(
			cpDefinitionId, locale, ignoreSKUCombinations, skuContributor,
			false, false, cpDefinitionOptionRelCPDefinitionOptionValueRels);
	}

	@Override
	public DDMForm getPublicStoreDDMForm(
			long groupId, long commerceAccountId, long cpDefinitionId,
			Locale locale, boolean ignoreSKUCombinations,
			boolean skuContributor,
			Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
				cpDefinitionOptionRelCPDefinitionOptionValueRels)
		throws PortalException {

		DDMForm ddmForm = _getDDMForm(
			cpDefinitionId, locale, ignoreSKUCombinations, skuContributor,
			false, true, cpDefinitionOptionRelCPDefinitionOptionValueRels);

		if (!ignoreSKUCombinations) {
			ddmForm.addDDMFormRule(
				_createDDMFormRule(
					ddmForm, groupId, commerceAccountId, cpDefinitionId));
		}

		return ddmForm;
	}

	@Override
	public String renderCPAttachmentFileEntryOptions(
			long cpDefinitionId, String json, RenderRequest renderRequest,
			RenderResponse renderResponse,
			Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
				cpDefinitionOptionRelCPDefinitionOptionValueRels)
		throws PortalException {

		Locale locale = _portal.getLocale(renderRequest);

		DDMForm ddmForm = getCPAttachmentFileEntryDDMForm(
			cpDefinitionId, locale,
			cpDefinitionOptionRelCPDefinitionOptionValueRels);

		return _render(
			cpDefinitionId, locale, ddmForm, json, renderRequest,
			renderResponse);
	}

	@Override
	public String renderCPInstanceOptions(
			long cpDefinitionId, String json, boolean ignoreSKUCombinations,
			boolean skuContributor, RenderRequest renderRequest,
			RenderResponse renderResponse,
			Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
				cpDefinitionOptionRelCPDefinitionOptionValueRels)
		throws PortalException {

		Locale locale = _portal.getLocale(renderRequest);

		DDMForm ddmForm = getCPInstanceDDMForm(
			cpDefinitionId, locale, ignoreSKUCombinations, skuContributor,
			cpDefinitionOptionRelCPDefinitionOptionValueRels);

		return _render(
			cpDefinitionId, locale, ddmForm, json, renderRequest,
			renderResponse);
	}

	@Override
	public String renderPublicStoreOptions(
			long cpDefinitionId, String json, boolean ignoreSKUCombinations,
			boolean skuContributor, RenderRequest renderRequest,
			RenderResponse renderResponse,
			Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
				cpDefinitionOptionRelCPDefinitionOptionValueRels)
		throws PortalException {

		Locale locale = _portal.getLocale(renderRequest);

		CommerceAccount commerceAccount =
			_commerceAccountHelper.getCurrentCommerceAccount(
				_commerceChannelLocalService.
					getCommerceChannelGroupIdBySiteGroupId(
						_portal.getScopeGroupId(renderRequest)),
				_portal.getHttpServletRequest(renderRequest));

		long commerceAccountId = 0;

		if (commerceAccount != null) {
			commerceAccountId = commerceAccount.getCommerceAccountId();
		}

		DDMForm ddmForm = getPublicStoreDDMForm(
			_portal.getScopeGroupId(renderRequest), commerceAccountId,
			cpDefinitionId, locale, ignoreSKUCombinations, skuContributor,
			cpDefinitionOptionRelCPDefinitionOptionValueRels);

		return _render(
			cpDefinitionId, locale, ddmForm, json, renderRequest,
			renderResponse);
	}

	private DDMFormRule _createDDMFormRule(
		DDMForm ddmForm, long groupId, long commerceAccountId,
		long cpDefinitionId) {

		String action = _createDDMFormRuleAction(
			ddmForm, groupId, commerceAccountId, cpDefinitionId);

		return new DDMFormRule("TRUE", action);
	}

	/**
	 * Create a DDM form rule action as a call function, e.g.
	 * <pre>
	 * call(
	 * 	'getCPInstanceOptionsValues',
	 * 	concat(
	 * 		'cpDefinitionId=56698', ';', '56703=', getValue('56703'), ';',
	 * 		'56706=', getValue('56706')),
	 * 	'56703=color;56706=size')
	 * </pre>
	 */
	private String _createDDMFormRuleAction(
		DDMForm ddmForm, long groupId, long commerceAccountId,
		long cpDefinitionId) {

		String callFunctionStatement =
			"call('getCPInstanceOptionsValues', concat(%s), '%s')";

		return String.format(
			callFunctionStatement,
			_createDDMFormRuleInputMapping(
				ddmForm, groupId, commerceAccountId, cpDefinitionId),
			_createDDMFormRuleOutputMapping(ddmForm));
	}

	private String _createDDMFormRuleInputMapping(
		DDMForm ddmForm, long groupId, long commerceAccountId,
		long cpDefinitionId) {

		// The input information will be transformed in parameter request of
		// DDMDataProviderRequest class and it'll be accessible in the data
		// provider implementation.

		String inputMappingStatement = "'%s=', getValue('%s')";
		String delimiter = ", ';',";

		List<DDMFormField> ddmFormFields = ddmForm.getDDMFormFields();

		Stream<DDMFormField> stream = ddmFormFields.stream();

		Stream<String> inputMappingStatementStream = stream.map(
			field -> String.format(
				inputMappingStatement, field.getName(), field.getName()));

		inputMappingStatementStream = Stream.concat(
			Stream.of(
				String.format(
					"'cpDefinitionId=%s'", String.valueOf(cpDefinitionId))),
			inputMappingStatementStream);

		inputMappingStatementStream = Stream.concat(
			Stream.of(String.format("'groupId=%s'", String.valueOf(groupId))),
			inputMappingStatementStream);

		inputMappingStatementStream = Stream.concat(
			Stream.of(
				String.format(
					"'commerceAccountId=%s'",
					String.valueOf(commerceAccountId))),
			inputMappingStatementStream);

		return inputMappingStatementStream.collect(
			Collectors.joining(delimiter));
	}

	private String _createDDMFormRuleOutputMapping(DDMForm ddmForm) {
		String outputMappingStatement = "%s=%s";

		List<DDMFormField> ddmFormFields = ddmForm.getDDMFormFields();

		Stream<DDMFormField> stream = ddmFormFields.stream();

		Stream<String> stringStream = stream.map(
			field -> String.format(
				outputMappingStatement, field.getName(), field.getName()));

		return stringStream.collect(Collectors.joining(StringPool.SEMICOLON));
	}

	private DDMForm _getDDMForm(
			long cpDefinitionId, Locale locale, boolean ignoreSKUCombinations,
			boolean skuContributor, boolean optional, boolean publicStore,
			Map<CPDefinitionOptionRel, List<CPDefinitionOptionValueRel>>
				cpDefinitionOptionRelCPDefinitionOptionValueRels)
		throws PortalException {

		if (cpDefinitionOptionRelCPDefinitionOptionValueRels.isEmpty()) {
			return null;
		}

		DDMForm ddmForm = new DDMForm();

		for (CPDefinitionOptionRel cpDefinitionOptionRel :
				cpDefinitionOptionRelCPDefinitionOptionValueRels.keySet()) {

			if (Validator.isNull(
					cpDefinitionOptionRel.getDDMFormFieldTypeName())) {

				continue;
			}

			List<CPDefinitionOptionValueRel> cpDefinitionOptionValueRels =
				cpDefinitionOptionRelCPDefinitionOptionValueRels.get(
					cpDefinitionOptionRel);

			DDMFormField ddmFormField = _getDDMFormField(
				cpDefinitionOptionRel, cpDefinitionOptionValueRels, locale);

			ddmFormField.setRequired(
				_isDDMFormRequired(
					cpDefinitionOptionRel, ignoreSKUCombinations, optional,
					publicStore));

			ddmForm.addDDMFormField(ddmFormField);
		}

		ddmForm.addAvailableLocale(locale);
		ddmForm.setDefaultLocale(locale);

		return ddmForm;
	}

	private DDMFormField _getDDMFormField(
		CPDefinitionOptionRel cpDefinitionOptionRel,
		List<CPDefinitionOptionValueRel> cpDefinitionOptionValueRels,
		Locale locale) {

		DDMFormField ddmFormField = new DDMFormField(
			cpDefinitionOptionRel.getKey(),
			cpDefinitionOptionRel.getDDMFormFieldTypeName());

		LocalizedValue ddmFormFieldLabelLocalizedValue = new LocalizedValue(
			locale);

		ddmFormFieldLabelLocalizedValue.addString(
			locale, cpDefinitionOptionRel.getName(locale));

		ddmFormField.setLabel(ddmFormFieldLabelLocalizedValue);

		if (cpDefinitionOptionValueRels.isEmpty()) {
			return ddmFormField;
		}

		DDMFormFieldOptions ddmFormFieldOptions = _getDDMFormFieldOptions(
			cpDefinitionOptionValueRels, locale);

		ddmFormField.setDDMFormFieldOptions(ddmFormFieldOptions);

		if (cpDefinitionOptionRel.isSkuContributor()) {
			ddmFormField.setPredefinedValue(
				_getDDMFormFieldPredefinedValue(ddmFormFieldOptions));
		}

		return ddmFormField;
	}

	private DDMFormFieldOptions _getDDMFormFieldOptions(
		List<CPDefinitionOptionValueRel> cpDefinitionOptionValueRels,
		Locale locale) {

		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		for (CPDefinitionOptionValueRel cpDefinitionOptionValueRel :
				cpDefinitionOptionValueRels) {

			ddmFormFieldOptions.addOptionLabel(
				cpDefinitionOptionValueRel.getKey(), locale,
				cpDefinitionOptionValueRel.getName(locale));
		}

		return ddmFormFieldOptions;
	}

	private LocalizedValue _getDDMFormFieldPredefinedValue(
		DDMFormFieldOptions ddmFormFieldOptions) {

		Map<String, LocalizedValue> options = ddmFormFieldOptions.getOptions();

		if (options.isEmpty()) {
			return new LocalizedValue(ddmFormFieldOptions.getDefaultLocale());
		}

		for (Map.Entry<String, LocalizedValue> entry : options.entrySet()) {
			LocalizedValue localizedValue = new LocalizedValue();

			LocalizedValue curLocalizedValue = entry.getValue();

			localizedValue.addString(
				curLocalizedValue.getDefaultLocale(), entry.getKey());

			return localizedValue;
		}

		throw new IllegalArgumentException(
			"Provided DDM field options miss valid field value");
	}

	private boolean _isDDMFormRequired(
		CPDefinitionOptionRel cpDefinitionOptionRel,
		boolean ignoreSKUCombinations, boolean optional, boolean publicStore) {

		if (optional) {
			return false;
		}

		Map<String, Object> properties =
			_ddmFormFieldTypeServicesTracker.getDDMFormFieldTypeProperties(
				cpDefinitionOptionRel.getDDMFormFieldTypeName());

		String fieldTypeDataDomain = MapUtil.getString(
			properties, "ddm.form.field.type.data.domain");

		if (Validator.isNotNull(fieldTypeDataDomain) &&
			fieldTypeDataDomain.equals("list")) {

			int cpDefinitionOptionValueRelsCount =
				_cpDefinitionOptionValueRelLocalService.
					getCPDefinitionOptionValueRelsCount(
						cpDefinitionOptionRel.getCPDefinitionOptionRelId());

			if (cpDefinitionOptionValueRelsCount == 0) {
				return false;
			}
		}

		if (ignoreSKUCombinations) {
			return cpDefinitionOptionRel.isRequired();
		}

		if (cpDefinitionOptionRel.isSkuContributor() ||
			(publicStore && cpDefinitionOptionRel.isRequired())) {

			return true;
		}

		return false;
	}

	private String _render(
			long cpDefinitionId, Locale locale, DDMForm ddmForm, String json,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortalException {

		if (ddmForm == null) {
			return StringPool.BLANK;
		}

		HttpServletRequest httpServletRequest = _portal.getHttpServletRequest(
			renderRequest);

		HttpServletResponse httpServletResponse =
			_portal.getHttpServletResponse(renderResponse);

		DDMFormRenderingContext ddmFormRenderingContext =
			new DDMFormRenderingContext();

		ddmFormRenderingContext.setContainerId(
			"ProductOptions" + String.valueOf(cpDefinitionId));
		ddmFormRenderingContext.setHttpServletRequest(httpServletRequest);
		ddmFormRenderingContext.setHttpServletResponse(httpServletResponse);
		ddmFormRenderingContext.setLocale(locale);
		ddmFormRenderingContext.setPortletNamespace(
			renderResponse.getNamespace());
		ddmFormRenderingContext.setShowRequiredFieldsWarning(false);

		if (Validator.isNotNull(json)) {
			DDMFormValues ddmFormValues = _ddmFormValuesHelper.deserialize(
				ddmForm, json, locale);

			if (ddmFormValues != null) {
				ddmFormRenderingContext.setDDMFormValues(ddmFormValues);
			}
		}

		return _ddmFormRenderer.render(ddmForm, ddmFormRenderingContext);
	}

	@Reference
	private CommerceAccountHelper _commerceAccountHelper;

	@Reference
	private CommerceChannelLocalService _commerceChannelLocalService;

	@Reference
	private CommerceMediaResolver _commerceMediaResolver;

	@Reference
	private CommerceProductViewPermission _commerceProductViewPermission;

	@Reference
	private CPAttachmentFileEntryLocalService
		_cpAttachmentFileEntryLocalService;

	@Reference
	private CPDefinitionLocalService _cpDefinitionLocalService;

	@Reference
	private CPDefinitionOptionRelLocalService
		_cpDefinitionOptionRelLocalService;

	@Reference
	private CPDefinitionOptionValueRelLocalService
		_cpDefinitionOptionValueRelLocalService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference
	private CPInstanceOptionValueRelLocalService
		_cpInstanceOptionValueRelLocalService;

	@Reference
	private DDMFormFieldTypeServicesTracker _ddmFormFieldTypeServicesTracker;

	@Reference
	private DDMFormRenderer _ddmFormRenderer;

	@Reference
	private DDMFormValuesHelper _ddmFormValuesHelper;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private JsonHelper _jsonHelper;

	@Reference
	private Portal _portal;

}