import Cell from '../Cell';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Cell', () => {
	it('should render', () => {
		const {container} = render(<Cell />);
		expect(container).toMatchSnapshot();
	});

	it('should render as a table title', () => {
		const {container} = render(<Cell title />);
		expect(container).toMatchSnapshot();
	});
});
