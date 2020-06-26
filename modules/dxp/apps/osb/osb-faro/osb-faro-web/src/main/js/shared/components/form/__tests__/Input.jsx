import Input from '../Input';
import React from 'react';
import {mockForm} from 'test/data';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Input', () => {
	
	it('should render', () => {
		const {container} = render(
			<Input field={{name: 'foo'}} form={mockForm()} />
		);

		expect(container).toMatchSnapshot();
	});

	it('should render a masked input', () => {
		const {container} = render(
			<Input field={{name: 'foo'}} form={mockForm()} mask={[]} />
		);

		expect(container).toMatchSnapshot();
	});
});
