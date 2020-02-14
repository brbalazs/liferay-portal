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
		<>
			<div className="col-6 col-md-9">
				<p className="m-0">{props.label}</p>
			</div>
			<div className="col-6 col-md-3">
				<p className="m-0">{props.value}</p>
			</div>
		</>
	);
}

SummaryItemBase.propTypes = baseItemDefaultProps;

function SummaryItemBigVariant(props) {
	return (
		<>
			<div className="col-6 col-md-9">
				<h3 className="my-2">{props.label}</h3>
			</div>
			<div className="col-6 col-md-3">
				<h3 className="my-2">{props.value}</h3>
			</div>
		</>
	);
}

SummaryItemBigVariant.propTypes = baseItemDefaultProps;

function SummaryItemDangerVariant(props) {
	return (
		<>
			<div className="col-6 col-md-9 text-danger">
				<p className="m-0">{props.label}</p>
			</div>
			<div className="col-6 col-md-3 text-danger">
				<p className="m-0">{props.value}</p>
			</div>
		</>
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
		function refreshItems(payload) {
			if (
				!props.datasetDisplayId ||
				!props.apiUrl ||
				payload.id !== props.datasetDisplayId
			) {
				return;
			}
			fetch(props.apiUrl, {
				credentials: 'include',
				headers: new Headers({'x-csrf-token': Liferay.authToken}),
				method: 'GET'
			})
				.then(data => data.json())
				.then(updateItems);
		}

		Liferay.on(DATASET_DISPLAY_UPDATED, refreshItems);
		return Liferay.detach(DATASET_DISPLAY_UPDATED, refreshItems);
	}, [props.apiUrl, props.datasetDisplayId]);

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
	datasetDisplayId: PropTypes.string,
	items: PropTypes.array.isRequired
};

Summary.defaultProps = {
	items: []
};

export default Summary;
