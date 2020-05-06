import React from 'react';
import toggleSwitch from '../ToggleSwitch';
import {shallow} from 'enzyme';

const DefaultComponent = props => toggleSwitch({field: {}, ...props});

describe('ToggleSwitch', () => {
	it('should render', () => {
		const component = shallow(<DefaultComponent />);
		expect(component.shallow()).toMatchSnapshot();
	});

	it('should render with an initial value', () => {
		const CheckedComponent = props =>
			toggleSwitch({field: {value: true}, ...props});
		const component = shallow(<CheckedComponent />);
		expect(component.shallow()).toMatchSnapshot();
	});
});
