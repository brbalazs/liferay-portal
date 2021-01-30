import AttributesList from './AttributesList';
import BreakdownFilter from './filter';
import ClayDropdown, {Align} from '@clayui/drop-down';
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

interface IBreakdownDropdownProps {
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

const BreakdownDropdown: React.FC<IBreakdownDropdownProps> = ({
	attributes,
	breakdowns,
	filters,
	onBreakdownChange,
	trigger
}) => {
	const [active, setActive] = useState(false);
	const [attributesList, setAttributesList] = useState<Attribute[]>([]); //TODO: Remove one we have actual requests
	const [attributeType, setAttributeType] = useState<AttributeTypes>(
		AttributeTypes.Event
	);
	const [dataType, setDataType] = useState<DataTypes>();
	const [query, setQuery] = useState('');
	const [selectedAttribute, setSelectedAttribute] = useState<Attribute>(null);

	useEffect(() => {
		Promise.resolve(MOCKED_MAP[attributeType]).then(response =>
			setAttributesList(response)
		);
	}, [attributeType]);

	const handleChange = () => {};

	return (
		<ClayDropdown
			active={active}
			alignmentPosition={Align.RightTop}
			menuElementAttrs={{className: 'event-analysis-dropdown-menu-root'}}
			onActiveChange={setActive}
			trigger={trigger}
		>
			{!selectedAttribute && (
				<AttributesList
					attributesList={attributesList}
					attributeType={attributeType}
					onAttributeTypeChange={setAttributeType}
					onSelectedAttributeChange={setSelectedAttribute}
					onQueryChange={setQuery}
					query={query}
				/>
			)}

			{selectedAttribute && (
				<BreakdownFilter
					attribute={selectedAttribute}
					onAttributeChange={setSelectedAttribute}
				/>
			)}
		</ClayDropdown>
	);
};

export default BreakdownDropdown;
