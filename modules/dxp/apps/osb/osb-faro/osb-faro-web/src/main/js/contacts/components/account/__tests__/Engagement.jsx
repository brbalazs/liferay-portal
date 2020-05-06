import AccountEngagement from '../Engagement';
import React from 'react';
import {getTimestamp} from 'test/data';
import {shallow} from 'enzyme';

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
		const component = shallow(
			<AccountEngagement
				channelId={'123'}
				data={mockEngagementData}
				groupId={'23'}
				id={'3'}
				previousScore={1}
				score={7}
			/>
		);

		expect(component.shallow()).toMatchSnapshot();
	});
});
