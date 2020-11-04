import React from 'react';
import TimeZoneAdminModal from '../TimeZoneAdminModal';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('TimeZoneAdminModal', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<TimeZoneAdminModal />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
