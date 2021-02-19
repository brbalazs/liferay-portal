import Card from 'shared/components/Card';
import CodeSnippet from 'shared/components/CodeSnippet';
import Label from 'shared/components/form/Label';
import React, {useEffect, useState} from 'react';
import Table from 'shared/components/table';
import {Attribute} from 'event-analysis/components/event-analysis-editor/types';
import {attributesColumns} from 'shared/util/table-columns';
import {OrderedMap} from 'immutable';

// TODO: LRAC-7486 Use eventId to fetch related attributes
const MOCKED_ATTRIBUTES = [
	{
		attributeId: 'myid1',
		defaultDataType: 'TYPE',
		description: 'mydescription',
		displayName: 'displayNamehere',
		id: 'myid1',
		name: 'firstTest',
		sampleValue: '1'
	},
	{
		attributeId: 'myid2',
		defaultDataType: 'TYPE2',
		description: 'seconddescription',
		displayName: 'seconddisplay',
		id: 'myid2',
		name: 'testingtest',
		sampleValue: '2'
	},
	{
		attributeId: 'myid3',
		defaultDataType: 'TYPE3',
		description: 'mydescription',
		displayName: 'displayNamehere',
		id: 'myid3',
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
		`Analytics.send('${eventName}', {`,
		'});'
	]);
	const [selectedAttributes, setSelectedAttributes] = useState(
		new Map<String, Attribute>()
	);

	useEffect(() => {
		const attributesRepresentations = [];

		selectedAttributes.forEach(({name, sampleValue}) => {
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
			new Map(selectedAttributes).set(attribute.id, attribute)
		);

	const removeSelectedAttribute = (key: string): void => {
		setSelectedAttributes(previous => {
			const newSelectedAttributes = new Map(previous);
			newSelectedAttributes.delete(key);
			return newSelectedAttributes;
		});
	};

	return (
		<Card key='cardContainer' pageDisplay>
			<Card.Header>
				<Card.Title>{'Send This Event'}</Card.Title>
			</Card.Header>
			<Card.Body>
				<span className='mt-2 mb-4 w-50'>
					{Liferay.Language.get(
						'use-this-script-to-start-sending-events-to-analytics-cloud.-you-can-customize-which-attributes-to-send-with-a-specific-event.-selecting-the-attributes-below-will-generate-a-new-sample-script'
					)}
				</span>

				<Label>{Liferay.Language.get('sample-javascript')}</Label>
				<CodeSnippet codeLines={codeLines}></CodeSnippet>
			</Card.Body>

			<Table
				className='mb-0'
				columns={[
					attributesColumns.getName({
						channelId: 'channelId',
						groupId
					}),
					attributesColumns.displayName,
					attributesColumns.description,
					attributesColumns.sampleValue,
					attributesColumns.defaultDataType
				]}
				items={MOCKED_ATTRIBUTES}
				onSelectItemsChange={selectedAttribute =>
					selectedAttributes.has(selectedAttribute.id)
						? removeSelectedAttribute(selectedAttribute.id)
						: addSelectedAttribute(selectedAttribute)
				}
				rowIdentifier='id'
				selectedItemsIOMap={OrderedMap(selectedAttributes)}
				showCheckbox
			/>
		</Card>
	);
};

export default EventDetailsCard;
