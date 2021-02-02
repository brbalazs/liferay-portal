import AttributeChip from '../AttributeChip';
import React from 'react';
import {render} from '@testing-library/react';
import {wrapInTestContext} from 'react-dnd-test-utils';

jest.unmock('react-dom');

describe('AttributeChip', () => {
	const AttributeChipContext = wrapInTestContext(AttributeChip);

	it('render', () => {
		const {container} = render(
			<AttributeChipContext
				attribute={{
					displayName: 'Article View',
					id: '123123',
					name: 'articleView'
				}}
				breakdown={{
					attributeId: '123123',
					dataType: 'string',
					type: 'event'
				}}
				filter={{
					attributeId: '123123',
					operator: 'eq',
					value: ['Stuff']
				}}
				index={1}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
