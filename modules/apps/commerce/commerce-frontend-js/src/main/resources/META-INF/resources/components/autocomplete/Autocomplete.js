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

import ClayAutocomplete from '@clayui/autocomplete';
import ClayDropDown from '@clayui/drop-down';
import PropTypes from 'prop-types';
import React, {useState, useEffect, useCallback, useRef} from 'react';

import {
	fetchParams,
	showErrorNotification,
	getSchemaString,
	debounce
} from '../../utilities/index.es';

function Autocomplete(props) {
	const [query, setQuery] = useState('');
	const [label, setLabel] = useState('');
	const [renderingCounter, updateRenderingCounter] = useState(0);
	const [active, setActive] = useState(false);
	const [value, setValue] = useState('');
	const [items, updateItems] = useState(null);
	const [loading, setLoading] = useState(false);
	const node = useRef();
	const dropdownNode = useRef();

	const getData = () => {
		return fetch(`${props.apiUrl}${query ? `?search=${query}` : ''}`, {
			...fetchParams,
			method: 'GET'
		})
			.then(data => data.json())
			.then(jsonResponse => {
				updateItems(jsonResponse.items);
				setLoading(false);
			})
			.catch(() => {
				showErrorNotification();
				setLoading(false);
			});
	};

	const debouncedGetData = debounce(getData, 100);
	const memoizedGetData = useCallback(debouncedGetData, [
		query,
		props.apiUrl
	]);

	useEffect(() => {
		if (renderingCounter > 1) {
			setLoading(true);
			memoizedGetData();
			setActive(true);
		}
	}, [memoizedGetData, renderingCounter, query]);

	useEffect(() => {
		updateRenderingCounter(counter => counter + 1);
	}, [memoizedGetData]);

	const handleClick = e => {
		if (
			node.current.contains(e.target) ||
			(dropdownNode.current && dropdownNode.current.contains(e.target))
		)
			return;

		setActive(false);
	};

	useEffect(() => {
		document.addEventListener('mousedown', handleClick);

		return () => {
			document.removeEventListener('mousedown', handleClick);
		};
	}, []);

	return (
		<ClayAutocomplete ref={node}>
			<input name={props.inputName} type="hidden" value={value || ''} />
			<ClayAutocomplete.Input
				onChange={event => {
					setLabel(null);
					setValue(null);
					if (event.target.value !== query) {
						setQuery(event.target.value);
					}
				}}
				placeholder={props.inputPlaceholder}
				value={label || query}
			/>
			{active && (
				<ClayAutocomplete.DropDown active={active}>
					<div className="autocomplete-items" ref={dropdownNode}>
						<ClayDropDown.ItemList className="mb-0">
							{items && items.length === 0 && (
								<ClayDropDown.Item className="disabled">
									{Liferay.Language.get('no-results-found')}
								</ClayDropDown.Item>
							)}
							{items &&
								items.length > 0 &&
								items.map(item => (
									<ClayAutocomplete.Item
										key={String(item[props.itemsKey])}
										onClick={() => {
											setValue(item[props.itemsKey]);
											setLabel(
												getSchemaString(
													item,
													props.itemsLabel
												)
											);
											setActive(false);
										}}
										value={String(
											getSchemaString(
												item,
												props.itemsLabel
											)
										)}
									/>
								))}
						</ClayDropDown.ItemList>
					</div>
				</ClayAutocomplete.DropDown>
			)}
			{loading && <ClayAutocomplete.LoadingIndicator />}
		</ClayAutocomplete>
	);
}

Autocomplete.propTypes = {
	apiUrl: PropTypes.string.isRequired,
	inputName: PropTypes.string.isRequired,
	inputPlaceholder: PropTypes.string,
	itemsKey: PropTypes.string.isRequired,
	itemsLabel: PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.arrayOf(PropTypes.string)
	]).isRequired
};

Autocomplete.defaultProps = {
	inputPlaceholder: Liferay.Language.get('type-here')
};

export default Autocomplete;
