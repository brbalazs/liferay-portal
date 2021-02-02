import AttributeSection from '../AttributeSection';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('AttributeSection', () => {
	it('render', () => {
		const {container} = render(
			<AttributeSection
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
			<AttributeSection
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
