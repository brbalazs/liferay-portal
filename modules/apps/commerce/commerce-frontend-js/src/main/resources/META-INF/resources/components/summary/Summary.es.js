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
import React, {useState, useEffect} from 'react';

import {DATASET_DISPLAY_UPDATED} from '../../utilities/eventsDefinitions.es';

function SummaryItemDividerVariant() {
	return (
		<div className="col-12">
			<hr />
		</div>
	);
}

const baseItemDefaultProps = {
	label: PropTypes.string,
	value: PropTypes.string
};

function SummaryItemBase(props) {
	return (
		<React.Fragment>
			<div className="col-6 col-md-9">
				<p className="m-0">{props.label}</p>
			</div>
			<div className="col-6 col-md-3">
				<p className="m-0">{props.value}</p>
			</div>
		</React.Fragment>
	);
}

SummaryItemBase.propTypes = baseItemDefaultProps;

function SummaryItemBigVariant(props) {
	return (
		<React.Fragment>
			<div className="col-6 col-md-9">
				<h3 className="my-2">{props.label}</h3>
			</div>
			<div className="col-6 col-md-3">
				<h3 className="my-2">{props.value}</h3>
			</div>
		</React.Fragment>
	);
}

SummaryItemBigVariant.propTypes = baseItemDefaultProps;

function SummaryItemDangerVariant(props) {
	return (
		<React.Fragment>
			<div className="col-6 col-md-9 text-danger">
				<p className="m-0">{props.label}</p>
			</div>
			<div className="col-6 col-md-3 text-danger">
				<p className="m-0">{props.value}</p>
			</div>
		</React.Fragment>
	);
}

SummaryItemDangerVariant.propTypes = baseItemDefaultProps;

function SummaryItem(props) {
	const {style, ...itemProps} = props;

	let ItemVariant;

	switch (style) {
		case 'big':
			ItemVariant = SummaryItemBigVariant;
			break;
		case 'divider':
			ItemVariant = SummaryItemDividerVariant;
			break;
		case 'danger':
			ItemVariant = SummaryItemDangerVariant;
			break;
		default:
			ItemVariant = SummaryItemBase;
			break;
	}

	return <ItemVariant {...itemProps} />;
}

SummaryItem.propTypes = {
	style: PropTypes.string
};

function Summary(props) {
	const [items, updateItems] = useState(props.items);

	useEffect(() => {
		function getData() {
			fetch(props.apiUrl, {
				credentials: 'include',
				headers: new Headers({'x-csrf-token': Liferay.authToken}),
				method: 'GET'
			})
				.then(data => data.json())
				.then(
					data => (props.dataMapper && props.dataMapper(data)) || data
				)
				.then(updateItems);
		}

		function refreshItems(payload) {
			if (
				!props.datasetDisplayId ||
				!props.apiUrl ||
				payload.id !== props.datasetDisplayId
			) {
				return;
			}
			return getData();
		}

		getData();
		Liferay.on(DATASET_DISPLAY_UPDATED, refreshItems);
		return Liferay.detach(DATASET_DISPLAY_UPDATED, refreshItems);
	}, [props, props.apiUrl, props.datasetDisplayId]);

	return (
		<div className="row summary-table text-right">
			{items.map((item, i) => (
				<SummaryItem key={i} {...item} />
			))}
		</div>
	);
}

Summary.propTypes = {
	apiUrl: PropTypes.string,
	dataMapper: PropTypes.func,
	datasetDisplayId: PropTypes.string,
	items: PropTypes.array.isRequired
};

Summary.defaultProps = {
	dataMapper: jsonData => {
		const values = [
			{
				label: Liferay.Language.get('items-subtotal'),
				value: jsonData.subtotal
			},
			{
				label: Liferay.Language.get('items-subtotal-discount'),
				value: jsonData.subtotalDiscountAmount
			},
			{
				label: Liferay.Language.get('order-discount'),
				value: jsonData.totalDiscountAmount
			},
			{
				label: Liferay.Language.get('promotion-code'),
				value: jsonData.couponCode || '--'
			},
			{
				label: Liferay.Language.get('estimated-tax'),
				value: jsonData.taxAmount
			},
			{
				label: Liferay.Language.get('shipping-and-handing'),
				value: jsonData.shippingAmount
			},
			{
				label: Liferay.Language.get('shipping-and-handing-discount'),
				value: jsonData.shippingDiscountAmount
			},
			{
				style: 'divider'
			},
			{
				label: Liferay.Language.get('shipping-and-handing-discount'),
				style: 'big',
				value: jsonData.total
			}
		];
		return values;
	},
	items: []
};

export default Summary;
