import * as API from 'shared/api';
import * as data from 'test/data';
import Activities from '../Activities';
import Promise from 'metal-promise';
import React from 'react';
import {Account} from 'shared/util/records';
import {ENGAGEMENT} from 'shared/util/router';
import {shallow} from 'enzyme';

describe('Activities', () => {
	const account = data.getImmutableMock(Account, data.mockAccount);

	it('should render', () => {
		const component = shallow(
			<Activities account={account} groupId={'23'} />
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with error display', () => {
		API.engagement.fetchHistory.mockReturnValueOnce(Promise.reject({}));

		const component = shallow(
			<Activities account={account} groupId={'23'} />
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render with Engagement tab as active', () => {
		const component = shallow(
			<Activities account={account} groupId={'23'} tabId={ENGAGEMENT} />
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render a fallback display when engagement score is null', () => {
		const component = shallow(
			<Activities
				account={data.getImmutableMock(Account, data.mockAccount, 1, {
					engagementScore: null
				})}
				groupId={'23'}
				tabId={ENGAGEMENT}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});
});
