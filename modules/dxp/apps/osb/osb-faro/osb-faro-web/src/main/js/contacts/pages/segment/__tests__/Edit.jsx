jest.mock('../edit/Dynamic', () => 'DynamicSegment');
jest.mock('../edit/Static', () => 'StaticSegment');

import FaroConstants from 'shared/util/constants';
import React from 'react';
import {Edit} from '../Edit';
import {shallow} from 'enzyme';

const {segmentTypes} = FaroConstants;

describe('Edit', () => {
	it('should render', () => {
		const component = shallow(<Edit groupId='23' />);

		expect(component.name()).toMatchSnapshot();
	});

	it('should render a dynamic segment', () => {
		const component = shallow(
			<Edit groupId='23' type={segmentTypes.dynamic} />
		);

		expect(component.name()).toEqual('DynamicSegment');
	});

	it('should render a static segment', () => {
		const component = shallow(
			<Edit groupId='23' type={segmentTypes.static} />
		);

		expect(component.name()).toEqual('StaticSegment');
	});
});
