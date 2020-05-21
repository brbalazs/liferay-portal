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

import Summary from '../summary/Summary';
import CartItem from './CartItem';
import CartItemsListActions from './CartItemsListActions';
import MiniCartContext from './MiniCartContext';
import {summaryDataMapper} from './util/index';

function CartItemsList({items}) {
	const {cartState, isUpdating} = useContext(MiniCartContext),
		{summary = {}} = cartState,
		{itemsQuantity = 0} = summary,
		numberOfItems = (items || []).length;

	return (
		<div className={'mini-cart__items-list'}>
			<CartItemsListActions numberOfItems={numberOfItems} />

			{numberOfItems > 0 ? (
				<>
					<div className={'mini-cart__cart-items'}>
						{items.map(item => (
							<CartItem item={item} key={item.id} />
						))}
					</div>

					{itemsQuantity > 0 && (
						<>
							<Summary
								dataMapper={summaryDataMapper}
								isLoading={isUpdating}
								summaryData={summary}
							/>
						</>
					)}
				</>
			) : (
				<CartItem.NoItems />
			)}
		</div>
	);
}

CartItemsList.propTypes = {
	datasetDisplayContext: PropTypes.object,
	items: PropTypes.array
};

export default CartItemsList;
