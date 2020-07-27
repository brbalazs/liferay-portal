import ListComponent from 'shared/hoc/ListComponent';
import React, {useEffect, useState} from 'react';
import {
	fetchLocalData,
	SearchFnType,
	ViewSelectedToggle,
	withSelection
} from './WithCrossPageSelect';
import {IPagination} from 'shared/types';
import {omit, pickBy} from 'lodash';
import {useSelectionContext} from 'shared/context/selection';
import {withStatefulPagination} from 'shared/hoc';

interface ICrossPageSelectProps extends IPagination {
	children: (val) => React.ReactElement;
	searchSelectedFn: SearchFnType;
	stagedProps: {[key: string]: any};
	toolbarProps: {[key: string]: any};
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
	filterBy,
	searchSelectedFn,
	stagedProps,
	toolbarProps,
	...otherProps
}) => {
	const {selectedItems, selectionDispatch} = useSelectionContext();
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
				delta,
				filterBy: stagedFilterBy,
				orderBy,
				orderByField,
				page,
				query,
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
				filterBy,
				selectedItems,
				selectionDispatch,
				toolbarProps: {...toolbarProps, ...renderLinkProp}
		  };

	const localData = fetchLocalData({
		delta,
		filterBy,
		items: selectedItems,
		orderBy,
		orderByField,
		page,
		query,
		searchSelectedFn
	});

	return showSelected ? (
		<ListComponent
			{...passThruProps}
			{...localData}
			onSortChange={onOrderByFieldsChange}
		/>
	) : (
		children({...passThruProps})
	);
};

const DefaultComponent = withStatefulPagination(
	CrossPageSelect,
	({
		defaultDelta,
		defaultOrderBy,
		defaultOrderByField
	}: {
		defaultDelta: string;
		defaultOrderBy: string;
		defaultOrderByField: string;
	}) => pickBy({defaultDelta, defaultOrderBy, defaultOrderByField}),
	(props, {paginationProps, toolbarProps}) => ({
		paginationProps,
		stagedProps: omit(props, 'onSearchValueChange'),
		toolbarProps
	}),
	false
);

export default withSelection(DefaultComponent);
