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

import {ServiceProvider} from '../../../ServiceProvider/index';
import Headless from '../../../utilities/Headless/index';

function configureByBaseEndpoint(apiEndpoint) {
	return {
		deleteItemById: itemId => Headless.DELETE(`${apiEndpoint}/${itemId}`),
		getCartByIdWithItems: cartId =>
			Headless.GET(`${apiEndpoint}/${cartId}`),
		updateCartById: (cartId, cartModel) =>
			Headless.PATCH(`${apiEndpoint}/${cartId}`, cartModel),
		updateItemById: (itemId, itemModel) =>
			Headless.PATCH(`${apiEndpoint}/${itemId}`, itemModel)
	};
}

export default (apiEndpoint = null) => {
	if (apiEndpoint) {
		return configureByBaseEndpoint(apiEndpoint);
	}

	return ServiceProvider.DeliveryCartAPI('v1');
};
