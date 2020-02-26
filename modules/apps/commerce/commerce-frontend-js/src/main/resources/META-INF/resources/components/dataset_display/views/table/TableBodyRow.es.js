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

import ClayTable from '@clayui/table';
import React from 'react';

import {DraggableTableBodyRow} from './prioritization/index.es';

function TableBodyRow({prioritize, index, item,
	itemsList, setItemsList, ...remnant}) {

	return (
		prioritize
			? (
				<DraggableTableBodyRow
					index={index}
					item={item}
					itemsList={itemsList}
					setItemsList={setItemsList}
					{...remnant}
				/>
			) : (
				<ClayTable.Row>
					{remnant.children}
				</ClayTable.Row>
			)
	);
}

export default TableBodyRow;