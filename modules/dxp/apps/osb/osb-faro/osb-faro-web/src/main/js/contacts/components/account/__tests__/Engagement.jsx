import AccountEngagement from '../Engagement';
import React from 'react';
import {getTimestamp} from 'test/data';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const mockEngagementData = [
	{
		intervalInitDate: getTimestamp(-1),
		scoreAvg: 2
	},
	{
		intervalInitDate: getTimestamp(),
		scoreAvg: 7
	}
];

describe('AccountEngagement', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<AccountEngagement
					channelId={'123'}
					data={mockEngagementData}
					groupId={'23'}
					id={'3'}
					previousScore={1}
					score={7}
				/>
			</StaticRouter>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
