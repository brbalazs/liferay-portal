import * as data from 'test/data';
import EngagementHistoryCell from '../EngagementHistory';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('EngagementHistoryCell', () => {
	it('should render', () => {
		const tableRow = document.createElement('tr');

		const {container} = render(
			<EngagementHistoryCell
				data={{
					engagementHistory: data.mockEngagementData()
				}}
			/>,
			{container: document.body.appendChild(tableRow)}
		);

		expect(container).toMatchSnapshot();
	});
});
