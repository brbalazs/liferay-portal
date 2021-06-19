import BaseDropdown from '../base-dropdown';
import BreakdownOptions from './options';
import EventAttributeDefinitionQuery, {
	UPDATE_EVENT_ATTRIBUTE_DEFINITION
} from 'event-analysis/queries/EventAttributeDefinitionQuery';
import EventAttributeDefinitionsQuery, {
	EventAttributeDefinitionsData,
	EventAttributeDefinitionsVariables
} from 'event-analysis/queries/EventAttributeDefinitionsQuery';
import React, {useState} from 'react';
import {AddBreakdown, EditBreakdown} from '../../context/attributes';
import {Align} from '@clayui/drop-down';
import {
	Attribute,
	AttributeOwnerTypes,
	AttributeTypes,
	Breakdown,
	DataTypes
} from 'event-analysis/utils/types';
import {BREAKDOWN_FNS_MAP} from 'event-analysis/utils/utils';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {CSSTransition, TransitionGroup} from 'react-transition-group';
import {Modal} from 'shared/types';
import {NAME} from 'shared/util/pagination';
import {OrderByDirections} from 'shared/util/constants';
import {SafeResults} from 'shared/hoc/util';
import {useQuery} from '@apollo/react-hooks';

interface IAttributeBreakdownDropdownProps {
	alignmentPosition?: typeof Align[keyof typeof Align];
	attribute?: Attribute;
	breakdown: Breakdown;
	close: Modal.close;
	disabledIds: string[];
	eventId: string;
	onAttributeSelect: AddBreakdown | EditBreakdown;
	open: Modal.open;
	trigger: React.ReactElement;
}

const AttributeBreakdownDropdown: React.FC<IAttributeBreakdownDropdownProps> = ({
	alignmentPosition = Align.RightTop,
	attribute,
	breakdown,
	close,
	disabledIds,
	eventId,
	onAttributeSelect,
	open,
	trigger
}) => {
	const [
		attributeOwnerType,
		setAttributeOwnerType
	] = useState<AttributeOwnerTypes>(AttributeOwnerTypes.Event);
	const [query, setQuery] = useState('');
	const [selectedAttribute, setSelectedAttribute] = useState<Attribute>(
		breakdown ? attribute : null
	); // TODO  why do we check for breakdown or filter?

	const result = useQuery<
		EventAttributeDefinitionsData,
		EventAttributeDefinitionsVariables
	>(EventAttributeDefinitionsQuery, {
		variables: {
			eventDefinitionId: eventId,
			keyword: '',
			page: 0,
			size: 200,
			sort: {
				column: NAME,
				type: OrderByDirections.Ascending
			},
			type: AttributeTypes.All
		}
	});

	const oldAttributeId = attribute ? attribute.id : null;

	const hasOptions = (attribute: Attribute) =>
		[DataTypes.Date, DataTypes.Duration, DataTypes.Number].includes(
			attribute.dataType
		);

	return (
		<BaseDropdown
			alignmentPosition={alignmentPosition}
			className='event-analysis-editor-attribute-dropdown-root'
			onActiveChange={active => {
				if (!active) {
					setAttributeOwnerType(AttributeOwnerTypes.Event);
					setQuery('');
					setSelectedAttribute(null);
				}
			}}
			trigger={trigger}
		>
			{({setActive}) => (
				<TransitionGroup className='transition-carousel-group'>
					{!selectedAttribute && (
						<CSSTransition
							classNames='transition-attribute-carousel-right'
							timeout={250}
						>
							<div className='d-flex flex-column'>
								<BaseDropdown.Header
									activeTabId={attributeOwnerType}
									tabs={[
										{
											onClick: () =>
												setAttributeOwnerType(
													AttributeOwnerTypes.Event
												),
											tabId: AttributeOwnerTypes.Event,
											title: Liferay.Language.get('event')
										}
									]}
									title={Liferay.Language.get('attributes')}
								/>

								<SafeResults
									page={false}
									pageDisplay={false}
									{...result}
								>
									{({
										eventAttributeDefinitions: {
											eventAttributeDefinitions
										}
									}: {
										eventAttributeDefinitions: {
											eventAttributeDefinitions: Attribute[];
										};
									}) => {
										// TODO: Remove mock data
										const attributes = [
											...eventAttributeDefinitions,
											{
												dataType: DataTypes.Number,
												description: null,
												displayName: 'Number',
												id: '20',
												name: 'Number',
												sampleValue: null,
												type: 'LOCAL'
											},

											{
												dataType: DataTypes.Date,
												description: null,
												displayName: 'Date',
												id: '17',
												name: 'Date',
												sampleValue: null,
												type: 'LOCAL'
											},
											{
												dataType: DataTypes.Duration,
												description: null,
												displayName: 'Duration',
												id: '22',
												name: 'Duration',
												sampleValue: null,
												type: 'LOCAL'
											}
										];

										return (
											<BaseDropdown.SearchableList
												activeId={oldAttributeId}
												disabledIds={disabledIds}
												items={attributes}
												onEditClick={(
													attribute: Attribute
												) => {
													open(
														modalTypes.EDIT_ATTRIBUTE_EVENT_MODAL,
														{
															id: attribute.id,
															mutation: UPDATE_EVENT_ATTRIBUTE_DEFINITION,
															onCancel: close,
															query: EventAttributeDefinitionQuery,
															showTypecast: true
														}
													);

													setActive(false);
												}}
												onItemClick={(
													attribute: Attribute
												) => {
													const {
														dataType,
														id: attributeId
													} = attribute;

													const breakdownFn =
														BREAKDOWN_FNS_MAP[
															dataType
														];

													onAttributeSelect({
														attribute,
														attributeId,
														breakdown: breakdownFn({
															attributeId,
															type: attributeOwnerType
														}),
														oldAttributeId
													});

													setActive(false);
												}}
												onItemOptionsClick={
													setSelectedAttribute
												}
												onQueryChange={setQuery}
												query={query}
												showOptionsCondition={
													hasOptions
												}
											/>
										);
									}}
								</SafeResults>
							</div>
						</CSSTransition>
					)}

					{selectedAttribute && hasOptions(selectedAttribute) && (
						<CSSTransition
							classNames='transition-attribute-carousel-left'
							timeout={250}
						>
							<div className='w-100'>
								<BreakdownOptions
									attribute={selectedAttribute}
									attributeOwnerType={attributeOwnerType}
									oldAttributeId={oldAttributeId}
									onActiveChange={setActive}
									onAttributeChange={params => {
										setSelectedAttribute(params);
									}}
									onEditClick={
										selectedAttribute.id === oldAttributeId
											? null
											: () => {
													open(
														modalTypes.EDIT_ATTRIBUTE_EVENT_MODAL,
														{
															id:
																selectedAttribute.id,
															mutation: UPDATE_EVENT_ATTRIBUTE_DEFINITION,
															onCancel: close,
															query: EventAttributeDefinitionQuery,
															showTypecast: true
														}
													);

													setActive(false);
											  }
									}
								/>
							</div>
						</CSSTransition>
					)}
				</TransitionGroup>
			)}
		</BaseDropdown>
	);
};

export default connect(null, {close, open})(AttributeBreakdownDropdown);
