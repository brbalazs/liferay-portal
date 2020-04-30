import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Constants from 'shared/util/constants';
import DateFilterConjunctionInput from './components/DateFilterConjunctionInput';
import Form from 'shared/components/form';
import getCN from 'classnames';
import Input from 'shared/components/Input';
import React from 'react';
import SelectEntityFromModal from './components/SelectEntityFromModal';
import {ACTIVITY_KEY, OCCURENCE_OPTIONS} from '../utils/constants';
import {activityAssetsListColumns} from 'shared/util/table-columns';
import {buildOrderByFields, COUNT} from 'shared/util/pagination';
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
import {Modal} from 'shared/types/Modal';

const {assetNames} = Constants;

const isValidOccurenceCount = occurenceCount =>
	isValid(occurenceCount) && occurenceCount >= 0;

export const AssetItem: React.FC<{
	dataSourceAssetPK?: string;
	name: string;
}> = ({dataSourceAssetPK = '', name}) => (
	<div className='asset-display-root' title={dataSourceAssetPK}>
		<div className='asset-name text-truncate'>{name}</div>

		{!!dataSourceAssetPK && (
			<div
				data-tooltip
				data-tooltip-align='top'
				title={dataSourceAssetPK}
			>
				<div className='asset-url text-secondary text-truncate'>
					{dataSourceAssetPK}
				</div>
			</div>
		)}
	</div>
);

const ASSET_MODAL_CONFIG_MAP = {
	[assetNames.commentPosted]: {
		columns: [activityAssetsListColumns.commentCount],
		label: Liferay.Language.get('comments')
	},
	[assetNames.documentDownloaded]: {
		columns: [activityAssetsListColumns.downloadCount],
		label: Liferay.Language.get('downloads')
	},
	[assetNames.formSubmitted]: {
		columns: [activityAssetsListColumns.submissionCount],
		label: Liferay.Language.get('submissions')
	}
};

type Asset = {
	dataSourceAssetPK: string;
	id: string;
	name: string;
};

type Touched = {
	asset: boolean;
	dateFilter: boolean;
	occurenceCount: boolean;
};

type Valid = {
	asset: boolean;
	dateFilter: boolean;
	occurenceCount: boolean;
};

interface IBehaviorInputProps extends ISegmentEditorCustomInputBase {
	channelId: string;
	close: Modal.close;
	open: Modal.open;
	touched: Touched;
	valid: Valid;
}

export class BehaviorInput extends React.Component<IBehaviorInputProps> {
	static contextType = ReferencedObjectsContext;

	componentDidMount() {
		this.validateAsset();
	}

	componentDidUpdate() {
		this.validateAsset();
	}

	@autobind
	assetsDataFn({delta, orderBy, orderByField, page, query}) {
		const {
			channelId,
			groupId,
			property: {entityType, name}
		} = this.props;

		return API.activities.searchAssets({
			applicationId: entityType,
			channelId,
			cur: page,
			delta,
			eventId: name,
			groupId,
			orderByFields: buildOrderByFields({
				field: orderByField,
				sortOrder: orderBy
			}),
			query
		});
	}

	createActivityKey(asset) {
		const {property} = this.props;

		return `${property.entityType}#${property.name}#${asset.id}`;
	}

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
				payload: items
					.map(Map)
					.valueSeq()
					.toArray()
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

		const {
			columns = [activityAssetsListColumns.viewCount],
			label = Liferay.Language.get('views')
		}: {columns?: any; label?: any} = get(
			ASSET_MODAL_CONFIG_MAP,
			property.name,
			{}
		);

		const conjunctionCriterion = (
			this.getConjunctionDateFilterIMap(value) ||
			Map({propertyName: 'day'})
		).toJS();

		return (
			<div className='criteria-statement'>
				<Form.Group autoFit>
					<Form.GroupItem className='entity-name' label shrink>
						{property.entityName}
					</Form.GroupItem>

					<OperatorDropdown />

					<Form.GroupItem className='display-value' label shrink>
						<b>{displayValue}</b>
					</Form.GroupItem>

					<SelectEntityFromModal
						columns={[
							activityAssetsListColumns.nameUrl,
							...columns
						]}
						dataSourceFn={this.assetsDataFn}
						entity={this.getAssetFromContext()}
						error={touched.asset && !valid.asset}
						groupId={groupId}
						noResultsIcon='web-content'
						onSubmit={this.handleAssetSelect}
						orderByField={COUNT}
						orderByOptions={[
							{
								label,
								value: COUNT
							}
						]}
						renderEntity={asset => (
							<AssetItem {...asset} title={label} />
						)}
						title={property.label}
					/>
				</Form.Group>

				<Form.Group autoFit>
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
			</div>
		);
	}
}

export default BehaviorInput;
