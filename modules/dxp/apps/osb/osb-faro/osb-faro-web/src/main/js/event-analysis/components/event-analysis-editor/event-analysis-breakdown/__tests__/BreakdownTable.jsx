jest.mock('test/data', () => ({
	getDummyBreakdownData: jest.fn(() => ({
		breakdownItems: [
			{
				breakdownItems: [
					{
						breakdownItems: [
							{
								name: 'All Individuals',
								previousValue: 2633,
								value: 1717
							}
						],
						isLeafNode: true,
						name: 'View Article',
						previousValue: 5033,
						value: 3367
					}
				],
				isLeafNode: false,
				name: 'articleTitle [0]',
				previousValue: 5033,
				value: 3367
			}
		],
		count: 1,
		totalEvents: 5033
	}))
}));

import BreakdownTable from '../index';
import React from 'react';
import {AttributesContext} from '../../context/attributes';
import {getDummyBreakdownData} from 'test/data';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

const initialAttributes = {
	attributes: {
		1: {
			defaultDataType: 'boolean',
			id: '1',
			name: 'booleanName'
		}
	},
	breakdowns: {
		1: {
			attributeId: '1',
			dataType: 'boolean',
			type: 'event'
		}
	},
	filters: {
		1: {
			attributeId: '1',
			operator: 'eq',
			value: ['true']
		}
	},
	order: ['1']
};

jest.unmock('react-dom');

describe('BreakdownTable', () => {
	const event = {name: 'View Article'};

	it('render', () => {
		const {container} = render(
			<StaticRouter>
				<AttributesContext.Provider value={initialAttributes}>
					<BreakdownTable
						compareToPrevious
						event={event}
						rangeSelectors={{
							rangeKey: '30'
						}}
					/>
				</AttributesContext.Provider>
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});

	it('render with single event', () => {
		getDummyBreakdownData.mockReturnValueOnce({
			breakdownItems: [
				{
					breakdownItems: [
						{
							name: 'All Individuals',
							previousValue: 2633,
							value: 1717
						}
					],
					isLeafNode: true,
					name: 'View Article',
					previousValue: 5033,
					value: 3367
				}
			],
			count: 1,
			totalEvents: 5033
		});

		const {container} = render(
			<BreakdownTable
				compareToPrevious
				event={event}
				rangeSelectors={{
					rangeKey: '30'
				}}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('render with empty state', () => {
		const {queryByText} = render(
			<BreakdownTable
				compareToPrevious={false}
				event={null}
				rangeSelectors={{
					rangeKey: '30'
				}}
			/>
		);

		expect(queryByText('Add an event to analyze.')).toBeTruthy();
	});
});
