import * as data from 'test/data';
import EngagementHistoryCell from '../EngagementHistory';
import React from 'react';
import {shallow} from 'enzyme';

describe('EngagementHistoryCell', () => {
	it('should render', () => {
		const component = shallow(
			<EngagementHistoryCell
				data={{
					engagementHistory: data.mockEngagementData()
				}}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
