import React from 'react';
import {attributesColumns} from 'shared/util/table-columns';
import {withBaseResults} from 'shared/hoc';

// TODO: LRAC-7330 Use the graphql query instead of mocked data
const withData = () => WrapperComponent => props => {
	const MOCKED_ITEMS = [
		{
			attributeId: 'myid1',
			defaultDataType: 'TYPE',
			description: 'mydescription',
			displayName: 'displayNamehere',
			name: 'firstTest',
			sampleValue: '1'
		},
		{
			attributeId: 'myid2',
			defaultDataType: 'TYPE2',
			description: 'seconddescription',
			displayName: 'seconddisplay',
			name: 'testingtest',
			sampleValue: '2'
		},
		{
			attributeId: 'myid3',
			defaultDataType: 'TYPE3',
			description: 'mydescription',
			displayName: 'displayNamehere',
			name: 'anothernamet',
			sampleValue: '3'
		}
	];
	return <WrapperComponent {...props} items={MOCKED_ITEMS} />;
};

const AttributeList = withBaseResults(withData, {
	getColumns: ({channelId, groupId}) => [
		attributesColumns.getName({channelId, groupId}),
		attributesColumns.displayName,
		attributesColumns.description,
		attributesColumns.sampleValue,
		attributesColumns.defaultDataType
	],
	rowIdentifier: 'attributeId',
	showDropdownRangeKey: false
});

export default AttributeList;
