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
					dataType: 'string',
					displayName: 'Article View',
					id: '0',
					name: 'articleView'
				}}
				breakdown={{
					attributeId: '0',
					dataType: 'string',
					type: 'event'
				}}
				filter={{
					attributeId: '0',
					operator: 'eq',
					value: ['Stuff']
				}}
				index={1}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
