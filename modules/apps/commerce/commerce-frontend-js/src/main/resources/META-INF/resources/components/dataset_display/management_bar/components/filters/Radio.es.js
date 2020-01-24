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
import {ClayRadio, ClayRadioGroup} from '@clayui/form';
import React, {useState} from 'react';

import getAppContext from '../Context.es';

function RadioFilter(props) {
	const {actions} = getAppContext();
	const [value, setValue] = useState(props.value);

	return (
		<>
			<ClayRadioGroup
				onSelectedValueChange={setValue}
				selectedValue={value || ''}
			>
				{props.items.map(item => (
					<ClayRadio
						key={item.value}
						label={item.label}
						value={item.value}
					/>
				))}
			</ClayRadioGroup>
			<div className="mt-2">
				<ClayButton
					className="btn-sm"
					disabled={value === props.value}
					onClick={() => actions.updateFilterValue(props.id, value)}
				>
					{props.panelType === 'edit'
						? Liferay.Language.get('edit-filter')
						: Liferay.Language.get('add-filter')}
				</ClayButton>
			</div>
		</>
	);
}

export default RadioFilter;
