import AttributeChip from './AttributeChip';
import AttributeFilterDropdown from './attribute-filter-dropdown';
import React from 'react';
import {Attribute, Filter} from 'event-analysis/utils/types';
import {DeleteFilter, EditFilter} from '../context/attributes';
import {getFilterDisplay} from 'event-analysis/utils/utils';

const AttributeFilterChip: React.FC<{
	attribute: Attribute;
	eventId: string;
	filter: Filter;
	index: number;
	onCloseClick: DeleteFilter;
	onEditSubmit: EditFilter;
	onMove: (params: {from: number; to: number}) => void;
	order: string[];
}> = ({
	attribute,
	eventId,
	filter,
	index,
	onCloseClick,
	onEditSubmit,
	onMove,
	order
}) => {
	const [label, value] = getFilterDisplay(attribute, filter);

	return (
		<AttributeFilterDropdown
			attribute={attribute}
			disabledIds={order}
			eventId={eventId}
			filter={filter}
			onAttributeSelect={onEditSubmit}
			trigger={
				<AttributeChip
					dataType={filter.dataType}
					id={filter.attributeId}
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
