import AttributeBreakdownDropdown from './attribute-breakdown-dropdown';
import AttributeChip, {DragTypes} from './AttributeChip';
import React from 'react';
import {Attribute, Breakdown} from 'event-analysis/utils/types';
import {DeleteBreakdown, EditBreakdown} from '../context/attributes';
import {getBreakdownDisplay} from 'event-analysis/utils/utils';

const AttributeFilterChipWrapper: React.FC<{
	attribute: Attribute;
	breakdown: Breakdown;
	eventId: string;
	index: number;
	onCloseClick: DeleteBreakdown;
	onEditSubmit: EditBreakdown;
	onMove: (params: {from: number; to: number}) => void;
	order: string[];
}> = ({
	attribute,
	breakdown,
	eventId,
	index,
	onCloseClick,
	onEditSubmit,
	onMove,
	order
}) => {
	const [label, value] = getBreakdownDisplay(attribute, breakdown.type);

	return (
		<AttributeBreakdownDropdown
			attribute={attribute}
			breakdown={breakdown}
			disabledIds={order}
			eventId={eventId}
			onAttributeSelect={onEditSubmit}
			trigger={
				<AttributeChip
					dataType={breakdown.dataType}
					dragType={DragTypes.AttributeBreakdownChip}
					id={breakdown.id}
					index={index}
					label={label}
					onCloseClick={onCloseClick}
					onMove={onMove}
					value={value}
				/>
			}
		/>
	);
};
export default AttributeFilterChipWrapper;
