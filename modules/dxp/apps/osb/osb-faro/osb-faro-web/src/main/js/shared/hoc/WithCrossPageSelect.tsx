import ClayButton from '@clayui/button';
import React from 'react';
import {ACTION_TYPES, useSelectionContext} from 'shared/context/selection';
import {FilterByType} from 'shared/types';
import {get} from 'lodash';
import {getDisplayName} from 'shared/util/react';
import {getSafeDisplayValue} from 'shared/util/util';
import {OrderByDirections} from 'shared/util/constants';
import {OrderedMap} from 'immutable';
import {OrderParams} from 'shared/util/records';
import {sub} from 'shared/util/lang';

type SearchArgs = {
	filterBy?: FilterByType;
	items: OrderedMap<any, any>;
	query: string;
};

export type SearchFnType = ({items, query}: SearchArgs) => OrderedMap<any, any>;

/**
 * Function for local search on items.
 */
export const defaultSearch: SearchFnType = ({items, query}: SearchArgs) =>
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
	orderIOMap: OrderedMap<string, OrderParams>
): OrderedMap<any, any> => {
	const {field, sortOrder} = orderIOMap.first();

	const sorted = items.sortBy(item => {
		if (item[field]) {
			return item[field];
		} else if (get(item, ['properties', field])) {
			return item.properties[field];
		} else {
			return item;
		}
	});

	return sortOrder === OrderByDirections.Ascending
		? (sorted as OrderedMap<any, any>)
		: (sorted.reverse() as OrderedMap<any, any>);
};

export const fetchLocalData = ({
	delta,
	items,
	filterBy,
	orderIOMap,
	page,
	query,
	searchSelectedFn = defaultSearch
}: {
	delta: number;
	items: OrderedMap<any, any>;
	filterBy?: FilterByType;
	orderIOMap: OrderedMap<string, OrderParams>;
	page: number;
	query?: string;
	searchSelectedFn?: ({
		filterBy,
		items,
		query
	}: SearchArgs) => OrderedMap<any, any>;
}) => {
	const start = (page - 1) * delta;

	const end = start + delta;

	const result =
		query || filterBy
			? defaultSort(
					searchSelectedFn({filterBy, items, query}),
					orderIOMap
			  )
			: defaultSort(items, orderIOMap);

	return {
		empty: !result.size,
		items: result.slice(start, end).toArray(),
		total: result.size
	};
};

export const withLocalData = () => WrappedComponent => props => {
	const {delta, filterBy, orderIOMap, page, query, searchSelectedFn} = props;

	const {selectedItems} = useSelectionContext();

	return (
		<WrappedComponent
			{...props}
			{...fetchLocalData({
				delta,
				filterBy,
				orderIOMap,
				page,
				items: selectedItems,
				query,
				searchSelectedFn
			})}
		/>
	);
};

interface IwithSelectionProps {
	checkDisabled?: (item?: object) => boolean;
	items: {id: string}[];
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
		showCheckbox = true,
		...otherProps
	}) => {
		const {selectedItems, selectionDispatch} = useSelectionContext();

		const allChecked =
			!selectedItems.isEmpty() &&
			items.every(
				item => selectedItems.has(item.id) || checkDisabled(item)
			);

		const selectionProps = {
			alwaysShowSearch: true,
			onSelectEntirePage: checked => {
				selectionDispatch({
					payload: {
						items: items.filter(item => !checkDisabled(item))
					},
					type: checked ? ACTION_TYPES.add : ACTION_TYPES.remove
				});
			},
			onSelectItemsChange: item =>
				selectionDispatch({payload: {item}, type: ACTION_TYPES.toggle}),
			selectedItemsIOMap: selectedItems,
			selectEntirePage: allChecked,
			selectEntirePageIndeterminate:
				!allChecked && !selectedItems.isEmpty(),
			showCheckbox
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
