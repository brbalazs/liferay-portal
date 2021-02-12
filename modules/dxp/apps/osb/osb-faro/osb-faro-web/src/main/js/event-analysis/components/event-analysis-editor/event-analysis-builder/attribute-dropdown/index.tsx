import AttributeFilter from './filter';
import BaseDropdown from '../base-dropdown';
import Promise from 'metal-promise';
import React, {useEffect, useState} from 'react';
import {AddAttribute, EditAttribute} from '../../context/attributes';
import {Attribute, AttributeTypes, DataTypes, Filter} from '../../types';
import {BREAKDOWN_FNS_MAP} from '../../utils';

// TODO: LRAC-7466: Connect API
const MOCKED_EVENT_ATTRIBUTE_LIST = [
	{
		defaultDataType: DataTypes.Duration,
		displayName: 'Time on Page',
		id: '1',
		name: 'timeOnPage',
		type: AttributeTypes.Event
	},
	{
		defaultDataType: DataTypes.Date,
		displayName: 'Date Created',
		id: '2',
		name: 'dateCreated',
		type: AttributeTypes.Event
	},
	{
		defaultDataType: DataTypes.Boolean,
		displayName: 'Answered',
		id: '3',
		name: 'answered',
		type: AttributeTypes.Event
	},
	{
		defaultDataType: DataTypes.String,
		displayName: 'Filed Ticket',
		id: '4',
		name: 'filedTicket',
		type: AttributeTypes.Event
	},
	{
		defaultDataType: DataTypes.Number,
		displayName: 'Read Article',
		id: '5',
		name: 'readArticle',
		type: AttributeTypes.Event
	}
];

const MOCKED_MAP = {
	[AttributeTypes.Event]: MOCKED_EVENT_ATTRIBUTE_LIST
};

interface IAttributeDropdownProps {
	attribute?: Attribute;
	disabledIds: string[];
	filter?: Filter;
	onAttributeSelect: AddAttribute | EditAttribute;
	trigger: React.ReactElement;
}

const AttributeDropdown: React.FC<IAttributeDropdownProps> = ({
	attribute,
	disabledIds,
	filter,
	onAttributeSelect,
	trigger
}) => {
	const [attributesList, setAttributesList] = useState<Attribute[]>([]); // TODO: LRAC-7466: Remove one we have actual requests
	const [attributeType, setAttributeType] = useState<AttributeTypes>(
		AttributeTypes.Event
	);
	const [query, setQuery] = useState('');
	const [selectedAttribute, setSelectedAttribute] = useState<Attribute>(
		filter ? attribute : null
	);

	useEffect(() => {
		const mockRequest = Promise.resolve(MOCKED_MAP[attributeType]).then(
			response => setAttributesList(response)
		);

		return () => mockRequest.cancel();
	}, [attributeType]);

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

							<BaseDropdown.SearchableList
								activeId={oldAttributeId}
								disabledIds={disabledIds}
								items={attributesList}
								onEditClick={() => {
									// TODO: LRAC-7407 Connect to edit modal

									setActive(false);
								}}
								onItemClick={(attribute: Attribute) => {
									const {
										defaultDataType,
										id: attributeId
									} = attribute;

									const breakdownFn =
										BREAKDOWN_FNS_MAP[defaultDataType];

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
							onEditClick={() => {
								// TODO: LRAC-7407 Connect to edit modal

								setActive(false);
							}}
						/>
					)}
				</>
			)}
		</BaseDropdown>
	);
};

export default AttributeDropdown;
