import AttributeFilter from './filter';
import BaseDropdown from '../base-dropdown';
import EVENT_ATTRIBUTE_DEFINITION_QUERY from 'event-analysis/queries/EventAttributeDefinitionQuery';
import EVENT_ATTRIBUTE_DEFINITIONS_QUERY from 'event-analysis/queries/EventAttributeDefinitionsQuery';
import React, {useState} from 'react';
import {AddAttribute, EditAttribute} from '../../context/attributes';
import {Attribute, AttributeTypes, Filter} from 'event-analysis/utils/types';
import {BREAKDOWN_FNS_MAP} from 'event-analysis/utils/utils';
import {close, modalTypes, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {Modal} from 'shared/types';
import {SafeResults} from 'shared/hoc/util';
import {useQuery} from '@apollo/react-hooks';

interface IAttributeDropdownProps {
	attribute?: Attribute;
	close: Modal.close;
	disabledIds: string[];
	filter?: Filter;
	onAttributeSelect: AddAttribute | EditAttribute;
	open: Modal.open;
	trigger: React.ReactElement;
}

const AttributeDropdown: React.FC<IAttributeDropdownProps> = ({
	attribute,
	close,
	disabledIds,
	filter,
	onAttributeSelect,
	open,
	trigger
}) => {
	const [attributeType, setAttributeType] = useState<AttributeTypes>(
		AttributeTypes.Event
	);
	const [query, setQuery] = useState('');
	const [selectedAttribute, setSelectedAttribute] = useState<Attribute>(
		filter ? attribute : null
	);

	const result = useQuery(EVENT_ATTRIBUTE_DEFINITIONS_QUERY, {
		variables: {
			keyword: '',
			page: 0,
			size: 200
		}
	});

	const oldAttributeId = attribute ? attribute.id : null;

	return (
		<BaseDropdown
			className='event-analysis-editor-attribute-dropdown-root'
			onActiveChange={active => {
				if (!active) {
					setAttributeType(AttributeTypes.Event);
					setQuery('');
					setSelectedAttribute(filter ? attribute : null);
				}
			}}
			trigger={trigger}
		>
			{({setActive}) => (
				<>
					{!selectedAttribute && (
						<>
							<BaseDropdown.Header
								activeTabId={attributeType}
								tabs={[
									{
										onClick: () =>
											setAttributeType(
												AttributeTypes.Event
											),
										tabId: AttributeTypes.Event,
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
										onEditClick={(attribute: Attribute) => {
											open(
												modalTypes.EDIT_ATTRIBUTE_EVENT_MODAL,
												{
													id: attribute.id,
													onCancel: close,
													query: EVENT_ATTRIBUTE_DEFINITION_QUERY,
													showTypecast: true
												}
											);

											setActive(false);
										}}
										onItemClick={(attribute: Attribute) => {
											const {
												defaultDataType,
												id: attributeId
											} = attribute;

											const breakdownFn =
												BREAKDOWN_FNS_MAP[
													defaultDataType
												];

											onAttributeSelect({
												attribute,
												attributeId,
												breakdown: breakdownFn({
													attributeId,
													type: attributeType
												}),
												oldAttributeId
											});

											setActive(false);
										}}
										onItemFilterClick={setSelectedAttribute}
										onQueryChange={setQuery}
										query={query}
									/>
								)}
							</SafeResults>
						</>
					)}

					{selectedAttribute && (
						<AttributeFilter
							attribute={selectedAttribute}
							attributeType={attributeType}
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
													id: selectedAttribute.id,
													onCancel: close,
													query: EVENT_ATTRIBUTE_DEFINITION_QUERY,
													showTypecast: true
												}
											);

											setActive(false);
									  }
							}
						/>
					)}
				</>
			)}
		</BaseDropdown>
	);
};

export default connect(
	null,
	{close, open}
)(AttributeDropdown);
