import * as API from 'shared/api';
import * as data from 'test/data';
import Promise from 'metal-promise';
import React from 'react';
import {Individual} from 'shared/util/records';
import {IndividualProfileCard} from '../ProfileCard';
import {shallow} from 'enzyme';

describe('IndividualProfileCard', () => {
	it('should render', () => {
		const component = shallow(
			<IndividualProfileCard
				channelId='123123'
				entity={new Individual(data.mockIndividual())}
				groupId={'23'}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ an error display', () => {
		API.activities.fetchHistory.mockReturnValueOnce(Promise.reject({}));

		const component = shallow(
			<IndividualProfileCard
				channelId='123123'
				entity={new Individual(data.mockIndividual())}
				groupId={'23'}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render w/o loading', () => {
		const component = shallow(
			<IndividualProfileCard
				channelId='123123'
				entity={new Individual(data.mockIndividual())}
				groupId={'23'}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render selected info', () => {
		const component = shallow(
			<IndividualProfileCard
				channelId='123123'
				entity={new Individual(data.mockIndividual())}
				groupId={'23'}
				hasSelectedPoint
				onPointSelect={jest.fn()}
				selectedPoint={0}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});
});
