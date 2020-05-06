import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Checkbox from 'shared/components/Checkbox';
import faroConstants from 'shared/util/constants';
import FilterAndOrder from 'shared/components/FilterAndOrder';
import FilterTags from './FilterTags';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import Nav from 'shared/components/Nav';
import NavBar from 'shared/components/NavBar';
import React from 'react';
import SearchInput from 'shared/components/SearchInput';
import SubnavTbar from 'shared/components/SubnavTbar';
import {getDefaultSortOrder} from 'shared/util/pagination';
import {getPluralMessage} from 'shared/util/lang';
import {Map, Set} from 'immutable';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';
import {setUriFilterValues, setUriQueryValues} from 'shared/util/router';
import {withHistory} from 'shared/hoc';

const {cur, orderAscending, orderDescending} = faroConstants.pagination;

/**
 * Get the filter label from filterOptions.
 * @param {string} fieldName
 * @param {string} fieldValue
 * @param {Array.<Object>} filterByOptions
 * @returns {string} - The display label for the filter.
 */
function getFilterLabel(fieldName, fieldValue, filterByOptions) {
	const filterOption = filterByOptions.find(
		option => option.key === fieldName
	);

	const filterValueObject = filterOption.values.find(
		valueItem => valueItem.value === fieldValue
	);

	return filterValueObject.label;
}

@withHistory
export default class Toolbar extends React.Component {
	static defaultProps = {
		alwaysShowSearch: false,
		autoFocus: false,
		disabled: false,
		disableSearch: false,
		filterBy: new Map(),
		filterByOptions: [],
		flatFilter: false,
		onSearchValueChange: noop,
		orderByOptions: [],
		placeholder: Liferay.Language.get('search'),
		query: '',
		searchValue: '',
		selectEntirePage: false,
		selectEntirePageIndeterminate: false,
		showCheckbox: true,
		showSearch: true,
		total: 0
	};

	static propTypes = {
		alwaysShowSearch: PropTypes.bool,
		autoFocus: PropTypes.bool,
		disabled: PropTypes.bool,
		disableSearch: PropTypes.bool,
		filterBy: PropTypes.instanceOf(Map),
		filterByOptions: PropTypes.array,
		flatFilter: PropTypes.bool,
		history: PropTypes.object.isRequired,
		loading: PropTypes.bool,
		maxLength: PropTypes.number,
		onFilterByChange: PropTypes.func,
		onOrderByFieldChange: PropTypes.func,
		onOrderClick: PropTypes.func,
		onSearchSubmit: PropTypes.func,
		onSearchValueChange: PropTypes.func,
		onSelectAll: PropTypes.func,
		onSelectEntirePage: PropTypes.func,
		order: PropTypes.oneOf([orderAscending, orderDescending]),
		orderBy: PropTypes.string,
		orderByOptions: PropTypes.array,
		placeholder: PropTypes.string,
		query: PropTypes.string,
		renderViewSelectedToggle: PropTypes.func,
		searchValue: PropTypes.string,
		selectEntirePage: PropTypes.bool,
		selectEntirePageIndeterminate: PropTypes.bool,
		showCheckbox: PropTypes.bool,
		showSearch: PropTypes.bool,
		total: PropTypes.number
	};

	getActiveFilterTags() {
		const {filterBy, filterByOptions} = this.props;

		return filterBy
			.map((valuesISet, field) =>
				valuesISet.filter(Boolean).map(fieldValue => ({
					field,
					label: getFilterLabel(field, fieldValue, filterByOptions),
					value: fieldValue
				}))
			)
			.flatten()
			.toArray();
	}

	@autobind
	handleCheckboxChange(event) {
		this.props.onSelectEntirePage(event.currentTarget.checked);
	}

	@autobind
	handleClearAllFilters() {
		const {
			filterBy,
			history,
			onFilterByChange,
			onSearchSubmit
		} = this.props;

		const emptyFilterBy = filterBy.map(() => new Set([]));

		this.props.onSearchValueChange('');

		if (onSearchSubmit || onFilterByChange) {
			onSearchSubmit && onSearchSubmit('');

			onFilterByChange && onFilterByChange(emptyFilterBy);
		} else {
			history.push(
				setUriFilterValues(
					emptyFilterBy,
					setUriQueryValues({page: cur, query: ''})
				)
			);
		}
	}

	@autobind
	handleFilterByChange(value) {
		const {history, onFilterByChange} = this.props;

		if (onFilterByChange) {
			onFilterByChange(value);
		} else {
			history.push(
				setUriFilterValues(value, setUriQueryValues({page: cur}))
			);
		}
	}

	@autobind
	handleFilterRemove(field, value) {
		const {filterBy, history, onFilterByChange} = this.props;

		if (onFilterByChange) {
			onFilterByChange(
				filterBy.update(field, (values = new Set()) =>
					values.has(value) ? values.delete(value) : values
				)
			);
		} else {
			history.push(
				setUriQueryValues(
					{
						[field]: filterBy
							.get(field)
							.delete(value)
							.toArray(),
						page: cur
					},
					window.location.href
				)
			);
		}
	}

	@autobind
	handleOrderByFieldChange(value) {
		const {history, onOrderByFieldChange} = this.props;

		if (onOrderByFieldChange) {
			onOrderByFieldChange(value);
		} else {
			history.push(
				setUriQueryValues(
					{
						orderBy: getDefaultSortOrder(value),
						orderByField: value,
						page: cur
					},
					window.location.href
				)
			);
		}
	}

	@autobind
	handleSearchSubmit(query) {
		const {history, onSearchSubmit} = this.props;

		onSearchSubmit
			? onSearchSubmit(query)
			: history.push(
					setUriQueryValues({
						page: cur,
						query
					})
			  );
	}

	renderFilterAndOrder() {
		const {
			disabled,
			filterBy,
			filterByOptions,
			flatFilter,
			itemsSelected,
			onOrderClick,
			onSelectAll,
			order,
			orderBy,
			orderByOptions
		} = this.props;

		const ascending = order === orderAscending;

		const uri = setUriQueryValues({
			orderBy: ascending ? orderDescending : orderAscending,
			page: cur
		});

		if (itemsSelected) {
			return (
				onSelectAll && (
					<Nav.Item key='SELECT_ALL'>
						<Button
							className='nav-btn'
							display='link'
							onClick={onSelectAll}
						>
							{Liferay.Language.get('select-all')}
						</Button>
					</Nav.Item>
				)
			);
		} else if (filterByOptions.length || orderByOptions.length) {
			return (
				<>
					<FilterAndOrder
						disabled={disabled}
						filterBy={filterBy}
						filterByOptions={filterByOptions}
						flat={flatFilter}
						onFilterByChange={this.handleFilterByChange}
						onOrderByChange={this.handleOrderByFieldChange}
						orderBy={orderBy}
						orderByOptions={orderByOptions}
					/>

					<Nav.Item>
						<Button
							borderless
							className='nav-link nav-link-monospaced'
							disabled={disabled}
							display='unstyled'
							href={onOrderClick ? '' : uri.toString()}
							onClick={onOrderClick}
						>
							<Icon
								symbol={
									ascending
										? 'order-arrow-ascending'
										: 'order-arrow-descending'
								}
							/>
						</Button>
					</Nav.Item>
				</>
			);
		}
	}

	render() {
		const {
			alwaysShowSearch,
			autoFocus,
			children,
			className,
			disabled,
			disableSearch,
			filterBy,
			loading,
			maxLength,
			onSearchValueChange,
			placeholder,
			query,
			renderViewSelectedToggle,
			searchValue,
			selectEntirePage,
			selectEntirePageIndeterminate,
			showCheckbox,
			showSearch,
			total
		} = this.props;

		const itemsSelected = selectEntirePage || selectEntirePageIndeterminate;

		const activeFilters = filterBy.some(values => values.some(Boolean));

		const classes = getCN({
			disabled,
			'items-selected': itemsSelected
		});

		return (
			<div className={getCN('toolbar-root', className)}>
				<NavBar
					className={classes}
					display={itemsSelected ? 'primary' : 'light'}
					expand
					managementBar
				>
					<Nav className='front-nav'>
						{showCheckbox && (
							<Nav.Item>
								<Checkbox
									checked={selectEntirePage}
									data-testid='select-all-checkbox'
									disabled={disabled || loading}
									indeterminate={
										selectEntirePageIndeterminate
									}
									onChange={this.handleCheckboxChange}
								/>
							</Nav.Item>
						)}

						{this.renderFilterAndOrder()}
					</Nav>

					{(!itemsSelected || alwaysShowSearch) && showSearch && (
						<div className='navbar-form navbar-form-autofit'>
							<SearchInput
								autoFocus={autoFocus}
								className={getCN('search', {
									disabled: disabled || disableSearch
								})}
								disabled={disabled || disableSearch}
								maxLength={maxLength}
								onChange={onSearchValueChange}
								onSubmit={this.handleSearchSubmit}
								placeholder={placeholder}
								value={searchValue}
							/>
						</div>
					)}

					{children}
				</NavBar>

				{(query ||
					activeFilters ||
					(itemsSelected && renderViewSelectedToggle)) && (
					<SubnavTbar>
						{renderViewSelectedToggle && itemsSelected && (
							<SubnavTbar.Item className='view-selected-link-container'>
								{renderViewSelectedToggle()}
							</SubnavTbar.Item>
						)}

						{(query || activeFilters) && (
							<SubnavTbar.Item expand={!activeFilters}>
								{query
									? getPluralMessage(
											Liferay.Language.get(
												'x-result-for-x'
											),
											Liferay.Language.get(
												'x-results-for-x'
											),
											total,
											false,
											[
												total.toLocaleString(),
												<b key='QUERY_TERM'>{query}</b>
											]
									  )
									: getPluralMessage(
											Liferay.Language.get(
												'x-result-for'
											),
											Liferay.Language.get(
												'x-results-for'
											),
											total
									  )}
							</SubnavTbar.Item>
						)}

						<FilterTags
							onRemove={this.handleFilterRemove}
							tags={this.getActiveFilterTags()}
						/>

						{(query || activeFilters) && (
							<SubnavTbar.Item>
								<Button
									display='link'
									key='FILTER_CLEAR'
									onClick={this.handleClearAllFilters}
									size='sm'
								>
									{Liferay.Language.get('clear')}
								</Button>
							</SubnavTbar.Item>
						)}
					</SubnavTbar>
				)}
			</div>
		);
	}
}
