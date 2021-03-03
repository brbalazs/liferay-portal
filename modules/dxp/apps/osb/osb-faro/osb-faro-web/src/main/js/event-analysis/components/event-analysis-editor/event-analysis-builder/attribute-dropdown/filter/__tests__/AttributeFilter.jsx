import AttributeFilter from '../index';
import React from 'react';
import {render} from '@testing-library/react';
import {withAttributesProvider} from '../../../../context/attributes';

jest.unmock('react-dom');

describe('AttributeFilter', () => {
	it('should render', () => {
		const WrappedAttributeFilter = withAttributesProvider(AttributeFilter);

		const {container} = render(
			<WrappedAttributeFilter
				attribute={{
					dataType: 'string',
					displayName: 'Filed Ticket',
					id: '4',
					name: 'filedTicket'
				}}
				onActiveChange={jest.fn()}
				onAttributeChange={jest.fn()}
				onEditClick={jest.fn()}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
