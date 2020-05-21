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
import React, {useContext} from 'react';

import MiniCartContext from '../MiniCartContext';

function NoItems() {
	const {spritemap} = useContext(MiniCartContext);

	return (
		<div className="empty-cart">
			<div className="empty-cart-icon mb-3">
				<ClayIcon spritemap={spritemap} symbol={'shopping-cart'} />
			</div>

			<p className="empty-cart-label">
				{Liferay.Language.get('add-a-product-to-the-cart')}
			</p>
		</div>
	);
}

export default NoItems;
