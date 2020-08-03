import * as data from 'test/data';
import Overview from '../Overview';
import React from 'react';
import {Segment} from 'shared/util/records';
import {shallow} from 'enzyme';

describe('SegmentOverview', () => {
	it('should render', () => {
		const component = shallow(
			<Overview
				groupId='23'
				id='test'
				segment={data.getImmutableMock(Segment, data.mockSegment)}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
