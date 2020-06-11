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
import React, {useContext} from 'react';

import {liferayNavigate} from '../../utilities/index';
import MiniCartContext from './MiniCartContext';
import {WORKFLOW_STATUS_APPROVED} from './util/constants';

function OrderButton() {
	const {actionURLs, cartState} = useContext(MiniCartContext),
		{cartItems} = cartState,
		{length: numberOfItems = 0} = cartItems || {},
		{orderStatusInfo} = cartState,
		{code: workflowStatus = WORKFLOW_STATUS_APPROVED} =
			orderStatusInfo || {},
		{checkoutURL} = actionURLs;

	return (
		<div className={'mini-cart-submit'}>
			<ClayButton
				block
				disabled={!numberOfItems}
				onClick={() => {
					liferayNavigate(checkoutURL);
				}}
			>
				{workflowStatus === WORKFLOW_STATUS_APPROVED
					? Liferay.Language.get('submit')
					: Liferay.Language.get('review-order')}
			</ClayButton>
		</div>
	);
}

export default OrderButton;
