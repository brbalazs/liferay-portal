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

package com.liferay.commerce.theme.minium.impl.internal.product.renderer.list.entry;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.constants.CPDefinitionInventoryConstants;
import com.liferay.commerce.constants.CommerceWebKeys;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.frontend.template.soy.renderer.ComponentDescriptor;
import com.liferay.commerce.frontend.template.soy.renderer.SoyComponentRenderer;
import com.liferay.commerce.inventory.CPDefinitionInventoryEngineRegistry;
import com.liferay.commerce.model.CPDefinitionInventory;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.price.CommerceProductPrice;
import com.liferay.commerce.price.CommerceProductPriceCalculation;
import com.liferay.commerce.product.catalog.CPCatalogEntry;
import com.liferay.commerce.product.catalog.CPSku;
import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.commerce.product.content.constants.CPContentWebKeys;
import com.liferay.commerce.product.content.render.list.entry.CPContentListEntryRenderer;
import com.liferay.commerce.product.content.util.CPContentHelper;
import com.liferay.commerce.product.service.CPInstanceLocalService;
import com.liferay.commerce.service.CPDefinitionInventoryLocalService;
import com.liferay.commerce.theme.minium.impl.internal.product.model.PriceModel;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(
	immediate = true,
	property = {
		"commerce.product.content.list.entry.renderer.key=" + MiniumCPContentListEntryRenderer.KEY,
		"commerce.product.content.list.entry.renderer.portlet.name=" + CPPortletKeys.CP_COMPARE_CONTENT_WEB,
		"commerce.product.content.list.entry.renderer.portlet.name=" + CPPortletKeys.CP_PUBLISHER_WEB,
		"commerce.product.content.list.entry.renderer.portlet.name=" + CPPortletKeys.CP_SEARCH_RESULTS,
		"commerce.product.content.list.entry.renderer.type=grouped",
		"commerce.product.content.list.entry.renderer.type=simple",
		"commerce.product.content.list.entry.renderer.type=virtual"
	},
	service = CPContentListEntryRenderer.class
)
public class MiniumCPContentListEntryRenderer
	implements CPContentListEntryRenderer {

	public static final String KEY = "list-entry-minium";

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.get(resourceBundle, "minium");
	}

	@Override
	public void render(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws Exception {

		CommerceContext commerceContext =
			(CommerceContext)httpServletRequest.getAttribute(
				CommerceWebKeys.COMMERCE_CONTEXT);

		CPContentHelper cpContentHelper =
			(CPContentHelper)httpServletRequest.getAttribute(
				CPContentWebKeys.CP_CONTENT_HELPER);

		CPCatalogEntry cpCatalogEntry = cpContentHelper.getCPCatalogEntry(
			httpServletRequest);

		List<CPSku> cpSkus = cpCatalogEntry.getCPSkus();

		CPSku cpSku = null;

		if (cpSkus.size() == 1) {
			cpSku = cpSkus.get(0);
		}

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Map<String, Object> context = new HashMap<>();

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		String portletName = portletDisplay.getPortletName();

		if (portletName.equals(CPPortletKeys.CP_COMPARE_CONTENT_WEB)) {
			PortletURL editCompareProductActionURL =
				PortletURLFactoryUtil.create(
					httpServletRequest, CPPortletKeys.CP_COMPARE_CONTENT_WEB,
					PortletRequest.ACTION_PHASE);

			editCompareProductActionURL.setParameter(
				ActionRequest.ACTION_NAME, "editCompareProduct");

			context.put(
				"compareContentNamespace",
				_portal.getPortletNamespace(
					CPPortletKeys.CP_COMPARE_CONTENT_WEB));
			context.put(
				"editCompareProductActionURL",
				editCompareProductActionURL.toString());
			context.put("isCompareCheckboxVisible", false);
			context.put("isDeleteButtonVisible", true);
		}
		else {
			context.put("isCompareCheckboxVisible", true);
			context.put("isDeleteButtonVisible", false);
		}

		CommerceAccount commerceAccount = commerceContext.getCommerceAccount();

		if (commerceAccount != null) {
			context.put("accountId", commerceAccount.getCommerceAccountId());
		}

		CommerceOrder commerceOrder = commerceContext.getCommerceOrder();

		if (commerceOrder != null) {
			context.put("orderId", commerceOrder.getCommerceOrderId());
		}

		context.put("availability", "available");
		context.put(
			"cartAPI",
			_portal.getPortalURL(httpServletRequest) +
				"/o/commerce-ui/cart-item");
		context.put("categories", null);
		context.put("description", null);
		context.put(
			"detailsLink",
			cpContentHelper.getFriendlyURL(cpCatalogEntry, themeDisplay));
		context.put("minQuantity", null);
		context.put("name", cpCatalogEntry.getName());
		context.put("pictureUrl", cpCatalogEntry.getDefaultImageFileUrl());
		context.put("productId", cpCatalogEntry.getCPDefinitionId());

		if (cpSku != null) {
			context.put("sku", cpSku.getSku());
			context.put("skuId", cpSku.getCPInstanceId());

			PriceModel priceModel = _getPrice(
				cpSku.getCPInstanceId(),
				_getMinQuantity(cpCatalogEntry.getCPDefinitionId()),
				commerceContext, themeDisplay.getLocale());

			context.put("prices", priceModel);
		}

		context.put(
			"spritemap",
			themeDisplay.getPathThemeImages() + "/commerce-icons.svg");

		String module =
			"commerce-theme-minium-impl@1.0.6/product_card/ProductCard.es";

		Set<String> dependencies = new HashSet<>();

		dependencies.add(
			"commerce-frontend-taglib@1.0.3/add_to_cart/AddToCartButton.es");
		dependencies.add("commerce-frontend-taglib@1.0.3/price/Price.es");

		ComponentDescriptor componentDescriptor = new ComponentDescriptor(
			"ProductCard.render", module, null, dependencies);

		_soyComponentRenderer.renderSoyComponent(
			httpServletRequest, httpServletResponse, componentDescriptor,
			context);
	}

	private int _getMinQuantity(long cpDefinitionId) {
		CPDefinitionInventory cpDefinitionInventory =
			_cpDefinitionInventoryLocalService.
				fetchCPDefinitionInventoryByCPDefinitionId(cpDefinitionId);

		if (cpDefinitionInventory != null) {
			return cpDefinitionInventory.getMinOrderQuantity();
		}

		return CPDefinitionInventoryConstants.DEFAULT_MIN_ORDER_QUANTITY;
	}

	private PriceModel _getPrice(
			long cpInstanceId, int quantity, CommerceContext commerceContext,
			Locale locale)
		throws PortalException {

		CommerceProductPrice commerceProductPrice =
			_commerceProductPriceCalculation.getCommerceProductPrice(
				cpInstanceId, quantity, true, commerceContext);

		if (commerceProductPrice == null) {
			return null;
		}

		CommerceMoney unitPrice = commerceProductPrice.getUnitPrice();

		PriceModel priceModel = new PriceModel(unitPrice.format(locale));

		CommerceMoney unitPromoPrice = commerceProductPrice.getUnitPromoPrice();

		BigDecimal promoPrice = unitPromoPrice.getPrice();

		if ((promoPrice.compareTo(BigDecimal.ZERO) > 0) &&
			(promoPrice.compareTo(unitPrice.getPrice()) < 0)) {

			priceModel.setPromoPrice(unitPromoPrice.format(locale));
		}

		CommerceDiscountValue discountValue =
			commerceProductPrice.getDiscountValue();

		if (discountValue != null) {
			CommerceMoney discountAmount = discountValue.getDiscountAmount();

			priceModel.setDiscount(discountAmount.format(locale));
		}

		return priceModel;
	}

	/*private String _getAvaiability(CPCatalogEntry cpCatalogEntry) {

		CPDefinitionInventory cpDefinitionInventory =
			_cpDefinitionInventoryLocalService.
				fetchCPDefinitionInventoryByCPDefinitionId(
					cpCatalogEntry.getCPDefinitionId());

		CPDefinitionInventoryEngine cpDefinitionInventoryEngine =
			_cpDefinitionInventoryEngineRegistry.getCPDefinitionInventoryEngine(
				cpDefinitionInventory);

		List<CPSku> cpSkus = cpCatalogEntry.getCPSkus();

		for (CPSku cpSku : cpSkus) {
			int availableQuantity =
				cpDefinitionInventoryEngine.getStockQuantity(
					cpSku.getCPInstanceId());
		}

	}*/

	@Reference
	private CommerceProductPriceCalculation _commerceProductPriceCalculation;

	@Reference
	private CPDefinitionInventoryEngineRegistry
		_cpDefinitionInventoryEngineRegistry;

	@Reference
	private CPDefinitionInventoryLocalService
		_cpDefinitionInventoryLocalService;

	@Reference
	private CPInstanceLocalService _cpInstanceLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private SoyComponentRenderer _soyComponentRenderer;

}