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

function defineServerResponses(app) {
	app.get('/dataset-display-default-data', (_, res) => {
		res.json({
			items: [
				{
					actionItems: [
						{
							href: '/view/url',
							icon: 'view',
							id: 'view',
							label: 'View'
						},
						{
							href: '/select/url',
							icon: 'message-boards',
							id: 'select',
							label: 'Select',
							target: 'modal'
						},
						{
							href: '/delete/url',
							icon: 'trash',
							id: 'delete',
							label: 'Delete',
							method: 'delete',
							target: 'async'
						},
						{
							href: '/edit/url',
							icon: 'pencil',
							id: 'edit',
							label: 'Edit',
							target: 'sidePanel'
						},
						{
							icon: 'warning-full',
							id: 'alert',
							label: 'Alert',
							onClick: 'alert("asd")'
						}
					],
					deliveryGroup: '',
					detailedPrice: {
						details: [
							{
								label: 'Catalog price',
								value: '$ 15'
							},
							{
								label: 'Final price',
								value: '$ 31.123'
							},
							{
								label: 'Promo price',
								value: '$ 15.600'
							},
							{
								label: 'Discounts',
								value: [40, 30, 20, 10]
							}
						],
						final: {
							label: 'Final price',
							value: '12.000 $'
						}
					},
					discount: '$0.00',
					image: {
						alt: 'Oil Pump',
						shape: 'rounded',
						size: 'lg',
						src: '//via.placeholder.com/250x250'
					},
					name: 'Oil Pump',
					orderId: 41023,
					orderItemId: 45317,
					price: '$8.00',
					quantity: 30,
					requestedDeliveryDate: '',
					sku: 'MIN00677',
					status: {
						displayStyle: 'success',
						label: 'delivered'
					},
					subscriptionDuration: '',
					subscriptionPeriod: '',
					testQuantity: {
						inputName: 'sdf-quantity',
						maxQuantity: 1000,
						minQuantity: 2,
						multipleQuantity: 2,
						quantity: 6
					},
					total: '$711.00',
					type: {
						content: 'DOC',
						displayType: 'danger'
					}
				},
				{
					actionItems: [
						{
							href: '/view/url',
							icon: 'view',
							id: 'view',
							label: 'View'
						},
						{
							href: '/select/url',
							icon: 'message-boards',
							id: 'select',
							label: 'Select',
							target: 'modal'
						},
						{
							href: '/delete/url',
							icon: 'trash',
							id: 'delete',
							label: 'Delete',
							method: 'delete',
							target: 'async'
						},
						{
							href: '/edit/url',
							icon: 'pencil',
							id: 'edit',
							label: 'Edit',
							target: 'sidePanel'
						},
						{
							icon: 'warning-full',
							id: 'alert',
							label: 'Alert',
							onClick: 'alert("asd")'
						}
					],
					deliveryGroup: '',
					detailedPrice: {
						details: [
							{
								label: 'Catalog price',
								value: '$ 15'
							},
							{
								label: 'Final price',
								value: '$ 31.123'
							},
							{
								label: 'Promo price',
								value: '$ 15.600'
							},
							{
								label: 'Discounts',
								value: [40, 30, 20, 10]
							}
						],
						final: {
							label: 'Final price',
							value: '12.000 $'
						}
					},
					discount: '$0.00',
					image: {
						alt: 'Timing Belt',
						shape: 'rounded',
						size: 'lg',
						src: '//via.placeholder.com/250x250'
					},
					name: 'Timing Belt',
					orderId: 41023,
					orderItemId: 45317,
					price: '$79.00',
					quantity: 9,
					requestedDeliveryDate: '',
					sku: 'MIN00609',
					status: {
						displayStyle: 'danger',
						label: 'lost'
					},
					subscriptionDuration: '',
					subscriptionPeriod: '',
					testQuantity: {
						allowedQuantities: [3, 6, 7, 100],
						disabled: false,
						inputName: 'asd-quantity',
						quantity: 6
					},
					total: '$711.00',
					type: {
						content: 'DOC',
						displayType: 'danger'
					}
				}
			],
			totalItems: 1
		});
	});

	app.get('/dataset-display-selectable-data', (_, res) => {
		res.json({
			items: [
				{
					countryId: '001',
					countryName: 'United States',
					fields: [
						{
							label: 'Money Order',
							name: 'autorizeDotNet',
							value: false
						},
						{
							label: 'Money Order',
							name: 'moneyOrder',
							value: false
						},
						{
							label: 'PayPal',
							name: 'payPal',
							value: false
						}
					]
				},
				{
					countryId: '002',
					countryName: 'Afghanistan',
					fields: [
						{
							label: 'Money Order',
							name: 'autorizeDotNet',
							value: true
						},
						{
							label: 'Money Order',
							name: 'moneyOrder',
							value: false
						},
						{
							label: 'PayPal',
							name: 'payPal',
							value: false
						}
					]
				},
				{
					countryId: '003',
					countryName: 'Albania',
					fields: [
						{
							label: 'Money Order',
							name: 'autorizeDotNet',
							value: false
						},
						{
							label: 'Money Order',
							name: 'moneyOrder',
							value: true
						},
						{
							label: 'PayPal',
							name: 'payPal',
							value: false
						}
					]
				},
				{
					countryId: '004',
					countryName: 'Algeria',
					fields: [
						{
							label: 'Money Order',
							name: 'autorizeDotNet',
							value: false
						},
						{
							label: 'Money Order',
							name: 'moneyOrder',
							value: false
						},
						{
							label: 'PayPal',
							name: 'payPal',
							value: false
						}
					]
				},
				{
					countryId: '005',
					countryName: 'American Samoa',
					fields: [
						{
							label: 'Money Order',
							name: 'autorizeDotNet',
							value: true
						},
						{
							label: 'Money Order',
							name: 'moneyOrder',
							value: false
						},
						{
							label: 'PayPal',
							name: 'payPal',
							value: true
						}
					]
				}
			],
			totalItems: 5
		});
	});

	app.get('/dataset-display-email-data', (_, res) => {
		res.json({
			items: [
				{
					actionItems: [
						{
							href: '/delete/action/url',
							icon: 'trash',
							label: 'Delete'
						}
					],
					author: {
						avatarSrc: 'https://via.placeholder.com/150',
						email: 'john.doe@gmail.com',
						name: 'John Doe'
					},
					date: '1 day ago',
					href: '/side-panel/email.html',
					status: {
						displayStyle: 'danger',
						label: 'Order not placed'
					},
					subject:
						'Mauris blandit aliquet elit, eget tincidunt nibh pulvinar.',
					summary:
						'Pellentesque in ipsum id orci porta dapibus. Vivamus magna justo, lacinia eget consectetur sed, convallis at tellus. Nulla quis lorem ut libero malesuada feugiat. Pellentesque in ipsum id orci porta dapibus...'
				},
				{
					actionItems: [
						{
							href: '/delete/action/url',
							icon: 'trash',
							label: 'Delete'
						}
					],
					author: {
						avatarSrc: 'https://via.placeholder.com/150',
						email: 'john.doe@gmail.com',
						name: 'John Doe'
					},
					date: '14th April 2018',
					href: '/side-panel/email.html',
					status: {
						displayStyle: 'success',
						label: 'Order placed'
					},
					subject:
						'Curabitur aliquet quam id dui posuere blandit. Proin eget tortor risus.',
					summary:
						'Cras ultricies ligula sed magna dictum porta. Donec rutrum congue leo eget malesuada. Proin eget tortor risus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec velit neque, auctor sit amet aliquam vel, ullamcorper sit amet ligula. Quisque velit nisi, pretium ut lacinia in, elementum id enim...'
				}
			],
			totalItems: 2
		});
	});
}

module.exports = {
	defineServerResponses
};
