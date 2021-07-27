import AttributeChip, {DragTypes} from './AttributeChip';
import AttributeFilterDropdown from './attribute-filter-dropdown';
import React from 'react';
import {Attribute, Filter} from 'event-analysis/utils/types';
import {DeleteFilter} from '../context/attributes';
import {getFilterDisplay} from 'event-analysis/utils/utils';

const AttributeFilterChip: React.FC<{
	attribute: Attribute;
	eventId: string;
	filter: Filter;
	index: number;
	onCloseClick: DeleteFilter;
	onMove: (params: {from: number; to: number}) => void;
}> = ({attribute, eventId, filter, index, onCloseClick, onMove}) => {
	const [label, value] = getFilterDisplay(attribute, filter);

	return (
		<AttributeFilterDropdown
			attribute={attribute}
			eventId={eventId}
			filter={filter}
			trigger={
				<AttributeChip
					dataType={filter.dataType}
					dragType={DragTypes.AttributeFilterChip}
					id={filter.id}
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

export default AttributeFilterChip;
