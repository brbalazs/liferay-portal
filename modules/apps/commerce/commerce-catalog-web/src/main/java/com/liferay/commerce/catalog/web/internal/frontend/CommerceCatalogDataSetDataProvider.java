package com.liferay.commerce.catalog.web.internal.frontend;

import com.liferay.commerce.catalog.web.internal.model.Catalog;
import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
import com.liferay.commerce.frontend.Filter;
import com.liferay.commerce.frontend.Pagination;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.commerce.product.service.CommerceCatalogService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gianmarco Brunialti Masera
 */
@Component(
	immediate = true,
	property = "commerce.data.provider.key=" + CommerceCatalogDataSetConstants.COMMERCE_DATA_SET_KEY_CATALOGS,
	service = CommerceDataSetDataProvider.class
)
public class CommerceCatalogDataSetDataProvider
	implements CommerceDataSetDataProvider<Catalog> {
	@Override
	public int countItems(
		HttpServletRequest httpServletRequest, Filter filter)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay) httpServletRequest
			.getAttribute(WebKeys.THEME_DISPLAY);

		return _commerceCatalogService.searchCommerceCatalogsCount(
			themeDisplay.getCompanyId(), filter.getKeywords());
	}

	@Override
	public List<Catalog> getItems(
		HttpServletRequest httpServletRequest, Filter filter,
		Pagination pagination, Sort sort) throws PortalException {

		List<Catalog> catalogs = new ArrayList<>();

		ThemeDisplay themeDisplay = (ThemeDisplay) httpServletRequest
			.getAttribute(WebKeys.THEME_DISPLAY);

		long companyId = themeDisplay.getCompanyId();

		List<CommerceCatalog> commerceCatalogs =
			_commerceCatalogService.getCommerceCatalogs(
				companyId, pagination.getStartPosition(),
				pagination.getEndPosition());

		for (CommerceCatalog catalog : commerceCatalogs) {
			catalogs.add(
				new Catalog(
					catalog.getCommerceCatalogId(), catalog.getName(),
					catalog.getCatalogDefaultLanguageId(),
					catalog.getCommerceCurrencyCode())
			);
		}

		return catalogs;
	}

	@Reference
	private CommerceCatalogService _commerceCatalogService;
}
