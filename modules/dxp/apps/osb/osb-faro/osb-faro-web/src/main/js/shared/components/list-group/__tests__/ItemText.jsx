import ItemText from '../ItemText';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('ItemText', () => {
	it('should render', () => {
		const {container} = render(<ItemText />);

		expect(container).toMatchSnapshot();
	});
});
