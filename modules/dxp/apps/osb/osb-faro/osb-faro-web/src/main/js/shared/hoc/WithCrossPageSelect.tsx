import BaseResults from 'shared/components/BaseResults';
import ClayButton from '@clayui/button';
import React, {useEffect, useState} from 'react';
import {ACTION_TYPES, useSelectionContext} from 'shared/context/selection';
import {createOrderIOMap} from 'shared/util/pagination';
import {FilterByType, IPagination, Router} from 'shared/types';
import {get, omit, pickBy} from 'lodash';
import {getDisplayName} from 'shared/util/react';
import {getSafeDisplayValue} from 'shared/util/util';
import {OrderByDirections} from 'shared/util/constants';
import {OrderedMap} from 'immutable';
import {OrderParams} from 'shared/util/records';
import {sub} from 'shared/util/lang';
import {useStatefulPagination} from 'shared/hooks';
import {withBaseResults, withStatefulPagination} from 'shared/hoc';

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
		showCheckbox = true,
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

interface ICrossPageSelectProps {
	filterBy?: FilterByType;
	router: Router;
	searchSelectedFn: ({
		filterBy,
		items,
		query
	}: SearchArgs) => OrderedMap<any, any>;
	stagedProps: IPagination & {
		onOrderIOMapChange: (
			orderIOMap: OrderedMap<string, OrderParams>
		) => void;
		toolbarProps: object; // TODO Remove this
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
				delta,
				filterBy,
				onDeltaChange,
				onFilterByChange,
				onOrderIOMapChange,
				onPageChange,
				onQueryChange,
				orderIOMap,
				page,
				query,
				router,
				searchSelectedFn,
				// stagedProps,
				toolbarProps,
				...otherProps
			},
			ref
		) => {
			const {selectedItems, selectionDispatch} = useSelectionContext();

			const {
				filterBy: stagedFilterBy,
				onFilterByChange: onStagedFilterByChange,
				onPageChange: onStagedPageChange,
				onQueryChange: onStagedQueryChange,
				page: stagedPage,
				query: stagedQuery,
				resetPage
			} = useStatefulPagination(null, {
				initialDelta: delta,
				initialFilterBy: filterBy,
				initialOrderIOMap: orderIOMap,
				initialPage: page,
				initialQuery: query
			});

			const [showSelected, setShowSelected] = useState(false);

			// const {
			// 	delta,
			// 	filterBy: stagedFilterBy,
			// 	onOrderByFieldsChange,
			// 	orderBy,
			// 	orderByField,
			// 	page,
			// 	query,
			// 	toolbarProps: stagedToolbarProps,
			// 	...otherStagedProps
			// } = stagedProps;

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
						delta,
						filterBy: stagedFilterBy,
						onDeltaChange,
						onFilterByChange: onStagedFilterByChange,
						onOrderIOMapChange,
						onPageChange: onStagedPageChange,
						onQueryChange: onStagedQueryChange,
						orderIOMap,
						page: stagedPage,
						query: stagedQuery,
						selectedItems,
						selectionDispatch,
						toolbarProps: {
							...stagedToolbarProps,
							...renderLinkProp,
							filterBy: stagedFilterBy
						}
				  }
				: {
						...otherProps,
						delta,
						filterBy,
						onDeltaChange,
						onFilterByChange,
						onOrderIOMapChange,
						onPageChange,
						onQueryChange,
						orderIOMap,
						page,
						query,
						selectedItems,
						selectionDispatch,
						toolbarProps: {...toolbarProps, ...renderLinkProp}
				  };

			return showSelected ? (
				<TableWithLocalData
					onSortChange={onOrderByFieldsChange}
					ref={ref}
					searchSelectedFn={searchSelectedFn}
					{...passThruProps}
				/>
			) : (
				<TableWithData {...passThruProps} ref={ref} router={router} />
			);
		}
	);

	return CrossPageSelect;

	// return withStatefulPagination(
	// 	CrossPageSelect,
	// 	({
	// 		defaultOrderBy,
	// 		defaultOrderByField
	// 	}: {
	// 		defaultOrderBy: string;
	// 		defaultOrderByField: string;
	// 	}) => pickBy({defaultOrderBy, defaultOrderByField}),
	// 	(props, {toolbarProps}) => ({
	// 		stagedProps: omit(props, 'onSearchValueChange'),
	// 		toolbarProps
	// 	})
	// );
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
