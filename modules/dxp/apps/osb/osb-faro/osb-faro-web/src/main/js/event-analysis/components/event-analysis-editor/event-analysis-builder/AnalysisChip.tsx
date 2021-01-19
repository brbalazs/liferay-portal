import Chip, {IChipProps} from 'shared/components/Chip';
import React from 'react';
import {Event} from '../types';

interface IAnalysisChipProps extends IChipProps {
	event: Event;
}

const AnalysisChip: React.FC<IAnalysisChipProps> = ({
	event: {name},
	onCloseClick
}) => (
	<Chip className='analysis-chip-root' onCloseClick={onCloseClick}>
		<div className='event-name'>{name}</div>
	</Chip>
);

export default AnalysisChip;
