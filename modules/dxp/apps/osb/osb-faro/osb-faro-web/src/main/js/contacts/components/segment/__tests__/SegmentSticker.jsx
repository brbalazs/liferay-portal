import FaroConstants from 'shared/util/constants';
import React from 'react';
import SegmentSticker from '../SegmentSticker';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

const {segmentStates, segmentTypes} = FaroConstants;

describe('SegmentSticker', () => {
	it('should render', () => {
		const {container} = render(
			<SegmentSticker segmentType={segmentTypes.static} />
		);
		expect(container).toMatchSnapshot();
	});

	it('should render with a dynamic segment icon', () => {
		const {container} = render(
			<SegmentSticker segmentType={segmentTypes.dynamic} />
		);

		expect(container.querySelector('use')).toHaveAttribute(
			'xlink:href',
			'#individual-dynamic-segment'
		);
	});

	it('should render with a disabled segment icon', () => {
		const {container} = render(
			<SegmentSticker state={segmentStates.disabled} />
		);

		expect(container.querySelector('use')).toHaveAttribute(
			'xlink:href',
			'#warning'
		);
	});
});
