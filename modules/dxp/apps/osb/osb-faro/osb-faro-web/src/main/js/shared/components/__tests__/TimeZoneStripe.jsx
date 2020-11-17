import React from 'react';
import TimeZoneStripe from '../TimeZoneStripe';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('TimeZoneStripe', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<TimeZoneStripe />);
		expect(container).toMatchSnapshot();
	});
});
