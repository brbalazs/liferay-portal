import autobind from 'autobind-decorator';
import DateFilterConjunctionInput from './components/DateFilterConjunctionInput';

import Form from 'shared/components/form';
import getCN from 'classnames';
import Input from 'shared/components/Input';
import React, {useEffect} from 'react';
import {ACTIVITY_KEY, OCCURENCE_OPTIONS} from '../utils/constants';
import {ClaySelectWithOption} from '@clayui/select';
import {
	EntityType,
	ReferencedObjectsContext
} from '../context/referencedObjects';
import {fromJS, Map} from 'immutable';
import {get} from 'lodash';
import {
	getFilterCriterionIMap,
	getIndexFromPropertyName,
	getPropertyValue,
	setPropertyValue
} from '../utils/custom-inputs';
import {ISegmentEditorCustomInputBase} from '../utils/types';
import {isNull} from 'lodash';
import {isValid, parseActivityKey} from '../utils/utils';

const isValidOccurenceCount = occurenceCount =>
	isValid(occurenceCount) && occurenceCount >= 0;

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

export class EventInput extends React.Component<IEventInputProps> {
	static contextType = ReferencedObjectsContext;

	_completedAnalytics = false;

	componentDidUpdate() {
		const {
			id,
			property: {entityName, type},
			valid: {attribute, attributeValue, dateFilter, occurenceCount}
		} = this.props;

		this.validateAsset();

		const valid = attribute && dateFilter && occurenceCount;

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

	// TODO: Prob should grab attribute & Event from context
	getAssetFromContext(): Asset | undefined {
		const {
			context: {referencedEntities}
		} = this;

		const id = this.getAssetId();

		const reference = referencedEntities.getIn([EntityType.Assets, id]);

		return reference && reference.toJS();
	}

	getAssetId() {
		const {value} = this.props;

		const activityKeyIndex = getIndexFromPropertyName(value, ACTIVITY_KEY);

		const activityKey = getPropertyValue(value, 'value', activityKeyIndex);

		const {id} = parseActivityKey(activityKey);

		return id;
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
	handleAssetSelect(items) {
		const {
			context: {addEntities, addEntity},
			props: {onChange, touched, valid, value}
		} = this;

		const asset = items.first();

		const propertyIndex = getIndexFromPropertyName(value, ACTIVITY_KEY);

		if (items.size === 1) {
			addEntity({entityType: EntityType.Assets, payload: Map(asset)});

			onChange({
				valid: {...valid, asset: true},
				value: setPropertyValue(
					value,
					'value',
					propertyIndex,
					this.createActivityKey(asset)
				)
			});
		} else {
			addEntities({
				entityType: EntityType.Assets,
				payload: items.map(Map).valueSeq().toArray()
			});

			onChange(
				items
					.valueSeq()
					.map(assetItem => ({
						touched,
						valid: {...valid, asset: true},
						value: setPropertyValue(
							value,
							'value',
							propertyIndex,
							this.createActivityKey(assetItem)
						)
					}))
					.toArray()
			);
		}
	}

	@autobind
	handleEventOperatorChange(event) {
		const {value} = event.target;

		const {onChange, value: valueIMap} = this.props;

		onChange({
			value: valueIMap.merge(
				Map({operator: value, value: valueIMap.get('value', 1)})
			)
		});
	}

	@autobind
	handleOccurenceCountBlur() {
		const {onChange, touched} = this.props;

		onChange({
			touched: {...touched, occurenceCount: true}
		});
	}

	@autobind
	handleOccurenceCountChange(event) {
		const {value} = event.target;

		const {onChange, valid, value: valueIMap} = this.props;

		let numberVal: string | number = '';

		if (isValid(value)) {
			numberVal = parseInt(value);
		}

		onChange({
			valid: {...valid, occurenceCount: isValidOccurenceCount(numberVal)},
			value: valueIMap.set('value', numberVal)
		});
	}

	@autobind
	handleConjunctionChange(criterion) {
		const {onChange, touched, valid, value} = this.props;

		onChange({
			touched: {...touched, dateFilter: criterion && criterion.touched},
			valid: {...valid, dateFilter: isNull(criterion) || criterion.valid},
			value: isNull(criterion)
				? value.deleteIn(['criterionGroup', 'items', 1])
				: value.mergeIn(
						['criterionGroup', 'items', 1],
						fromJS(criterion)
				  )
		});
	}

	invalidateAsset() {
		const {onChange, touched, valid} = this.props;

		onChange({
			touched: {...touched, asset: true},
			valid: {...valid, asset: false}
		});
	}

	validateAsset() {
		const {valid} = this.props;

		if (valid.asset && !this.getAssetFromContext()) {
			this.invalidateAsset();
		}
	}

	render() {
		const {
			displayValue,
			groupId,
			operatorRenderer: OperatorDropdown,
			property,
			touched,
			valid,
			value
		} = this.props;

		const conjunctionCriterion = (
			this.getConjunctionDateFilterIMap(value) ||
			Map({propertyName: 'day'})
		).toJS();

		return (
			<div className='criteria-statement'>
				<Form.Group autoFit>
					<OperatorDropdown />

					<Form.GroupItem className='entity-name' label shrink>
						{Liferay.Language.get('performed')}
					</Form.GroupItem>

					<Form.GroupItem className='display-value' label shrink>
						<b>{displayValue}</b>
					</Form.GroupItem>

					<Form.GroupItem shrink>
						<ClaySelectWithOption
							className='operator-input'
							onChange={this.handleEventOperatorChange}
							options={OCCURENCE_OPTIONS.map(({key, label}) => ({
								label,
								value: key
							}))}
							value={value.get('operator')}
						/>
					</Form.GroupItem>

					<Form.GroupItem
						className={getCN({
							'has-error':
								!valid.occurenceCount && touched.occurenceCount
						})}
						shrink
					>
						<Input
							data-testid='occurence-count-input'
							min='0'
							onBlur={this.handleOccurenceCountBlur}
							onChange={this.handleOccurenceCountChange}
							type='number'
							value={value.get('value')}
						/>
					</Form.GroupItem>

					<Form.GroupItem className='unit' label shrink>
						{Liferay.Language.get('times')}
					</Form.GroupItem>

					<DateFilterConjunctionInput
						conjunctionCriterion={conjunctionCriterion}
						onChange={this.handleConjunctionChange}
					/>
				</Form.Group>

				<Form.Group autoFit>
					<Form.GroupItem label shrink>
						{Liferay.Language.get('where')}
					</Form.GroupItem>

					<Form.GroupItem shrink>
						<ClaySelectWithOption
							className='operator-input'
							onChange={this.handleEventOperatorChange}
							options={OCCURENCE_OPTIONS.map(({key, label}) => ({
								label,
								value: key
							}))}
							value={value.get('operator')}
						/>
					</Form.GroupItem>

					<Form.GroupItem shrink>
						<ClaySelectWithOption
							className='operator-input'
							onChange={this.handleEventOperatorChange}
							options={OCCURENCE_OPTIONS.map(({key, label}) => ({
								label,
								value: key
							}))}
							value={value.get('operator')}
						/>
					</Form.GroupItem>
				</Form.Group>
			</div>
		);
	}
}

export default EventInput;
