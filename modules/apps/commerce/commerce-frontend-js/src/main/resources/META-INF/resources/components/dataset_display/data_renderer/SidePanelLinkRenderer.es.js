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

import PropTypes from 'prop-types';
import React, {useContext} from 'react';

import DatasetDisplayContext from '../DatasetDisplayContext.es';
import DefaultContent from './DefaultRenderer.es';

function SidePanelLinkRenderer(props) {
	const {highlightItems, openSidePanel} = useContext(DatasetDisplayContext);

	function handleClickOnLink(e, payload) {
		e.preventDefault();

		highlightItems([props.itemId]);
		openSidePanel(payload);
	}

	return (
		<button
			className="btn btn-link btn-sm p-0"
			onClick={e =>
				handleClickOnLink(e, {
					size: props.value.size || 'lg',
					url: props.value.href
				})
			}
		>
			<DefaultContent value={props.value} />
		</button>
	);
}

SidePanelLinkRenderer.propTypes = {
	itemId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
	value: PropTypes.shape({
		href: PropTypes.string.isRequired,
		icon: PropTypes.string,
		label: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
			.isRequired
	}).isRequired
};

export default SidePanelLinkRenderer;
