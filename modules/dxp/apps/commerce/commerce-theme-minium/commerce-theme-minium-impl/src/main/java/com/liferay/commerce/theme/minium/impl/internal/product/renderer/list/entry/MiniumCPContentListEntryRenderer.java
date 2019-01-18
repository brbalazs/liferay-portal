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

import com.liferay.commerce.frontend.template.soy.renderer.ComponentDescriptor;
import com.liferay.commerce.frontend.template.soy.renderer.SoyComponentRenderer;
import com.liferay.commerce.product.catalog.CPCatalogEntry;
import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.commerce.product.content.constants.CPContentWebKeys;
import com.liferay.commerce.product.content.render.list.entry.CPContentListEntryRenderer;
import com.liferay.commerce.product.content.util.CPContentHelper;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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

		CPContentHelper cpContentHelper =
			(CPContentHelper)httpServletRequest.getAttribute(
				CPContentWebKeys.CP_CONTENT_HELPER);

		CPCatalogEntry cpCatalogEntry = cpContentHelper.getCPCatalogEntry(
			httpServletRequest);

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		Map<String, Object> context = new HashMap<>();

		context.put("availability", "inStock");
		context.put("categories", null);
		context.put("description", null);
		context.put(
			"detailsLink",
			cpContentHelper.getFriendlyURL(cpCatalogEntry, themeDisplay));
		context.put("minQuantity", null);
		context.put("name", cpCatalogEntry.getName());
		context.put("pictureUrl", cpCatalogEntry.getDefaultImageFileUrl());
		context.put("sku", "AR351184");

		context.put(
			"spritemap",
			themeDisplay.getPathThemeImages() + "/commerce-icons.svg");

		String module =
			"commerce-theme-minium-impl@1.0.0/product_card/ProductCard.es";

		Set<String> dependencies = new HashSet<>();

		// System.out.println(_npmResolver.resolveModuleName(
		// 	"commerce-frontend-taglib/add_to_cart/AddToCartButton.es"));

		dependencies.add(
			"commerce-frontend-taglib@1.0.0/add_to_cart/AddToCartButton.es");

		ComponentDescriptor componentDescriptor = new ComponentDescriptor(
			"ProductCard.render", module, null, dependencies);

		_soyComponentRenderer.renderSoyComponent(
			httpServletRequest, httpServletResponse, componentDescriptor,
			context);

		ComponentDescriptor testDescriptor = new ComponentDescriptor(
			"ProductsCompare.render",
			"commerce-theme-minium-impl@1.0.0/products_compare" +
				"/ProductsCompare.es",
			null, null);

		// _soyComponentRenderer.renderSoyComponent(
		// 	httpServletRequest, httpServletResponse, componentDescriptor,
		// 	context);

		Map<String, Object> testContext = new HashMap<>();

		testContext.put(
			"spritemap",
			themeDisplay.getPathThemeImages() + "/commerce-icons.svg");

		_soyComponentRenderer.renderSoyComponent(
			httpServletRequest, httpServletResponse, testDescriptor,
			testContext);
	}

	@Reference
	private SoyComponentRenderer _soyComponentRenderer;

}