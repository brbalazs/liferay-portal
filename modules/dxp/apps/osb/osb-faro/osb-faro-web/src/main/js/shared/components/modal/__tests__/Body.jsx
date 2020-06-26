import Body from '../Body';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Modal Body', () => {
	it('should render', () => {
		const {container} = render(<Body />);

		expect(container).toMatchSnapshot();
	});
});
