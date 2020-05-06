import DXPConnectionType from '../DXPConnectionType';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {noop} from 'lodash';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('DXPConnectionType', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<DXPConnectionType groupId='123' onClose={noop} onNext={noop} />
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render w/ skip button when onboarding is true', () => {
		const spy = jest.fn();

		const {queryByText} = render(
			<StaticRouter>
				<DXPConnectionType
					groupId='123'
					onboarding
					onClose={noop}
					onNext={spy}
				/>
			</StaticRouter>
		);

		expect(queryByText('Cancel')).toBeNull();
		expect(queryByText('Skip')).toBeTruthy();
	});
});
