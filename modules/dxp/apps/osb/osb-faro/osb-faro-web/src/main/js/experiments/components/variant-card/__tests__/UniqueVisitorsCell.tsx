import React from 'react';
import UniqueVisitorsCell from '../UniqueVisitorsCell';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Unique Visitors Cell', () => {
	it('should render', () => {
		const {container} = render(
			<UniqueVisitorsCell trafficSplit={50} uniqueVisitors={123} />
		);

		expect(container).toMatchSnapshot();
	});
});
