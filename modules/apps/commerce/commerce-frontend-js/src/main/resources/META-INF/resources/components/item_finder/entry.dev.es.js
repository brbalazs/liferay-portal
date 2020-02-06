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

import launcher from './entry.es';

import '../../styles/main.scss';
import {getRandomId} from '../../utilities/index.es';

function addNewItem(_name) {
	return new Promise(resolve => {
		setTimeout(() => resolve(getRandomId()), 200);
	});
}

function selectItem(id) {
	return new Promise(resolve => {
		setTimeout(() => resolve(id), 200);
	});
}

launcher('itemFinder', 'item-finder-root-id', {
	apiUrl: '/o/headless-commerce-admin-catalog/v1.0/specifications',
	itemsKey: 'id',
	onItemCreated: addNewItem,
	onItemSelected: selectItem,
	// eslint-disable-next-line no-console
	onSubmit: console.log,
	pageSize: 5,
	schema: {
		itemTitle: ['title', 'en_US']
	},
	spritemap: './assets/icons.svg'
});
