jest.mock('shared/hoc/WithRequest', () => () => component => component);

import * as data from 'test/data';
import Membership, {ChartViews} from '../Membership';
import React from 'react';
import {ENGAGEMENT, GROWTH} from 'shared/util/router';
import {Segment} from 'shared/util/records';
import {shallow} from 'enzyme';

const defaultProps = {
	engagementHistory: {data: [], previousScore: 0},
	groupId: '23',
	growthHistory: [],
	segment: data.getImmutableMock(Segment, data.mockSegment)
};

describe('Membership', () => {
	it('should render', () => {
		const component = shallow(<Membership {...defaultProps} />);

		expect(component).toMatchSnapshot();
	});

	it('should render the growth tab', () => {
		const component = shallow(<Membership {...defaultProps} />);

		expect(component.find('CardTabs').props().activeTabId).toEqual(GROWTH);
	});

	it('should render the engagement tab', () => {
		const component = shallow(
			<Membership {...defaultProps} tabId={ENGAGEMENT} />
		);

		expect(component.find('CardTabs').props().activeTabId).toEqual(
			ENGAGEMENT
		);
	});
});

describe('ChartViews', () => {
	it('should render', () => {
		const component = shallow(<ChartViews {...defaultProps} />);

		expect(component.children().shallow()).toMatchSnapshot();
	});

	it('should render the growth tab', () => {
		const component = shallow(<ChartViews {...defaultProps} />);

		jest.runAllTimers();

		expect(
			component
				.children()
				.shallow()
				.find('SegmentGrowthWithList').length
		).toEqual(1);
	});

	it('should render the engagement tab', () => {
		const component = shallow(
			<ChartViews {...defaultProps} tabId={ENGAGEMENT} />
		);

		jest.runAllTimers();

		expect(component.find('SegmentEngagementWithList').length).toEqual(1);
	});
});
