import AttributeListItem from './AttributeListItem';
import Button from 'shared/components/Button';
import CardTabs, {CardTabSizes} from 'shared/components/CardTabs';
import ClayDropdown, {Align} from '@clayui/drop-down';
import React, {useEffect, useState} from 'react';
import {
	Attribute,
	AttributeTypes,
	Breakdown,
	DataTypes,
	Filter
} from '../../types';
import {spritemap} from 'shared/util/constants';

interface IAttributesListProps {
	attributesList: Attribute[];
	attributeType: AttributeTypes;
	onAttributeTypeChange: (attributeType: AttributeTypes) => void;
	onQueryChange: (query: string) => void;
	onSelectedAttributeChange: (selectedAttribute: Attribute) => void;
	query: string;
}

const AttributesList: React.FC<IAttributesListProps> = ({
	attributesList,
	attributeType,
	onAttributeTypeChange,
	onQueryChange,
	onSelectedAttributeChange,
	query
}) => {
	const filteredAttributes = attributesList.filter(({displayName, name}) =>
		(displayName || name)
			.toString()
			.toLowerCase()
			.includes(query.toLowerCase())
	);

	return (
		<>
			<div className='event-analysis-dropdown-header'>
				{Liferay.Language.get('attributes')}
			</div>

			<CardTabs
				activeTabId={attributeType}
				className='event-type-selector'
				size={CardTabSizes.Small}
				tabs={[
					{
						onClick: () =>
							onAttributeTypeChange(AttributeTypes.Event),
						tabId: AttributeTypes.Event,
						title: Liferay.Language.get('event')
					}
				]}
			/>

			<ClayDropdown.Search
				formProps={{onSubmit: e => e.preventDefault()}}
				onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
					onQueryChange(event.target.value)
				}
				placeholder={Liferay.Language.get('search')}
				spritemap={spritemap}
				value={query}
			/>

			<ClayDropdown.ItemList>
				{filteredAttributes.map((attribute: Attribute) => (
					<AttributeListItem  attribute={attribute} key={attribute.id} onSelectedAttributeChange={onSelectedAttributeChange} />
				))}
			</ClayDropdown.ItemList>
		</>
	);
};

export default AttributesList;
