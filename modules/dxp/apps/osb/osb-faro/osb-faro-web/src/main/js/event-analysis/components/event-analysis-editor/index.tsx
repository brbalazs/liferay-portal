import Card from 'shared/components/Card';
import CardTabs, {CardTabSizes} from 'shared/components/CardTabs';
import Checkbox from 'shared/components/Checkbox';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import EventAnalysisBuilder from './event-analysis-builder';
import React, {useEffect, useState} from 'react';
import {Attribute, Breakdown, CalculationTypes, Event, Filter} from './types';
import {withRangeKey} from 'shared/hoc';
import {WithRangeKeyProps} from 'shared/hoc/WithRangeKey';

interface IEventAnalysisEditorProps
	extends WithRangeKeyProps,
		React.HTMLAttributes<HTMLElement> {}

const EventAnalysisEditor: React.FC<IEventAnalysisEditorProps> = ({
	onRangeSelectorsChange,
	rangeSelectors
}) => {
	const [attributes, setAttributes] = useState<Attribute[]>([]);
	const [breakdowns, setBreakdowns] = useState<Breakdown[]>([]);
	const [compareToPrevious, setCompareToPrevious] = useState(false);
	const [event, setEvent] = useState<Event>(null);
	const [filters, setFilters] = useState<Filter[]>([]);
	const [type, setType] = useState<CalculationTypes>(CalculationTypes.Total);

	useEffect(() => {
		// TODO: LRAC-7333 Add request here
		console.log({attributes, breakdowns, filters});
	}, [
		attributes,
		breakdowns,
		compareToPrevious,
		event,
		filters,
		rangeSelectors,
		type
	]);

	return (
		<Card className='event-analysis-editor-root'>
			<EventAnalysisBuilder
				attributes={attributes}
				breakdowns={breakdowns}
				event={event}
				filters={filters}
				onAttributesChange={setAttributes}
				onBreakdownsChange={setBreakdowns}
				onEventChange={setEvent}
				onFiltersChange={setFilters}
			/>

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

export default withRangeKey(EventAnalysisEditor);
