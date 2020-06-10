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

import classnames from 'classnames';
import PropTypes from 'prop-types';
import React, {useEffect, useState} from 'react';

import {showErrorNotification} from '../../utilities/notifications';
import CartItemsList from './CartItemsList';
import MiniCartContext from './MiniCartContext';
import Opener from './Opener';
import Wrapper from './Wrapper';
import {generateActionURLs} from './util/cartActionURLs';
import {ADD_TO_ORDER, CHANGE_ACCOUNT, CHANGE_ORDER} from '../../utilities/eventsDefinitions';
import {resolveView} from './util/index';
import {ServiceProvider} from '../../ServiceProvider/index';

function MiniCart({
	cartItemsListView,
	cartView,
	checkoutPortletId,
	displayDiscountLevels,
	orderDetailsPortletId,
	spritemap
}) {
	const AJAX = ServiceProvider.DeliveryCartAPI('v1');

	const [isOpen, setIsOpen] = useState(false),
		[isUpdating, setIsUpdating] = useState(false),
		[cartState, updateCartState] = useState({}),
		[actionURLs, setActionURLs] = useState({}),
		[CartView, setCartView] = useState(null);

	const closeCart = () => setIsOpen(false),
		openCart = () => setIsOpen(true),
		resetCartState = () => updateCartState({});

	const updateCartModel = ({orderId: cartId}) =>
		AJAX.getCartByIdWithItems(cartId)
			.then(model => {
				if (model.id !== cartId) {
					const {orderUUID} = model;

					setActionURLs(
						generateActionURLs({
							checkoutPortletId,
							orderDetailsPortletId,
							orderUUID
						})
					);
				}

				updateCartState({...cartState, ...model});
			})
			.catch(error => {
				showErrorNotification(error);
			});

	useEffect(() => {
		if (!CartView) {
			resolveView(cartView).then(view => setCartView(() => view));
		}
	}, [CartView, cartView]);

	useEffect(() => {
		Liferay.on(ADD_TO_ORDER, updateCartModel);
		Liferay.on(CHANGE_ORDER, updateCartModel);

		return () => {
			Liferay.detach(ADD_TO_ORDER, updateCartModel);
			Liferay.detach(CHANGE_ORDER, updateCartModel);
		};
	}, [updateCartModel]);

	useEffect(() => {
		Liferay.on(CHANGE_ACCOUNT, resetCartState);

		return () => {
			Liferay.detach(CHANGE_ACCOUNT, resetCartState);
		};
	}, [resetCartState]);

	return (
		<MiniCartContext.Provider
			value={{
				actionURLs,
				AJAX,
				cartState,
				closeCart,
				displayDiscountLevels,
				isOpen,
				isUpdating,
				setIsUpdating,
				spritemap,
				updateCartModel,
				updateCartState
			}}
		>
			{!!CartView && (
				<div className={classnames('mini-cart', isOpen && 'is-open')}>
					<div
						className={'mini-cart-overlay'}
						onClick={() => setIsOpen(false)}
					/>

					<Opener openCart={openCart} />

					<CartView cartItemsListView={cartItemsListView} />
				</div>
			)}
		</MiniCartContext.Provider>
	);
}

MiniCart.defaultProps = {
	cartItemsListView: {
		component: CartItemsList
	},
	cartView: {
		component: Wrapper
	},
	displayDiscountLevels: false
};

MiniCart.propTypes = {
	cartItemsListView: PropTypes.shape({
		component: PropTypes.func,
		contentRendererModuleUrl: PropTypes.string
	}),
	cartView: PropTypes.shape({
		component: PropTypes.func,
		contentRendererModuleUrl: PropTypes.string
	}),
	checkoutPortletId: PropTypes.string,
	displayDiscountLevels: PropTypes.bool,
	orderDetailsPortletId: PropTypes.string,
	spritemap: PropTypes.string
};

export default MiniCart;
