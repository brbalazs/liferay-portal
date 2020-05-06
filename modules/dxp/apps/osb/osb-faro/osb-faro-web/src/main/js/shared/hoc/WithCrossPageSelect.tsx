import BaseResults from 'shared/components/BaseResults';
import ClayButton from '@clayui/button';
import Constants from 'shared/util/constants';
import React, {useEffect, useState} from 'react';
import {ACTION_TYPES, useSelectionContext} from 'shared/context/selection';
import {FilterByType, IPagination, RouterType} from 'shared/types';
import {get, omit, pickBy} from 'lodash';
import {getDisplayName} from 'shared/util/react';
import {getSafeDisplayValue} from 'shared/util/util';
import {OrderedMap} from 'immutable';
import {sub} from 'shared/util/lang';
import {withBaseResults, withStatefulPagination} from 'shared/hoc';

const {orderAscending} = Constants.pagination;

type SearchArgs = {
	filterBy?: FilterByType;
	items: OrderedMap<any, any>;
	query: string;
};

export type SearchFnType = ({items, query}: SearchArgs) => OrderedMap<any, any>;

/**
 * Function for local search on items.
 */
export const defaultSearch: SearchFnType = ({items, query}) =>
	items.filter(
		item =>
			Object.values(get(item, 'properties', {})).some((value: any) =>
				String(getSafeDisplayValue(value, ''))
					.toLowerCase()
					.match(query.toLowerCase())
			) ||
			(item.name || item.emailAddress || '')
				.toLowerCase()
				.match(query.toLowerCase())
	) as OrderedMap<any, any>;

/**
 * Function for local sort on items.
 */
export const defaultSort = (
	items: OrderedMap<any, any>,
	orderBy: string,
	orderByField: string
): OrderedMap<any, any> => {
	const sorted = items.sortBy(item => {
		if (item[orderByField]) {
			return item[orderByField];
		} else if (get(item, ['properties', orderByField])) {
			return item.properties[orderByField];
		} else {
			return item;
		}
	});

	return orderBy === orderAscending
		? (sorted as OrderedMap<any, any>)
		: (sorted.reverse() as OrderedMap<any, any>);
};

export const fetchLocalData = ({
	delta,
	items,
	filterBy,
	orderBy,
	orderByField,
	page,
	query,
	searchSelectedFn = defaultSearch
}) => {
	const start = (page - 1) * delta;

	const end = start + delta;

	const result =
		query || filterBy
			? defaultSort(
					searchSelectedFn({filterBy, items, query}),
					orderBy,
					orderByField
			  )
			: defaultSort(items, orderBy, orderByField);

	return {
		empty: !result.size,
		items: result.slice(start, end).toArray(),
		total: result.size
	};
};

export const withLocalData = () => WrappedComponent => props => {
	const {
		filterBy,
		router: {query},
		searchSelectedFn
	} = props;

	const {selectedItems} = useSelectionContext();

	return (
		<WrappedComponent
			{...props}
			{...fetchLocalData({
				filterBy,
				items: selectedItems,
				searchSelectedFn,
				...query
			})}
		/>
	);
};

interface IwithSelectionProps {
	checkDisabled?: (item?: object) => boolean;
	items: {id: string}[];
	toolbarProps?: object;
	[key: string]: any;
}

/**
 * HOC for mapping information about items
 * and SelectionContext into props for Toolbar and Table
 * @param WrappedComponent
 * @returns {Function}
 */
export const withSelection: (
	WrappedComponent
) => React.FC<IwithSelectionProps> = WrappedComponent => {
	const WithSelection: React.FC<IwithSelectionProps> = ({
		checkDisabled = () => false,
		items = [],
		toolbarProps = {},
		...otherProps
	}) => {
		const {selectedItems, selectionDispatch} = useSelectionContext();

		const allChecked =
			!selectedItems.isEmpty() &&
			items.every(
				item => selectedItems.has(item.id) || checkDisabled(item)
			);

		const selectionProps = {
			onSelectItemsChange: item =>
				selectionDispatch({payload: {item}, type: ACTION_TYPES.toggle}),
			selectedItemsIOMap: selectedItems,
			showCheckbox: true,
			toolbarProps: {
				...toolbarProps,
				alwaysShowSearch: true,
				onSelectEntirePage: checked => {
					selectionDispatch({
						payload: {
							items: items.filter(item => !checkDisabled(item))
						},
						type: checked ? ACTION_TYPES.add : ACTION_TYPES.remove
					});
				},
				selectEntirePage: allChecked,
				selectEntirePageIndeterminate:
					!allChecked && !selectedItems.isEmpty(),
				showCheckbox: true
			}
		};

		return (
			<WrappedComponent
				{...otherProps}
				{...selectionProps}
				checkDisabled={checkDisabled}
				items={items}
			/>
		);
	};

	WithSelection.displayName = `WithSelection(${getDisplayName(
		WrappedComponent
	)})`;

	return WithSelection;
};

interface ICrossPageSelectProps {
	filterBy?: FilterByType;
	router: RouterType;
	searchSelectedFn: ({
		filterBy,
		items,
		query
	}: SearchArgs) => OrderedMap<any, any>;
	stagedProps: IPagination & {
		onOrderByFieldsChange: () => void;
		toolbarProps: object;
	};
	toolbarProps: object;
}

/**
 * WithCrossPageSelect
 */
const WithCrossPageSelect = (withData, configs = {}) => {
	const TableWithData = withBaseResults(withData, {
		...configs,
		withSelection
	});

	const TableWithLocalData = withBaseResults(withLocalData, {
		...configs,
		withSelection
	});

	const CrossPageSelect = React.forwardRef<
		BaseResults,
		ICrossPageSelectProps
	>(
		(
			{
				filterBy,
				router,
				searchSelectedFn,
				stagedProps,
				toolbarProps,
				...otherProps
			},
			ref
		) => {
			const {selectedItems} = useSelectionContext();
			const [showSelected, setShowSelected] = useState(false);

			const {
				delta,
				filterBy: stagedFilterBy,
				onOrderByFieldsChange,
				orderBy,
				orderByField,
				page,
				query,
				toolbarProps: stagedToolbarProps,
				...otherStagedProps
			} = stagedProps;

			useEffect(() => {
				if (selectedItems.isEmpty() && showSelected) {
					setShowSelected(false);
				}
			});

			const renderLinkProp = {
				renderViewSelectedToggle: () => (
					<ViewSelectedToggle
						onClick={() => setShowSelected(!showSelected)}
						selectedItemsCount={selectedItems.size}
						showSelected={showSelected}
					/>
				)
			};

			const passThruProps = showSelected
				? {
						...otherProps,
						...otherStagedProps,
						filterBy: stagedFilterBy,
						toolbarProps: {
							...stagedToolbarProps,
							...renderLinkProp,
							filterBy: stagedFilterBy
						}
				  }
				: {
						...otherProps,
						filterBy,
						toolbarProps: {...toolbarProps, ...renderLinkProp}
				  };

			return showSelected ? (
				<TableWithLocalData
					onSortChange={onOrderByFieldsChange}
					ref={ref}
					router={{
						query: {
							delta,
							orderBy,
							orderByField,
							page,
							query
						}
					}}
					searchSelectedFn={searchSelectedFn}
					{...passThruProps}
				/>
			) : (
				<TableWithData {...passThruProps} ref={ref} router={router} />
			);
		}
	);

	return withStatefulPagination(
		CrossPageSelect,
		({
			defaultOrderBy,
			defaultOrderByField
		}: {
			defaultOrderBy: string;
			defaultOrderByField: string;
		}) => pickBy({defaultOrderBy, defaultOrderByField}),
		(props, {toolbarProps}) => ({
			stagedProps: omit(props, 'onSearchValueChange'),
			toolbarProps
		})
	);
};

export const ViewSelectedToggle = ({
	onClick,
	selectedItemsCount,
	showSelected
}) => (
	<ClayButton
		data-testid='view-selected'
		displayType='link'
		onClick={onClick}
		small
	>
		<b>
			{showSelected
				? Liferay.Language.get('return-to-list')
				: sub(Liferay.Language.get('view-selected-x'), [
						selectedItemsCount
				  ])}
		</b>
	</ClayButton>
);

export default WithCrossPageSelect;
