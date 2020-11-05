import * as data from 'test/data';
import React from 'react';
import TimeZoneSelectionModal from '../TimeZoneSelectionModal';
import {cleanup, render} from '@testing-library/react';
import {mockGetDateNow} from 'test/mock-date';

jest.unmock('react-dom');

const TIME_ZONE = {
	timeZoneId: 'UTC'
};

describe('TimeZoneSelectionModal', () => {
	beforeAll(() => {
		mockGetDateNow(data.getTimestamp(0));
	});

	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<TimeZoneSelectionModal timeZone={TIME_ZONE} />
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
