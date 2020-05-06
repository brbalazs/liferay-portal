import * as data from 'test/data';
import KnownIndividualsCard from '../KnownIndividualsCard';
import Promise from 'metal-promise';
import React from 'react';
import {shallow} from 'enzyme';

const mockIndividualWithEngagementHistory = () => ({
	...data.mockIndividual(),
	engagementHistory: data.mockEngagementData()
});

describe('KnownIndividualsCard', () => {
	it('should render', () => {
		const component = shallow(
			<KnownIndividualsCard
				channelId={'123'}
				dataSourceFn={() => Promise.resolve()}
				groupId={'23'}
				id={'23'}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ NoResultsDisplay', () => {
		const dataSourceFn = () =>
			Promise.resolve(
				data.mockSearch(mockIndividualWithEngagementHistory, 0)
			);

		const component = shallow(
			<KnownIndividualsCard
				channelId={'123'}
				dataSourceFn={dataSourceFn}
				groupId={'23'}
				id={'23'}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render w/ ErrorDisplay', () => {
		const component = shallow(
			<KnownIndividualsCard
				channelId={'123'}
				dataSourceFn={() => Promise.reject({})}
				groupId={'23'}
				id={'23'}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});
});
