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

import {ClayButtonWithIcon} from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import PropTypes from 'prop-types';
import React, {useState, useContext} from 'react';

import {
	OPEN_MODAL,
	OPEN_SIDE_PANEL
} from '../../../../utilities/eventsDefinitions.es';
import DatasetDisplayContext from '../../DatasetDisplayContext.es';

function CreationMenu(props) {
	const [active, setActive] = useState(false);
	const datasetContext = useContext(DatasetDisplayContext);

	function executeAction(i) {
		const clickedItem = props.items[i];

		switch (clickedItem.target) {
			case 'modal':
				Liferay.fire(OPEN_MODAL, {
					id: datasetContext.modalId,
					onClose: datasetContext.loadData,
					url: clickedItem.href
				});
				break;
			case 'sidePanel':
				Liferay.fire(OPEN_SIDE_PANEL, {
					id: datasetContext.sidePanelId,
					onAfterSubmit: datasetContext.loadData,
					url: clickedItem.href
				});
				break;
			case 'event':
				Liferay.fire(clickedItem.href);
				break;
			default:
				window.location.href = clickedItem.href;
				break;
		}
	}

	if (!props.items || !props.items.length) return;

	return (
		<ul className="navbar-nav">
			<li className="nav-item">
				{props.items.length > 1 ? (
					<ClayDropDown
						active={active}
						onActiveChange={setActive}
						trigger={<ClayButtonWithIcon symbol="plus" />}
					>
						<ClayDropDown.ItemList>
							{props.items.map((item, i) => (
								<ClayDropDown.Item
									href={item.href || '#'}
									key={i}
									onClick={
										item.target &&
										item.target !== 'link' &&
										(e => {
											e.preventDefault();
											setActive(false);
											executeAction(i);
										})
									}
								>
									{item.label}
								</ClayDropDown.Item>
							))}
						</ClayDropDown.ItemList>
					</ClayDropDown>
				) : (
					<ClayButtonWithIcon
						onClick={() => executeAction(0)}
						symbol="plus"
					/>
				)}
			</li>
		</ul>
	);
}

CreationMenu.propTypes = {
	items: PropTypes.arrayOf(
		PropTypes.shape({
			href: PropTypes.string.isRequired,
			label: PropTypes.string.isRequired,
			target: PropTypes.oneOf(['modal', 'sidePanel', 'event', 'link'])
		})
	).isRequired
};

export default CreationMenu;
