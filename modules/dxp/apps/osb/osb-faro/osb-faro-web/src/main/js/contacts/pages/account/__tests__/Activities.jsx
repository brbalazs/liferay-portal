import * as API from 'shared/api';
import * as data from 'test/data';
import Activities from '../Activities';
import Promise from 'metal-promise';
import React from 'react';
import {Account} from 'shared/util/records';
import {ENGAGEMENT} from 'shared/util/router';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const DefaultComponent = props => (
	<StaticRouter>
		<Activities
			account={data.getImmutableMock(Account, data.mockAccount)}
			channelId='123123'
			groupId={'23'}
			interval={'D'}
			rangeSelectors={{rangeKey: 30}}
			{...props}
		/>
	</StaticRouter>
);

describe('Activities', () => {
	it('should render', () => {
		const {container} = render(
			<DefaultComponent />
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render with error display', () => {
		API.engagement.fetchHistory.mockReturnValueOnce(Promise.reject({}));

		const {getByText} = render(
			<DefaultComponent />
		);

		jest.runAllTimers();

		expect(getByText('An unexpected error occurred.')).toBeTruthy();
	});

	it('should render with Engagement tab as active', () => {
		const {container} = render(
			<DefaultComponent tabId={ENGAGEMENT} />
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render a fallback display when engagement score is null', () => {
		const {container} = render(
			<DefaultComponent
				account={data.getImmutableMock(Account, data.mockAccount, 1, {
					engagementScore: null
				})}
				tabId={ENGAGEMENT}
			/>
		);

		jest.runAllTimers();

		const selectedTab = container.querySelector('.card-tab.active');
		const value = selectedTab.querySelector('.metric-value');

		expect(value).toHaveTextContent('0.00');
	});
});
