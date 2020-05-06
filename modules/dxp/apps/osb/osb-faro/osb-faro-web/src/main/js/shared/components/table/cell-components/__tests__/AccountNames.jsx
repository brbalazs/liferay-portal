import AccountNames from '../AccountNames';
import React from 'react';
import {shallow} from 'enzyme';

describe('AccountNames', () => {
	it('should render', () => {
		const component = shallow(
			<AccountNames
				data={{
					accountNames: ['foo', 'bar', 'baz']
				}}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render a fallback display', () => {
		const component = shallow(
			<AccountNames
				data={{
					accountNames: []
				}}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
