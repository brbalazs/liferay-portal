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

const fluidDataSetDisplayProps = {
	activeView: 2,
	apiUrl: '/dataset-display-nested-items',
	bulkActions: [
		{
			href: '/side-panel/edit.html',
			icon: 'plus',
			label: 'Add',
			target: 'sidePanel'
		},
		{
			href: '/delete',
			icon: 'trash',
			label: 'Delete',
			method: 'delete'
		}
	],
	creationMenuItems: [
		{
			href: 'modal/url',
			label: 'Add',
			target: 'modal'
		}
	],
	filters: [
		{
			id: 'number-test',
			inputText: '$',
			label: 'Number',
			max: 200,
			min: 20,
			operator: 'eq',
			type: 'number',
			value: 123
		},
		{
			id: 'order-date',
			label: 'Order Range',
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
			label: 'Cards',
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
			label: 'Table',
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
			label: 'Add'
		},
		{
			href: 'modal/url',
			label: 'Add via modal',
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
			label: 'Email'
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
			label: 'Table',
			schema: {
				firstColumnLabel: 'Country',
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
			label: 'Add',
			target: 'sidePanel'
		},
		{
			href: '/delete',
			icon: 'trash',
			label: 'Delete',
			method: 'delete'
		}
	],
	creationMenuItems: [
		{
			href: '/standard/edit',
			label: 'Add'
		},
		{
			href: 'modal/url',
			label: 'Add-via-modal',
			target: 'modal'
		}
	],
	filters: [
		{
			apiUrl: '/o/headless-commerce-admin-catalog/v1.0/products',
			id: 'productId',
			inputPlaceholder: 'Search for Products',
			itemKey: 'productId',
			itemLabel: ['name', 'LANG'],
			label: 'Product',
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
			label: 'Table',
			schema: {
				fields: [
					{
						contentRenderer: 'actionLink',
						fieldName: ['name', 'LANG'],
						label: 'Name',
						sortable: true
					},
					{
						fieldName: 'productType',
						label: 'Product Type',
						mapData: value => value.toUpperCase()
					},
					{
						fieldName: 'externalReferenceCode',
						label: 'Sku'
					},
					{
						contentRenderer: 'date',
						fieldName: 'modifiedDate',
						label: 'Modified-date'
					},
					{
						contentRenderer: 'label',
						fieldName: 'active',
						label: 'Status',
						mapData: value =>
							value
								? {
										displayStyle: 'success',
										label: 'Active'
								  }
								: {
										displayStyle: 'danger',
										label: 'Disabled'
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
	batchTasksStatusApiUrl: '/o/fake-batch-engine/v1.0/import-task',
	bulkActions: [
		{
			bodyKeys: ['id', 'productId'],
			href: '/o/fake-bulk-action/v1.0/products/0/batch',
			icon: 'trash',
			label: 'Delete',
			method: 'delete',
			target: 'async'
		}
	],
	creationMenuItems: [
		{
			href: 'modal/url',
			label: 'Add',
			target: 'modal'
		}
	],
	filters: [
		{
			apiUrl: '/o/headless-commerce-admin-account/v1.0/accounts',
			id: 'accountId',
			inputPlaceholder: 'Search for account',
			itemKey: 'id',
			itemLabel: 'name',
			label: 'Account',
			selectionType: 'single',
			type: 'autocomplete'
		},
		{
			apiUrl: '/o/headless-commerce-admin-channel/v1.0/channels',
			id: 'channelId',
			inputPlaceholder: 'Search for Channel',
			itemKey: 'id',
			itemLabel: 'name',
			label: 'Channel',
			selectionType: 'single',
			type: 'autocomplete'
		},
		{
			id: 'createDate',
			label: 'Order Range',
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
					label: 'Completed',
					value: 1
				},
				{
					label: 'Not-completed',
					value: 999
				}
			],
			label: 'Status',
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
			label: 'View'
		},
		{
			href: '/o/headless-commerce-admin-order/v1.0/orders/{id}',
			icon: 'trash',
			id: 'delete',
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
			label: 'Table',
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
						label: 'Creation Date',
						sortable: true
					},
					{
						contentRenderer: 'date',
						fieldName: 'modifiedDate',
						label: 'Modification Date',
						sortable: true
					},
					{
						contentRenderer: 'status',
						fieldName: 'orderStatusInfo',
						label: 'Status'
					},
					{
						contentRenderer: 'status',
						fieldName: 'workflowStatusInfo',
						label: 'Workflow Status'
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
			label: 'Delete',
			method: 'delete'
		}
	],
	creationMenuItems: [
		{
			href: 'modal/url',
			label: 'Add Product',
			target: 'modal'
		}
	],
	filters: [
		{
			id: 'blbl',
			label: 'Custom Filter',
			moduleUrl: '/blblasd/asd/basdkj'
		},
		{
			id: 'createDate',
			label: 'Creation date',
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
			inputPlaceholder: 'Search for Category',
			itemKey: 'id',
			itemLabel: 'name',
			label: 'Category',
			type: 'autocomplete'
		},
		{
			apiUrl: '/o/headless-commerce-admin-catalog/v1.0/catalogs',
			id: 'catalogId',
			inputPlaceholder: 'Search for Catalog',
			itemKey: 'id',
			itemLabel: 'name',
			label: 'Catalog',
			selectionType: 'single',
			type: 'autocomplete'
		},
		{
			id: 'productType',
			items: [
				{
					label: 'Simple',
					value: 'simple'
				},
				{
					label: 'Multiple',
					value: 'multiple'
				}
			],
			label: 'Product Type',
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
			label: 'View',
			permissionKey: 'get'
		},
		{
			href:
				'/o/headless-commerce-admin-catalog/v1.0/products/{productId}',
			icon: 'trash',
			id: 'delete',
			label: 'Delete',
			method: 'delete',
			permissionKey: 'delete',
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
			label: 'Table',
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
						label: 'Name',
						sortable: true
					},
					{
						fieldName: 'productType',
						label: 'Product Type'
					},
					{
						contentRenderer: 'list',
						fieldName: 'skus',
						label: 'Sku',
						labelKey: 'sku',
						multipleItemsLabel: 'Multiple-skus'
					},
					{
						fieldName: ['catalog', 'name'],
						label: 'Catalog'
					},
					{
						contentRenderer: 'date',
						fieldName: 'createDate',
						label: 'Created Date',
						sortable: true
					},
					{
						contentRenderer: 'date',
						fieldName: 'modifiedDate',
						label: 'Modified Date',
						sortable: true
					},
					{
						contentRenderer: 'status',
						fieldName: 'workflowStatusInfo',
						label: 'Status'
					}
				]
			}
		}
	]
};

const dndDataSetDisplayProps = {
	activeView: 2,
	apiUrl:
		'http://localhost:8080/o/commerce-ui/commerce-data-set/20124/commerceOrderItems/commerceOrderItems?plid=1&portletId=com_liferay_commerce_order_web_internal_portlet_CommerceOrderPortlet&commerceOrderId=38938',
	creationMenuItems: [
		{
			href: '/standard/edit',
			label: 'Add'
		},
		{
			href: 'modal/url',
			label: 'Add via modal',
			target: 'modal'
		}
	],
	enableDragDrop: true,
	filters: [
		{
			id: 'text-test',
			label: 'Text test',
			operator: 'eq',
			type: 'text',
			value: 'Test input'
		},
		{
			id: 'select-test',
			items: [
				{
					label: 'First option',
					value: 'first-option'
				},
				{
					label: 'Second option',
					value: 'second-option'
				}
			],
			label: 'Select test',
			operator: 'eq',
			type: 'select',
			value: 'second-option'
		},
		{
			id: 'radio-test',
			items: [
				{
					label: 'First option',
					value: 'first-option'
				},
				{
					label: 'Second option',
					value: 'second-option'
				}
			],
			label: 'Radio test',
			operator: 'eq',
			type: 'radio'
		},
		{
			id: 'checkbox-test',
			items: [
				{
					label: 'First option',
					value: 'first-option'
				},
				{
					label: 'Second option',
					value: 'second-option'
				},
				{
					label: 'Third option',
					value: 'third-option'
				}
			],
			label: 'Checkbox test',
			operator: 'eq',
			type: 'checkbox',
			value: ['first-option', 'third-option']
		},
		{
			id: 'number-test',
			inputText: '$',
			label: 'Number test',
			max: 200,
			min: 20,
			operator: 'gt',
			type: 'number',
			value: 123
		}
	],
	formId: 'form-id',
	id: 'tableTest',
	items: [
		{
			actionItems: [
				{
					cssClasses: '',
					href:
						'http://localhost:8080/group/guest/~/control_panel/manage?p_p_id=com_liferay_commerce_order_web_internal_portlet_CommerceOrderPortlet&p_p_lifecycle=0&p_p_state=maximized&_com_liferay_commerce_order_web_internal_portlet_CommerceOrderPortlet_javax.portlet.action=editCommerceOrderItem&_com_liferay_commerce_order_web_internal_portlet_CommerceOrderPortlet_cmd=delete&_com_liferay_commerce_order_web_internal_portlet_CommerceOrderPortlet_redirect=%2Fgroup%2Fguest%2F%7E%2Fcontrol_panel%2Fmanage%3Fp_p_id%3Dcom_liferay_commerce_order_web_internal_portlet_CommerceOrderPortlet%26p_p_lifecycle%3D0%26p_p_state%3Dmaximized%26p_p_mode%3Dview%26_com_liferay_commerce_order_web_internal_portlet_CommerceOrderPortlet_redirect%3Dhttp%253A%252F%252Flocalhost%253A8080%252Fgroup%252Fguest%252F%7E%252Fcontrol_panel%252Fmanage%253Fp_p_id%253Dcom_liferay_commerce_order_web_internal_portlet_CommerceOrderPortlet%2526p_p_lifecycle%253D0%2526p_p_state%253Dmaximized%2526p_p_mode%253Dview%2526p_p_auth%253DVLNBjvR0%26_com_liferay_commerce_order_web_internal_portlet_CommerceOrderPortlet_mvcRenderCommandName%3DeditCommerceOrder%26_com_liferay_commerce_order_web_internal_portlet_CommerceOrderPortlet_commerceOrderId%3D38938%26p_p_auth%3DVLNBjvR0&_com_liferay_commerce_order_web_internal_portlet_CommerceOrderPortlet_commerceOrderItemId=38943&p_p_auth=VLNBjvR0',
					icon: 'trash',
					label: 'Delete',
					onClick: '',
					order: 0,
					quickAction: false,
					separator: false
				}
			],
			bookedQuantityId: 0,
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 200,
			id: 37175,
			name: 'ABS Sensor',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 4,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN93015'
			},
			skuExternalReferenceCode: 'min93015',
			skuId: 35663,
			someOrderableField: 13.0,
			status: {
				displayStyle: 'info',
				label: 'delivered'
			},
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 50
		},
		{
			bookedQuantityId: 0,
			comments: {
				name: 'Square pls',
				quantity: "This is a test! I don't like this number btw"
			},
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 304,
			id: 37176,
			name: 'Ball Joints',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 2,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN38794'
			},
			skuExternalReferenceCode: 'min38794',
			skuId: 36456,
			someOrderableField: 12.0,
			status: {
				displayStyle: 'secondary',
				label: 'delivered'
			},
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 152
		},
		{
			bookedQuantityId: 0,
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 70,
			id: 37177,
			name: 'Bearings',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 1,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN00673'
			},
			skuExternalReferenceCode: 'min00673',
			skuId: 36114,
			someOrderableField: 11.0,
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 70
		},
		{
			bookedQuantityId: 0,
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 37.8,
			id: 37178,
			name: 'Brake Pads',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 2,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN93018'
			},
			skuExternalReferenceCode: 'min93018',
			skuId: 35798,
			someOrderableField: 10.0,
			status: {
				displayStyle: 'success',
				label: 'delivered'
			},
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 21
		},
		{
			bookedQuantityId: 0,
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 400,
			id: 37197,
			name: 'Brake Rotors',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 10,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN93020'
			},
			skuExternalReferenceCode: 'min93020',
			skuId: 35872,
			someOrderableField: 9.0,
			status: {
				displayStyle: 'warning',
				label: 'delivered'
			},
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 40
		},
		{
			bookedQuantityId: 0,
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 36,
			id: 37198,
			name: 'Bushings',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 2,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN38795'
			},
			skuExternalReferenceCode: 'min38795',
			skuId: 36474,
			someOrderableField: 8.0,
			status: {
				displayStyle: 'danger',
				label: 'delivered'
			},
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 18
		},
		{
			bookedQuantityId: 0,
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 90,
			id: 37199,
			name: 'Calipers',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 1,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN93021',
				size: 'lg'
			},
			skuExternalReferenceCode: 'min93021',
			skuId: 35900,
			someOrderableField: 7.0,
			status: {
				displayStyle: 'success',
				label: 'delivered'
			},
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 90
		},
		{
			bookedQuantityId: 0,
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 4170,
			id: 37200,
			name: 'Cams',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 6,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN00674',
				size: 'sm'
			},
			skuExternalReferenceCode: 'min00674',
			skuId: 36132,
			someOrderableField: 6.0,
			status: {
				displayStyle: 'success',
				label: 'delivered'
			},
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 695
		},
		{
			bookedQuantityId: 0,
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 624,
			id: 37201,
			name: 'Coil Spring - Rear',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 6,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN38799'
			},
			skuExternalReferenceCode: 'min38799',
			skuId: 36553,
			someOrderableField: 5.0,
			status: {
				displayStyle: 'success',
				label: 'delivered'
			},
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 104
		},
		{
			bookedQuantityId: 0,
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 623,
			id: 37202,
			name: 'CV Axles',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 7,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN38796'
			},
			skuExternalReferenceCode: 'min38796',
			skuId: 36492,
			someOrderableField: 4.0,
			status: {
				displayStyle: 'success',
				label: 'delivered'
			},
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 89
		},
		{
			bookedQuantityId: 0,
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 1068,
			id: 37203,
			name: 'CV Axles',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 12,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN55853'
			},
			skuExternalReferenceCode: 'min55853',
			skuId: 36700,
			someOrderableField: 3.0,
			status: {
				displayStyle: 'success',
				label: 'delivered'
			},
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 89
		},
		{
			bookedQuantityId: 0,
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 10550,
			id: 37204,
			name: 'Differential Ring and Pinion - Universal',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 50,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN38801'
			},
			skuExternalReferenceCode: 'min38801',
			skuId: 36604,
			someOrderableField: 2.0,
			status: {
				displayStyle: 'success',
				label: 'delivered'
			},
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 211
		},
		{
			bookedQuantityId: 0,
			date: {
				href: '/modal/date/url',
				icon: 'date'
			},
			discountAmount: 0,
			discountPercentageLevel1: 0,
			discountPercentageLevel2: 0,
			discountPercentageLevel3: 0,
			discountPercentageLevel4: 0,
			finalPrice: 396,
			id: 37205,
			name: 'Drive Shafts',
			order: {
				href: '/modal/order/url',
				label: '#37174'
			},
			quantity: 1,
			shippedQuantity: 0,
			shippingAddress: {},
			shippingAddressId: 0,
			sku: {
				href: '/sidepanel-1.html',
				label: 'MIN55855'
			},
			skuExternalReferenceCode: 'min55855',
			skuId: 36744,
			someOrderableField: 1.0,
			status: {
				displayStyle: 'success',
				label: 'delivered'
			},
			subscription: false,
			thumbnail: {
				alt: 'ABS Sensor',
				shape: 'rounded',
				size: 'lg',
				src: 'https://via.placeholder.com/150'
			},
			unitPrice: 396
		}
	],
	selectedItemsKey: 'id',
	selectionType: 'single',
	showPagination: false,
	sidePanelId: 'sidePanelTestId',
	spritemap: './assets/icons.svg',
	views: [
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
			label: 'Custom list'
		},
		{
			contentRendererModuleUrl: '/fake/url',
			icon: 'code',
			label: 'JSON'
		},
		{
			contentRenderer: 'table',
			icon: 'table',
			label: 'Table',
			orderable: true,
			orderableField: 'someOrderableField',
			schema: {
				fields: [
					{
						contentRendererModuleUrl:
							'/fake/content/renderer/picture',
						// contentRenderer: 'image',
						expand: false,
						fieldName: 'thumbnail',
						label: ''
					},
					{
						contentRenderer: 'sidePanelLink',
						fieldName: 'sku',
						label: 'SKU',
						sortable: true
					},
					{
						fieldName: 'name',
						label: 'Name',
						sortable: true
					},
					{
						fieldName: 'unitPrice',
						label: 'Price',
						sortable: true
					},
					{
						contentRenderer: 'modalLink',
						fieldName: 'order',
						label: 'Order'
					},
					{
						contentRenderer: 'label',
						fieldName: 'status',
						label: 'Status'
					},
					{
						fieldName: 'quantity',
						label: 'Quantity',
						sortable: true
					},
					{
						fieldName: 'finalPrice',
						label: 'Total',
						sortable: false
					},
					{
						contentRenderer: 'modalLink',
						fieldName: 'date'
					}
				]
			}
		},
		{
			activeItemValue: 36553,
			contentRenderer: 'list',
			icon: 'list',
			label: 'List',
			schema: {
				description: 'name',
				title: 'skuId'
			}
		}
	]
};

function warehouseNumbersGenerator(warehouse, index) {
	const MAX = 10000,
		MIN = 100;

	const stock = Math.floor(Math.random() * (MAX - MIN + 1) + MIN);

	const committed = Math.floor(Math.random() * (stock / 2 - MIN + 1) + MIN),
		awaiting = Math.floor(Math.random() * (committed / 4) + MIN),
		lowStockThreshold = Math.floor(Math.random() * (stock / 4) + MIN);

	const available = stock - committed;

	const sourceField = 'warehouse';

	return {
		available,
		awaiting,
		committed,
		id: `id-${index}`,
		lowStockThreshold,
		modalUrl: 'someModalUrl',
		sourceField,
		stock,
		warehouse
	};
}

const warehouses = [
	'Cagnano Varano',
	'Monza',
	'Rotterdam',
	'Istanbul',
	'Tel Aviv'
];

const inventoryDatasetDisplayProps = {
	apiUrl: '',
	creationMenuItems: [
		{
			href: '/standard/edit',
			label: 'Add'
		},
		{
			href: 'modal/url',
			label: 'Add via modal',
			type: 'modal'
		}
	],
	enableDragDrop: true,
	id: 'inventoryDatasetDisplay',
	items: [...warehouses.map(warehouseNumbersGenerator)],
	showPagination: false,
	sidePanelId: '',
	spritemap: './assets/icons.svg',
	views: [
		{
			contentRenderer: 'table',
			icon: 'table',
			label: 'Table',
			schema: {
				fields: [
					{
						fieldName: 'warehouse',
						label: 'Warehouse',
						sortable: true
					},
					{
						contentRenderer: 'draggableDroppable',
						fieldName: 'stock',
						label: 'Stock',
						sortable: true
					},
					{
						fieldName: 'committed',
						label: 'Committed',
						sortable: true
					},
					{
						fieldName: 'available',
						label: 'Available',
						sortable: true
					},
					{
						fieldName: 'awaiting',
						label: 'Awaiting',
						sortable: true
					},
					{
						fieldName: 'lowStockThreshold',
						label: 'Low Stock Threshold',
						sortable: true
					}
				]
			}
		}
	]
};

/**
 * Too many dataset displays generate a React Warning
 * caused by too many State updates (> 25), at the very beginning.
 *
 * Comment out some if you want to remove the error in the console.
 */

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

datasetDisplayLauncher(
	'dnd-dataset-display',
	'dnd-dataset-display-root',
	dndDataSetDisplayProps
);

datasetDisplayLauncher(
	'inventory-dataset-display',
	'inventory-dataset-display-root',
	inventoryDatasetDisplayProps
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
