import AnalysisDropdown from './AnalysisDropdown';
import Button from 'shared/components/Button';
import Chip from 'shared/components/Chip';
import getCN from 'classnames';
import React from 'react';
import {Event} from '../types';

interface IAnalysisChipProps {
	event: Event;
	onEventChange: (event: Event) => void;
}

const AnalysisChip: React.FC<IAnalysisChipProps> = ({
	event: {displayName, id, name},
	onEventChange
}) => {
	const TriggerComponent: React.FC<any> = React.forwardRef(
		({className, onClick, ...otherProps}, ref) => (
			<Chip
				{...otherProps}
				className={getCN('analysis-chip-root', className)}
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
		<AnalysisDropdown
			eventId={id}
			onEventChange={onEventChange}
			trigger={<TriggerComponent />}
		/>
	);
};

export default AnalysisChip;
