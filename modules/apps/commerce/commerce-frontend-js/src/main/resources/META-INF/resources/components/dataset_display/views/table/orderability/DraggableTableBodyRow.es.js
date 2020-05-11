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

import ClayIcon from '@clayui/icon';
import ClayTable from '@clayui/table';
import getCN from 'classnames';
import {useDrag, useDrop} from 'react-dnd';
import React from 'react';

import {TableBodyRowConfiguration} from './index.es';

function getClassNames(isDragging) {
	return getCN('draggable-row', isDragging ? 'is-dragging' : '');
}

function DraggableTableBodyRow({children, ...orderabilityProps}) {
	const {
		configureDragSource,
		configureDropTarget
	} = TableBodyRowConfiguration;

	const [{isDragging}, drag, preview] = useDrag(
		configureDragSource({...orderabilityProps})
	);

	const [, drop] = useDrop(configureDropTarget({...orderabilityProps}));

	return (
		<ClayTable.Row
			className={getClassNames(isDragging)}
			ref={node => preview(drop(node))}
		>
			<ClayTable.Cell ref={drag}>
				<ClayIcon symbol={'drag'} />
			</ClayTable.Cell>
			{children}
		</ClayTable.Row>
	);
}

export default DraggableTableBodyRow;
