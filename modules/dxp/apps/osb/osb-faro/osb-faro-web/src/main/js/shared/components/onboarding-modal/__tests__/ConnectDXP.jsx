import * as API from 'shared/api';
import ConnectDXP from '../ConnectDXP';
import Promise from 'metal-promise';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {noop} from 'lodash';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

describe('ConnectDXP', () => {
	afterEach(cleanup);

	it('renders', () => {
		const {container} = render(
			<StaticRouter>
				<ConnectDXP groupId='123' onClose={noop} onNext={noop} />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});

	it('renders "Connected" when dxpConnected is true', () => {
		const {queryByText} = render(
			<StaticRouter>
				<ConnectDXP
					dxpConnected
					groupId='123'
					onClose={noop}
					onNext={noop}
				/>
			</StaticRouter>
		);

		expect(queryByText('Back')).toBeNull();
		expect(queryByText('Connected')).not.toBeNull();
	});

	it('fires "setDxpConnected" when the token value changes', () => {
		const spy = jest.fn();

		render(
			<StaticRouter>
				<ConnectDXP
					groupId='123'
					onboarding
					onClose={noop}
					onDxpConnected={spy}
					onNext={noop}
				/>
			</StaticRouter>
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
