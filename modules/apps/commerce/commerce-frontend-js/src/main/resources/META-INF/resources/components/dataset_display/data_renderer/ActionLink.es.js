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
import ClayLink from '@clayui/link';
import PropTypes from 'prop-types';
import React, {useContext} from 'react';

import DatasetDisplayContext from '../DatasetDisplayContext.es';
import DefaultContent from './Default.es';

function ActionLink(props) {
	const {openModal, openSidePanel} = useContext(DatasetDisplayContext);

	const currentAction =
		props.options && props.options.actionId
			? props.actions.find(action => action.id === props.options.actionId)
			: props.actions[0];

	if (!currentAction) {
		return <DefaultContent value={props.value} />;
	}

	function handleClickOnLink(e, payload, target) {
		e.preventDefault();
		
		if (target === 'modal') {
			return openModal(payload);
		}

		if (target === 'sidePanel') {
			return openSidePanel(payload);
		}
	}

	return (
		<ClayLink
			data-senna-off
			href={currentAction.href}
			onClick={
				currentAction.target &&
				currentAction.target !== 'link' &&
				(e =>
					handleClickOnLink(
						e,
						{
							size: currentAction.size || 'lg',
							title: currentAction.title,
							url: currentAction.href
						},
						currentAction.target
					)
				)
			}

		>
			{props.value || <ClayIcon symbol={currentAction.icon} />}
		</ClayLink>
	);
}

ActionLink.propTypes = {
	actions: PropTypes.arrayOf(
		PropTypes.shape({
			disabled: PropTypes.bool,
			href: PropTypes.string.isRequired,
			icon: PropTypes.string,
			size: PropTypes.string,
			target: PropTypes.oneOf(['modal', 'sidePanel', 'link']),
			title: PropTypes.string
		})
	),
	options: PropTypes.shape({
		actionId: PropTypes.string
	}),
	value: PropTypes.string
};

export default ActionLink;
