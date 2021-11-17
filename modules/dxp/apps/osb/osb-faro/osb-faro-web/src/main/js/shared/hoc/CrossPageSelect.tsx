import ListComponent from 'shared/hoc/ListComponent';
import React, {useEffect, useState} from 'react';
import {
	fetchLocalData,
	SearchFnType,
	ViewSelectedToggle,
	withSelection
} from './WithCrossPageSelect';
import {FilterByType, IPagination} from 'shared/types';
import {OrderedMap} from 'immutable';
import {OrderParams} from 'shared/util/records';
import {useSelectionContext} from 'shared/context/selection';
import {useStatefulPagination} from 'shared/hooks';

interface ICrossPageSelectProps extends IPagination {
	children: (val) => React.ReactElement;
	onDeltaChange: (delta: number) => void;
	onFilterByChange: (filterBy: FilterByType) => void;
	onOrderIOMapChange: (orderIOMap: OrderedMap<string, OrderParams>) => void;
	onPageChange: (page: number) => void;
	onQueryChange: (query: string) => void;
	searchSelectedFn: SearchFnType;
	stagedProps: {[key: string]: any};
}

/**
 * CrossPageSelect
 *
 * This component is essentially the same as WithCrossPageSelect
 * but does no data-fetching of its own. This can be useful if we have
 * the data fetched beforehand, such as with useQuery.
 */

const CrossPageSelect: React.FC<ICrossPageSelectProps> = ({
	children,
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
	searchSelectedFn,
	showCheckbox,
	toolbarProps, // TODO: remove toolbarProps
	...otherProps
}) => {
	console.log(toolbarProps); // TODO: REmove me

	const {
		filterBy: stagedFilterBy,
		onFilterByChange: onStagedFilterByChange,
		onPageChange: onStagedPageChange,
		onQueryChange: onStagedQueryChange,
		page: stagedPage,
		query: stagedQuery
	} = useStatefulPagination(null, {
		initialDelta: delta,
		initialOrderIOMap: orderIOMap
	});
	const {selectedItems, selectionDispatch} = useSelectionContext();
	const [showSelected, setShowSelected] = useState(false);

	useEffect(() => {
		if (selectedItems.isEmpty() && showSelected) {
			setShowSelected(false);
		}

		if (
			stagedPage > 1 &&
			selectedItems.size <= delta * stagedPage - delta
		) {
			onStagedPageChange(stagedPage - 1);
		}
	});

	const renderViewSelectedToggle = () => (
		<ViewSelectedToggle
			onClick={() => setShowSelected(!showSelected)}
			selectedItemsCount={selectedItems.size}
			showSelected={showSelected}
		/>
	);

	const localData = fetchLocalData({
		delta,
		filterBy: stagedFilterBy,
		items: selectedItems,
		orderIOMap,
		page: stagedPage,
		query: stagedQuery,
		searchSelectedFn
	});

	return showSelected ? (
		<ListComponent
			{...otherProps}
			delta={delta}
			filterBy={stagedFilterBy}
			onDeltaChange={onDeltaChange}
			onFilterByChange={onStagedFilterByChange}
			onOrderIOMapChange={onOrderIOMapChange}
			onPageChange={onStagedPageChange}
			onQueryChange={onStagedQueryChange}
			orderIOMap={orderIOMap}
			page={stagedPage}
			query={stagedQuery}
			renderViewSelectedToggle={renderViewSelectedToggle}
			selectedItems={selectedItems}
			selectionDispatch={selectionDispatch}
			showCheckbox
			{...localData}
		/>
	) : (
		children({
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
			renderViewSelectedToggle,
			selectedItems,
			selectionDispatch,
			showCheckbox
		})
	);
};

// const DefaultComponent = withStatefulPagination(
// 	CrossPageSelect,
// 	({
// 		defaultDelta,
// 		defaultOrderBy,
// 		defaultOrderByField
// 	}: {
// 		defaultDelta: string;
// 		defaultOrderBy: string;
// 		defaultOrderByField: string;
// 	}) => pickBy({defaultDelta, defaultOrderBy, defaultOrderByField}),
// 	(props, {paginationProps, toolbarProps}) => ({
// 		paginationProps,
// 		stagedProps: omit(props, 'onSearchValueChange'),
// 		toolbarProps
// 	}),
// 	false
// );

export default withSelection(CrossPageSelect);
