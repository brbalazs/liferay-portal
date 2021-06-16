import React from 'react';
import StringFilter from '../StringFilter';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('StringFilter', () => {
	it('should render', () => {
		const {container} = render(<StringFilter onFilterSubmit={jest.fn()} />);

		expect(container).toMatchSnapshot();
	});
});
