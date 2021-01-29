import AnalysisChip from './AnalysisChip';
import AnalysisDropdown from './AnalysisDropdown';
import Button from 'shared/components/Button';
import React from 'react';
import {Event} from '../types';

interface IAnalysisSectionProps {
	event: Event;
	onEventChange: (event: Event) => void;
}

const AnalysisSection: React.FC<IAnalysisSectionProps> = ({
	event,
	onEventChange
}) => (
	<div className='analysis-section-root'>
		<div className='section-header'>{Liferay.Language.get('analyze')}</div>

		<div className='analysis-list'>
			{event && (
				<AnalysisChip event={event} onEventChange={onEventChange} />
			)}

			{!event && (
				<AnalysisDropdown
					onEventChange={onEventChange}
					trigger={
						<Button
							className='add-analysis-button'
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

export default AnalysisSection;
