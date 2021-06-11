import * as data from 'test/data';
import EventInput from '../EventInput';
import React from 'react';
import {AttributeTypes} from 'event-analysis/utils/types';
import {createNewGroup} from '../../utils/utils';
import {CustomValue, Property} from 'shared/util/records';
import {fromJS} from 'immutable';
import {MockedProvider} from '@apollo/react-testing';
import {mockEventAttributeDefinitionsReq} from 'test/graphql-data';
import {range} from 'lodash';
import {RelationalOperators} from '../../utils/constants';
import {render} from '@testing-library/react';
import {waitForLoading} from 'test/helpers';

jest.unmock('react-dom');

describe('EventInput', () => {
	it('should render', async () => {
		const {container} = render(
			<MockedProvider
				mocks={[
					mockEventAttributeDefinitionsReq(
						range(10).map(i =>
							data.mockEventAttributeDefinition(i, {
								__typename: 'EventAttributeDefinition'
							})
						),
						{
							eventDefinitionId: '3',
							size: 25,
							type: AttributeTypes.Global
						}
					)
				]}
			>
				<EventInput
					displayValue='Asset Clicked'
					onChange={jest.fn()}
					operatorRenderer={() => <div>{'test'}</div>}
					property={Property({
						entityName: 'Event',
						id: '3',
						label: 'assetDepthReached',
						name: '3',
						propertyKey: 'event',
						type: 'event'
					})}
					touched={{attribute: true, attributeValue: true}}
					valid={{attribute: true, attributeValue: true}}
					value={CustomValue(
						fromJS({
							criterionGroup: createNewGroup([
								{
									operatorName: 'eq',
									propertyName: 'eventDefinitionId',
									value: '1'
								},
								{
									operatorName: 'contains',
									propertyName: 'attribute/2',
									value: ''
								},
								{
									operatorName: 'gt',
									propertyName: 'day',
									value: 'last24Hours'
								}
							]),
							operator: RelationalOperators.GT,
							value: 1
						})
					)}
				/>
			</MockedProvider>
		);

		await waitForLoading(container);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
