import ImprovementCell from '../ImprovementCell';
import React from 'react';
import {shallow} from 'enzyme';

describe('Unique Visitors Cell', () => {
	let component;

	afterEach(() => {
		if (component) {
			component.unmount();
		}
	});

	it('should render', () => {
		component = shallow(<ImprovementCell improvement={10.12345} />);

		expect(component.find('span').text()).toEqual(
			'<ClayIcon /> 10.12% lift'
		);
		expect(component).toMatchSnapshot();
	});

	it('should render negative improvements', () => {
		component = shallow(<ImprovementCell improvement={-10.12345} />);

		expect(component.find('span').text()).toEqual(
			'<ClayIcon /> 10.12% loss'
		);
		expect(component).toMatchSnapshot();
	});
});
