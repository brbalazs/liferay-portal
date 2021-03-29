import HelperWidget from '../HelperWidget';
import React from 'react';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('HelperWidget', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<HelperWidget />);
		expect(container).toMatchSnapshot();
	});
});
