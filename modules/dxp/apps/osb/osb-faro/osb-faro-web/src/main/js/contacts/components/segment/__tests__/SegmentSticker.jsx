import FaroConstants from 'shared/util/constants';
import React from 'react';
import SegmentSticker from '../SegmentSticker';
import {shallow} from 'enzyme';

const {segmentStates, segmentTypes} = FaroConstants;

describe('SegmentSticker', () => {
	it('should render', () => {
		const component = shallow(
			<SegmentSticker segmentType={segmentTypes.static} />
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with a dynamic segment icon', () => {
		const component = shallow(
			<SegmentSticker segmentType={segmentTypes.dynamic} />
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with a disabled segment icon', () => {
		const component = shallow(
			<SegmentSticker state={segmentStates.disabled} />
		);

		expect(component).toMatchSnapshot();
	});
});
