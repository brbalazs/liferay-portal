import EventAnalysisBuilder from '../index';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Event Analysis Builder', () => {
	it('render', () => {
		const {container} = render(<EventAnalysisBuilder />);

		expect(container).toMatchSnapshot();
	});

	it('render with filters & breakdowns', () => {
		const {container} = render(
			<EventAnalysisBuilder
				attributes={[
					{
						id: '321321',
						name: 'Article Title'
					},
					{
						id: '123123',
						name: 'Job Title'
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
				event={{
					id: '123123',
					name: 'Article Views',
					type: 'custom'
				}}
				filters={[
					{
						attributeId: '123123',
						operator: 'eq',
						value: ['Stuff']
					}
				]}
				onAttributesChange={jest.fn()}
				onBreakdownsChange={jest.fn()}
				onEventChange={jest.fn()}
				onFiltersChange={jest.fn()}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
