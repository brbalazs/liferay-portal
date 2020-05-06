import React from 'react';
import UniqueVisitorsCell from '../UniqueVisitorsCell';
import {shallow} from 'enzyme';

describe('Unique Visitors Cell', () => {
	let component;

	afterEach(() => {
		if (component) {
			component.unmount();
		}
	});

	it('should render', () => {
		component = shallow(
			<UniqueVisitorsCell trafficSplit={50} uniqueVisitors={123} />
		);

		expect(component).toMatchSnapshot();
	});
});
