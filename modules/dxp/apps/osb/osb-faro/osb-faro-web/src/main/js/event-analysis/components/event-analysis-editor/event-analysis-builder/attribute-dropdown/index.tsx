import AttributeFilter from './filter';
import BaseDropdown from '../base-dropdown';
import EVENT_ATTRIBUTE_DEFINITION_QUERY, {
	UPDATE_EVENT_ATTRIBUTE_DEFINITION
} from 'event-analysis/queries/EventAttributeDefinitionQuery';
import EVENT_ATTRIBUTE_DEFINITIONS_QUERY, {
	EventAttributeDefinitionsData,
	EventAttributeDefinitionsVariables
} from 'event-analysis/queries/EventAttributeDefinitionsQuery';
import React, {useState} from 'react';
import {AddAttribute, EditAttribute} from '../../context/attributes';
import {
	Attribute,
	AttributeOwnerTypes,
	AttributeTypes,
	Filter
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

interface IAttributeDropdownProps {
	attribute?: Attribute;
	close: Modal.close;
	disabledIds: string[];
	eventId: string;
	filter?: Filter;
	onAttributeSelect: AddAttribute | EditAttribute;
	open: Modal.open;
	trigger: React.ReactElement;
}

const AttributeDropdown: React.FC<IAttributeDropdownProps> = ({
	attribute,
	close,
	disabledIds,
	eventId,
	filter,
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
		filter ? attribute : null
	);

	const result = useQuery<
		EventAttributeDefinitionsData,
		EventAttributeDefinitionsVariables
	>(EVENT_ATTRIBUTE_DEFINITIONS_QUERY, {
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

	return (
		<BaseDropdown
			className='event-analysis-editor-attribute-dropdown-root'
			onActiveChange={active => {
				if (!active) {
					setAttributeOwnerType(AttributeOwnerTypes.Event);
					setQuery('');
					setSelectedAttribute(filter ? attribute : null);
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
									}) => (
										<BaseDropdown.SearchableList
											activeId={oldAttributeId}
											disabledIds={disabledIds}
											items={eventAttributeDefinitions}
											onEditClick={(
												attribute: Attribute
											) => {
												open(
													modalTypes.EDIT_ATTRIBUTE_EVENT_MODAL,
													{
														id: attribute.id,
														mutation: UPDATE_EVENT_ATTRIBUTE_DEFINITION,
														onCancel: close,
														query: EVENT_ATTRIBUTE_DEFINITION_QUERY,
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
													BREAKDOWN_FNS_MAP[dataType];

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
											onItemFilterClick={
												setSelectedAttribute
											}
											onQueryChange={setQuery}
											query={query}
										/>
									)}
								</SafeResults>
							</div>
						</CSSTransition>
					)}

					{selectedAttribute && (
						<CSSTransition
							classNames='transition-attribute-carousel-left'
							timeout={250}
						>
							<div className='w-100'>
								<AttributeFilter
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
															query: EVENT_ATTRIBUTE_DEFINITION_QUERY,
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

export default connect(null, {close, open})(AttributeDropdown);
