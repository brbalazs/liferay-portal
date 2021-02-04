import BaseDropdown from '../base-dropdown';
import BreakdownFilter from './filter';
import Promise from 'metal-promise';
import React, {useEffect, useState} from 'react';
import {AddAttribute, EditAttribute} from '../../context/attributes';
import {
	Attribute,
	AttributeTypes,
	Breakdown,
	DataTypes,
	Filter
} from '../../types';
import {BREAKDOWN_FNS_MAP} from '../../utils';

// TODO: Replace Mock Data
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
	disabledIds: string[];
	onAttributeSelect: AddAttribute | EditAttribute;
	trigger: React.ReactElement;
}

const AttributeDropdown: React.FC<IAttributeDropdownProps> = ({
	disabledIds,
	onAttributeSelect,
	trigger
}) => {
	const [attributesList, setAttributesList] = useState<Attribute[]>([]); //TODO: Remove one we have actual requests
	const [attributeType, setAttributeType] = useState<AttributeTypes>(
		AttributeTypes.Event
	);
	const [query, setQuery] = useState('');
	const [selectedAttribute, setSelectedAttribute] = useState<Attribute>(null);

	useEffect(() => {
		Promise.resolve(MOCKED_MAP[attributeType]).then(response =>
			setAttributesList(response)
		);
	}, [attributeType]);

	const handleChange = () => {};

	return (
		<BaseDropdown trigger={trigger}>
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
								disabledIds={disabledIds}
								items={attributesList}
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
										})
									});

									setActive(false);
									/** DO SOMETHING HERE **/
								}}
								onItemFilterClick={setSelectedAttribute}
								onQueryChange={setQuery}
								query={query}
							/>
						</>
					)}

					{selectedAttribute && (
						<BreakdownFilter
							attribute={selectedAttribute}
							onAttributeChange={setSelectedAttribute}
						/>
					)}
				</>
			)}
		</BaseDropdown>
	);
};

export default AttributeDropdown;
