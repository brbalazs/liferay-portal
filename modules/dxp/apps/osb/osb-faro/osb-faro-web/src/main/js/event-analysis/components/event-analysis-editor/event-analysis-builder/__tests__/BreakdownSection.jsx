import BreakdownSection from '../BreakdownSection';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('BreakdownSection', () => {
	it('render', () => {
		const {container} = render(
			<BreakdownSection
				attributes={[]}
				breakdowns={[]}
				filters={[]}
				onAttributesChange={jest.fn()}
				onBreakdownsChange={jest.fn()}
				onFiltersChange={jest.fn()}
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('render with breakdown & filter', () => {
		const {container} = render(
			<BreakdownSection
				attributes={[
					{
						displayName: 'Article Title',
						id: '321321',
						name: 'articleTitle'
					},
					{
						displayName: 'Job Title',
						id: '123123',
						name: 'jobTitle'
					}
				]}
				breakdowns={[
					{
						attributeId: '321321',
						dataType: 'string',
						type: 'event'
					},
					{
						attributeId: '123123',
						dataType: 'string',
						type: 'event'
					}
				]}
				filters={[
					{
						attributeId: '123123',
						operator: 'eq',
						value: ['Stuff']
					}
				]}
				onAttributesChange={jest.fn()}
				onBreakdownsChange={jest.fn()}
				onFiltersChange={jest.fn()}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
