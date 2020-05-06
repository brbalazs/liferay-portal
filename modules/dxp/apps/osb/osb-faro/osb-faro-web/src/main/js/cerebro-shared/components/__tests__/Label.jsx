import Label from '../Label';
import React from 'react';
import {shallow} from 'enzyme';

describe('Label', () => {
	it('should render', () => {
		const component = shallow(<Label label='Label' />);

		expect(component).toMatchSnapshot();
	});

	it('should render closeable Label', () => {
		const component = shallow(<Label closeable />);

		expect(component).toMatchSnapshot();
	});
});
