import React from 'react';
import {ConfigureCSVTesting} from '../ConfigureCSV';
import {shallow} from 'enzyme';

describe('ConfigureCSV', () => {
	it('should render', () => {
		const component = shallow(
			<ConfigureCSVTesting groupId='23' id='123' />
		);

		expect(component).toMatchSnapshot();
	});
});
