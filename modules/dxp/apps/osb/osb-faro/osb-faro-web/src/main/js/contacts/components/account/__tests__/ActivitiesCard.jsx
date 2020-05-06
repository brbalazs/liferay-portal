import * as API from 'shared/api';
import * as data from 'test/data';
import ActivitiesCard from '../ActivitiesCard';
import Promise from 'metal-promise';
import React from 'react';
import {Account} from 'shared/util/records';
import {shallow} from 'enzyme';

describe('ActivitiesCard', () => {
	it('should render', () => {
		const component = shallow(
			<ActivitiesCard
				account={data.getImmutableMock(
					Account,
					data.mockAccount,
					'test'
				)}
				groupId={'23'}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render w/o loading', () => {
		const component = shallow(
			<ActivitiesCard
				account={data.getImmutableMock(
					Account,
					data.mockAccount,
					'test'
				)}
				groupId={'23'}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render w/ ErrorDisplay', () => {
		API.activities.fetchHistory.mockReturnValueOnce(Promise.reject({}));

		const component = shallow(
			<ActivitiesCard
				account={data.getImmutableMock(
					Account,
					data.mockAccount,
					'test'
				)}
				groupId={'23'}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});
});
