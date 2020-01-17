import React from 'react';
import { useState } from 'react';
import Proptypes from 'prop-types';
import ClayButton from '@clayui/button';
import ClayIcon from '@clayui/icon';
import ClayTooltip from '@clayui/tooltip';

function TooltipPrice(props) {
	const [ visible, setVisible ] = useState(false);

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
			{
				(props.value.details && visible) || true ? (
					<ClayTooltip show>
						<table className="tooltip-table">
							{props.value.details.map((detail, i) => (
								<tr key={i}>
									<td className="table-column-text-start">{detail.label}</td>
									<td className="table-column-text-end">
										{
											detail.value instanceof Array 
												? detail.value.join(' | ')
												: detail.value
										}
									</td>
								</tr>
							))}
						</table>
					</ClayTooltip>
				) : null
			}
		</>
	)
}

TooltipPrice.propTypes = {
	value: Proptypes.shape({
		details: Proptypes.arrayOf(Proptypes.oneOf([
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
		])),
		final: Proptypes.string.isRequired,
	})
}

export default TooltipPrice;
