import autobind from 'autobind-decorator';
import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import Promise from 'metal-promise';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import {
	defaultSearch,
	defaultSort,
	ViewSelectedToggle
} from 'shared/hoc/WithCrossPageSelect';
import {omit} from 'lodash';
import {PropTypes} from 'prop-types';
import {SelectionContext} from 'shared/context/selection';
import {withStatefulPagination} from 'shared/hoc';

/**
 * @deprecated Use CrossPageSelect instead.
 */
export class SearchableTableWithStaged extends React.Component {
	static contextType = SelectionContext;

	static defaultProps = {
		entityLabel: Liferay.Language.get('items'),
		toolbarProps: {}
	};

	static propTypes = {
		dataSourceFn: PropTypes.func,
		entityLabel: PropTypes.string,
		onSearch: PropTypes.func,
		onSort: PropTypes.func,
		onUndoChanges: PropTypes.func,
		stagedProps: PropTypes.object.isRequired
	};

	state = {
		showStaged: false
	};

	constructor(props) {
		super(props);

		this._tableRef = React.createRef();
	}

	componentDidUpdate() {
		const {
			context: {selectedItems: selectedItemsIOMap},
			state: {showStaged}
		} = this;

		if (selectedItemsIOMap.isEmpty() && showStaged) {
			this.setState({showStaged: false});
		}
	}

	@autobind
	getCurrentItems() {
		const {
			context: {selectedItems: selectedItemsIOMap},
			props: {
				stagedProps: {delta, page, query}
			}
		} = this;

		const start = (page - 1) * delta;
		const end = start + delta;

		const itemsIMap = query
			? this.handleSort(this.handleSearch(selectedItemsIOMap, query))
			: this.handleSort(selectedItemsIOMap);

		return Promise.resolve({
			items: itemsIMap.slice(start, end).toArray(),
			total: itemsIMap.size
		});
	}

	@autobind
	handleSearch(items, query) {
		const {onSearch} = this.props;

		return onSearch
			? onSearch(items, query)
			: defaultSearch({items, query});
	}

	@autobind
	handleSort(items) {
		const {
			onSort,
			stagedProps: {orderBy, orderByField}
		} = this.props;

		return onSort
			? onSort(items, orderBy, orderByField)
			: defaultSort(items, orderBy, orderByField);
	}

	/**
	 * Public method for refreshing data
	 */
	reload() {
		this._tableRef.current.reload();
	}

	render() {
		const {
			context: {selectedItems: selectedItemsIOMap},
			props: {
				className,
				dataSourceFn,
				entityLabel,
				stagedProps,
				toolbarProps,
				...otherProps
			},
			state: {showStaged}
		} = this;

		const dataFn = showStaged ? this.getCurrentItems : dataSourceFn;

		const passThruProps = showStaged
			? {
					...omitDefinedProps(
						otherProps,
						SearchableTableWithStaged.propTypes
					),
					...stagedProps
			  }
			: {
					toolbarProps,
					...omitDefinedProps(
						otherProps,
						SearchableTableWithStaged.propTypes
					)
			  };

		return (
			<div
				className={getCN(
					'searchable-table-with-staged-root d-flex flex-column flex-grow-1',
					className
				)}
			>
				<SearchableEntityTable
					crossPageSelect
					dataSourceFn={dataFn}
					entityLabel={entityLabel}
					{...passThruProps}
					ref={this._tableRef}
					toolbarProps={{
						...passThruProps.toolbarProps,
						renderViewSelectedToggle: () => (
							<ViewSelectedToggle
								onClick={() =>
									this.setState({
										showStaged: !showStaged
									})
								}
								selectedItemsCount={selectedItemsIOMap.size}
								showSelected={showStaged}
							/>
						)
					}}
				/>
			</div>
		);
	}
}

export default withStatefulPagination(
	SearchableTableWithStaged,
	({defaultOrderByField}) =>
		defaultOrderByField ? {defaultOrderByField} : {},
	(statefulProps, props) => ({
		stagedProps: omit(statefulProps, 'onSearchValueChange'),
		...props
	})
);
