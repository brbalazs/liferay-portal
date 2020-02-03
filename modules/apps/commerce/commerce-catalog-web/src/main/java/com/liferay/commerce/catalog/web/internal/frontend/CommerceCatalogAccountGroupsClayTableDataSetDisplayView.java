package com.liferay.commerce.catalog.web.internal.frontend;

import com.liferay.commerce.frontend.CommerceDataSetDataProvider;
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
	property = "commerce.data.provider.key=" + CommerceCatalogDataSetConstants.COMMERCE_DATA_SET_KEY_CATALOG_ACCOUNT_GROUPS,
	service = CommerceDataSetDataProvider.class
)
public class CommerceCatalogAccountGroupsClayTableDataSetDisplayView
	extends ClayTableDataSetDisplayView {


	@Override
	public ClayTableSchema getClayTableSchema() {
		ClayTableSchemaBuilder clayTableSchemaBuilder =
			_clayTableSchemaBuilderFactory.clayTableSchemaBuilder();

		clayTableSchemaBuilder.addField("name", "name");

		return clayTableSchemaBuilder.build();
	}

	@Reference
	private ClayTableSchemaBuilderFactory _clayTableSchemaBuilderFactory;
}
