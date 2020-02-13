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

import launcher from './entry.es';

launcher('summary', 'summary', {
	apiUrl: '/fake-summary-item',
	datasetDisplayId: 'testId',
	items: [
		{
			label: 'Items Subtotal',
			value: '$ 2,208.50'
		},
		{
			label: 'Order Discount',
			value: '$ 0.00'
		},
		{
			label: 'Promotion codes',
			value: '--'
		},
		{
			label: 'Estimated Tax',
			value: '$ 0.00'
		},
		{
			label: 'Shipping & Handing',
			value: '$ 50.00'
		},
		{
			label: 'Grand Total',
			style: 'big',
			value: '$ 2,258.50'
		},
		{
			style: 'divider'
		},
		{
			label: 'Total return refounds',
			style: 'danger',
			value: '$ 0.00'
		},
		{
			label: 'Total appeasement refounds',
			style: 'danger',
			value: '$ 0.00'
		}
	]
});
