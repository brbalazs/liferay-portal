import FaroConstants from 'shared/util/constants';
import React from 'react';
import Table from 'shared/components/table';
import {compose, withError} from 'shared/hoc';
import {withEmpty} from 'cerebro-shared/hocs/utils';

const {
	pagination: {orderDescending}
} = FaroConstants;

const withTableData = (withData, configs) => {
	const {
		defaultOrderByField,
		emptyDescription,
		emptyTitle,
		getColumns,
		rowIdentifier
	} = configs;

	const TableWithData = compose(
		withData(),
		WrappedComponent => props => (
			<WrappedComponent {...props} columns={getColumns(props)} />
		),
		withError({page: false}),
		withEmpty({emptyDescription, emptyTitle, spacer: true})
	)(Table);

	class TableData extends React.Component {
		render() {
			const {
				orderBy = orderDescending,
				orderByField = defaultOrderByField,
				...otherProps
			} = this.props;
			return (
				<TableWithData
					defaultSort={{
						field: orderByField,
						sortOrder: orderBy
					}}
					rowIdentifier={rowIdentifier}
					{...otherProps}
				/>
			);
		}
	}

	return TableData;
};

export default withTableData;
