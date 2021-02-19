import * as API from 'shared/api';
import * as data from 'test/data';
import Activities from '../Activities';
import mockStore from 'test/mock-store';
import Promise from 'metal-promise';
import React from 'react';
import {Account} from 'shared/util/records';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const DefaultComponent = props => (
	<StaticRouter>
		<Provider store={mockStore()}>
			<Activities
				account={data.getImmutableMock(Account, data.mockAccount)}
				channelId='123123'
				groupId='23'
				interval='D'
				rangeSelectors={{rangeKey: 30}}
				{...props}
			/>
		</Provider>
	</StaticRouter>
);

describe('Activities', () => {
	it('should render', () => {
		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render with error display', () => {
		API.activities.fetchHistory.mockReturnValueOnce(Promise.reject({}));

		const {getByText} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(getByText('An unexpected error occurred.')).toBeTruthy();
	});
});
