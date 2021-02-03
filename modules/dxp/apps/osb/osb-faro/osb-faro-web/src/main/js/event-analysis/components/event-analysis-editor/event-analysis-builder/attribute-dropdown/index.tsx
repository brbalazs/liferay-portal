import BaseDropdown from '../base-dropdown';
import BreakdownFilter from './filter';
import Promise from 'metal-promise';
import React, {useEffect, useState} from 'react';
import {
	Attribute,
	AttributeTypes,
	Breakdown,
	DataTypes,
	Filter
} from '../../types';

// TODO: Replace Mock Data
const MOCKED_EVENT_ATTRIBUTE_LIST = [
	{
		defaultDataType: DataTypes.Boolean,
		displayName: 'Form Submit',
		id: '3',
		name: 'formSubmit',
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
	attributes: Attribute[];
	breakdowns: Breakdown[];
	filters: Filter[];
	onBreakdownChange: (
		attribute: Attribute,
		breakdown: Breakdown,
		filter: Filter
	) => void;
	trigger: React.ReactElement;
}

const AttributeDropdown: React.FC<IAttributeDropdownProps> = ({
	attributes,
	breakdowns,
	filters,
	onBreakdownChange,
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
								items={attributesList}
								onItemClick={() => {
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
