import Button from 'shared/components/Button';
import EventChip from './EventChip';
import EventDropdown from './EventDropdown';
import React from 'react';
import {
	DeleteAllAttributes,
	withAttributesConsumer
} from '../context/attributes';
import {Event} from 'event-analysis/utils/types';

interface IEventSectionProps {
	deleteAllAttributes: DeleteAllAttributes;
	event: Event;
	onEventChange: (event: Event) => void;
}

const EventSection: React.FC<IEventSectionProps> = ({
	deleteAllAttributes,
	event,
	onEventChange
}) => (
	<div className='event-section-root'>
		<div className='section-header'>{Liferay.Language.get('analyze')}</div>

		<div className='event-list'>
			{event && <EventChip event={event} onEventChange={onEventChange} />}

			{!event && (
				<EventDropdown
					onEventChange={(event: Event) => {
						onEventChange(event);

						deleteAllAttributes();
					}}
					trigger={
						<Button
							className='add-event-button'
							display='primary'
							icon='plus'
							iconAlignment='left'
							size='sm'
						/>
					}
				/>
			)}
		</div>
	</div>
);

export default withAttributesConsumer(EventSection);
