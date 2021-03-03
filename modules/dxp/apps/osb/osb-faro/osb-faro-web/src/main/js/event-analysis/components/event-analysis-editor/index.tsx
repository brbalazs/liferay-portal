import Card from 'shared/components/Card';
import CardTabs, {CardTabSizes} from 'shared/components/CardTabs';
import Checkbox from 'shared/components/Checkbox';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import EventAnalysisBuilder from './event-analysis-builder';
import React, {useEffect, useState} from 'react';
import {
	Attribute,
	Breakdown,
	CalculationTypes,
	Event,
	Filter
} from 'event-analysis/utils/types';
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
	attributes: {[key: string]: Attribute};
	breakdowns: {[key: string]: Breakdown};
	filters: {[key: string]: Filter};
	order: string[];
}

const EventAnalysisEditor: React.FC<IEventAnalysisEditorProps> = ({
	attributes,
	breakdowns,
	filters,
	onRangeSelectorsChange,
	order,
	rangeSelectors
}) => {
	const [compareToPrevious, setCompareToPrevious] = useState(false);
	const [event, setEvent] = useState<Event>(null);
	const [type, setType] = useState<CalculationTypes>(CalculationTypes.Total);

	useEffect(() => {
		// TODO: LRAC-7333 Add request here
	}, [
		attributes,
		breakdowns,
		compareToPrevious,
		event,
		filters,
		order,
		rangeSelectors,
		type
	]);

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
						onChange={onRangeSelectorsChange}
						rangeSelectors={rangeSelectors}
					/>
				</div>
			</div>

			<div>{'Insert Chart Component here'}</div>
		</Card>
	);
};

export default compose<any>(
	withRangeKey,
	withAttributesProvider,
	withAttributesConsumer
)(EventAnalysisEditor);
