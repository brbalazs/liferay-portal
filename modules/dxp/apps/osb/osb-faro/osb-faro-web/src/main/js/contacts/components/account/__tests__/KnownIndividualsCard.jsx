import * as data from 'test/data';
import KnownIndividualsCard from '../KnownIndividualsCard';
import Promise from 'metal-promise';
import React from 'react';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const mockIndividualWithEngagementHistory = () => ({
	...data.mockIndividual(),
	engagementHistory: data.mockEngagementData()
});

describe('KnownIndividualsCard', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<KnownIndividualsCard
					channelId={'123'}
					dataSourceFn={() => Promise.resolve()}
					groupId={'23'}
					id={'23'}
				/>
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render w/ NoResultsDisplay', () => {
		const dataSourceFn = () =>
			Promise.resolve(
				data.mockSearch(mockIndividualWithEngagementHistory, 0)
			);

		const {container} = render(
			<StaticRouter>
				<KnownIndividualsCard
					channelId={'123'}
					dataSourceFn={dataSourceFn}
					groupId={'23'}
					id={'23'}
				/>
			</StaticRouter>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render w/ ErrorDisplay', () => {
		const {container} = render(
			<StaticRouter>
				<KnownIndividualsCard
					channelId={'123'}
					dataSourceFn={() => Promise.reject({})}
					groupId={'23'}
					id={'23'}
				/>
			</StaticRouter>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
