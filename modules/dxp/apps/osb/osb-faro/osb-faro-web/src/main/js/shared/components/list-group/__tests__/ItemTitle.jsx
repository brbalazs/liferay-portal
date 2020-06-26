import ItemTitle from '../ItemTitle';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('ItemTitle', () => {
	it('should render', () => {
		const {container} = render(<ItemTitle />);

		expect(container).toMatchSnapshot();
	});
});
