import BasePage from 'shared/components/base-page';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import Table from 'shared/components/table';
import {compose, withError, withPaginationBar, withToolbar} from 'shared/hoc';
import {withEmpty} from 'cerebro-shared/hocs/utils';
import {withLoading} from 'shared/hoc/util';

const {
	pagination: {delta: defaultDelta, orderDescending}
} = FaroConstants;

const defaultHOC = WrappedComponent => props => <WrappedComponent {...props} />;

const withBaseResults = (withData, configs) => {
	const {
		defaultOrderByField,
		disableSearch = false,
		emptyDescription,
		emptyPrimary = true,
		emptyTitle,
		getColumns,
		legacyDropdownRangeKey = true,
		rowIdentifier,
		showRangeKeyDropdown = true,
		withQueryOptions = defaultHOC,
		withSelection = defaultHOC
	} = configs;

	const TableWithData = compose(
		withData(),
		withQueryOptions,
		WrappedComponent => props => (
			<WrappedComponent {...props} columns={getColumns(props)} />
		),
		withSelection,
		withToolbar({
			disableSearch,
			legacyDropdownRangeKey,
			showRangeKeyDropdown
		}),
		withPaginationBar({defaultDelta}),
		withLoading({alignCenter: true, page: false}),
		withError({page: false}),
		withEmpty({emptyDescription, emptyTitle, primary: emptyPrimary})
	)(Table);

	class BaseResults extends React.Component {
		static contextType = BasePage.Context;

		render() {
			const {
				context: {filters},
				props: {rangeKey, router, ...otherProps}
			} = this;

			const {
				query: {
					delta,
					orderBy = orderDescending,
					orderByField = defaultOrderByField,
					page,
					query
				}
			} = router;

			return (
				<TableWithData
					defaultSort={{
						field: orderByField,
						sortOrder: orderBy
					}}
					delta={delta}
					filters={filters}
					orderBy={orderBy}
					orderByField={orderByField}
					page={page}
					query={query}
					rangeKey={rangeKey}
					router={router}
					rowIdentifier={rowIdentifier}
					{...otherProps}
				/>
			);
		}
	}

	return BaseResults;
};

export default withBaseResults;
