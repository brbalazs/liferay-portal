import Card from 'shared/components/Card';
import CardTabs from 'shared/components/CardTabs';
import Checkbox from 'shared/components/Checkbox';
import DropdownRangeKey from 'shared/hoc/DropdownRangeKey';
import EventAnalysisBuilder from './event-analysis-builder';
import React, {useEffect, useState} from 'react';
import {
	Attribute,
	AttributeTypes,
	Breakdown,
	CalculationTypes,
	DataTypes,
	DateGroupings,
	Event,
	EventTypes,
	Filter,
	Operators
} from './types';
import {withRangeKey} from 'shared/hoc';
import {WithRangeKeyProps} from 'shared/hoc/WithRangeKey';

interface IEventAnalysisEditorProps
	extends WithRangeKeyProps,
		React.HTMLAttributes<HTMLElement> {}

const EventAnalysisEditor: React.FC<IEventAnalysisEditorProps> = ({
	onRangeSelectorsChange,
	rangeSelectors
}) => {
	const [attributes, setAttributes] = useState<Attribute[]>([
		{
			defaultDataType: DataTypes.String,
			displayName: 'Article Title',
			id: '0',
			name: 'Article Title'
		},
		{
			defaultDataType: DataTypes.Number,
			displayName: 'Article Count',
			id: '1',
			name: 'Article Count'
		},
		{
			defaultDataType: DataTypes.String,
			displayName: 'Job Title',
			id: '2',
			name: 'Job Title'
		},
		{
			defaultDataType: DataTypes.Duration,
			displayName: 'Session Time',
			id: '3',
			name: 'sessionTime'
		},
		{
			defaultDataType: DataTypes.Boolean,
			displayName: 'Enrolled in Autopay',
			id: '4',
			name: 'autopay'
		},
		{
			defaultDataType: DataTypes.Date,
			displayName: 'Article Date',
			id: '5',
			name: 'dateCreated'
		}
	]);
	const [breakdowns, setBreakdowns] = useState<Breakdown[]>([
		{
			attributeId: '0',
			dataType: DataTypes.String,
			type: AttributeTypes.Event
		},
		{
			attributeId: '1',
			bin: 10,
			dataType: DataTypes.Number,
			type: AttributeTypes.Event
		},
		{
			attributeId: '2',
			dataType: DataTypes.String,
			type: AttributeTypes.Event
		},
		{
			attributeId: '3',
			dataType: DataTypes.Duration,
			type: AttributeTypes.Session
		},
		{
			attributeId: '4',
			dataType: DataTypes.Boolean,
			type: AttributeTypes.Individual
		},
		{
			attributeId: '5',
			dataType: DataTypes.Date,
			dateGrouping: DateGroupings.Years,
			type: AttributeTypes.Event
		}
	]); // TODO: remove default values here
	const [compareToPrevious, setCompareToPrevious] = useState(false);
	const [event, setEvent] = useState<Event>({
		id: '0',
		name: 'Article Views',
		type: EventTypes.Custom
	}); // TODO: Remove default event
	const [filters, setFilters] = useState<Filter[]>([
		{
			attributeId: '2',
			operator: Operators.NE,
			value: ['Stuff']
		},
		{
			attributeId: '1',
			operator: Operators.GT,
			value: [3232]
		},
		{
			attributeId: '3',
			operator: Operators.GT,
			value: [123123123]
		},
		{
			attributeId: '4',
			operator: Operators.EQ,
			value: [false]
		},
		{
			attributeId: '5',
			operator: Operators.Between,
			value: ['2020-01-20', '2020-01-24']
		}
	]); // TODO: remove default values here
	const [type, setType] = useState<CalculationTypes>(CalculationTypes.Total);

	useEffect(() => {
		// console.log('change', {
		// 	attributes,
		// 	breakdowns,
		// 	compareToPrevious,
		// 	event,
		// 	filters,
		// 	rangeSelectors,
		// 	type
		// });
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

			<div className='options-wrapper d-flex justify-content-between'>
				<CardTabs
					activeTabId={type}
					className='type-selector'
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
