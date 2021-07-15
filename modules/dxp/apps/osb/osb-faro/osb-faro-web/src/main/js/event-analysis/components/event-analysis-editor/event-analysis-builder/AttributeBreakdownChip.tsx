import AttributeBreakdownDropdown from './attribute-breakdown-dropdown';
import AttributeChip, {DragTypes} from './AttributeChip';
import React from 'react';
import {Attribute, Breakdown} from 'event-analysis/utils/types';
import {DeleteBreakdown, EditBreakdown} from '../context/attributes';
import {getBreakdownDisplay} from 'event-analysis/utils/utils';

const AttributeFilterChipWrapper: React.FC<{
	attribute: Attribute;
	breakdown: Breakdown;
	disabledIds: string[];
	eventId: string;
	index: number;
	onCloseClick: DeleteBreakdown;
	onEditSubmit: EditBreakdown;
	onMove: (params: {from: number; to: number}) => void;
	uneditableIds: string[];
}> = ({
	attribute,
	breakdown,
	disabledIds,
	eventId,
	index,
	onCloseClick,
	onEditSubmit,
	onMove,
	uneditableIds
}) => {
	const [label, value] = getBreakdownDisplay(
		attribute,
		breakdown.attributeType
	);

	return (
		<AttributeBreakdownDropdown
			attribute={attribute}
			breakdown={breakdown}
			disabledIds={disabledIds}
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
			uneditableIds={uneditableIds}
		/>
	);
};
export default AttributeFilterChipWrapper;
