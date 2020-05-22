import HeaderDefault from '../HeaderDefault';
import React from 'react';
import {INTERVAL_KEY_MAP} from 'shared/util/time';
import {LAST_24_HOURS} from 'shared/util/constants';
import {shallow} from 'enzyme';

describe('HeaderDefault', () => {
	it('should render', () => {
		const component = shallow(<HeaderDefault />);

		expect(component).toMatchSnapshot();
	});

	it('should call the onChangeInterval prop fn with "day" if the rangekey is changed to an hourly value', () => {
		const spy = jest.fn();
		const component = shallow(
			<HeaderDefault
				onChangeInterval={spy}
				rangeSelectors={{rangeKey: '30'}}
				showInterval
			/>
		);

		component
			.find('Apollo(Component)')
			.props()
			.onChange(LAST_24_HOURS);

		expect(spy).toHaveBeenCalledWith(INTERVAL_KEY_MAP.day);
	});
});
