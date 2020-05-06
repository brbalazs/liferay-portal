import autobind from 'autobind-decorator';
import BaseResults from 'shared/components/BaseResults';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import Table from 'shared/components/table';
import {FAMILY_NAME, GIVEN_NAME} from 'shared/util/pagination';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';
import {withStatefulPagination} from 'shared/hoc';
const {orderDefault} = FaroConstants.pagination;

class SearchableEntityTable extends React.Component {
	static defaultProps = {
		bordered: false,
		checkDisabled: noop,
		internalSort: false,
		nowrap: true,
		overrideLoading: false,
		showCheckbox: false
	};

	static propTypes = {
		bordered: PropTypes.bool,
		checkDisabled: PropTypes.func,
		columns: PropTypes.array,
		defaultSort: PropTypes.shape({
			field: PropTypes.string,
			sortOrder: PropTypes.string
		}),
		entityType: PropTypes.string,
		internalSort: PropTypes.bool,
		nowrap: PropTypes.bool,
		onOrderByFieldsChange: PropTypes.func,
		overrideLoading: PropTypes.bool,
		renderInlineRowActions: PropTypes.func,
		renderRowActions: PropTypes.func,
		rowIdentifier: PropTypes.string,
		showCheckbox: PropTypes.bool
	};

	constructor(props) {
		super(props);

		this._resultsRef = React.createRef();
	}

	/**
	 * Public method for refreshing data
	 */
	reload() {
		this._resultsRef.current.reload();
	}

	@autobind
	renderTable({
		className,
		items,
		loading,
		onSelectItemsChange,
		selectedItemsIOMap
	}) {
		const {
			bordered,
			checkDisabled,
			columns,
			defaultSort,
			entityType,
			internalSort,
			nowrap,
			onOrderByFieldsChange,
			orderBy,
			orderByField,
			overrideLoading,
			renderInlineRowActions,
			renderRowActions,
			rowBordered,
			rowIdentifier,
			showCheckbox
		} = this.props;

		return (
			<Table
				checkDisabled={checkDisabled}
				className={className}
				columns={columns}
				defaultSort={
					defaultSort || {
						field: orderByField,
						sortOrder: orderBy
					}
				}
				entityType={entityType}
				headingNowrap
				internalSort={internalSort}
				items={items}
				list={bordered}
				loading={loading || overrideLoading}
				nowrap={nowrap}
				onSelectItemsChange={onSelectItemsChange}
				onSortChange={onOrderByFieldsChange}
				renderInlineRowActions={renderInlineRowActions}
				renderRowActions={renderRowActions}
				rowBordered={rowBordered}
				rowIdentifier={rowIdentifier}
				selectedItemsIOMap={selectedItemsIOMap}
				showCheckbox={showCheckbox}
			/>
		);
	}

	render() {
		const {
			bordered,
			checkDisabled,
			className,
			showCheckbox,
			...otherProps
		} = this.props;

		const classes = getCN('searchable-table-root', className, {
			'searchable-table-borderless': !bordered
		});
		return (
			<BaseResults
				{...omitDefinedProps(
					otherProps,
					SearchableEntityTable.propTypes
				)}
				checkDisabled={checkDisabled}
				className={classes}
				ref={this._resultsRef}
				resultsRenderer={this.renderTable}
				showCheckbox={showCheckbox}
			/>
		);
	}
}

SearchableEntityTable.StatefulPagination = withStatefulPagination(
	SearchableEntityTable
);

SearchableEntityTable.IndividualStatefulPagination = withStatefulPagination(
	SearchableEntityTable,
	{
		defaultDelta: 10,
		defaultOrderByFields: [
			{
				fieldName: GIVEN_NAME,
				orderBy: orderDefault
			},
			{
				fieldName: FAMILY_NAME,
				orderBy: orderDefault
			}
		]
	}
);

export default SearchableEntityTable;
