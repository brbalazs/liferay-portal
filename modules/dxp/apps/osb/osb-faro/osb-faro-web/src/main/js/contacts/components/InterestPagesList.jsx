import * as API from 'shared/api';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import {
	buildOrderByFields,
	TITLE,
	UNIQUE_VISITS_COUNT,
	URL
} from 'shared/util/pagination';
import {PAGES} from 'shared/util/router';
import {pagesListColumns} from 'shared/util/table-columns';
import {Routes} from 'shared/util/router';

const {
	pagination: {orderAscending, orderDescending}
} = FaroConstants;

const PAGES_ORDER_BY_OPTIONS = [
	{
		label: Liferay.Language.get('page-title'),
		value: TITLE
	},
	{
		label: Liferay.Language.get('url'),
		value: URL
	}
];

function fetchPagesVisited({active, orderBy, orderByField, ...otherParams}) {
	return API.pagesVisited.search({
		...otherParams,
		active,
		orderByFields: buildOrderByFields(
			{field: orderByField, sortOrder: orderBy},
			PAGES
		)
	});
}

const ActivePagesList = ({
	groupId,
	orderBy = orderDescending,
	orderByField = UNIQUE_VISITS_COUNT,
	...otherProps
}) => (
	<SearchableEntityTable
		columns={[
			pagesListColumns.getTitleUrl({
				groupId,
				route: Routes.SITES_TOUCHPOINTS_OVERVIEW
			}),
			pagesListColumns.url,
			pagesListColumns.viewCount
		]}
		orderBy={orderBy}
		orderByField={orderByField}
		orderByOptions={[
			...PAGES_ORDER_BY_OPTIONS,
			{
				label: Liferay.Language.get('views'),
				value: UNIQUE_VISITS_COUNT
			}
		]}
		{...otherProps}
	/>
);

const InactivePagesList = ({
	groupId,
	orderBy = orderAscending,
	orderByField = URL,
	...otherProps
}) => (
	<SearchableEntityTable
		columns={[
			pagesListColumns.getTitleUrl({
				groupId,
				route: Routes.SITES_TOUCHPOINTS_OVERVIEW
			}),
			pagesListColumns.url,
			pagesListColumns.inactiveViewCount
		]}
		orderBy={orderBy}
		orderByField={orderByField}
		orderByOptions={PAGES_ORDER_BY_OPTIONS}
		{...otherProps}
	/>
);

const InterestPagesList = ({dataSourceParams, ...otherProps}) => {
	const {active} = dataSourceParams;

	const sharedProps = {
		dataSourceFn: fetchPagesVisited,
		dataSourceParams,
		entityLabel: Liferay.Language.get('pages'),
		rowIdentifier: 'url'
	};

	const PagesListComponent = active ? ActivePagesList : InactivePagesList;

	return <PagesListComponent {...otherProps} {...sharedProps} />;
};

export default InterestPagesList;
