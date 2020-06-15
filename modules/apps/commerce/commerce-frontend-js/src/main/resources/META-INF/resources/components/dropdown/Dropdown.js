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
import React, {useState} from 'react';

import {ACTION_ITEM_TARGETS} from '../../utilities/actionItems/constants';
import {OPEN_MODAL} from '../../utilities/eventsDefinitions';
import {getRandomId} from '../../utilities/index';
import {
	openPermissionsModal,
	resolveModalSize
} from '../../utilities/modals/index';
import Modal from '../modal/Modal';

const {MODAL_PERMISSIONS} = ACTION_ITEM_TARGETS;

function Dropdown(props) {
	const [active, setActive] = useState(false);

	const [dropdownSupportModalId] = useState('support-modal-' + getRandomId());

	function openModal(configs) {
		return Liferay.fire(OPEN_MODAL, {
			closeOnSubmit: true,
			id: dropdownSupportModalId,
			...configs
		});
	}

	function handleAction({onClick = '', target = '', title = '', url = ''}) {
		if (!!target && target.includes('modal')) {
			switch (target) {
				case MODAL_PERMISSIONS:
					openPermissionsModal(url);
					break;
				default:
					openModal({
						size: resolveModalSize(target),
						title,
						url
					});
					break;
			}
		}

		if (onClick) {
			eval(onClick);
		}
	}

	if (!props.items || !props.items.length) {
		return null;
	}

	return (
		<ClayDropDown
			active={active}
			onActiveChange={setActive}
			trigger={
				<ClayButton
					className="component-action dropdown-toggle"
					displayType="unstyled"
				>
					<ClayIcon
						spritemap={
							themeDisplay.getPathThemeImages() +
							'/lexicon/icons.svg'
						}
						symbol="ellipsis-v"
					/>
				</ClayButton>
			}
		>
			<Modal id={dropdownSupportModalId} />

			<ClayDropDown.ItemList>
				<ClayDropDown.Group>
					{JSON.parse(props.items).map((item, i) => {
						return (
							<ClayDropDown.Item
								data-senna-off
								href={item.href || '#'}
								key={i}
								onClick={e => {
									if (props.target.includes('modal')) {
										e.preventDefault();
										setActive(false);
										return handleAction({
											onClick: item.onClick,
											target: item.target,
											title: item.title,
											url: item.href
										});
									}
								}}
							>
								{item.icon && (
									<span className="pr-2">
										<ClayIcon symbol={item.icon} />
									</span>
								)}
								{item.label}
							</ClayDropDown.Item>
						);
					})}
				</ClayDropDown.Group>
			</ClayDropDown.ItemList>
		</ClayDropDown>
	);
}

Dropdown.propTypes = {
	items: PropTypes.arrayOf(
		PropTypes.shape({
			href: PropTypes.string.isRequired,
			icon: PropTypes.string,
			label: PropTypes.string.isRequired,
			order: PropTypes.number,
			target: PropTypes.string.isRequired
		})
	)
};

export default Dropdown;
