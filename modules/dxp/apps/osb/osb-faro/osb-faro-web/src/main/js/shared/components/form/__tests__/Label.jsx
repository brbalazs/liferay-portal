import Label from '../Label';
import React from 'react';
import {shallow} from 'enzyme';

describe('Label', () => {
	it('should render', () => {
		const component = shallow(<Label />);
		expect(component).toMatchSnapshot();
	});

	it('should render as required', () => {
		const component = shallow(<Label required />);
		expect(component).toMatchSnapshot();
	});

	it('should render with a tooltip and icon', () => {
		const component = shallow(<Label info={'foo bar baz'} />);
		expect(component).toMatchSnapshot();
	});
});
