import AnalysisChip from './AnalysisChip';
import Button from 'shared/components/Button';
import React from 'react';
import {Event} from '../types';

interface IAnalysisSectionProps {
	event: Event;
	onClearAll: () => void;
}

const AnalysisSection: React.FC<IAnalysisSectionProps> = ({
	event,
	onClearAll
}) => (
	<div className='analysis-section-root'>
		<div className='section-header'>{Liferay.Language.get('analyze')}</div>

		<div className='analysis-list'>
			{event && <AnalysisChip event={event} onCloseClick={onClearAll} />}

			{!event && (
				<Button display='primary' icon='plus' iconAlignment='left' />
			)}
		</div>
	</div>
);

export default AnalysisSection;
