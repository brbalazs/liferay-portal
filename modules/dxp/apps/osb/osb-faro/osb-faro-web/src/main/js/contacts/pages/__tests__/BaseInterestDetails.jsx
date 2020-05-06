import * as data from 'test/data';
import BaseInterestDetails from '../BaseInterestDetails';
import mockDate from 'test/mock-date';
import React from 'react';
import {Account, Segment} from 'shared/util/records';
import {ACCOUNTS, Routes, SEGMENTS} from 'shared/util/router';
import {shallow} from 'enzyme';

describe('BaseInterestDetails', () => {
	beforeAll(() => mockDate());

	afterAll(() => jest.restoreMocks());

	it('should render', () => {
		const component = shallow(
			<BaseInterestDetails
				channelId='123'
				entity={new Segment(data.mockSegment())}
				groupId='23'
				id='test'
				interestDetailsRoute={Routes.CONTACTS_SEGMENT_INTEREST_DETAILS}
				interestId='1'
				type={SEGMENTS}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render an individuals list tab', () => {
		const component = shallow(
			<BaseInterestDetails
				entity={new Account(data.mockAccount())}
				groupId='23'
				id='test'
				interestDetailsRoute={Routes.CONTACTS_ACCOUNT_INTEREST_DETAILS}
				interestId='1'
				tabId='individuals'
				type={ACCOUNTS}
			/>
		);

		jest.runAllTimers();

		expect(
			component
				.find('InterestDetailsList')
				.shallow()
				.name()
		).toBe('IndividualsList');
	});

	it('should render an active pages list tab', () => {
		const component = shallow(
			<BaseInterestDetails
				active='true'
				entity={new Account(data.mockAccount())}
				groupId='23'
				id='test'
				interestDetailsRoute={Routes.CONTACTS_ACCOUNT_INTEREST_DETAILS}
				interestId='1'
				tabId='pages'
				type={ACCOUNTS}
			/>
		);

		jest.runAllTimers();

		expect(
			component
				.find('InterestDetailsList')
				.shallow()
				.name()
		).toBe('InterestPagesList');

		expect(
			component
				.find('Item')
				.at(1)
				.prop('active')
		).toBe(true);
	});

	it('should render a pages list tab of inactive pages', () => {
		const component = shallow(
			<BaseInterestDetails
				active='false'
				entity={new Account(data.mockAccount())}
				groupId='23'
				id='test'
				interestDetailsRoute={Routes.CONTACTS_ACCOUNT_INTEREST_DETAILS}
				interestId='1'
				tabId='pages'
				type={ACCOUNTS}
			/>
		);

		jest.runAllTimers();

		expect(
			component
				.find('InterestDetailsList')
				.shallow()
				.name()
		).toBe('InterestPagesList');

		expect(
			component
				.find('Item')
				.at(2)
				.prop('active')
		).toBe(true);
	});
});
