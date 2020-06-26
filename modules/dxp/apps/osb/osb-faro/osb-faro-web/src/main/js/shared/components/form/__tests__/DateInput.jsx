import DateInput from '../DateInput';
import React from 'react';
import {mockForm} from 'test/data';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('DateInput', () => {
	it('should render', () => {
		const {container} = render(
			<DateInput field={{name: 'foo'}} form={mockForm()} />
		);

		expect(container).toMatchSnapshot();
	});
});
