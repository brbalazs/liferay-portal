package com.liferay.commerce.catalog.web.internal.servlet.taglib.ui;

import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationCategory;
import com.liferay.frontend.taglib.servlet.taglib.ScreenNavigationEntry;
import com.liferay.frontend.taglib.servlet.taglib.util.JSPRenderer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@Component(
	property = {
		"screen.navigation.category.order:Integer=40",
		"screen.navigation.entry.order:Integer=10"
	},
	service = {ScreenNavigationCategory.class, ScreenNavigationEntry.class}
)
public class CommerceCatalogVersionsScreenNavigationEntry
	implements ScreenNavigationCategory, ScreenNavigationEntry<CommerceCatalog> {
	@Override
	public String getCategoryKey() {
		return CommerceCatalogScreenNavigationConstants.
			CATEGORY_KEY_COMMERCE_CATALOG_VERSIONS;
	}

	@Override
	public String getEntryKey() {
		return getCategoryKey();
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.get(resourceBundle, getCategoryKey());
	}

	@Override
	public String getScreenNavigationKey() {
		return CommerceCatalogScreenNavigationConstants.
			SCREEN_NAVIGATION_KEY_COMMERCE_CATALOG_GENERAL;
	}

	@Override
	public void render(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse)
		throws IOException {

		_jspRenderer.renderJSP(httpServletRequest, httpServletResponse,
			"/details/versions.jsp");
	}

	@Reference
	private JSPRenderer _jspRenderer;
}
