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
import ClayIcon from '@clayui/icon';
import classnames from 'classnames';
import PropTypes from 'prop-types';
import React, {useContext, useState} from 'react';

import QuantitySelector from '../../quantity_selector/QuantitySelector';
import MiniCartContext from '../MiniCartContext';
import API from '../util/apiConfiguration';
import {PRODUCT_REMOVED} from '../util/events';
import {parseOptions} from '../util/index';
import ItemInfo from './views/ItemInfo';
import ItemPrice from './views/ItemPrice';

const REMOVAL_TIMEOUT = 2000,
	REMOVAL_CANCELING_TIMEOUT = 700,
	REMOVAL_ERRORS_TIMEOUT = 4000,
	INITIAL_ITEM_STATE = {
		isGettingRemoved: false,
		isRemovalCanceled: false,
		isRemoved: false,
		isShowingErrors: false,
		previousQuantity: null,
		removalTimeoutRef: null
	};

function Item({item: cartItem}) {
	const {
		cartItems: childItems,
		id: cartItemId,
		name,
		options: rawOptions,
		price,
		productId,
		quantity,
		settings,
		sku,
		subscription,
		thumbnail
	} = cartItem;

	const options = parseOptions(rawOptions);

	const {
			apiEndpoint,
			cartState,
			displayDiscountLevels,
			setIsUpdating,
			spritemap,
			updateCartModel
		} = useContext(MiniCartContext),
		{id: orderId} = cartState,
		[itemState, setItemState] = useState(INITIAL_ITEM_STATE);

	const showErrors = () => {
		setIsUpdating(false);

		setItemState({
			...INITIAL_ITEM_STATE,
			isShowingErrors: true,
			removalTimeoutRef: setTimeout(() => {
				setItemState(INITIAL_ITEM_STATE);
			}, REMOVAL_ERRORS_TIMEOUT)
		});

		return Promise.resolve();
	};

	const cancelRemoveItem = () => {
			clearTimeout(removalTimeoutRef);

			setItemState({
				...INITIAL_ITEM_STATE,
				isRemovalCanceled: true,
				removalTimeoutRef: setTimeout(() => {
					setItemState(INITIAL_ITEM_STATE);
				}, REMOVAL_CANCELING_TIMEOUT)
			});
		},
		removeItem = () => {
			setItemState({
				...INITIAL_ITEM_STATE,
				isGettingRemoved: true,
				removalTimeoutRef: setTimeout(() => {
					setIsUpdating(true);

					setItemState({
						...INITIAL_ITEM_STATE,
						isGettingRemoved: true,
						isRemoved: true,
						removalTimeoutRef: setTimeout(() => {
							API(apiEndpoint)
								.deleteItemById(cartItemId)
								.then(() => updateCartModel({orderId}))
								.then(() => {
									setIsUpdating(false);
									Liferay.fire(PRODUCT_REMOVED, {
										productId
									});
								})
								.catch(showErrors);
						}, REMOVAL_CANCELING_TIMEOUT)
					});
				}, REMOVAL_TIMEOUT)
			});
		};

	const updateItemQuantity = quantity => {
		setIsUpdating(true);

		API(apiEndpoint)
			.updateItemById(cartItemId, {
				...cartItem,
				quantity
			})
			.catch(showErrors)
			.then(() => updateCartModel({orderId}))
			.then(() => setIsUpdating(false));
	};

	const {
		isGettingRemoved,
		isRemovalCanceled,
		isRemoved,
		isShowingErrors,
		removalTimeoutRef
	} = itemState;

	return (
		<div
			className={classnames('mini-cart__item', isRemoved && 'is-removed')}
		>
			{!!thumbnail && (
				<div
					className={'mini-cart__item-thumbnail'}
					style={{backgroundImage: `url(${thumbnail})`}}
				/>
			)}

			<div
				className={classnames(
					'mini-cart__item-info',
					options.length > 0 && 'has-options'
				)}
			>
				<ItemInfo
					childItems={childItems}
					name={name}
					options={options}
					sku={sku}
				/>
			</div>

			<div className={'mini-cart__item-quantity'}>
				<QuantitySelector
					quantity={quantity}
					size={'small'}
					spritemap={spritemap}
					updateQuantity={updateItemQuantity}
					{...settings}
				/>
			</div>

			<div className={'mini-cart__item-price'}>
				<ItemPrice
					displayDiscountLevels={displayDiscountLevels}
					price={price}
				/>
			</div>

			<div className={'mini-cart__item-delete'}>
				<button
					className={'btn btn-unstyled'}
					onClick={removeItem}
					type={'button'}
				>
					<ClayIcon spritemap={spritemap} symbol={'times'} />
				</button>
			</div>

			{isShowingErrors && (
				<div className={'mini-cart__item-errors'}>
					<ClayIcon
						spritemap={spritemap}
						symbol={'exclamation-circle'}
					/>

					<span>
						{Liferay.Language.get('an-unexpected-error-occurred')}
					</span>
				</div>
			)}

			<div
				className={classnames(
					'mini-cart__item-removing',
					isGettingRemoved && 'active',
					isRemovalCanceled && 'canceled'
				)}
			>
				<span>{Liferay.Language.get('the-item-has-been-removed')}</span>
				<span>
					<ClayButton
						displayType={'link'}
						href={'#'}
						onClick={cancelRemoveItem}
						small
						type={'button'}
					>
						{Liferay.Language.get('undo')}
					</ClayButton>
				</span>
			</div>
		</div>
	);
}

Item.propTypes = {
	item: PropTypes.object
};

export default Item;
