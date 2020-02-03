package com.liferay.commerce.catalog.web.internal.frontend;

import com.liferay.commerce.frontend.clay.data.set.ClayDataSetDisplayView;
import com.liferay.commerce.frontend.clay.table.ClayTableDataSetDisplayView;
import com.liferay.commerce.frontend.clay.table.ClayTableSchema;
import com.liferay.commerce.frontend.clay.table.ClayTableSchemaBuilder;
import com.liferay.commerce.frontend.clay.table.ClayTableSchemaBuilderFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Gianmarco Brunialti Masera
 */
@Component(
	immediate = true,
	property = "commerce.data.set.display.name=" + CommerceCatalogDataSetConstants.COMMERCE_DATA_SET_KEY_CATALOGS,
	service = ClayDataSetDisplayView.class
)
public class CommerceCatalogClayTableDataSetDisplayView
	extends ClayTableDataSetDisplayView {

	@Override
	public ClayTableSchema getClayTableSchema() {
		ClayTableSchemaBuilder clayTableSchemaBuilder =
			_clayTableSchemaBuilderFactory.clayTableSchemaBuilder();

		clayTableSchemaBuilder.addField("catalogName", "name");

		clayTableSchemaBuilder.addField(
			"defaultLanguageId", "default-language");

		clayTableSchemaBuilder.addField(
			"currencyCode", "currency");

		return clayTableSchemaBuilder.build();
	}

	@Reference
	private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;
}
