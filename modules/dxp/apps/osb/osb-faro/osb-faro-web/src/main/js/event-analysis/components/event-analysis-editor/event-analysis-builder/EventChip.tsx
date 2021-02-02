import Button from 'shared/components/Button';
import Chip from 'shared/components/Chip';
import EventDropdown from './EventDropdown';
import getCN from 'classnames';
import React from 'react';
import {Event} from '../types';

interface IEventChipProps {
	event: Event;
	onEventChange: (event: Event) => void;
}

const EventChip: React.FC<IEventChipProps> = ({
	event: {displayName, id, name},
	onEventChange
}) => {
	const TriggerComponent: React.FC<any> = React.forwardRef(
		({className, onClick, ...otherProps}, ref) => (
			<Chip
				{...otherProps}
				className={getCN('event-chip-root', className)}
				onCloseClick={onEventChange}
				ref={ref}
			>
				<Button
					className='event-name'
					display='unstyled'
					onClick={onClick}
				>
					{displayName || name}
				</Button>
			</Chip>
		)
	);

	return (
		<EventDropdown
			eventId={id}
			onEventChange={onEventChange}
			trigger={<TriggerComponent />}
		/>
	);
};

export default EventChip;
