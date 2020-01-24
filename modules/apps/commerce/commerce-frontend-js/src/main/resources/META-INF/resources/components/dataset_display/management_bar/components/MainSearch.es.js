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

import Icon from '@clayui/icon';
import classNames from 'classnames';
import React, {useState, useEffect} from 'react';

import getAppContext from './Context.es';

function MainSearch() {
	const {actions, state} = getAppContext();
	const mainFilter = state.filters.find(f => f.main);

	const [inputValue, updateInputValue] = useState(
		(mainFilter && mainFilter.value) || ''
	);

	useEffect(() => {
		updateInputValue(mainFilter.value || '');
	}, [mainFilter.value]);

	return (
		<div className="d-inline">
			<form
				onSubmit={e => {
					e.preventDefault();
					actions.updateFilterValue(mainFilter.id, inputValue);
				}}
				role="search"
			>
				<div className="input-group">
					<div className="input-group-item">
						<input
							className="form-control input-group-inset input-group-inset-after"
							onChange={e => updateInputValue(e.target.value)}
							placeholder={
								mainFilter.placeholder ||
								Liferay.Language.get('search-for')
							}
							type="text"
							value={inputValue}
						/>
						<span className="input-group-inset-item input-group-inset-item-after">
							<button
								className={classNames(
									'btn btn-unstyled',
									!inputValue.length && 'invisible'
								)}
								disabled={!inputValue.length}
								onClick={() => updateInputValue('')}
								type="button"
							>
								<Icon symbol="times-circle" />
							</button>
							<button
								className="btn btn-unstyled"
								onSubmit={e => {
									e.preventDefault();
									actions.updateFilterValue(
										mainFilter.id,
										inputValue
									);
								}}
							>
								<Icon symbol="search" />
							</button>
						</span>
					</div>
				</div>
			</form>
		</div>
	);
}

export default MainSearch;
