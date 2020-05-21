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
import React from 'react';

import Bundle from './Bundle';

function ItemInfo({childItems = [], name, options = '', sku}) {
	return (
		<>
			<div className={'item-info-base'}>
				<h5 className={'item-name'}>{name}</h5>
				<p className={'item-sku'}>{sku}</p>
			</div>

			{childItems.length > 0 && <Bundle childItems={childItems} />}

			{!!options && (
				<div className={'item-info-extra mt-3'}>
					<h6 className={'options'}>{options}</h6>
				</div>
			)}
		</>
	);
}

ItemInfo.propTypes = {
	childItems: PropTypes.array,
	name: PropTypes.string.isRequired,
	options: PropTypes.string,
	sku: PropTypes.string.isRequired
};

export default ItemInfo;
