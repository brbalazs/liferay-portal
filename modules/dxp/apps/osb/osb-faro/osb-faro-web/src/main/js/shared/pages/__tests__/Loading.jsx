import Loading from '../Loading';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Loading', () => {
	it('should render', () => {
		const {container} = render(<Loading />);

		expect(container).toMatchSnapshot();
	});
});
