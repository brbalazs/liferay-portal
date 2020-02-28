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
	const {
		executeAsyncItemAction,
		highlightItems,
		openModal,
		openSidePanel
	} = useContext(DatasetDisplayContext);

	function handleClickOnLink(e) {
		e.preventDefault();

		if (props.target === 'modal') {
			openModal({
				size: props.size || 'lg',
				title: props.title,
				url: props.href
			});
		}

		if (props.target === 'sidePanel') {
			highlightItems([props.itemId]);
			openSidePanel({
				size: props.size || 'lg',
				title: props.title,
				url: props.href
			});
		}

		if (props.target === 'async') {
			executeAsyncItemAction(props.href, props.method);
		}

		if (props.onClick) {
			eval(props.onClick);
		}

		props.closeMenu();
	}

	function isNotALink() {
		return Boolean(
			(props.target && props.target !== 'link') || props.onClick
		);
	}

	return (
		<ClayDropDown.Item
			data-senna-off
			href={props.href || '#'}
			onClick={isNotALink() ? handleClickOnLink : null}
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

	if (!props.actions || !props.actions.length) {
		return null;
	}

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
					{props.actions.map((action, i) => (
						<ActionItem
							key={i}
							{...action}
							closeMenu={() => setActive(false)}
							itemId={props.itemId}
						/>
					))}
				</ClayDropDown.Group>
			</ClayDropDown.ItemList>
		</ClayDropDown>
	);
}

ActionsDropdown.propTypes = {
	actions: PropTypes.arrayOf(
		PropTypes.shape({
			href: PropTypes.string,
			icon: PropTypes.string,
			label: PropTypes.string.isRequired,
			method: PropTypes.oneOf(['get', 'delete']),
			onClick: PropTypes.string,
			target: PropTypes.oneOf(['modal', 'sidePanel', 'link', 'async'])
		})
	),
	itemId: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
};

export default ActionsDropdown;
