import * as data from 'test/data';
import BaseDetails from '../BaseDetails';
import Promise from 'metal-promise';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('BaseDetails', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<BaseDetails
					dataSourceFn={() =>
						Promise.resolve(data.mockAccountDetails())
					}
					groupId='23'
					id='test'
				/>
			</StaticRouter>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render w/o loading', () => {
		const {container} = render(
			<StaticRouter>
				<BaseDetails
					dataSourceFn={() =>
						Promise.resolve(data.mockAccountDetails())
					}
					groupId='23'
					id='test'
				/>
			</StaticRouter>
		);

		expect(container.querySelector('.loading-animation')).toBeTruthy();
	});

	it('should render w/ ErrorDisplay', () => {
		const {queryByText} = render(
			<StaticRouter>
				<BaseDetails
					dataSourceFn={() => Promise.reject({})}
					groupId='23'
					id='test'
				/>
			</StaticRouter>
		);

		jest.runAllTimers();

		expect(queryByText('Reload')).toBeTruthy();
	});
});
