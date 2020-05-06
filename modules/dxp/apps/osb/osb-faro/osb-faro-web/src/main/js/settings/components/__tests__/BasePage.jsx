import React from 'react';
import {SettingsBasePage as BasePage} from '../BasePage';
import {shallow} from 'enzyme';

describe('BasePage', () => {
	it('should render', () => {
		const component = shallow(<BasePage groupId='23' />);

		expect(component).toMatchSnapshot();
	});
});
