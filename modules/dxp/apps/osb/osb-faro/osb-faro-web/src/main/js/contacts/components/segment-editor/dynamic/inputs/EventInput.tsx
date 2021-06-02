import AttributeConjunctionInput from './components/attribute-conjunction-input';
import autobind from 'autobind-decorator';
import client from 'shared/apollo/client';
import DateFilterConjunctionInput from './components/DateFilterConjunctionInput';
import EventAttributeDefinitionsQuery from 'event-analysis/queries/EventAttributeDefinitionsQuery';
import Form from 'shared/components/form';
import OccurenceConjunctionInput from './components/OccurenceConjunctionInput';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import {Attribute, AttributeTypes} from 'event-analysis/utils/types';
import {Criterion, ISegmentEditorCustomInputBase} from '../utils/types';
import {CustomValue} from 'shared/util/records';
import {fromJS, Map} from 'immutable';
import {FunctionalOperators, RelationalOperators} from '../utils/constants';
import {
	getFilterCriterionIMap,
	getIndexFromPropertyName
} from '../utils/custom-inputs';
import {isNull} from 'lodash';

type Touched = {
	attribute: boolean;
	attributeValue: string;
	dateFilter: boolean;
	occurenceCount: boolean;
};

type Valid = {
	attribute: boolean;
	attributeValue: string;
	dateFilter: boolean;
	occurenceCount: boolean;
};

interface IEventInputProps extends ISegmentEditorCustomInputBase {
	channelId: string;
	touched: Touched;
	valid: Valid;
}

interface IEventInputState {
	eventAttributeDefinitions: Attribute[];
	loading: boolean;
}

export class EventInput extends React.Component<
	IEventInputProps,
	IEventInputState
> {
	_completedAnalytics = false;

	state = {
		eventAttributeDefinitions: [],
		loading: true
	};

	componentDidMount() {
		this.fetchAttributes().then(
			({
				data: {
					eventAttributeDefinitions: {eventAttributeDefinitions}
				}
			}) => {
				this.setState({
					eventAttributeDefinitions: [
						...eventAttributeDefinitions,
						// TODO: Remove mocked Date input
						{
							dataType: 'DATE',
							description: null,
							displayName: 'Some Date',
							id: '1111',
							name: 'someDate',
							sampleValue: null
						}
					],
					loading: false
				});
			}
		);
	}

	componentDidUpdate() {
		const {
			id,
			property: {entityName, type},
			valid: {attributeValue, dateFilter, occurenceCount}
		} = this.props;

		const valid = attributeValue && dateFilter && occurenceCount;

		if (!id && valid && !this._completedAnalytics) {
			this._completedAnalytics = true;

			analytics.track('Dynamic Segment Creation - Completed Attribute', {
				entityName,
				type
			});
		}
	}

	createActivityKey(attribute) {
		const {property} = this.props;

		return `${property.entityType}#${property.name}#${attribute.id}`;
	}

	fetchAttributes() {
		const {
			property: {id}
		} = this.props;

		return client.query({
			// TODO: Update this to use AttributeTypes.Global only
			query: EventAttributeDefinitionsQuery,
			variables: {
				eventDefinitionId: id,
				page: 0,
				size: 25,
				sort: {
					column: 'name',
					type: 'ASC'
				},
				type: AttributeTypes.All
			}
		});
	}

	getConjunctionDateFilterIMap(value) {
		const conjunctionDateFilterIndex = getIndexFromPropertyName(
			value,
			'day'
		);

		if (conjunctionDateFilterIndex >= 0) {
			return getFilterCriterionIMap(value, conjunctionDateFilterIndex);
		}
	}

	@autobind
	handleAttributeConjunctionChange({
		criterion,
		touched: conjunctionTouched,
		valid: conjunctionValid
	}) {
		const {onChange, touched, valid, value} = this.props;

		onChange({
			touched: {...touched, ...conjunctionTouched},
			valid: {...valid, ...conjunctionValid},
			value: value.mergeIn(
				['criterionGroup', 'items', 1],
				fromJS(criterion)
			)
		});
	}

	@autobind
	handleAttributeValueBlur() {
		const {onChange, touched} = this.props;

		onChange({
			touched: {...touched, attributeValue: true}
		});
	}

	@autobind
	handleDateFilterConjunctionChange(criterion) {
		const {onChange, touched, valid, value} = this.props;

		onChange({
			touched: {...touched, dateFilter: criterion && criterion.touched},
			valid: {...valid, dateFilter: isNull(criterion) || criterion.valid},
			value: isNull(criterion)
				? value.deleteIn(['criterionGroup', 'items', 2])
				: value.mergeIn(
						['criterionGroup', 'items', 2],
						fromJS(criterion)
				  )
		});
	}

	@autobind
	handleOccurenceConjunctionChange({
		criterion,
		touched: occurenceCountTouched,
		valid: occurenceCountValid
	}: {
		criterion?: Criterion;
		touched?: boolean;
		valid?: boolean;
	}) {
		const {onChange, touched, valid, value: valueIMap} = this.props;

		let params: {touched?: Touched; valid?: Valid; value?: CustomValue} = {
			touched,
			valid
		};

		if (criterion) {
			const {operatorName, value} = criterion;

			params = {
				...params,
				value: valueIMap.merge(
					fromJS({operator: operatorName, value})
				) as CustomValue
			};
		}

		if (touched) {
			params = {
				...params,
				touched: {...touched, occurenceCount: occurenceCountTouched}
			};
		}

		if (valid) {
			params = {
				...params,
				valid: {...valid, occurenceCount: occurenceCountValid}
			};
		}

		onChange(params);
	}

	render() {
		const {
			props: {
				displayValue,
				operatorRenderer: OperatorDropdown,
				touched,
				valid,
				value
			},
			state: {eventAttributeDefinitions, loading}
		} = this;

		const dateFilterConjunctionCriterion = (
			this.getConjunctionDateFilterIMap(value) ||
			Map({propertyName: 'day'})
		).toJS();

		return (
			<div className='criteria-statement'>
				{loading ? (
					<Spinner />
				) : (
					<>
						<Form.Group autoFit>
							<OperatorDropdown />

							<Form.GroupItem
								className='entity-name'
								label
								shrink
							>
								{Liferay.Language.get('performed')}
							</Form.GroupItem>

							<Form.GroupItem
								className='display-value'
								label
								shrink
							>
								<b>{displayValue}</b>
							</Form.GroupItem>

							<OccurenceConjunctionInput
								onChange={this.handleOccurenceConjunctionChange}
								operatorName={
									value.get(
										'operator'
									) as FunctionalOperators &
										RelationalOperators
								}
								touched={touched.occurenceCount}
								valid={valid.occurenceCount}
								value={value.get('value')}
							/>

							<DateFilterConjunctionInput
								conjunctionCriterion={
									dateFilterConjunctionCriterion
								}
								onChange={
									this.handleDateFilterConjunctionChange
								}
							/>
						</Form.Group>

						<Form.Group autoFit>
							<Form.GroupItem
								className='conjunction'
								label
								shrink
							>
								{Liferay.Language.get('where')}
							</Form.GroupItem>

							<AttributeConjunctionInput
								attributes={eventAttributeDefinitions}
								conjunctionCriterion={getFilterCriterionIMap(
									value,
									1
								).toJS()}
								onChange={this.handleAttributeConjunctionChange}
								touched={{
									attribute: touched.attribute,
									attributeValue: touched.attributeValue
								}}
								valid={{
									attribute: valid.attribute,
									attributeValue: valid.attributeValue
								}}
							/>
						</Form.Group>
					</>
				)}
			</div>
		);
	}
}

export default EventInput;
