import BreakdownTable from './event-analysis-breakdown';
import Card from 'shared/components/Card';
import CardTabs, {CardTabSizes} from 'shared/components/CardTabs';
import Checkbox from 'shared/components/Checkbox';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import EventAnalysisBuilder from './event-analysis-builder';
import React, {useState} from 'react';
import {CalculationTypes, Event} from 'event-analysis/utils/types';
import {compose} from 'redux';
import {
	withAttributesConsumer,
	withAttributesProvider
} from './context/attributes';
import {withRangeKey} from 'shared/hoc';
import {WithRangeKeyProps} from 'shared/hoc/WithRangeKey';

interface IEventAnalysisEditorProps
	extends WithRangeKeyProps,
		React.HTMLAttributes<HTMLElement> {
	channelId: string;
}

const EventAnalysisEditor: React.FC<IEventAnalysisEditorProps> = ({
	channelId,
	onRangeSelectorsChange,
	rangeSelectors
}) => {
	const [compareToPrevious, setCompareToPrevious] = useState(false);
	const [event, setEvent] = useState<Event>(null);
	const [type, setType] = useState<CalculationTypes>(CalculationTypes.Total);

	return (
		<Card className='event-analysis-editor-root'>
			<EventAnalysisBuilder event={event} onEventChange={setEvent} />

			<div className='options-container d-flex justify-content-between'>
				<CardTabs
					activeTabId={type}
					className='type-selector'
					size={CardTabSizes.Small}
					tabs={[
						{
							onClick: () => setType(CalculationTypes.Total),
							tabId: CalculationTypes.Total,
							title: Liferay.Language.get('total')
						},
						{
							onClick: () => setType(CalculationTypes.Unique),
							tabId: CalculationTypes.Unique,
							title: Liferay.Language.get('unique')
						},
						{
							onClick: () => setType(CalculationTypes.Average),
							tabId: CalculationTypes.Average,
							title: Liferay.Language.get('average')
						}
					]}
				/>

				<div className='d-flex align-items-center'>
					<Checkbox
						checked={compareToPrevious}
						className='compare-to-previous-checkbox mb-0 mr-4'
						label={Liferay.Language.get('compare-to-previous')}
						onChange={event =>
							setCompareToPrevious(event.currentTarget.checked)
						}
					/>

					<DropdownRangeKey
						legacy={false}
						onChange={onRangeSelectorsChange}
						rangeSelectors={rangeSelectors}
					/>
				</div>
			</div>

			<BreakdownTable
				channelId={channelId}
				compareToPrevious={compareToPrevious}
				event={event}
				rangeSelectors={rangeSelectors}
				type={type}
			/>
		</Card>
	);
};

export default compose<any>(
	withRangeKey,
	withAttributesProvider,
	withAttributesConsumer
)(EventAnalysisEditor);
