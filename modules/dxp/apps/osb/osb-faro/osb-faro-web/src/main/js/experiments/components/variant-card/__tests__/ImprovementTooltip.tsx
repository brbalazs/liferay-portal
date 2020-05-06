import ImprovementTooltip from '../ImprovementTooltip';
import React from 'react';
import {shallow} from 'enzyme';

describe('Unique Visitors Tooltip', () => {
	let component;

	afterEach(() => {
		if (component) {
			component.unmount();
		}
	});

	it('should render', () => {
		component = shallow(<ImprovementTooltip improvement={10} />);

		expect(component).toMatchSnapshot();
	});

	it('should render negative improvements', () => {
		component = shallow(<ImprovementTooltip improvement={-10} />);

		expect(component).toMatchSnapshot();
	});
});
