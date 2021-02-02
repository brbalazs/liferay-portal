import BreakdownSection from './BreakdownSection';
import EventSection from './EventSection';
import React from 'react';
import {Attribute, Breakdown, Event, Filter} from '../types';

interface IEventAnalysisBuilderProps {
	attributes: Attribute[];
	breakdowns: Breakdown[];
	event?: Event;
	filters: Filter[];
	onAttributesChange: (attributes: Attribute[]) => void;
	onBreakdownsChange: (breakdowns: Breakdown[]) => void;
	onEventChange: (event: Event) => void;
	onFiltersChange: (filters: Filter[]) => void;
}

const EventAnalysisBuilder: React.FC<IEventAnalysisBuilderProps> = ({
	attributes,
	breakdowns,
	event,
	filters,
	onAttributesChange,
	onBreakdownsChange,
	onEventChange,
	onFiltersChange
}) => {
	const handleEventChange = (event?: Event) => {
		onAttributesChange([]);
		onBreakdownsChange([]);
		onEventChange(event);
		onFiltersChange([]);
	};

	return (
		<div className='event-analysis-builder-root d-flex'>
			<EventSection event={event} onEventChange={handleEventChange} />

			{event && (
				<BreakdownSection
					attributes={attributes}
					breakdowns={breakdowns}
					filters={filters}
					onAttributesChange={onAttributesChange}
					onBreakdownsChange={onBreakdownsChange}
					onFiltersChange={onFiltersChange}
				/>
			)}
		</div>
	);
};

export default EventAnalysisBuilder;
