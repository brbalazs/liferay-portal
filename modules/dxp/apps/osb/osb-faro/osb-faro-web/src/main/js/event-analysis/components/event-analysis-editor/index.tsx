import Card from 'shared/components/Card';
import CardTabs from 'shared/components/CardTabs';
import Checkbox from 'shared/components/Checkbox';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import EventAnalysisBuilder from './event-analysis-builder';
import React, {useEffect, useState} from 'react';
import {Breakdown, Event, Filter, Type} from './types';
import {withRangeKey} from 'shared/hoc';
import {WithRangeKeyProps} from 'shared/hoc/WithRangeKey';

interface IEventAnalysisEditorProps
	extends WithRangeKeyProps,
		React.HTMLAttributes<HTMLElement> {}

const EventAnalysisEditor: React.FC<IEventAnalysisEditorProps> = ({
	onRangeSelectorsChange,
	rangeSelectors
}) => {
	const [breakdowns, setBreakdowns] = useState<Breakdown[]>([
		{attributeId: '321321', attributeType: 'event', name: 'Article Title'},
		{attributeId: '232123', attributeType: 'event', name: 'Article Author'},
		{attributeId: '123123', attributeType: 'event', name: 'Job Title'}
	]); // TODO: remove default values here
	const [compareToPrevious, setCompareToPrevious] = useState(false);
	const [event, setEvent] = useState<Event>({
		id: '123123',
		name: 'Article Views'
	}); // TODO: Remove default eventId probably need to store event object here so we can have the name. Meaning we'll need to store the whole object here
	const [filters, setFilters] = useState<Filter[]>([
		{
			attributeId: '123123',
			dataType: 'string',
			operator: 'EQ',
			value: ['Stuff']
		}
	]); // TODO: remove default values here
	const [type, setType] = useState<Type>(Type.Average);

	useEffect(() => {
		console.log('change', {
			breakdowns,
			compareToPrevious,
			event,
			filters,
			rangeSelectors,
			type
		});
	}, [breakdowns, compareToPrevious, event, filters, rangeSelectors, type]);

	return (
		<Card className='event-analysis-editor-root'>
			<EventAnalysisBuilder
				breakdowns={breakdowns}
				event={event}
				filters={filters}
				onBreakdownsChange={setBreakdowns}
				onEventChange={setEvent}
				onFiltersChange={setFilters}
			/>

			<div className='options-wrapper d-flex justify-content-between'>
				<CardTabs
					activeTabId={type}
					className='type-selector'
					tabs={[
						{
							onClick: () => setType(Type.Total),
							tabId: Type.Total,
							title: Liferay.Language.get('total')
						},
						{
							onClick: () => setType(Type.Unique),
							tabId: Type.Unique,
							title: Liferay.Language.get('unique')
						},
						{
							onClick: () => setType(Type.Average),
							tabId: Type.Average,
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
