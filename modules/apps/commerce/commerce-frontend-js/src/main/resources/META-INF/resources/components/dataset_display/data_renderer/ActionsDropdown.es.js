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

import ClayButton from '@clayui/button';
import ClayDropDown from '@clayui/drop-down';
import ClayIcon from '@clayui/icon';
import PropTypes from 'prop-types';
import React, {useContext, useState} from 'react';

import DatasetDisplayContext from '../DatasetDisplayContext.es';

function ActionItem(props) {
	const {loadData, openModal, openSidePanel} = useContext(
		DatasetDisplayContext
	);

	function handleClickOnLink(e, payload, target) {
		e.preventDefault();

		if (target === 'modal') {
			openModal(payload);
			props.closeMenu();
		}

		if (target === 'sidePanel') {
			openSidePanel(payload);
			props.closeMenu();
		}
	}

	return (
		<ClayDropDown.Item
			href={props.href}
			onClick={
				props.target &&
				props.target !== 'link' &&
				(e =>
					handleClickOnLink(
						e,
						{
							onSubmit: loadData,
							size: props.size || 'lg',
							title: props.title,
							url: props.href
						},
						props.target
					)
				)
			}
		>
			{props.icon && (
				<span className="pr-2">
					<ClayIcon symbol={props.icon} />
				</span>
			)}
			{props.label}
		</ClayDropDown.Item>
	);
}

function ActionsDropdown(props) {
	const [active, setActive] = useState(false);

	return (
		<ClayDropDown
			active={active}
			onActiveChange={setActive}
			trigger={
				<ClayButton className="btn-sm px-1" displayType="unstyled">
					<ClayIcon symbol="ellipsis-v" />
				</ClayButton>
			}
		>
			<ClayDropDown.ItemList>
				<ClayDropDown.Group>
					{props.items.map((item, i) => (
						<ActionItem
							key={i}
							{...item}
							closeMenu={() => setActive(false)}
						/>
					))}
				</ClayDropDown.Group>
			</ClayDropDown.ItemList>
		</ClayDropDown>
	);
}

ActionsDropdown.propTypes = {
	items: PropTypes.arrayOf(
		PropTypes.shape({
			href: PropTypes.string,
			icon: PropTypes.string,
			label: PropTypes.string.isRequired,
			target: PropTypes.oneOf(['modal', 'sidePanel', 'link'])
		})
	)
};

export default ActionsDropdown;
