import Item from '../Item';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Item', () => {
	
	it('should render', () => {
		const {container} = render(<Item />);

		expect(container).toMatchSnapshot();
	});

	it('should render as active', () => {
		const {container} = render(<Item active />);

		expect(container).toMatchSnapshot();
	});
});
