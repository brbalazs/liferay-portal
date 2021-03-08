import Card from 'shared/components/Card';
import CodeSnippet from 'shared/components/CodeSnippet';
import Label from 'shared/components/form/Label';
import React, {useEffect, useState} from 'react';
import Table from 'shared/components/table';
import {Attribute} from 'event-analysis/utils/types';
import {attributeListColumns} from 'shared/util/table-columns';
import {Map, OrderedMap} from 'immutable';

// TODO: LRAC-7486 Use eventId to fetch related attributes
const MOCKED_ATTRIBUTES = [
	{
		attributeId: '4486',
		defaultDataType: 'TYPE',
		description: 'mydescription',
		displayName: 'displayNamehere',
		id: '4486',
		name: 'firstTest',
		sampleValue: '1'
	},
	{
		attributeId: '4588',
		defaultDataType: 'TYPE2',
		description: 'seconddescription',
		displayName: 'seconddisplay',
		id: '4588',
		name: 'testingtest',
		sampleValue: '2'
	},
	{
		attributeId: '4581',
		defaultDataType: 'TYPE3',
		description: '',
		displayName: 'displayNamehere',
		id: '4581',
		name: 'anothernamet',
		sampleValue: '3'
	}
];

interface IEventDetailsCardProps {
	eventName: string;
	groupId: string;
}

// TODO: LRAC-7486 Receive eventId when able to fetch the attributes
const EventDetailsCard: React.FC<IEventDetailsCardProps> = ({
	eventName,
	groupId
}) => {
	const [codeLines, setCodeLines] = useState([
		`Analytics.track('${eventName}', {`,
		'});'
	]);

	const [selectedAttributes, setSelectedAttributes] = useState(
		OrderedMap<string, Map<string, string>>(
			MOCKED_ATTRIBUTES.map(
				attribute =>
					[attribute.id, Map(attribute)] as [
						string,
						Map<string, string>
					]
			)
		)
	);

	useEffect(() => {
		const attributesRepresentations = [];

		selectedAttributes.forEach(attribute => {
			const name = attribute.get('name');
			const sampleValue = attribute.get('sampleValue');

			attributesRepresentations.push(`'${name}': '${sampleValue}',`);
		});

		setCodeLines([
			codeLines[0],
			...attributesRepresentations,
			codeLines[codeLines.length - 1]
		]);
	}, [selectedAttributes]);

	const addSelectedAttribute = (attribute: Attribute): void =>
		setSelectedAttributes(
			selectedAttributes.set(attribute.id, Map(attribute))
		);

	const removeSelectedAttribute = (key: string): void => {
		setSelectedAttributes(previous => previous.remove(key));
	};

	return (
		<Card key='cardContainer'>
			<Card.Header>
				<Card.Title>
					{Liferay.Language.get('send-this-event')}
				</Card.Title>
			</Card.Header>

			<Card.Body>
				<span className='mt-2 mb-4 w-50'>
					{Liferay.Language.get(
						'use-this-script-to-start-sending-events-to-analytics-cloud.-you-can-customize-which-attributes-to-send-with-a-specific-event.-selecting-the-attributes-below-will-generate-a-new-sample-script'
					)}
				</span>

				<Label>{Liferay.Language.get('sample-javascript-colon')}</Label>

				<CodeSnippet codeLines={codeLines} />
			</Card.Body>

			<Table
				className='mb-0'
				columns={[
					attributeListColumns.getName({
						channelId: 'channelId',
						groupId
					}),
					attributeListColumns.displayName,
					attributeListColumns.description,
					attributeListColumns.sampleValue,
					attributeListColumns.defaultDataType
				].map(column => ({...column, sortable: false}))}
				items={MOCKED_ATTRIBUTES}
				onSelectItemsChange={selectedAttribute =>
					selectedAttributes.has(selectedAttribute.id)
						? removeSelectedAttribute(selectedAttribute.id)
						: addSelectedAttribute(selectedAttribute)
				}
				rowIdentifier='id'
				selectedItemsIOMap={selectedAttributes}
				showCheckbox
			/>
		</Card>
	);
};

export default EventDetailsCard;
