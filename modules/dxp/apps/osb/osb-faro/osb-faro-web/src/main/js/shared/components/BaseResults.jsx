import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import debounce from 'shared/util/debounce-decorator';
import getCN from 'classnames';
import NoResultsDisplay, {
	getFormattedTitle
} from 'shared/components/NoResultsDisplay';
import PaginationBar from 'shared/components/PaginationBar';
import React from 'react';
import Toolbar from 'shared/components/toolbar';
import {ACTION_TYPES, SelectionContext} from 'shared/context/selection';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {hasChanges} from 'shared/util/react';
import {paginationConfig, paginationDefaults} from 'shared/util/pagination';
import {PropTypes} from 'prop-types';

@hasRequest
export default class BaseResults extends React.Component {
	static contextType = SelectionContext;

	static defaultProps = {
		...paginationDefaults,
		checkDisabled: () => false,
		crossPageSelect: false,
		filterByOptions: [],
		noResultsTitle: Liferay.Language.get('there-are-no-x-found'),
		orderByOptions: [],
		paginationProps: {},
		placeholder: Liferay.Language.get('search'),
		query: '',
		showCheckbox: false,
		showPagination: true,
		toolbarProps: {}
	};

	static propTypes = {
		...paginationConfig,
		checkDisabled: PropTypes.func,
		crossPageSelect: PropTypes.bool,
		dataSourceFn: PropTypes.func.isRequired,
		dataSourceParams: PropTypes.object,
		entityLabel: PropTypes.string,
		filterByOptions: PropTypes.array,
		maxLength: PropTypes.number,
		navRenderer: PropTypes.func,
		noResultsDescription: PropTypes.string,
		noResultsIcon: PropTypes.string,
		noResultsRenderer: PropTypes.func,
		noResultsTitle: PropTypes.string,
		onSearchValueChange: PropTypes.func,
		onSelectItemsChange: PropTypes.func,
		orderByFields: PropTypes.array,
		orderByOptions: PropTypes.array,
		paginationProps: PropTypes.object,
		placeholder: PropTypes.string,
		renderSubNav: PropTypes.func,
		resultsRenderer: PropTypes.func.isRequired,
		showCheckbox: PropTypes.bool,
		showPagination: PropTypes.bool,
		toolbarProps: PropTypes.object
	};

	state = {
		disableSearch: false,
		error: false,
		items: [],
		loading: true,
		searchValue: '',
		total: 0
	};

	constructor(props) {
		super(props);

		const {maxLength, query} = this.props;

		this.state = {
			...this.state,
			searchValue: this.getSearchValue(maxLength, query)
		};
	}

	componentDidMount() {
		this.handleFetchResults();
	}

	componentDidUpdate(nextProps) {
		if (
			hasChanges(
				this.props,
				nextProps,
				'dataSourceFn',
				'dataSourceParams',
				'delta',
				'filterBy',
				'orderBy',
				'orderByField',
				'orderByFields',
				'page',
				'query'
			)
		) {
			this.handleFetchResults();
		}
	}

	componentWillUnmount() {
		this.fetchResults.cancel();
	}

	allChecked() {
		const {
			context: {selectedItems: selectedItemsIOMap},
			props: {checkDisabled},
			state: {items}
		} = this;

		return (
			!selectedItemsIOMap.isEmpty() &&
			items.every(
				item => selectedItemsIOMap.has(item.id) || checkDisabled(item)
			)
		);
	}

	@autobind
	clearChecked() {
		const {
			context: {selectionDispatch},
			state: {items}
		} = this;

		selectionDispatch({
			payload: {items},
			type: ACTION_TYPES.remove
		});
	}

	@debounce(250)
	@autoCancel
	fetchResults() {
		const {
			context: {selectionDispatch},
			props: {
				crossPageSelect,
				dataSourceFn,
				dataSourceParams,
				delta,
				filterBy,
				orderBy,
				orderByField,
				orderByFields,
				page,
				showCheckbox
			},
			state: {searchValue: query}
		} = this;

		return dataSourceFn({
			...dataSourceParams,
			delta,
			filterBy,
			orderBy,
			orderByField,
			orderByFields,
			page,
			query
		})
			.then(({disableSearch, items, total = 0}) => {
				this.setState({
					disableSearch,
					items,
					loading: false,
					total
				});

				!crossPageSelect &&
					showCheckbox &&
					selectionDispatch({type: ACTION_TYPES.clearAll});
			})
			.catch(err => {
				if (!err.IS_CANCELLATION_ERROR) {
					this.setState({
						error: true,
						loading: false
					});
				}
			});
	}

	getSearchValue(maxLength, query) {
		let searchValue = query;

		if (maxLength && maxLength < searchValue.length) {
			searchValue = searchValue.slice(0, maxLength);
		}

		return searchValue;
	}

	@autobind
	handleCheckAll(checked) {
		const {
			context: {selectionDispatch},
			props: {checkDisabled},
			state: {items}
		} = this;

		selectionDispatch({
			payload: {items: items.filter(item => !checkDisabled(item))},
			type: checked ? ACTION_TYPES.add : ACTION_TYPES.remove
		});
	}

	@autobind
	handleFetchResults() {
		this.setState({
			error: false,
			loading: true
		});

		this.fetchResults();
	}

	@autobind
	handleItemsChange(item) {
		const {selectionDispatch} = this.context;

		selectionDispatch({payload: {item}, type: ACTION_TYPES.toggle});
	}

	@autobind
	handleSearchValueChange(value) {
		const {onSearchValueChange} = this.props;

		if (onSearchValueChange) {
			onSearchValueChange(value);
		}

		this.setState({
			searchValue: value
		});
	}

	/**
	 * Public method for refreshing data
	 */
	reload() {
		this.handleFetchResults();
	}

	renderContent() {
		const {
			context: {selectedItems: selectedItemsIOMap},
			props: {
				entityLabel,
				filterBy,
				noResultsDescription,
				noResultsIcon,
				noResultsRenderer,
				noResultsTitle,
				query,
				resultsRenderer,
				showCheckbox
			},
			state: {error, items, loading, total}
		} = this;

		const activeFilters = filterBy.some(values => values.some(Boolean));

		if (error) {
			return (
				<div className='error-info flex-grow-1'>
					<div>
						{Liferay.Language.get('an-unexpected-error-occurred')}
					</div>

					<Button onClick={this.handleFetchResults}>
						{Liferay.Language.get('reload')}
					</Button>
				</div>
			);
		} else if (!loading && !items.length && (!!total || !!query)) {
			return (
				<NoResultsDisplay
					description={noResultsDescription}
					icon={noResultsIcon ? {symbol: noResultsIcon} : undefined}
					spacer
					title={getFormattedTitle(entityLabel, noResultsTitle)}
				/>
			);
		} else if (!loading && !total) {
			return noResultsRenderer ? (
				noResultsRenderer(query, activeFilters)
			) : (
				<NoResultsDisplay
					description={noResultsDescription}
					icon={noResultsIcon ? {symbol: noResultsIcon} : undefined}
					spacer
					title={getFormattedTitle(entityLabel, noResultsTitle)}
				/>
			);
		} else {
			return resultsRenderer({
				items,
				loading,
				onSelectItemsChange: showCheckbox
					? this.handleItemsChange
					: null,
				selectedItemsIOMap,
				total
			});
		}
	}

	render() {
		const {
			context: {selectedItems: selectedItemsIOMap},
			props: {
				className,
				crossPageSelect,
				delta,
				filterBy,
				filterByOptions,
				maxLength,
				navRenderer,
				orderBy,
				orderByField,
				orderByOptions,
				page,
				paginationProps,
				placeholder,
				query,
				renderSubnav,
				showCheckbox,
				showPagination,
				toolbarProps
			},
			state: {disableSearch, error, items, loading, searchValue, total}
		} = this;

		const allChecked = this.allChecked();

		return (
			<div
				className={getCN(
					'base-results-root d-flex flex-column flex-grow-1',
					className
				)}
			>
				<Toolbar
					alwaysShowSearch={crossPageSelect}
					disabled={error}
					disableSearch={disableSearch}
					filterBy={filterBy}
					filterByOptions={filterByOptions}
					loading={loading}
					maxLength={maxLength}
					onSearchValueChange={this.handleSearchValueChange}
					onSelectEntirePage={this.handleCheckAll}
					order={orderBy}
					orderBy={orderByField}
					orderByOptions={orderByOptions}
					placeholder={placeholder}
					query={query}
					searchValue={searchValue}
					selectEntirePage={allChecked && !error}
					selectEntirePageIndeterminate={
						!allChecked && !selectedItemsIOMap.isEmpty()
					}
					showCheckbox={showCheckbox}
					total={total}
					{...toolbarProps}
				>
					{navRenderer && navRenderer(selectedItemsIOMap, items)}
				</Toolbar>

				{renderSubnav &&
					!error &&
					renderSubnav({handleClearChecked: this.clearChecked})}

				{this.renderContent()}

				{showPagination && !!total && !!items.length && (
					<PaginationBar
						{...paginationProps}
						href={window.location.href}
						key='PAGINATION_BAR'
						page={parseInt(page)}
						selectedDelta={parseInt(delta)}
						totalItems={total}
					/>
				)}
			</div>
		);
	}
}
