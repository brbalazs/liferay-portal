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
import ClayIcon from '@clayui/icon';
import ClayTooltip from '@clayui/tooltip';
import Proptypes from 'prop-types';
import React, {useState} from 'react';

function TooltipPrice(props) {
	const [visible, setVisible] = useState(false);

	return (
		<>
			{props.value.final}
			<ClayButton
				className="cell-comment text-info px-1 my-n2 inline-item"
				data-tooltip-align="top"
				data-tooltip-delay={0}
				displayType="link"
				onMouseEnter={() => setVisible(true)}
				onMouseLeave={() => setVisible(false)}
			>
				<ClayIcon symbol="info-circle" />
			</ClayButton>
			{props.value.details && visible ? (
				<ClayTooltip show>
					<table className="tooltip-table">
						{props.value.details.map((detail, i) => (
							<tr key={i}>
								<td className="table-column-text-start">
									{detail.label}
								</td>
								<td className="table-column-text-end">
									{detail.value instanceof Array
										? detail.value.join(' | ')
										: detail.value}
								</td>
							</tr>
						))}
					</table>
				</ClayTooltip>
			) : null}
		</>
	);
}

TooltipPrice.propTypes = {
	value: Proptypes.shape({
		details: Proptypes.arrayOf(
			Proptypes.oneOf([
				Proptypes.shape({
					label: Proptypes.string,
					value: Proptypes.string
				}),
				Proptypes.shape({
					label: Proptypes.string,
					value: Proptypes.arrayOf([
						Proptypes.string,
						Proptypes.number
					])
				})
			])
		),
		final: Proptypes.string.isRequired
	})
};

export default TooltipPrice;
