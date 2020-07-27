import ItemField from '../ItemField';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('ItemField', () => {
	it('should render', () => {
		const {container} = render(<ItemField children='Children' />);

		expect(container).toMatchSnapshot();
	});
});
