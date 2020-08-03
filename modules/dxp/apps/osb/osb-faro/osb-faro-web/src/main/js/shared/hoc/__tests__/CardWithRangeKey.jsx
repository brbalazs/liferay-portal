jest.mock('shared/hoc/DropdownRangeKey', () => 'DropdownRangeKey');

import CardWithRangeKey from '../CardWithRangeKey';
import React from 'react';
import {shallow} from 'enzyme';

const WrappedComponent = () => (
	<CardWithRangeKey>{() => <div>{'foo'}</div>}</CardWithRangeKey>
);

describe('CardWithRangeKey', () => {
	it('render', () => {
		const component = shallow(<WrappedComponent rangeKey='30' />);

		expect(
			component
				.dive()
				.shallow()
				.find('DropdownRangeKey').length
		).toBe(1);
	});
});
