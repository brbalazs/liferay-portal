import * as data from 'test/data';
import React from 'react';
import {ENGAGEMENT} from 'shared/util/router';
import {Segment} from 'shared/util/records';
import {SegmentProfileCard} from '../ProfileCard';
import {shallow} from 'enzyme';

describe('SegmentProfileCard', () => {
	it('should render', () => {
		const component = shallow(
			<SegmentProfileCard
				channelId='123'
				groupId='23'
				segment={data.getImmutableMock(Segment, data.mockSegment, '3')}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with Engagement tab as active', () => {
		const component = shallow(
			<SegmentProfileCard
				channelId='123'
				groupId='23'
				segment={data.getImmutableMock(Segment, data.mockSegment, '3')}
				tabId={ENGAGEMENT}
			/>
		);

		expect(component.find('CardTabs').props().activeTabId).toEqual(
			ENGAGEMENT
		);
	});
});
