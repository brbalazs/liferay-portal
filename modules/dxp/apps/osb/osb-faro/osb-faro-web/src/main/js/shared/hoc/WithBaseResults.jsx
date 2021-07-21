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
		defaultOrderBy,
		defaultOrderByField,
		disableSearch = false,
		emptyDescription,
		emptyPrimary = true,
		emptyTitle,
		getColumns,
		legacyDropdownRangeKey = true,
		rowIdentifier,
		showDropdownRangeKey = true,
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
			showDropdownRangeKey
		}),
		withPaginationBar({defaultDelta}),
		withLoading({alignCenter: true, page: false}),
		withError({page: false}),
		withEmpty({
			emptyDescription,
			emptyTitle,
			primary: emptyPrimary
		})
	)(Table);

	class BaseResults extends React.Component {
		static contextType = BasePage.Context;

		render() {
			const {
				context: {filters},
				props: {rangeSelectors, router, ...otherProps}
			} = this;

			const delta = router ? router.query.delta : otherProps.delta;
			const orderBy =
				(router ? router.query.orderBy : otherProps.orderBy) ||
				defaultOrderBy ||
				orderDescending;
			const orderByField =
				(router
					? router.query.orderByField
					: otherProps.orderByField) || defaultOrderByField;
			const page = router ? router.query.page : otherProps.page;
			const query = router ? router.query.query : otherProps.query;

			return (
				<div className='d-flex flex-column flex-grow-1 justify-content-between'>
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
						rangeSelectors={rangeSelectors}
						router={router}
						rowIdentifier={rowIdentifier}
						{...otherProps}
					/>
				</div>
			);
		}
	}

	return BaseResults;
};

export default withBaseResults;
