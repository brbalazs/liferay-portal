/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import {ClayIconSpriteContext} from '@clayui/icon';
import PropTypes from 'prop-types';
import React, {useEffect, useState} from 'react';

import {showErrorNotification} from '../../utilities/index.es';
import AddOrCreate from './AddOrCreate.es';

function ItemFinder(props) {
	const [items, updateItems] = useState([]);
	const [pageSize, updatePageSize] = useState(props.pageSize);
	const [currentPage, updateCurrentPage] = useState(props.currentPage);
	const [textFilter, updateTextFilter] = useState('');
	const [itemsCount, updateItemsCount] = useState(props.itemsCount || 0);
	const [selectedItems, updateSelectedItems] = useState(props.selectedItems);

	useEffect(() => {
		fetch(
			`${props.apiUrl}?pageSize=${pageSize}&page=${currentPage}${
				textFilter ? `&search=${textFilter}` : ''
			}`
		)
			.then(data => data.json())
			.then(jsonResponse => {
				updateItems(jsonResponse.items);
				updateItemsCount(jsonResponse.totalCount);
			});
	}, [
		pageSize,
		currentPage,
		textFilter,
		updateItems,
		updateItemsCount,
		props.apiUrl
	]);

	function selectItem(itemId) {
		props
			.onItemSelected(itemId)
			.then(() => {
				updateSelectedItems(i => [...i, itemId]);
			})
			.catch(showErrorNotification);
	}

	function createItem() {
		props
			.onItemCreated(textFilter)
			.then(id => {
				updateSelectedItems(i => [...i, id]);
			})
			.catch(showErrorNotification);
	}

	return (
		<ClayIconSpriteContext.Provider value={props.spritemap}>
			<AddOrCreate
				currentPage={currentPage}
				inputSearchValue={textFilter}
				items={items}
				itemsCount={itemsCount}
				itemsKey={props.itemsKey}
				onInputSearchChange={updateTextFilter}
				onItemCreated={createItem}
				onItemSelected={selectItem}
				pageSize={pageSize}
				schema={props.schema}
				searchInputValue={textFilter}
				selectedItems={selectedItems}
				spritemap={props.spritemap}
				updateCurrentPage={updateCurrentPage}
				updatePageSize={updatePageSize}
			/>
		</ClayIconSpriteContext.Provider>
	);
}

ItemFinder.propTypes = {
	apiUrl: PropTypes.string.isRequired,
	itemsKey: PropTypes.string.isRequired,
	onItemCreated: PropTypes.func.isRequired,
	onItemSelected: PropTypes.func.isRequired,
	pageSize: PropTypes.number,
	schema: PropTypes.object.isRequired,
	selectedItems: PropTypes.array
};

ItemFinder.defaultProps = {
	currentPage: 1,
	pageSize: 5,
	selectedItems: []
};

export default ItemFinder;
