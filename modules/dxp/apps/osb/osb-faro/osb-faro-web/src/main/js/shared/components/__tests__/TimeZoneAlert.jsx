import mockStore from 'test/mock-store';
import React from 'react';
import TimeZoneAlert from '../TimeZoneAlert';
import {cleanup, render} from '@testing-library/react';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('TimeZoneAlert', () => {
	afterEach(cleanup);
	// Updates the snapshot when add the request of TimeZoneAlert LRAC-6961
	it('should render', () => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<TimeZoneAlert />
				</StaticRouter>
			</Provider>
		);
		expect(container).toMatchSnapshot();
	});
});
