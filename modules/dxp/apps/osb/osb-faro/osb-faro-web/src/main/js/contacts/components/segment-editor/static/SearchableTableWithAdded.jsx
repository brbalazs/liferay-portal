import autobind from 'autobind-decorator';
import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import Promise from 'metal-promise';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import StagedSubNav from './StagedSubnav';
import {defaultSearch, defaultSort} from 'shared/hoc/WithCrossPageSelect';
import {omit} from 'lodash';
import {OrderedMap} from 'immutable';
import {PropTypes} from 'prop-types';
import {sub} from 'shared/util/lang';
import {withStatefulPagination} from 'shared/hoc';

/**
 * This component is specifically for the static segment editor
 * because it does not fit the typical pattern of cross-page selection.
 *
 * addedItemsIOMap is not a reflection of selected items from the server list,
 * but is a list of individuals to be added to the segment
 * from a modal and it is controlled directly by SegmentEditStatic.
 */
export class SearchableTableWithAdded extends React.Component {
	static defaultProps = {
		addedItemsIOMap: new OrderedMap(),
		entityLabel: Liferay.Language.get('items'),
		showStaged: false
	};

	static propTypes = {
		addedItemsIOMap: PropTypes.instanceOf(OrderedMap),
		dataSourceFn: PropTypes.func,
		entityLabel: PropTypes.string,
		onSearch: PropTypes.func,
		onShowStagedToggle: PropTypes.func,
		onSort: PropTypes.func,
		onUndoChanges: PropTypes.func,
		showStaged: PropTypes.bool,
		stagedProps: PropTypes.object.isRequired,
		viewCurrentLinkText: PropTypes.string,
		viewStagedLinkText: PropTypes.string
	};

	constructor(props) {
		super(props);

		this._tableRef = React.createRef();
	}

	@autobind
	getCurrentItems() {
		const {
			addedItemsIOMap,
			stagedProps: {delta, page, query}
		} = this.props;

		const start = (page - 1) * delta;
		const end = start + delta;

		const itemsIMap = query
			? this.handleSort(this.handleSearch(addedItemsIOMap, query))
			: this.handleSort(addedItemsIOMap);

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
			props: {
				addedItemsIOMap,
				className,
				dataSourceFn,
				entityLabel,
				onShowStagedToggle,
				showStaged,
				stagedProps,
				toolbarProps,
				...otherProps
			}
		} = this;

		const dataFn = showStaged ? this.getCurrentItems : dataSourceFn;

		const passThruProps = showStaged
			? {
					...omitDefinedProps(
						otherProps,
						SearchableTableWithAdded.propTypes
					),
					...stagedProps
			  }
			: {
					paginationProps: {},
					toolbarProps,
					...omitDefinedProps(
						otherProps,
						SearchableTableWithAdded.propTypes
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
					dataSourceFn={dataFn}
					entityLabel={entityLabel}
					{...passThruProps}
					ref={this._tableRef}
					renderSubnav={() =>
						!!addedItemsIOMap.size && (
							<StagedSubNav
								onToggle={onShowStagedToggle}
								selectedCountMessage={sub(
									Liferay.Language.get('x-members-added'),
									[addedItemsIOMap.size]
								)}
								showStaged={showStaged}
								stagedMessage={sub(
									Liferay.Language.get(
										'showing-only-added-x'
									),
									[entityLabel]
								)}
								viewCurrentLinkText={Liferay.Language.get(
									'view-current-members'
								)}
								viewStagedLinkText={Liferay.Language.get(
									'view-added-members'
								)}
							/>
						)
					}
				/>
			</div>
		);
	}
}

export default withStatefulPagination(
	SearchableTableWithAdded,
	({defaultOrderByField}) =>
		defaultOrderByField ? {defaultOrderByField} : {},
	(props, {toolbarProps}) => ({
		stagedProps: omit(props, 'onSearchValueChange'),
		toolbarProps
	})
);
