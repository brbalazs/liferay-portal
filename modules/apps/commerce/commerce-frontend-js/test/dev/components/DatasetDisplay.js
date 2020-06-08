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

import React from 'react';

import datasetDisplayLauncher from '../../../src/main/resources/META-INF/resources/components/dataset_display/entry';
import sidePanelLauncher from '../../../src/main/resources/META-INF/resources/components/side_panel/entry';

import '../../../src/main/resources/META-INF/resources/styles/main.scss';

const lang_id = themeDisplay.getLanguageId();

const fluidDataSetDisplayProps = {
	activeView: 2,
	apiUrl: '/dataset-display-nested-items',
	bulkActions: [
		{
			href: '/side-panel/edit.html',
			icon: 'plus',
			label: Liferay.Language.get('add'),
			target: 'sidePanel'
		},
		{
			href: '/delete',
			icon: 'trash',
			label: Liferay.Language.get('delete'),
			method: 'delete'
		}
	],
	creationMenuItems: [
		{
			href: 'modal/url',
			label: Liferay.Language.get('add-via-modal'),
			target: 'modal'
		}
	],
	filters: [
		{
			id: 'number-test',
			inputText: '$',
			label: Liferay.Language.get('number-test'),
			max: 200,
			min: 20,
			operator: 'eq',
			type: 'number',
			value: 123
		},
		{
			id: 'order-date',
			label: Liferay.Language.get('order-range'),
			max: {
				day: 2,
				month: 9,
				year: 2026
			},
			min: {
				day: 14,
				month: 6,
				year: 2020
			},
			placeholder: 'dd/mm/yyyy',
			type: 'dateRange',
			value: {
				from: {
					day: 18,
					month: 7,
					year: 2020
				},
				to: {
					day: 18,
					month: 7,
					year: 2025
				}
			}
		}
	],
	id: 'tableTest',
	nestedItemsKey: 'skuId',
	nestedItemsReferenceKey: 'testSubItems',
	pageSize: 5,
	pagination: {
		deltas: [
			{
				label: 5
			},
			{
				label: 10
			},
			{
				label: 20
			},
			{
				label: 30
			},
			{
				label: 50
			},
			{
				href:
					'http://localhost:8080/group/test-1/pending-orders?p_p_id=com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&_com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet_delta=75',
				label: 75
			}
		],
		initialDelta: 10,
		initialPageNumber: 1,
		initialTotalItems: 40
	},
	showPagination: true,
	sidePanelId: 'sidePanelTestId',
	spritemap: './assets/icons.svg',
	style: 'fluid',
	views: [
		{
			contentRenderer: 'cards',
			icon: 'documents-and-media',
			label: Liferay.Language.get('cards'),
			schema: {
				description: 'name',
				href: 'productPage',
				imgProps: 'img',
				labels: 'status',
				stickerProps: 'type',
				title: 'skuId'
			}
		},
		{
			component: props => {
				return (
					<>
						<h4 className="p-3 mb-0 bg-dark text-center text-white">
							Hey, I&apos;m a custom template from the outside
						</h4>
						{props.items.map(item => (
							<div
								className="p-3 text-center bg-white"
								key={item.skuId}
							>
								<strong className="mr-3">{item.skuId}</strong>
								{item.name}
							</div>
						))}
					</>
				);
			},
			icon: 'merge',
			id: 'custom-table',
			label: "Hey you don't know me",
			schema: {}
		},
		{
			contentRenderer: 'table',
			icon: 'table',
			label: Liferay.Language.get('table'),
			schema: {
				fields: [
					{
						contentRenderer: 'image',
						fieldName: 'img',
						label: ''
					},
					{
						contentRenderer: 'actionLink',
						fieldName: 'name',
						label: 'Name',
						sortable: true
					},
					{
						actionId: 'edit',
						contentRenderer: 'actionLink'
					},
					{
						actionId: 'delete',
						contentRenderer: 'actionLink'
					},
					{
						actionId: 'alert',
						contentRenderer: 'actionLink'
					},
					{
						actionId: 'select',
						contentRenderer: 'actionLink'
					},
					{
						contentRenderer: 'tooltipPrice',
						fieldName: 'price',
						label: 'Price'
					},
					{
						contentRenderer: 'quantitySelector',
						fieldName: 'testQuantity',
						label: 'Qt. Selector'
					}
				]
			}
		}
	]
};

const emailsDataSetDisplayProps = {
	apiUrl: '/dataset-display-email-data',
	creationMenuItems: [
		{
			href: '/standard/edit',
			label: Liferay.Language.get('add')
		},
		{
			href: 'modal/url',
			label: Liferay.Language.get('add-via-modal'),
			target: 'modal'
		}
	],
	id: 'emailsDatasetDIsplay',
	pageSize: 5,
	pagination: {
		deltas: [
			{
				label: 5
			},
			{
				label: 10
			},
			{
				label: 20
			},
			{
				label: 30
			},
			{
				label: 50
			},
			{
				href:
					'http://localhost:8080/group/test-1/pending-orders?p_p_id=com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&_com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet_delta=75',
				label: 75
			}
		],
		initialDelta: 10
	},
	showPagination: true,
	sidePanelId: 'sidePanelTestId',
	spritemap: './assets/icons.svg',
	style: 'stacked',
	views: [
		{
			contentRenderer: 'emailsList',
			icon: 'email',
			label: Liferay.Language.get('email')
		}
	]
};

const selectableTableProps = {
	apiUrl: '/dataset-display-selectable-data',
	formId: 'form-id',
	id: 'tableTest',
	pageSize: 5,
	pagination: {
		deltas: [
			{
				label: 5
			},
			{
				label: 10
			},
			{
				label: 20
			},
			{
				label: 30
			},
			{
				label: 50
			},
			{
				href:
					'http://localhost:8080/group/test-1/pending-orders?p_p_id=com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&_com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet_delta=75',
				label: 75
			}
		],
		initialDelta: 10
	},
	selectedItemsKey: 'countryId',
	showPagination: true,
	sidePanelId: 'sidePanelTestId',
	spritemap: './assets/icons.svg',
	views: [
		{
			contentRenderer: 'selectableTable',
			icon: 'table',
			label: Liferay.Language.get('table'),
			schema: {
				firstColumnLabel: Liferay.Language.get('country'),
				firstColumnName: 'countryName'
			}
		}
	]
};

const headlessDataSetDisplayProps = {
	apiUrl: '/o/headless-commerce-admin-catalog/v1.0/products',
	bulkActions: [
		{
			href: '/side-panel/edit.html',
			icon: 'plus',
			label: Liferay.Language.get('add'),
			target: 'sidePanel'
		},
		{
			href: '/delete',
			icon: 'trash',
			label: Liferay.Language.get('delete'),
			method: 'delete'
		}
	],
	creationMenuItems: [
		{
			href: '/standard/edit',
			label: Liferay.Language.get('add')
		},
		{
			href: 'modal/url',
			label: Liferay.Language.get('add-via-modal'),
			target: 'modal'
		}
	],
	filters: [
		{
			apiUrl: '/o/headless-commerce-admin-catalog/v1.0/products',
			id: 'productId',
			inputPlaceholder: Liferay.Language.get('search-for-products'),
			itemKey: 'productId',
			itemLabel: ['name', lang_id],
			label: Liferay.Language.get('product-id'),
			selectionType: 'single',
			type: 'autocomplete'
		}
	],
	id: 'tableTest',
	itemsActions: [
		{
			href: '/edit/{productId}',
			icon: 'pencil',
			label: 'Edit'
		},
		{
			href: '/delete/{productId}',
			icon: 'trash',
			label: 'Delete',
			method: 'delete',
			target: 'async'
		}
	],
	pageSize: 5,
	pagination: {
		deltas: [
			{
				label: 5
			},
			{
				label: 10
			},
			{
				label: 20
			},
			{
				label: 30
			},
			{
				label: 50
			},
			{
				href:
					'http://localhost:8080/group/test-1/pending-orders?p_p_id=com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&_com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet_delta=75',
				label: 75
			}
		],
		initialDelta: 10
	},
	selectedItemsKey: 'productId',
	showPagination: true,
	sidePanelId: 'sidePanelTestId',
	spritemap: './assets/icons.svg',
	views: [
		{
			contentRenderer: 'table',
			icon: 'table',
			label: Liferay.Language.get('table'),
			schema: {
				fields: [
					{
						contentRenderer: 'actionLink',
						fieldName: ['name', 'LANG'],
						label: Liferay.Language.get('name'),
						sortable: true
					},
					{
						fieldName: 'productType',
						label: Liferay.Language.get('product-type'),
						mapData: value => value.toUpperCase()
					},
					{
						fieldName: 'externalReferenceCode',
						label: Liferay.Language.get('sku')
					},
					{
						contentRenderer: 'date',
						fieldName: 'modifiedDate',
						label: Liferay.Language.get('modified-date')
					},
					{
						contentRenderer: 'label',
						fieldName: 'active',
						label: Liferay.Language.get('status'),
						mapData: value =>
							value
								? {
										displayStyle: 'success',
										label: Liferay.Language.get('active')
								  }
								: {
										displayStyle: 'danger',
										label: Liferay.Language.get('disabled')
								  }
					}
				]
			}
		}
	]
};

const today = new Date();

const ordersDataSetDisplayProps = {
	apiUrl:
		'/o/headless-commerce-admin-order/v1.0/orders?nestedFields=account,channel',
	bulkActions: [
		{
			bodyKeys: ['id'],
			href: '/o/headless-commerce-admin-order/v1.0/orders/0/batch',
			icon: 'trash',
			label: Liferay.Language.get('delete'),
			method: 'delete'
		}
	],
	creationMenuItems: [
		{
			href: 'modal/url',
			label: Liferay.Language.get('add'),
			target: 'modal'
		}
	],
	filters: [
		{
			apiUrl: '/o/headless-commerce-admin-account/v1.0/accounts',
			id: 'accountId',
			inputPlaceholder: Liferay.Language.get('search-for-account'),
			itemKey: 'id',
			itemLabel: 'name',
			label: Liferay.Language.get('account-id'),
			selectionType: 'single',
			type: 'autocomplete'
		},
		{
			apiUrl: '/o/headless-commerce-admin-channel/v1.0/channels',
			id: 'channelId',
			inputPlaceholder: Liferay.Language.get('search-for-channel'),
			itemKey: 'id',
			itemLabel: 'name',
			label: Liferay.Language.get('channel-id'),
			selectionType: 'single',
			type: 'autocomplete'
		},
		{
			id: 'createDate',
			label: Liferay.Language.get('order-range'),
			max: {
				day: today.getDate(),
				month: today.getMonth() + 1,
				year: today.getFullYear()
			},
			min: {
				day: today.getDate(),
				month: today.getMonth() + 1,
				year: today.getFullYear() - 1
			},
			placeholder: 'dd/mm/yyyy',
			type: 'dateRange'
		},
		{
			id: 'orderStatus',
			items: [
				{
					label: Liferay.Language.get('completed'),
					value: 1
				},
				{
					label: Liferay.Language.get('not-completed'),
					value: 999
				}
			],
			label: Liferay.Language.get('status'),
			operator: 'eq',
			type: 'radio'
		}
	],
	id: 'tableTest',
	itemsActions: [
		{
			href: '/view/{id}',
			icon: 'view',
			id: 'view',
			label: Liferay.Language.get('view')
		},
		{
			href: '/o/headless-commerce-admin-order/v1.0/orders/{id}',
			icon: 'trash',
			id: 'delete',
			label: Liferay.Language.get('delete'),
			method: 'delete',
			target: 'async'
		}
	],
	pageSize: 5,
	pagination: {
		deltas: [
			{
				label: 5
			},
			{
				label: 10
			},
			{
				label: 20
			},
			{
				label: 30
			},
			{
				label: 50
			},
			{
				href:
					'http://localhost:8080/group/test-1/pending-orders?p_p_id=com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&_com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet_delta=75',
				label: 75
			}
		],
		initialDelta: 10
	},
	selectedItemsKey: 'id',
	showPagination: true,
	sidePanelId: 'sidePanelTestId',
	sorting: [
		{
			direction: 'desc',
			key: 'createDate'
		}
	],
	spritemap: './assets/icons.svg',
	views: [
		{
			contentRenderer: 'table',
			icon: 'table',
			label: Liferay.Language.get('table'),
			schema: {
				fields: [
					{
						actionId: 'view',
						contentRenderer: 'actionLink',
						fieldName: 'id',
						label: 'order-id'
					},
					{
						fieldName: ['account', 'name'],
						label: 'account'
					},
					{
						fieldName: ['channel', 'name'],
						label: 'channel'
					},
					{
						fieldName: 'totalFormatted',
						label: 'amount'
					},
					{
						contentRenderer: 'date',
						fieldName: 'createDate',
						label: Liferay.Language.get('creation-date'),
						sortable: true
					},
					{
						contentRenderer: 'date',
						fieldName: 'modifiedDate',
						label: Liferay.Language.get('modification-date'),
						sortable: true
					},
					{
						contentRenderer: 'status',
						fieldName: 'orderStatusInfo',
						label: Liferay.Language.get('status')
					},
					{
						contentRenderer: 'status',
						fieldName: 'workflowStatusInfo',
						label: Liferay.Language.get('workflow-status')
					}
				]
			}
		}
	]
};

const productsDataSetDisplayProps = {
	apiUrl:
		'/o/headless-commerce-admin-catalog/v1.0/products?nestedFields=skus,catalog',
	bulkActions: [
		{
			href: '/delete',
			icon: 'trash',
			label: Liferay.Language.get('delete'),
			method: 'delete'
		}
	],
	creationMenuItems: [
		{
			href: 'modal/url',
			label: Liferay.Language.get('add-product'),
			target: 'modal'
		}
	],
	filters: [
		{
			id: 'createDate',
			label: Liferay.Language.get('creation-date'),
			max: {
				day: today.getDate(),
				month: today.getMonth() + 1,
				year: today.getFullYear()
			},
			min: {
				day: today.getDate(),
				month: today.getMonth() + 1,
				year: today.getFullYear() - 10
			},
			placeholder: 'dd/mm/yyyy',
			type: 'dateRange'
		},
		{
			apiUrl:
				'/o/headless-admin-taxonomy/v1.0/taxonomy-categories/0/taxonomy-categories',
			id: 'categoryIds',
			inputPlaceholder: Liferay.Language.get('search-for-category'),
			itemKey: 'id',
			itemLabel: 'name',
			label: Liferay.Language.get('category'),
			type: 'autocomplete'
		},
		{
			apiUrl: '/o/headless-commerce-admin-catalog/v1.0/catalogs',
			id: 'catalogId',
			inputPlaceholder: Liferay.Language.get('search-for-catalog'),
			itemKey: 'id',
			itemLabel: 'name',
			label: Liferay.Language.get('catalog'),
			selectionType: 'single',
			type: 'autocomplete'
		},
		{
			id: 'productType',
			items: [
				{
					label: Liferay.Language.get('simple'),
					value: 'simple'
				},
				{
					label: Liferay.Language.get('multiple'),
					value: 'multiple'
				}
			],
			label: Liferay.Language.get('product-type'),
			operator: 'eq',
			type: 'radio'
		}
	],
	id: 'tableTest',
	itemsActions: [
		{
			href: '/page/{id}',
			icon: 'view',
			id: 'view',
			label: Liferay.Language.get('view')
		},
		{
			href:
				'/o/headless-commerce-admin-catalog/v1.0/products/{productId}',
			icon: 'trash',
			id: 'delete',
			label: Liferay.Language.get('delete'),
			method: 'delete',
			target: 'async'
		}
	],
	pageSize: 5,
	pagination: {
		deltas: [
			{
				label: 5
			},
			{
				label: 10
			},
			{
				label: 20
			},
			{
				label: 30
			},
			{
				label: 50
			},
			{
				href:
					'http://localhost:8080/group/test-1/pending-orders?p_p_id=com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&_com_liferay_commerce_order_content_web_internal_portlet_CommerceOpenOrderContentPortlet_delta=75',
				label: 75
			}
		],
		initialDelta: 10
	},
	selectedItemsKey: 'id',
	showPagination: true,
	sidePanelId: 'sidePanelTestId',
	sorting: [
		{
			direction: 'desc',
			key: 'modifiedDate'
		}
	],
	spritemap: './assets/icons.svg',
	views: [
		{
			contentRenderer: 'table',
			icon: 'table',
			label: Liferay.Language.get('table'),
			schema: {
				fields: [
					{
						contentRenderer: 'image',
						fieldName: 'thumbnail',
						labelKey: ['name', 'LANG']
					},
					{
						actionId: 'view',
						contentRenderer: 'actionLink',
						fieldName: ['name', 'LANG'],
						label: Liferay.Language.get('name'),
						sortable: true
					},
					{
						fieldName: 'productType',
						label: Liferay.Language.get('product-type')
					},
					{
						contentRenderer: 'list',
						fieldName: 'skus',
						label: Liferay.Language.get('sku'),
						labelKey: 'sku',
						multipleItemsLabel: Liferay.Language.get(
							'multiple-skus'
						)
					},
					{
						fieldName: ['catalog', 'name'],
						label: Liferay.Language.get('catalog')
					},
					{
						contentRenderer: 'date',
						fieldName: 'createDate',
						label: Liferay.Language.get('created-date'),
						sortable: true
					},
					{
						contentRenderer: 'date',
						fieldName: 'modifiedDate',
						label: Liferay.Language.get('modified-date'),
						sortable: true
					},
					{
						contentRenderer: 'status',
						fieldName: 'workflowStatusInfo',
						label: Liferay.Language.get('status')
					}
				]
			}
		}
	]
};

datasetDisplayLauncher(
	'orders-dataset-display',
	'orders-dataset-display-root',
	ordersDataSetDisplayProps
);

datasetDisplayLauncher(
	'products-dataset-display',
	'products-dataset-display-root',
	productsDataSetDisplayProps
);

datasetDisplayLauncher(
	'headless-dataset-display',
	'headless-dataset-display-root',
	headlessDataSetDisplayProps
);

datasetDisplayLauncher(
	'fluid-dataset-display',
	'fluid-dataset-display-root',
	fluidDataSetDisplayProps
);

datasetDisplayLauncher(
	'selectable-dataset-display',
	'selectable-dataset-display-root',
	selectableTableProps
);

datasetDisplayLauncher(
	'emails-dataset-display',
	'emails-dataset-display-root',
	emailsDataSetDisplayProps
);

sidePanelLauncher('sidePanel', 'side-panel-root', {
	containerSelector: '.container',
	id: 'sidePanelTestId',
	items: [
		{
			href: '/side-panel/comments.html',
			icon: 'comments',
			slug: 'comments'
		},
		{
			href: '/side-panel/edit.html',
			icon: 'pencil',
			slug: 'edit'
		},
		{
			href: '/side-panel/changelog.html',
			icon: 'restore',
			slug: 'changelog'
		}
	],
	size: 'md',
	spritemap: './assets/icons.svg',
	topAnchorSelector: '.top-anchor'
});
