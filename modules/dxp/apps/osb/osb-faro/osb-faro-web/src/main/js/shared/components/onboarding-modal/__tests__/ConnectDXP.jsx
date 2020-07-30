import * as API from 'shared/api';
import ConnectDXP from '../ConnectDXP';
import mockStore from 'test/mock-store';
import Promise from 'metal-promise';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {noop} from 'lodash';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

describe('ConnectDXP', () => {
	afterEach(cleanup);

	it('renders', () => {
		const {container} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<ConnectDXP groupId='123' onClose={noop} onNext={noop} />
				</StaticRouter>
			</Provider>
		);

		expect(container).toMatchSnapshot();
	});

	it('renders "Connected" when dxpConnected is true', () => {
		const {queryByText} = render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<ConnectDXP
						dxpConnected
						groupId='123'
						onClose={noop}
						onNext={noop}
					/>
				</StaticRouter>
			</Provider>
		);

		expect(queryByText('Back')).toBeNull();
		expect(queryByText('Connected')).not.toBeNull();
	});

	it('fires "setDxpConnected" when the token value changes', () => {
		const spy = jest.fn();

		render(
			<Provider store={mockStore()}>
				<StaticRouter>
					<ConnectDXP
						groupId='123'
						onboarding
						onClose={noop}
						onDxpConnected={spy}
						onNext={noop}
					/>
				</StaticRouter>
			</Provider>
		);

		expect(spy).not.toBeCalled();

		jest.runOnlyPendingTimers();

		API.dataSource.fetchToken.mockReturnValue(
			Promise.resolve('New Token Value')
		);

		jest.runOnlyPendingTimers();
		jest.runOnlyPendingTimers();

		expect(spy).toBeCalled();
	});
});
